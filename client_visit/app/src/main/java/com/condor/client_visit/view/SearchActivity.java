package com.condor.client_visit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.condor.client_visit.adapters.ClienteAdapter;
import com.condor.client_visit.helpers.SharedPrefCaller;
import com.condor.client_visit.model.Cliente;
import com.condor.client_visit.R;
import com.condor.client_visit.databinding.ActivitySearchBinding;
import com.condor.client_visit.helpers.ConstantsManager;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.helpers.LoadingDialog;
import com.condor.client_visit.helpers.SimpleMessageDialog;
import com.condor.client_visit.viewmodel.SearchViewModel;

import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySearchBinding binding;

    private SearchViewModel searchVM;
    private String token;
    private String codusur;

    private String visit_id;

    private Button btn_cnpj,btn_cancel;
    private EditText editTxt_cnpj;

    private AlertDialog cnpjDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getCreds();
        searchVM = new ViewModelProvider(this).get(SearchViewModel.class);
        getSupportActionBar().setTitle(getString(R.string.search_appbar_title));
    }

    private void getCreds(){
        HashMap<String,String> arr = Lib.getCreds(this);
        token = arr.get(getString(R.string.sh_token));
        codusur = arr.get(getString(R.string.sh_codusur));
        visit_id = arr.get(getString(R.string.sh_idvisita));
    }

    private boolean hasLocationPermission(){
        boolean fineLoc = EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean coarseLoc = EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return (fineLoc == true & coarseLoc == true);
    }


    private View setCnpjDialogControls(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.insert_cnpj,null);
        btn_cnpj = layout.findViewById(R.id.insertCnpj_button);
        btn_cancel = layout.findViewById(R.id.insertCnpjCancel_button);
        editTxt_cnpj = layout.findViewById(R.id.insertCnpj_editText);
        btn_cancel.setOnClickListener(this);
        btn_cnpj.setOnClickListener(this);
        return  layout;
    }

    private void showDeinedPermissionMsg(){
        SimpleMessageDialog.showMessage(this,"",getString(R.string.no_location_per_message));
    }

    private void navigateNext(){
        Intent intent = new Intent(this,VisitActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void createVisit(String codcli,String cnpj){
        try{
            LoadingDialog.showLoadingDialog(this);
            searchVM.createVisit(codusur,codcli,cnpj,token).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if(s != null & s != ""){
                        SharedPrefCaller.setSharedPref(SearchActivity.this,"string",getString(R.string.sh_idvisita),s);
                        navigateNext();
                    }else{
                        Toast.makeText(SearchActivity.this, getString(R.string.create_visit_error_message), Toast.LENGTH_LONG).show();
                    }
                    LoadingDialog.closeLoadingDialog();
                }
            });
        }catch (Exception ex){
            Toast.makeText(SearchActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            LoadingDialog.closeLoadingDialog();
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(ConstantsManager.LOCATION_PERMISSION_REQUEST_CODE,permissions,grantResults,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(visit_id.equals(null) | visit_id.equals("")){
            if(!hasLocationPermission()){
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.per_location),
                        ConstantsManager.LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION);
            }
            return;
        }
        navigateNext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem search_item_menu = menu.findItem(R.id.nav_search);
        SearchView searchView =(SearchView)search_item_menu.getActionView();
        searchView.setQueryHint("Pesquisar Clientes....");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchVM.loadClientes(codusur,query,token).observe(SearchActivity.this, new Observer<List<Cliente>>() {
                    @Override
                    public void onChanged(List<Cliente> clientes) {
                        try{
                            binding.errorLayout.setVisibility(View.GONE);
                            if(clientes == null | clientes.size() == 0){
                                binding.noresultLayout.setVisibility(View.VISIBLE);
                            }else{
                                binding.noresultLayout.setVisibility(View.GONE);
                            }
                            ClienteAdapter clienteAdapter = new ClienteAdapter(clientes,SearchActivity.this);
                            binding.searchListview.setAdapter(clienteAdapter);
                            binding.searchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    AlertDialog msgDialog = new AlertDialog.Builder(SearchActivity.this)
                                            .setCancelable(false)
                                            .setMessage(getString(R.string.client_confirm_start_visit_msg))
                                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(hasLocationPermission()){
                                                        TextView codcli_edittext = view.findViewById(R.id.codcli_textview);
                                                        TextView cn_edittext = view.findViewById(R.id.cnpj_textview);
                                                        String codcli = String.valueOf(codcli_edittext.getText());
                                                        String cnpj = String.valueOf(cn_edittext.getText());
                                                        createVisit(codcli,cnpj);
                                                    }else{
                                                        showDeinedPermissionMsg();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create();
                                    msgDialog.show();
                                }
                            });
                        }catch (Exception ex){
                            binding.searchListview.setVisibility(View.GONE);
                            binding.noresultLayout.setVisibility(View.GONE);
                            binding.errorLayout.setVisibility(View.VISIBLE);
                        }

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_add:
                if(hasLocationPermission()){
                    cnpjDialog = new AlertDialog.Builder(this)
                            .setView(setCnpjDialogControls())
                            .setCancelable(false)
                            .create();
                    cnpjDialog.show();
                }else{
                    showDeinedPermissionMsg();
                }
                break;
            case R.id.nav_logout:
                AlertDialog confirmDialog = new AlertDialog.Builder(SearchActivity.this)
                        .setCancelable(false)
                        .setMessage(getString(R.string.confirm_logout_msg))
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SearchActivity.this,SplashActivity.class);
                                startActivity(intent);
                                SharedPrefCaller.clearAll(SearchActivity.this);
                                SearchActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                confirmDialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insertCnpj_button:
                if(cnpjDialog != null)
                    cnpjDialog.dismiss();
                String cnpj = String.valueOf(editTxt_cnpj.getText());
                if(cnpj != null && cnpj != "" && cnpj.length() == 14){
                    createVisit("0",cnpj);
                }
                break;
            case R.id.insertCnpjCancel_button:
                if(cnpjDialog != null)
                    cnpjDialog.dismiss();
                break;
        }
    }
}
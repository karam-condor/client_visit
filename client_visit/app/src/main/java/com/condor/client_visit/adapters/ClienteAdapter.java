package com.condor.client_visit.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.condor.client_visit.model.Cliente;
import com.condor.client_visit.R;
import com.condor.client_visit.helpers.ConstantsManager;

import java.util.List;

public class ClienteAdapter extends BaseAdapter {

    public ClienteAdapter(List<Cliente> clientes, Activity activity) {
        this.clientes = clientes;
        this.activity = activity;
    }

    List<Cliente> clientes;
    Activity activity;



    @Override
    public int getCount() {
        return clientes.size();
    }

    @Override
    public Object getItem(int position) {
        return clientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater =(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_lv_item,parent,false);
            TextView codcli_textview = convertView.findViewById(R.id.codcli_textview);
            TextView cliente_textview = convertView.findViewById(R.id.cliente_textview);
            TextView status_textview = convertView.findViewById(R.id.status_textview);
            TextView cidade_textview = convertView.findViewById(R.id.cidade_textview);
            TextView limcred_textview = convertView.findViewById(R.id.limcred_textview);
            TextView dias_textview = convertView.findViewById(R.id.dias_textview);
            TextView cnpj_textview = convertView.findViewById(R.id.cnpj_textview);
            Cliente cli = clientes.get(position);
            codcli_textview.setText(cli.getCODIGO());
            cliente_textview.setText("Cliente: " + cli.getCLIENTE().trim());
            status_textview.setText(cli.getSTATUS());
            if (cli.getSTATUS().equals("Ativo")){
                status_textview.setTextColor(Color.parseColor(ConstantsManager.STATUS_GREEN));
            }else{
                status_textview.setTextColor(Color.parseColor(ConstantsManager.STATUS_RED));
            }
            cidade_textview.setText(cli.getCIDADE());
            limcred_textview.setText("Lim.Cred: " + cli.getLIMCRED());
            dias_textview.setText("Dias: " + cli.getDIAS());
            cnpj_textview.setText(cli.getCNPJ());
        }
        return convertView;
    }
}

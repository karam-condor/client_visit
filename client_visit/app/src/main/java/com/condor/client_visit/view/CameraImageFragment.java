package com.condor.client_visit.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.condor.client_visit.R;

import java.io.File;


public class CameraImageFragment extends Fragment implements View.OnClickListener {

    ImageView cancelImgVw,confirmImgVw,previewImgVw;
    EditText descriptionEditTxt;
    Bitmap tokenImage;
    String imgPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_image, container, false);
        //Define layout controls
        setLayout(view);
        //show token image in the img vw
        showPhoto();
        return view;
    }


    private void setLayout(View view) {
        cancelImgVw = view.findViewById(R.id.camera_dismiss_imgVw);
        confirmImgVw = view.findViewById(R.id.camera_confirm_imgVw);
        previewImgVw = view.findViewById(R.id.camera_preview_imgVw);
        descriptionEditTxt = view.findViewById(R.id.camera_description_editTxt);
        cancelImgVw.setOnClickListener(this);
        confirmImgVw.setOnClickListener(this);
    }

    private void showPhoto() {
        imgPath = ((CameraActivity)getActivity()).getImgPath();
        tokenImage = BitmapFactory.decodeFile(imgPath);
        previewImgVw.setImageBitmap(tokenImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_dismiss_imgVw:
                File fdelete = new File(imgPath);
                if (fdelete.exists()) {
                    try{
                        fdelete.delete();
                    }catch (Exception e){

                    }
                }
                getActivity().finish();
                break;
            case R.id.camera_confirm_imgVw:
                String des = String.valueOf(descriptionEditTxt.getText());
                des = des.equals("") ? "S/N" : des;
                ((CameraActivity)getActivity()).sendImageResult(imgPath,des);
                break;
        }
    }
}
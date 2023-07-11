package com.condor.client_visit.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.os.Environment;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.condor.client_visit.R;
import com.condor.client_visit.databinding.FragmentCameraPreviewBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CameraPreviewFragment extends Fragment {

    String currentPhotoPath,imgFileName;
    PreviewView cameraVw;
    Button cameraTakePhotoBtn;
    File imgFile;
    int imgRotation = 0;
    Bitmap rotatedBmp;




    private Executor executor = Executors.newSingleThreadExecutor();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_preview, container, false);

        cameraVw = view.findViewById(R.id.cameraVw);
        cameraTakePhotoBtn = view.findViewById(R.id.camera_take_photo_btn);
        //start the camera preview mode
        startCamera();
        return view;
    }


    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        final ImageCapture imageCapture = builder
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .setTargetResolution(new Size(1200,900))
                .build();

        preview.setSurfaceProvider(cameraVw.createSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

        cameraTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageCapture.OutputFileOptions outputFileOptions = null;
                try {
                    //Methods.showLoadingDialog(getContext());
                    imgFile = createImageFile();
                    outputFileOptions = new ImageCapture.OutputFileOptions.Builder(imgFile).build();
                    imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
                        @Override
                        public void onCaptureSuccess(@NonNull ImageProxy image) {
                            imgRotation = image.getImageInfo().getRotationDegrees();
                        }
                    });
                    imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            if(outputFileResults != null){
                                switch (imgRotation){
                                    case 90:
                                        rotatedBmp = rotateBitmap(BitmapFactory.decodeFile(imgFile.getPath()),90);
                                        resaveImage(rotatedBmp);
                                        break;
                                    case 180:
                                        rotatedBmp = rotateBitmap(BitmapFactory.decodeFile(imgFile.getPath()),180);
                                        resaveImage(rotatedBmp);
                                        break;
                                    case 270:
                                        rotatedBmp = rotateBitmap(BitmapFactory.decodeFile(imgFile.getPath()),270);
                                        resaveImage(rotatedBmp);
                                        break;
                                }
                                ((CameraActivity)getActivity()).setImgPath(imgFile.getPath());
                                Fragment fragment = new CameraImageFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.camera_container_layout,fragment)
                                        .addToBackStack(null).commit();
                            }
                        }
                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            showError();
                        }
                    });
                } catch (IOException e) {
                    showError();
                }
                //Methods.closeLoadingDialog();
            }
        });
    }


    private void showError(){
        Toast.makeText(getActivity(), "Aconteceu um erro", Toast.LENGTH_LONG).show();
        getActivity().finish();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imgFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void resaveImage(Bitmap bitmap){
        try (FileOutputStream out = new FileOutputStream(imgFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
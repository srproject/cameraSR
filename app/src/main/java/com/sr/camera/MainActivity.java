package com.sr.camera;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @SuppressWarnings("deprecation")
    Camera camera; // camera class variable
    SurfaceView camView; // drawing camera preview using this variable
    SurfaceHolder surfaceHolder; // variable to hold surface for surfaceView which means display
    boolean camCondition = false;  // conditional variable for camera preview checking and set to false
    Button cap;    // image capturing button

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getWindow() to get window and set it's pixel format which is UNKNOWN
        getWindow().setFormat(PixelFormat.UNKNOWN);
        // refering the id of surfaceView
        camView = (SurfaceView) findViewById(R.id.camerapreview);
        // getting access to the surface of surfaceView and return it to surfaceHolder
        surfaceHolder = camView.getHolder();
        // adding call back to this context means MainActivity
        surfaceHolder.addCallback(this);
        // to set surface type
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);



        camView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Camera.Parameters parameters = camera.getParameters();
                //parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA); //applying effect on cameracamera.setParameters(parameters); // setting camera parameters
                try {
                    camera.setPreviewDisplay(surfaceHolder); // setting preview of camera
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Camera.Parameters parameters2 = camera.getParameters();
                parameters2.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters2);


                camera.startPreview();  // starting camera preview

                camCondition = true;

             }
        });


        // refering button id
        cap = (Button) findViewById(R.id.Bcapture);
        // click event on button
        cap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // Camera.Parameters parameters = camera.getParameters();
                //parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                //camera.setParameters(parameters);
                //camera.startPreview();


                // TODO Auto-generated method stub
                // calling a method of camera class takepicture by passing one picture callback interface parameter
                 camera.autoFocus(new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(parameters);

                            camera.takePicture(null, null, mPictureCallback);

                            camera.stopPreview(); // stop preview using stopPreview() method
                            camCondition = false;

                        }
                    }
                });


            }
        });
    }
    // camera picture taken image and store in directory
    @SuppressWarnings("deprecation")
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {

            FileOutputStream outStream = null;
            try {

                // Directory and name of the photo. We put system time
                // as a postfix, so all photos will have a unique file name.
                outStream = new FileOutputStream("/sdcard/AndroidCodec_" +
                        System.currentTimeMillis()+".jpg");
                outStream.write(data);
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }

        }
    };


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        // stop the camera
        if(camCondition){
            camera.stopPreview(); // stop preview using stopPreview() method
            camCondition = false; // setting camera condition to false means stop
        }
        // condition to check whether your device have camera or not
        if (camera != null){
            try {
                Camera.Parameters parameters = camera.getParameters();
                //parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA); //applying effect on cameracamera.setParameters(parameters); // setting camera parameters
                 camera.setPreviewDisplay(surfaceHolder); // setting preview of camera
                camera.startPreview();  // starting camera preview

                camCondition = true; // setting camera to true which means having camera
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera = Camera.open();   // opening camera
        //camera.setDisplayOrientation(90);   // setting camera preview orientation

        Camera.Parameters parameters = camera.getParameters();
        camera.setDisplayOrientation(270);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//or
        parameters.set("orientation", "portrait");
//or
       // parameters.setRotation(90);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();  // stopping camera preview
        camera.release();       // releasing camera
        camera = null;          // setting camera to null when left
        camCondition = false;   // setting camera condition to false also when exit from application
    }

    public void flashOn() {

        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();

    }


    public void flashOff() {

        Camera camera2 = Camera.open();
        Camera.Parameters parameters2 = camera2.getParameters();
        parameters2.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera2.setParameters(parameters2);

    }

}
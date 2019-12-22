package com.haanhgs.app.sensortiltdemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tvAzimuth)
    TextView tvAzimuth;
    @BindView(R.id.tvPitch)
    TextView tvPitch;
    @BindView(R.id.tvRoll)
    TextView tvRoll;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.clLabelGroup)
    ConstraintLayout clLabelGroup;
    @BindView(R.id.ivTop)
    ImageView ivTop;
    @BindView(R.id.ivBottom)
    ImageView ivBottom;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;

    private SensorManager sensorManager;
    private Sensor accelerator;
    private Sensor magnetor;

    private float[] acceleratorData = new float[3];
    private float[] magnetorData = new float[3];
    private boolean rotationCheck;
    private float[]rotationMatrixAdjusted = new float[9];
    private float pitch;
    private float roll;

    private void setFullScreen(){
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initSensors(){
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if (sensorManager != null){
            accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setFullScreen();
        initSensors();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (accelerator != null){
            sensorManager.registerListener(this, accelerator, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetor != null){
            sensorManager.registerListener(this, magnetor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    private void getSensorsData(SensorEvent event){
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_MAGNETIC_FIELD){
            magnetorData = event.values.clone();
        }
        if (type == Sensor.TYPE_ACCELEROMETER){
            acceleratorData = event.values.clone();
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void getRotationMatrixAdjusted(){
        float[] rotationMatrix = new float[9];
        rotationCheck = SensorManager
                .getRotationMatrix(rotationMatrix, null, acceleratorData, magnetorData);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation){
            case Surface.ROTATION_0:
                rotationMatrixAdjusted = rotationMatrix.clone();
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_MINUS_Y,
                        SensorManager.AXIS_X,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_MINUS_X,
                        SensorManager.AXIS_MINUS_Y,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_Y,
                        SensorManager.AXIS_MINUS_X,
                        rotationMatrixAdjusted);
                break;
        }
    }

    private void updateText(){
        float[] orientation = new float[3];
        if (rotationCheck){
            SensorManager.getOrientation(rotationMatrixAdjusted, orientation);
            float azimuth = orientation[0];
            pitch = orientation[1];
            roll = orientation[2];
            tvAzimuth.setText(getResources().getString(R.string.value, azimuth));
            tvPitch.setText(getResources().getString(R.string.value,pitch));
            tvRoll.setText(getResources().getString(R.string.value,roll));
        }
    }

    private void updateImageView(){
        if (Math.abs(pitch) < 0.1f) pitch = 0f;
        if (Math.abs(roll) < 0.1f) roll = 0f;

        ivTop.setAlpha(0f);
        ivBottom.setAlpha(0f);
        ivLeft.setAlpha(0f);
        ivRight.setAlpha(0f);

        if (pitch > 0) ivTop.setAlpha(pitch/(float)(Math.PI/2));
        if (pitch < 0) ivBottom.setAlpha(Math.abs(pitch)/(float)(Math.PI/2));
        if (roll > 0) ivRight.setAlpha(roll/(float)(Math.PI/2));
        if (roll < 0) ivLeft.setAlpha(Math.abs(roll)/(float)(Math.PI/2));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getSensorsData(event);
        getRotationMatrixAdjusted();
        updateText();
        updateImageView();
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}

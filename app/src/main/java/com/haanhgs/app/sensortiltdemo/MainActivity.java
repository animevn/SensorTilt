package com.haanhgs.app.sensortiltdemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}

package com.haanhgs.app.sensortilt.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;
import androidx.lifecycle.MutableLiveData;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

public class Repo implements SensorEventListener {

    private final MutableLiveData<Sensors> liveSensors = new MutableLiveData<>();
    private final Sensors sensors = new Sensors();

    private SensorManager sensorManager;
    private Sensor accelerator;
    private Sensor magnetor;
    private final Context context;

    private float[] acceleratorData = new float[3];
    private float[] magnetorData = new float[3];
    private boolean rotationCheck;
    private float[]rotationMatrixAdjusted = new float[9];

    private void initSensors(){
        sensorManager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    public Repo(Context context) {
        this.context = context;
        initSensors();
        liveSensors.setValue(sensors);
    }

    public MutableLiveData<Sensors> getLiveSensors() {
        return liveSensors;
    }

    public void start(){
        if (accelerator != null){
            sensorManager.registerListener(this, accelerator, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetor != null){
            sensorManager.registerListener(this, magnetor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop(){
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
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        if (manager != null) {
            int rotation = manager.getDefaultDisplay().getRotation();
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
    }

    private void updateSensors(){
        float[] orientation = new float[3];
        float azimuth = 0;
        float pitch = 0;
        float roll = 0;
        if (rotationCheck){
            SensorManager.getOrientation(rotationMatrixAdjusted, orientation);
            azimuth = orientation[0];
            pitch = orientation[1];
            roll = orientation[2];
        }

        sensors.setAzimuth(azimuth);
        sensors.setPitch(pitch);
        sensors.setRoll(roll);
        liveSensors.setValue(sensors);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getSensorsData(event);
        getRotationMatrixAdjusted();
        updateSensors();
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy){}
}

package com.example.accelerometer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];


    public Display display;
    public Point size;


    public TextView id_screen1, id_screen2, id_g1, id_g2, id_g3;
    public LinearLayout id_layout;

    public int edgeWidth, edgeHeight;

    public float x = 0, y = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        edgeWidth = size.x;
        edgeHeight = size.y;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        id_screen1 = findViewById(R.id.id_screen1);
        id_screen2 = findViewById(R.id.id_screen2);




        id_g1 = findViewById(R.id.id_g1);
        id_g2 = findViewById(R.id.id_g2);
        id_g3 = findViewById(R.id.id_g3);

        id_layout = findViewById(R.id.id_layout);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {


            size.set((int)x, (int)y);
            int width = size.x;
            int height = size.y;


            id_screen1.setText(String.valueOf(edgeWidth));
            id_screen2.setText(String.valueOf(edgeHeight));


            id_g1.setText(String.valueOf(event.values[0] * 10));
            id_g2.setText(String.valueOf(event.values[1] * 10));
            id_g3.setText(String.valueOf(event.values[2] * 10));

            if(height <= 0){
                x = x + ((event.values[0] * 10 )* -1);
                y = (y * -1);
            }else{
                x = x + ((event.values[0] * 10 )* -1);
                y = y + event.values[1] * 10;
            }


            if(height > edgeHeight - 600){
                x = x + ((event.values[0] * 10 )* -1);
                y = y * (-1);
            }else{
                x = x + ((event.values[0] * 10 )* -1);
                y = y + event.values[1] * 10;
            }

            if(width <= 0){
                x = x + 100;
                y = y + event.values[1] * 10;
            }else{
                x = x + ((event.values[0] * 10 )* -1);
                y = y + event.values[1] * 10;
            }








            id_layout.setX(x);
            id_layout.setY(y);





        }
//        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            id_g2.setText(String.valueOf(event.values[4]));
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }


    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        // "orientationAngles" now has up-to-date information.
    }

}
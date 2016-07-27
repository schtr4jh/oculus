package net.schtr4jh.oculus.oculus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    private LocationManager locationManager = null;
    private MyLocationListener locationListener = null;

    private SensorManager sensorManager = null;
    private MySensorListener sensorListener = null;

    private Sensor sensorAccellometer;
    private Sensor sensorOrientation;

    private Context context = null;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ERROR", "Creating camera");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createButton();
        // createCamera();
        createLocation();
        createCompass();
    }

    protected void createButton() {
        //btn to close the application
        ImageButton imgClose = (ImageButton) findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }

    protected void createCamera() {
        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {

            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }
    }

    protected void createLocation() {
        EditText locationText = (EditText) findViewById(R.id.locationText);
        locationText.setText("Creating location");

        context = getApplicationContext();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            locationText.setText("No permissions");
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationListener.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    }

    protected void createCompass() {
        EditText gravityText = (EditText) findViewById(R.id.gravityText);
        EditText orientationText = (EditText) findViewById(R.id.orientationText);

        context = getApplicationContext();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener(this);
        //sensorManager.registerListener(sensorListener, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorListener.onSensorChanged(sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR));

        if (null != sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {
            gravityText.setText("Creating gravity");
            sensorAccellometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(sensorListener, sensorAccellometer, SensorManager.SENSOR_DELAY_NORMAL);
            synchronized (sensorManager) {
                //sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).notify();
            }
        } else {
            gravityText.setText("No gravity");
        }

        if (null != sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)) {
            orientationText.setText("Creating orientation");
            sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            sensorManager.registerListener(sensorListener, sensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);
            synchronized (sensorManager) {
                //sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR).notify();
            }
        } else {
            orientationText.setText("No orientation");
        }
    }

}

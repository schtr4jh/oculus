package net.schtr4jh.oculus.oculus;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by schtr4jh on 27.7.2016.
 */
public class MySensorListener implements SensorEventListener {

    private MainActivity mainActivity = null;
    private final float alpha = (float) 0.8;
    private float gravity[] = new float[3];
    private float magnetic[] = new float[3];

    public MySensorListener(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        EditText gravityText = (EditText) mainActivity.findViewById(R.id.gravityText);
        EditText orientationText = (EditText) mainActivity.findViewById(R.id.orientationText);

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravityText.setText("Updating gravity");
            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]; // tilt left/right (-10, 0, 10)(levo, navpično, desno)
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]; // tile forward/backward (0, 10, -10)(naprej, navpično, nazaj)
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]; // tilt forward/backward (-10, 0, 10)(nazaj, navpično, naprej)

            DecimalFormat df = new DecimalFormat("#.#####");
            gravityText.setText("Gravity: " + df.format(gravity[0]) + " " + df.format(gravity[1]) + " " + df.format(gravity[2]));

        } else if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            orientationText.setText("Updating orientation");

            orientationText.setText("Orientation: " + Float.toString(event.values[0]));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

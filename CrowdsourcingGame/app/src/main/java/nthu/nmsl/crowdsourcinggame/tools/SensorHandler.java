package nthu.nmsl.crowdsourcinggame.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by YingYi on 2016/4/11.
 */
public class SensorHandler implements SensorEventListener {
    private SensorManager aSensorManager;
    private Sensor accelerSensor, CompassSensor;
    private float gravity[] = new float[3];
    private float currentDegree = 0f;
    private Context context;


    public SensorHandler(Context context) {
        this.context = context;
        aSensorManager=(SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        CompassSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        aSensorManager.registerListener(this, accelerSensor, aSensorManager.SENSOR_DELAY_FASTEST);
        aSensorManager.registerListener(this, CompassSensor, aSensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magneticFieldValues = event.values.clone();
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelerometerValues = event.values.clone();
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        values[0] = (float) Math.toDegrees(values[0]);
        //Log.i("degree", values[0]+"");
        //values[1] = (float) Math.toDegrees(values[1]);
        //values[2] = (float) Math.toDegrees(values[2]);

        /*if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            currentDegree = event.values[0];
            Log.e("degree 0 ", String.valueOf(event.values[0]));
            Log.e("degree 1 ", String.valueOf(event.values[1]));
            Log.e("degree 2 ", String.valueOf(event.values[2]));
        }
        else {
            gravity[0] = event.values[0];
            gravity[1] = event.values[1];
            gravity[2] = event.values[2];
            //Log.e("Gsensor 0 ", String.valueOf(gravity[0]));
        }*/
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        /* 取消註冊SensorEventListener */
        aSensorManager.unregisterListener(this);
    }
}



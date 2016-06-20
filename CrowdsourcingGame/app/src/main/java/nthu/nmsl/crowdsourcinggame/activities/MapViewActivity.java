package nthu.nmsl.crowdsourcinggame.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import nthu.nmsl.crowdsourcinggame.R;
import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.TaskDataManager;
import nthu.nmsl.crowdsourcinggame.tools.BeyondARWorld;

import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.plugin.googlemap.GoogleMapWorldPlugin;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import nthu.nmsl.crowdsourcinggame.fragment.FragmentFactory;
import nthu.nmsl.crowdsourcinggame.tools.LocationSensor;
import android.view.Window;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MapViewActivity extends FragmentActivity {
    private static final String TAG = "Tab.Home";
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_view);

        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        fragmentManager = getFragmentManager();
        radioGroup.setOnCheckedChangeListener(radioGroupListener);

        //test
        Location currentLocation = LocationSensor.getInstance(this).getCurrentLocation();
        //TaskDataManager.getInstance().insert(new TaskData(10, "學校風災樹木倒塌", currentLocation.getLatitude()-0.0001, currentLocation.getLongitude()-0.0001, 456, "sdfsdfs", 2));
        //TaskDataManager.getInstance().insert(new TaskData(132, "巡視倒塌樹木修復情形", 24.795304308962532, 120.99255711771548, 456, "sdfsdfs", 2));
        //TaskDataManager.getInstance().insert(new TaskData(133, "路上施工情形", 24.795645200252622, 120.99347443319857, 456, "sdfsdfs", 3));
        //TaskDataManager.getInstance().insert(new TaskData(134, "台達7F走廊髒亂", 24.795593990527102, 120.9921719902195, 456, "sdfsdfs", 4));
        //
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = FragmentFactory.getInstanceByIndex(R.id.radioButton2);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    public void setCheckRadio(int i) {
        ((RadioButton) radioGroup.getChildAt(i)).setChecked(true);
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //change fragment when the radio group checked item changed
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = FragmentFactory.getInstanceByIndex(checkedId);
            Log.e(TAG, "CheckId = " + checkedId);
            transaction.replace(R.id.content, fragment);
            transaction.commit();
        }
    };

    public void onResume() {
        super.onResume();
        radioGroup.setOnCheckedChangeListener(radioGroupListener);
        //radioGroup.check(1);
    }

    public void onDestroy(){
        super.onDestroy();
        radioGroup.setOnCheckedChangeListener(null);

        Log.d(TAG,"Home onDestroy");
        //SensorInstance.profiler.flushAll();
    }
}

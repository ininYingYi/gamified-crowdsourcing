package nthu.nmsl.crowdsourcinggame.fragment;


import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beyondar.android.plugin.googlemap.GoogleMapWorldPlugin;
import com.beyondar.android.world.GeoObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import nthu.nmsl.crowdsourcinggame.R;
import nthu.nmsl.crowdsourcinggame.instances.CreatureList;
import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.TaskDataManager;
import nthu.nmsl.crowdsourcinggame.tools.BeyondARWorld;
import nthu.nmsl.crowdsourcinggame.tools.LocationSensor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MapFragment extends Fragment {
    private static final String TAG = "Tab.Map";
	private Calendar today;
	private com.google.android.gms.maps.MapFragment planMap;
	private SupportMapFragment beyondarFragment;
    private BeyondARWorld arWorld;
    private GoogleMap mMap;
    private GoogleMapWorldPlugin mGoogleMapPlugin;
    private View view;

	public MapFragment(){

	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view=inflater.inflate(R.layout.map_page, null);
        }
        /*ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.view(view);
        }*/
        mapInitial(view);
        Timer timer = new Timer(true);
        timer.schedule(new MyTimerTask(), 1000, 1000);
        return view;
    }

    private class MyTimerTask extends TimerTask {
        public void run() {
            Location currentLocation = LocationSensor.getInstance().getCurrentLocation();
            arWorld.setGeoPosition(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    };

	public void mapInitial(View view) {
		Log.e(TAG, "Map initialization");
		beyondarFragment = (SupportMapFragment) ((FragmentActivity)getActivity()).getSupportFragmentManager().findFragmentById(
				R.id.map_beyondarFragment);
		mMap = beyondarFragment.getMap();
		Location currentLocation = LocationSensor.getInstance(getActivity()).getCurrentLocation();
        if (arWorld==null) {
            arWorld = new BeyondARWorld(getActivity(), currentLocation.getLatitude(), currentLocation.getLongitude(), true);
        }
		// As we want to use GoogleMaps, we are going to create the plugin and
		// attach it to the World
		mGoogleMapPlugin = new GoogleMapWorldPlugin(getActivity());
		// Then we need to set the map in to the GoogleMapPlugin
		mGoogleMapPlugin.setGoogleMap(mMap);
		// Now that we have the plugin created let's add it to our world.
		// NOTE: It is better to load the plugins before start adding object in to the world.
		arWorld.getWorld().addPlugin(mGoogleMapPlugin);
		mMap.setOnMarkerClickListener(markClick);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mGoogleMapPlugin.getLatLng(), 17));
		//mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		// Lets add the user position
		//arWorld.addObject(0,currentLocation.getLatitude(), currentLocation.getLongitude(), R.drawable.creature_1, "User position");
		//arWorld.addObject(currentLocation.getLatitude(), currentLocation.getLongitude()-0.000001, R.drawable.creature_2, "User position");
        ArrayList<TaskData> datas = TaskDataManager.getInstance().getAll();
        if ( datas != null ) {
            for (TaskData data : datas) {
                Log.e(TAG, data.getTaskName() + " " + String.valueOf(currentLocation.getLatitude()) + " " + String.valueOf(currentLocation.getLongitude()));
                if (data.getTaskID() == 11 ) {
                    data.setLatitude(currentLocation.getLongitude() - 0.0001);
                    data.setLongitude(currentLocation.getLatitude() - 0.0001);
                }
                arWorld.addObject(data.getTaskID(), data.getLongitude(), data.getLatitude(), CreatureList.pictures[data.getCreatureID()], data.getTaskName());
            }
        }
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//beyondarFragment = (SupportMapFragment) ((FragmentActivity)getActivity()).getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);
	}

    private GoogleMap.OnMarkerClickListener markClick = new GoogleMap.OnMarkerClickListener() {
        public boolean onMarkerClick(Marker marker) {
            // To get the GeoObject that owns the marker we use the following
            // method:
            GeoObject geoObject = mGoogleMapPlugin.getGeoObjectOwner(marker);
            if (geoObject != null) {
                Toast.makeText(getActivity(),
                        "Click on a marker owned by a GeoOject with the name: " + geoObject.getName(),
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };

    public void onResume() {
        super.onResume();
    }

    public void onDestroyView() {
        super.onDestroyView();
        //beyondarFragment.onDestroy();
        Log.i(TAG, "onDestroyView()............");
    }
}

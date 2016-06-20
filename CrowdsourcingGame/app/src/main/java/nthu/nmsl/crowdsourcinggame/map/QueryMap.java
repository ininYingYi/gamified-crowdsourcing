package nthu.nmsl.crowdsourcinggame.map;

import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import nthu.nmsl.crowdsourcinggame.tools.LocationSensor;

public class QueryMap extends MapFragment {

	private static final String TAG = "Map.QueryMap";
	
	boolean available = false;
	
	LatLng here;
	LatLng destination;
	LatLng defaultLocationAtNTHU = new LatLng(24.794973373413086, 120.99249267578125);
	MapFragment fragment;
	GoogleMap map;
	Geocoder coder;
	
	
	LatLng leftTop ;
	LatLng leftBottom ;
	LatLng rightTop ;
	LatLng rightBottom ;
	
	float zoomLevel = (float) 15.0;
	int maxResults = 1;
	
	public QueryMap(){
		super();
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "taskMap oncreateView");
		return view;
	}
	
	public void onResume(){
		super.onResume();
		available = true;
		
//		if(coder==null){
//			coder = new Geocoder(getActivity().getBaseContext(),Locale.TRADITIONAL_CHINESE);
//		}
		map = getMap();
		map.setMyLocationEnabled(true);
		
		//default to move camera to user location 
		Location location = LocationSensor.getInstance().getCurrentLocation();
		
		if(location!=null){
			map.clear();
			here = new LatLng(location.getLatitude(), location.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLng(defaultLocationAtNTHU));
			map.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
			addMarkers();
			
		}else{
			
		}
		
		//do not show zoom in/out buttons
		map.getUiSettings().setZoomControlsEnabled(false);
		
	}
	
	public void changeMapType(boolean satellite){
		if(map!=null){
			if(satellite){
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			}else{
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}
		}
	}
	
	private void addMarkers(){
		map.addMarker(new MarkerOptions().title("Me").position(here));
	}
	
	private void addMarker(LatLng position, String title){
		map.addMarker(new MarkerOptions().title(title).position(position));
	}
	
	public void addRectangleCornerMarkers(int frameRight, int frameBottom, int width, int height){
		
		map.clear();
		
		setRectangleCorner(frameRight, frameBottom, width, height);
		
		//show marker on screen
//		addMarker(leftTop,"leftTop");
//		addMarker(leftBottom,"leftBottom");
//		addMarker(rightTop,"rightTop");
//		addMarker(rightBottom,"rightBottom");
	}
	
	public void setRectangleCorner(int frameRight, int frameBottom, int width, int height){
		int left = (frameRight-width)/2;
		int top = (frameBottom-height)/2;
		int right = left + width;
		int bottom = top + height;
		
		Projection projection= map.getProjection();
		
		leftTop = projection.fromScreenLocation(new Point(left,top));
		leftBottom = projection.fromScreenLocation(new Point(left,bottom));
		rightTop = projection.fromScreenLocation(new Point(right,top));
		rightBottom = projection.fromScreenLocation(new Point(right,bottom));
	}
	
	public LatLng[] getCorners(){
		LatLng[] corners = new LatLng[4];
		
		corners[0] = leftTop;
		corners[1] = leftBottom;
		corners[2] = rightTop;
		corners[3] = rightBottom;
		
		return corners;
	}
	
	public boolean isAvailable(){
		return available;
	}
	
	public void onPause(){
		super.onPause();
		available = false;
	}
	
	
	
}

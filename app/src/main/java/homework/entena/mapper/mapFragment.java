package homework.entena.mapper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

public class mapFragment extends Fragment{
    private GoogleMap mMap; private LatLng lastCamPos = null;
    private MapView mapView;
    private EditText editLat, editLong;
    private MarkersFragment model;//Latitude: 37.227429 | Longitude: -80.42223 - Drillfield
    private static final double drillLat = 37.227429;
    private static final double drillLong = -80.42223;
    private Vector<MarkerOptions> markerOptionsOnMap;
    private Vector<Marker> markersOnMap;
    private int prevSelected = -1;
    private long lastUpdate;

    public mapFragment() {
        markerOptionsOnMap = new Vector<MarkerOptions>();
        markersOnMap = new Vector<Marker>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap = mapView.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        MapsInitializer.initialize(this.getActivity());
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                Toast.makeText(getActivity(), arg0.latitude+"-"+arg0.longitude, Toast.LENGTH_SHORT).show();
                Log.d("Point:", arg0.latitude + "-" + arg0.longitude);
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            public void onMapLongClick(LatLng point){
                MarkerOptions mark = new MarkerOptions().position(point)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                MarkerItem mk = new MarkerItem(point.latitude, point.longitude);
                addMarker(mk, mark, true);
            }
        });
        editLat = (EditText) view.findViewById(R.id.editLat);
        editLong = (EditText) view.findViewById(R.id.editLong);
        Button goTobtn = (Button) view.findViewById(R.id.btnGoTo);
        goTobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(v);
            }
        });
        Button createMarkerbtn = (Button) view.findViewById((R.id.btnCreateMarker));
        createMarkerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMarker(v);
            }
        });
        model = (MarkersFragment) getFragmentManager().findFragmentByTag("MODEL");
        populateMarkers();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(drillLat, drillLong), 15);
        mMap.animateCamera(cameraUpdate);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("ORIENT", "SETUP");
            setUpAccel();
        }
        return view;
    }

    public void setUpAccel(){
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        sensorManager.registerListener(new SensorEventListener() {
               float last_x = 0, last_y = 0, last_z = 0;
               @Override
               public void onSensorChanged(SensorEvent event) {
                    getAccelerometer(event);
               }
               @Override
               public void onAccuracyChanged(Sensor sensor, int accuracy) {

               }
               private void getAccelerometer(SensorEvent event) {
                   float[] values = event.values;
                   float x = values[0];
                   float y = values[1];
                   float z = values[2];
                   if(last_x == 0 && last_y == 0 && last_z == 0){
                       last_x = x; last_y = y; last_z = z;
                       return;
                   }
                   float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
                   long actualTime = event.timestamp;
                   if (accelationSquareRoot >= 2){
                       if (actualTime - lastUpdate < 1000) {
                           return;
                       }
                       float xdiff = Math.abs(last_x - x); float ydiff = Math.abs(last_y - y);
                       float zdiff = Math.abs(last_z - z);
                       float biggest = Math.max(xdiff, zdiff);
                       Log.e("Biggest", Float.toString(biggest));
                       if(biggest < 16.0){
                           return;
                       }
                       if(biggest == xdiff){
                           Log.e("ORIENT", "TESTING");
                           //Toast.makeText(getActivity(), "X shake", Toast.LENGTH_SHORT).show();
                           if(lastCamPos == null || !lastCamPos.equals(mMap.getCameraPosition().target) || model.getMarkers().size() == 0) {
                               Log.e("ORIENT", "X");
                               createMarkerShake(mMap.getCameraPosition().target);
                               Toast.makeText(getActivity(), "Added a point!", Toast.LENGTH_SHORT).show();
                           }
                       }
                       if(biggest == zdiff){
                           Log.e("ORIENT", "Z");
                           if(model.getMarkers().size() > 0) {
                               MarkerItem i = model.getMarker(model.getMarkers().size()-1);
                               Toast.makeText(getActivity(), "Last Point Added:\n"+i.latitude+","+i.longitude, Toast.LENGTH_SHORT).show();
                           }
                       }
                       Log.e("ORIENT", "SET NEW PREV");
                       lastCamPos = mMap.getCameraPosition().target;
                       lastUpdate = actualTime;
                   }
               }
           }, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void populateMarkers(){
        for(int i=0; i < model.getMarkers().size(); i++){
            LatLng point = new LatLng(model.getMarker(i).latitude, model.getMarker(i).longitude);
            MarkerOptions mark = new MarkerOptions().position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            addMarker(model.getMarker(i), mark, false);
        }
    }

    /**
     * Gets the coords from the text boxes and checks them for safety.
     * @return null if no/missing coordinates or invalid coordinates
     */
    public LatLng getCoord(){
        if(editLat.getText().toString().length() < 1 || editLong.getText().toString().length() < 1){
            Toast.makeText(getActivity(), "ERROR YOU MUST PROVIDE COORDINATES!", Toast.LENGTH_SHORT).show();
            return null;
        }
        double latit = Double.parseDouble(editLat.getText().toString());
        double longit = Double.parseDouble(editLong.getText().toString());
        if(latit > 90.0 || latit < -90.0){
            Toast.makeText(getActivity(), "ERROR: Latitude has a max of 90 and a min of -90", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(longit > 180.0 || longit < -180.0){
            Toast.makeText(getActivity(), "ERROR: Longitude has a max of 180 and a min of -180", Toast.LENGTH_SHORT).show();
            return null;
        }
        return new LatLng(latit, longit);
    }

    public void goTo(View view) {
        LatLng point = getCoord();
        if(point != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 10);
            mMap.animateCamera(cameraUpdate);
        }
    }

    /**
     * Handles manually adding point to map
     * @param view
     */
    public void createMarker(View view) {
        LatLng point = getCoord();
        if(point != null) {
            MarkerOptions mark = new MarkerOptions().position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            MarkerItem mk = new MarkerItem(point.latitude, point.longitude);
            addMarker(mk, mark, true);
        }
    }

    public void addMarker(MarkerItem mk, MarkerOptions mark, boolean addToModel){
        markerOptionsOnMap.add(mark);
        markersOnMap.add(mMap.addMarker(mark));
        if(addToModel) {
            model.addMarker(mk);
        }
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT){
            MarkerList ml = (MarkerList) getFragmentManager().findFragmentByTag("LIST");
            if(ml != null) {
                ml.buildList();
            }
        }
    }

    public void createMarkerShake(LatLng point){
        MarkerOptions mark = new MarkerOptions().position(point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        MarkerItem mk = new MarkerItem(point.latitude, point.longitude);
        addMarker(mk, mark, true);
    }

    public void markerSelected(int pos){
        MarkerOptions markerOpt;
        if(prevSelected != -1){
            markerOpt = markerOptionsOnMap.get(prevSelected);
            markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            markersOnMap.get(prevSelected).remove();
            markersOnMap.add(prevSelected, mMap.addMarker(markerOpt));
        }
        if(prevSelected == pos){
            return;
        }
        markerOpt = markerOptionsOnMap.get(pos);
        markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markersOnMap.get(pos).remove();
        markersOnMap.add(pos, mMap.addMarker(markerOpt));
        prevSelected = pos;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

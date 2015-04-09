package homework.entena.mapper;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mapView;
    private EditText editLat, editLong;

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);

    public mapFragment() {
        // Required empty public constructor
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
                Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            public void onMapLongClick(LatLng point){
                MarkerOptions mark = new MarkerOptions().position(point)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(mark);
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
        return view;
    }

    public void goTo(View view) {
        if(editLat.getText().toString().length() < 1 || editLong.getText().toString().length() < 1){
            Toast.makeText(getActivity(), "ERROR YOU MUST PROVIDE COORDINATES!", Toast.LENGTH_SHORT).show();
            return;
        }
        float latit = Float.parseFloat(editLat.getText().toString());
        float longit = Float.parseFloat(editLong.getText().toString());
        if(latit > 90.0 || latit < -90.0){
            Toast.makeText(getActivity(), "ERROR: Latitude has a max of 90 and a min of -90", Toast.LENGTH_SHORT).show();
            return;
        }
        if(longit > 180.0 || longit < -180.0){
            Toast.makeText(getActivity(), "ERROR: Longitude has a max of 180 and a min of -180", Toast.LENGTH_SHORT).show();
            return;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latit, longit), 10);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onMapReady(GoogleMap map) {
        mMap = map;
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));
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

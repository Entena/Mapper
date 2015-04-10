package homework.entena.mapper;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MarkerList extends Fragment {
    private MarkersFragment model;

    public MarkerList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_list, container, false);
        ListView list = (ListView) view.findViewById(R.id.markerList);
        ArrayList<String> coord = new ArrayList<String>();
        model = (MarkersFragment) getFragmentManager().findFragmentByTag("MODEL");
        Vector<MarkerItem> marks = model.getMarkers();
        for(int i=0; i < model.getMarkers().size(); i++){
            coord.add(model.getMarker(i).created+"\n"+model.getMarker(i).latitude+","+model.getMarker(i).longitude);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,coord);
        list.setAdapter(arrayAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

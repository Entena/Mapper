package homework.entena.mapper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MarkerList extends Fragment {
    private MarkersFragment model;
    private View lastSelected = null;
    private int selected = -1;
    private ListView list;
    private EditText label;

    public MarkerList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void buildList(){
        ArrayList<String> coord = new ArrayList<String>();
        model = (MarkersFragment) getFragmentManager().findFragmentByTag("MODEL");
        Vector<MarkerItem> marks = model.getMarkers();
        for(int i=0; i < model.getMarkers().size(); i++){
            String label = "";
            if(model.getMarker(i).label != null){
                label = model.getMarker(i).label+"\n";
                label += model.getMarker(i).created+":"+model.getMarker(i).latitude+","+model.getMarker(i).longitude;
            } else {
                label = model.getMarker(i).created+"\n"+model.getMarker(i).latitude+","+model.getMarker(i).longitude;
            }
            coord.add(label);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,coord);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lastSelected != null){
                    lastSelected.setBackgroundColor(lastSelected.getDrawingCacheBackgroundColor());
                    if(lastSelected.equals(view)){
                        selected = -1;
                        return;
                    }
                }
                lastSelected = view;
                view.setBackgroundColor(getResources().getColor(R.color.highlighted_text_material_light));
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    markOnMap(position);
                }
                selected = position;
            }
        });
    }

    public void markOnMap(int position){
        mapFragment mf = (mapFragment) getFragmentManager().findFragmentByTag("MAP");
        mf.markerSelected(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_list, container, false);
        list = (ListView) view.findViewById(R.id.markerList);
        buildList();
        label = (EditText) view.findViewById(R.id.editLabel);
        Button addLabelBtn = (Button) view.findViewById(R.id.btnAddLabel);
        addLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(label.getText().toString().length() < 1){
                    Toast.makeText(getActivity(), "Enter a label name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selected == -1){
                    Toast.makeText(getActivity(), "Select a list item!", Toast.LENGTH_SHORT).show();
                    return;
                }
                model.getMarker(selected).label = label.getText().toString();
                buildList();
            }
        });
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

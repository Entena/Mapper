package homework.entena.mapper;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

public class MarkersFragment extends Fragment {
    private Vector<MarkerItem> markers;

    public MarkersFragment() {
        markers = new Vector<MarkerItem>();
    }

    public void addMarker(MarkerItem item){
        markers.add(item);
    }

    public void removeMarker(MarkerItem item){
        markers.remove(item);
    }

    public Vector<MarkerItem> getMarkers(){
        return markers;
    }

    public MarkerItem getMarker(int i){
        return markers.get(i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}

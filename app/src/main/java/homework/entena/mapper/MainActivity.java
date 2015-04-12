package homework.entena.mapper;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); mapFragment mf;
        FragmentTransaction fragmentTransaction;
        MarkersFragment model = (MarkersFragment) getFragmentManager().findFragmentByTag("MODEL");
        if(model == null){
            model = new MarkersFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(model, "MODEL");
            fragmentTransaction.commit();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.layout_port);
            cleanFragments();
            mf = new mapFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainScrollView, mf, "MAP");
            fragmentTransaction.commit();
        } else
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.layout_land);
            MarkerList ml;
            cleanFragments();
            ml = new MarkerList();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rightScrollView, ml, "LIST");
            fragmentTransaction.commit();
            mf = new mapFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.leftScrollView, mf, "MAP");
            fragmentTransaction.commit();
        }
    }

    public void cleanFragments(){
        mapFragment mf; MarkerList ml;
        mf = (mapFragment) getFragmentManager().findFragmentByTag("MAP");
        if(mf != null){
            getFragmentManager().beginTransaction().remove(mf).commit();
        }
        ml = (MarkerList) getFragmentManager().findFragmentByTag("LIST");
        if(ml != null){
            getFragmentManager().beginTransaction().remove(ml).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        FragmentTransaction fragmentTransaction;
        switch(id){
            case R.id.mapFrag:
                mapFragment mf;// = (mapFragment) getFragmentManager().findFragmentByTag("MAP");
                //if(mf == null){
                    mf = new mapFragment();
                //}
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, mf, "MAP");
                fragmentTransaction.commit();
                break;
            case R.id.listFrag:
                MarkerList ml;// = (MarkerList) getFragmentManager().findFragmentByTag("LIST");
                //if(ml == null){
                    ml = new MarkerList();
                //}
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, ml, "LIST");
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

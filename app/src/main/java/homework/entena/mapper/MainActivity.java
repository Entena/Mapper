package homework.entena.mapper;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_port);
        MarkersFragment model = new MarkersFragment();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(model, "MODEL");
        fragmentTransaction.commit();
        mapFragment mf = new mapFragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainScrollView, mf, "MAP");
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                mapFragment mf = (mapFragment) getFragmentManager().findFragmentByTag("MAP");
                if(mf == null){
                    mf = new mapFragment();
                }
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, mf, "MAP");
                fragmentTransaction.commit();
                break;
            case R.id.listFrag:
                MarkerList ml = (MarkerList) getFragmentManager().findFragmentByTag("LIST");
                if(ml == null){
                    ml = new MarkerList();
                }
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainScrollView, ml, "LIST");
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

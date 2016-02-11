package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapDisplayEntry extends Activity {

    private static final String ACTIVITY_KEY = "activity_key";
    private static final String CLIMB_KEY = "climb_key";
    private static final String LOCATION_KEY = "location_key";
    private static final String CALORIES_KEY = "calories_key";
    private static final String DISTANCE_KEY = "distance_key";
    private static final String POSITION_KEY = "position_key";
    private static final String UNIT_KEY = "unit_key";
    private static final String SPEED_KEY = "speed_key";
    private static final String DELETE_CALLED = "delete_key";
    private static final String EXERCISE_POSITION = "exercise_position_key";

    private int position;
    private int mUnits;
    private final double miPerKm = 0.621371;
    private double multiplier;

    private String mActivityType;
    private double mDistance;
    private double mAvgSpeed;
    private double mCurSpeed;
    private int mCalories;
    private double mClimb;
    private ArrayList<Location> locList;

    private GoogleMap map;
    private int MAP_ZOOM = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mUnits = getIntent().getIntExtra(UNIT_KEY,0);
        String unitType;

        if (mUnits == 0){
            unitType = "km";
            multiplier = 1;
        }
        else{
            unitType = "mi";
            multiplier = miPerKm;
        }

        mActivityType = getIntent().getStringExtra(ACTIVITY_KEY);
        mDistance = getIntent().getDoubleExtra(DISTANCE_KEY,0);
        mClimb = getIntent().getDoubleExtra(CLIMB_KEY, 0);
        mCalories = getIntent().getIntExtra(CALORIES_KEY, 0);
        mAvgSpeed = getIntent().getDoubleExtra(SPEED_KEY, 0);
        position = getIntent().getIntExtra(POSITION_KEY, -1);
        locList = getIntent().getParcelableArrayListExtra(LOCATION_KEY);

        MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFrag.getMap();
        map.setMyLocationEnabled(false);

        TextView dataText = (TextView) findViewById(R.id.data_text);

        dataText.setText("Type: " + mActivityType + "\nAvg Speed: " + round(mAvgSpeed*multiplier) + " " + unitType + "/h\nCur Speed: N/A"
                +"\nClimb: " + round(mClimb*multiplier) + " " + unitType + "\nCalories: "
                + mCalories + "\nDistance: " + round(mDistance*multiplier) + " " + unitType);

        if (locList.size() > 0) {
            PolylineOptions rectOptions = new PolylineOptions();
            for (Location l : locList) {
                rectOptions.add(new LatLng(l.getLatitude(), l.getLongitude()));
            }
            rectOptions.width(5)
                    .color(Color.BLUE);
            map.addPolyline(rectOptions);

            LatLng LatLngStart = new LatLng(locList.get(0).getLatitude(), locList.get(0).getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            map.addMarker(markerOptions.position(LatLngStart).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_dot)));
            LatLng LatLngEnd = new LatLng(locList.get(locList.size() - 1).getLatitude(), locList.get(locList.size() - 1).getLongitude());
            map.addMarker(markerOptions.position(LatLngEnd).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)));
            centerMapOnLocation(locList.get(0));
        }

        ((Button)findViewById(R.id.btnSave)).setVisibility(View.INVISIBLE);
        ((Button)findViewById(R.id.btnSave)).setClickable(false);
        ((Button)findViewById(R.id.btnCancel)).setVisibility(View.INVISIBLE);
        ((Button)findViewById(R.id.btnCancel)).setClickable(false);
    }

    public double round(double d){
        double d2 = Math.round(d*100);
        return d2/100;
    }

    private void centerMapOnLocation(Location loc) {
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                    MAP_ZOOM));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entry_delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_button) {
            Intent intent = new Intent();
            intent.putExtra(DELETE_CALLED, true);
            intent.putExtra(EXERCISE_POSITION, position);
            setResult(RESULT_OK, intent);
            Toast.makeText(getApplicationContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
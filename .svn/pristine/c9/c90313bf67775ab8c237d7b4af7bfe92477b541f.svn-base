package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;

public class MapActivity extends Activity implements ServiceConnection {

    private static final String ACTIVITY_NAME = "edu.dartmouth.cs.myruns.ACTIVITY_NAME";
    private static final String INPUT_TYPE = "edu.dartmouth.cs.myruns.INPUT_TYPE";
    private static final String UNIT_TYPE = "edu.dartmouth.cs.myruns.UNIT_TYPE";

    private static final String INPUT_KEY = "input_key";
    private static final String ACTIVITY_KEY = "activity_key";

    private int mInputType;
    private String mActivityType;

    private final double miPerKm = 0.621371;

    private double multiplier;
    private Marker mEndMarker;

    private String unitType;

    private boolean initial = false;
    private boolean isBound = false;

    private ExerciseDataSource datasource;
    private ExerciseEntry exerciseEntry;

    private TrackingService.TrackingBinder localTrackingServiceBinder;
    private Messenger mServiceMessenger = null;
    private ServiceConnection mConnection = this;


    private GoogleMap map;
    private int MAP_ZOOM = 20;

    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler());
    private TextView dataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mInputType = getIntent().getIntExtra(INPUT_TYPE, 0);
        mActivityType = getIntent().getStringExtra(ACTIVITY_NAME);

        datasource = new ExerciseDataSource(this);
        datasource.open();

        if (getIntent().getIntExtra(UNIT_TYPE, 1) == 0){
            unitType = "km";
            multiplier = 1;
        }
        else{
            unitType = "mi";
            multiplier = miPerKm;
        }

        MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFrag.getMap();
        map.setMyLocationEnabled(true);
        getApplicationContext().bindService(new Intent(this, TrackingService.class), mConnection,
                Context.BIND_AUTO_CREATE);
        isBound = true;
        dataText = (TextView) findViewById(R.id.data_text);
        if (savedInstanceState == null){
            initial = true;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        localTrackingServiceBinder = (TrackingService.TrackingBinder) service;
        IBinder messServ = localTrackingServiceBinder.getService().getMessenger();
        mServiceMessenger = new Messenger(messServ);
        try {
            Message msg = Message.obtain(null, TrackingService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do
            // anything with it
        }

        if (initial){
            try {
                Message msg = Message.obtain(null, TrackingService.MSG_BEGIN_TRACKING);
                Bundle bundle = new Bundle();
                bundle.putString(ACTIVITY_KEY, mActivityType);
                bundle.putInt(INPUT_KEY, mInputType);
                msg.setData(bundle);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do
                // anything with it
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // This is called when the connection with the service has been
        // unexpectedly disconnected - process crashed.
        mServiceMessenger = null;
    }

    private void centerMapOnLocation(Location loc) {
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                    MAP_ZOOM));
        }
    }

    // to be implemented
    public void saveExercise(View view){
        datasource.createExerciseEntry(exerciseEntry);
        Toast.makeText(getApplicationContext(), "Entry #" + Integer.toString(datasource.getRowNumber()) + " saved", Toast.LENGTH_SHORT).show();
        try {
            Message msg = Message.obtain(null, TrackingService.MSG_STOP_TRACKING);
            msg.replyTo = mMessenger;
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do
            // anything with it
        }
        getApplicationContext().unbindService(mConnection);
        isBound = false;

        finish();
    }

    public void cancelExercise(View view){
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        try {
            Message msg = Message.obtain(null, TrackingService.MSG_STOP_TRACKING);
            msg.replyTo = mMessenger;
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do
            // anything with it
        }
        getApplicationContext().unbindService(mConnection);
        isBound = false;
        finish();
    }

    @Override
    protected void onDestroy() {
        if (isBound){
            getApplicationContext().unbindService(mConnection);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        try {
            Message msg = Message.obtain(null, TrackingService.MSG_STOP_TRACKING);
            msg.replyTo = mMessenger;
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do
            // anything with it
        }
        getApplicationContext().unbindService(mConnection);
        isBound = false;
        finish();
    }

    public double round(double d){
        double d2 = Math.round(d*100);
        return d2/100;
    }

    /**
     * Handle incoming messages from TimerService
     */
    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrackingService.MSG_SEND_PATH:
                    exerciseEntry = localTrackingServiceBinder.getExerciseEntry();
                    PolylineOptions rectOptions = new PolylineOptions();

                    ArrayList<Location> locList = exerciseEntry.getLocationList();

                    for (Location l : locList) {
                        rectOptions.add(new LatLng(l.getLatitude(), l.getLongitude()));
                    }
                    rectOptions.width(5)
                            .color(Color.BLUE);
                    map.addPolyline(rectOptions);

                    if (locList.size()>1) {
                        LatLng LatLngStart = new LatLng(locList.get(0).getLatitude(), locList.get(0).getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        map.addMarker(markerOptions.position(LatLngStart).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_dot)));
                        LatLng LatLngEnd = new LatLng(locList.get(locList.size()-1).getLatitude(), locList.get(locList.size()-1).getLongitude());
                        if (mEndMarker == null){
                            mEndMarker = map.addMarker(markerOptions.position(LatLngEnd).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)));
                        }
                        else{
                            mEndMarker.setPosition(LatLngEnd);
                        }
                    }
                    centerMapOnLocation(exerciseEntry.getLastLocation());

                    dataText.setText("Type: " + exerciseEntry.getCurrentActivity() + "\nAvg Speed: " + round(exerciseEntry.getAvgSpeed()*multiplier) + " " + unitType + "/h\nCur Speed: "
                            + round(exerciseEntry.getCurSpeed()*multiplier) + " " + unitType + "/h\nClimb: " + round(exerciseEntry.getClimb()*multiplier) + " " + unitType + "\nCalories: "
                            + exerciseEntry.getCalories() + "\nDistance: " + round(exerciseEntry.getDistance()*multiplier) + " " + unitType);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
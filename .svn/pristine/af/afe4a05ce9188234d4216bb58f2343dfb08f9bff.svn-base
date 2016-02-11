package edu.dartmouth.cs.myruns;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    private static final String TAG = StartFragment.class.getSimpleName();
    private static final String ACTIVITY_NAME = "edu.dartmouth.cs.myruns.ACTIVITY_NAME";
    private static final String INPUT_TYPE = "edu.dartmouth.cs.myruns.INPUT_TYPE";
    private static final String UNIT_TYPE = "edu.dartmouth.cs.myruns.UNIT_TYPE";
    private static final int MANUAL_INPUT = 0;
    private static final int GPS_INPUT = 1;
    private static final int AUTO_INPUT = 2;

    static final String MANUAL = "Manual Entry";
    static final String GPS = "GPS";
    static final String AUTO = "Automatic Mode";

    private int mUnits;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Button startButton = (Button) view.findViewById(R.id.start_button);
        Button syncButton = (Button) view.findViewById(R.id.sync_button);
        final Spinner inputSpinner = (Spinner) view.findViewById(R.id.input_spinner);
        final Spinner activitySpinner = (Spinner) view.findViewById(R.id.activity_spinner);

        mUnits = ((StartListener) getActivity()).getUnits2();

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseDataSource datasource = new ExerciseDataSource(getActivity());
                datasource.open();

                ArrayList<ExerciseEntry> entries = datasource.getAllExerciseEntries();

                String[] inputs = {"Manual Entry", "GPS", "Automatic Mode"};

                String id;
                String input_type;
                String activity_type;
                Calendar datetime;
                String date;
                String distance;
                String duration;
                String speed;
                String calories;
                String climb;
                String heartrate;
                String comment;

                clearDatastore();

                for (ExerciseEntry entry : entries){
                    id = Long.toString(entry.getId());
                    input_type = inputs[entry.getInputType()];
                    activity_type = entry.getActivityType();
                    datetime = entry.getDateTime();
                    date = new SimpleDateFormat("hh:mm:ss MMM dd yyyy").format(datetime.getTime());
                    distance = Double.toString(entry.getDistance());
                    duration = Double.toString(entry.getDuration());
                    speed = Double.toString(entry.getAvgSpeed());
                    calories = Integer.toString(entry.getCalories());
                    climb = Double.toString(entry.getClimb());
                    heartrate = Integer.toString(entry.getHeartRate());
                    comment = entry.getComment();

                    postEntry(id, input_type, activity_type, date, duration, distance, speed, calories, climb, heartrate, comment);
                }
            }
        });

        // handle different activity intents based on spinner selections
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = inputSpinner.getSelectedItem().toString();
                String activityName = activitySpinner.getSelectedItem().toString();
                switch (selectedItem){
                    case MANUAL:
                        Log.d(TAG, "manual input activity called");

                        Intent manualIntent = new Intent(getActivity(), ManualInputActivity.class);
                        manualIntent.putExtra(ACTIVITY_NAME, activityName);

                        startActivityForResult(manualIntent, MANUAL_INPUT);
                        break;

                    case GPS:
                        Log.d(TAG, "gps activity called");

                        Intent gpsIntent = new Intent(getActivity(), MapActivity.class);

                        gpsIntent.putExtra(ACTIVITY_NAME, activityName);
                        gpsIntent.putExtra(UNIT_TYPE, mUnits);
                        gpsIntent.putExtra(INPUT_TYPE, 1);

                        startActivityForResult(gpsIntent, GPS_INPUT);
                        break;

                    case AUTO:
                        Log.d(TAG, "automatic activity called");

                        Intent autoIntent = new Intent(getActivity(), MapActivity.class);

                        autoIntent.putExtra(ACTIVITY_NAME, activityName);
                        autoIntent.putExtra(UNIT_TYPE, mUnits);
                        autoIntent.putExtra(INPUT_TYPE, 2);

                        startActivityForResult(autoIntent, AUTO_INPUT);
                        break;

                    default:
                        break;
                }

            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    private void postEntry(String id, String input_type, String activity_type, String date, String duration, String distance,
                           String speed, String calories, String climb, String heartrate, String comment) {

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... args) {
                String url = getString(R.string.server_addr) + "/post.do";
                String res = "";
                Map<String, String> params = new HashMap<String, String>();

                params.put("id_key", args[0]);
                params.put("input_key", args[1]);
                params.put("activity_key", args[2]);
                params.put("date_key", args[3]);
                params.put("duration_key", args[4]);
                params.put("distance_key", args[5]);
                params.put("speed_key", args[6]);
                params.put("calories_key", args[7]);
                params.put("climb_key", args[8]);
                params.put("heartrate_key", args[9]);
                params.put("comment_key", args[10]);

                try {
                    res = ServerUtilities.post(url, params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return res;
            }

            @Override
            protected void onPostExecute(String res) {

            }

        }.execute(id, input_type, activity_type, date, duration, distance, speed, calories, climb, heartrate, comment);
    }

    private void clearDatastore() {

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... args) {
                String url = getString(R.string.server_addr) + "/clear.do";
                String res = "";
                Map<String, String> params = new HashMap<String, String>();

                try {
                    res = ServerUtilities.post(url, params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return res;
            }

            @Override
            protected void onPostExecute(String res) {

            }

        }.execute();
    }

    public void updateData(){
        mUnits = ((StartListener) getActivity()).getUnits2();
    }

    public interface StartListener{
        public int getUnits2();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MANUAL_INPUT){
            ((SaveClicked) getActivity()).onSave();
        }
        if (requestCode == GPS_INPUT){
            ((SaveClicked) getActivity()).onSave();
        }
        if (requestCode == AUTO_INPUT){
            ((SaveClicked) getActivity()).onSave();
        }
    }

    public interface SaveClicked {
        public void onSave();
    }
}
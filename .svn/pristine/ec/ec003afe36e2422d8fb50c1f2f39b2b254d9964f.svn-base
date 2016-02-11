package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends ListFragment {

    private ArrayList<ExerciseEntry> ActivityHistory;
    private ExerciseDataSource datasource;
    private ExerciseAdapter mExerciseAdapter;
    private int mUnits;

    private static final String ACTIVITY_KEY = "activity_key";
    private static final String DATETIME_KEY = "date_time_key";
    private static final String DURATION_KEY = "duration_key";
    private static final String DISTANCE_KEY = "distance_key";
    private static final String CLIMB_KEY = "climb_key";
    private static final String POSITION_KEY = "position_key";
    private static final String UNIT_KEY = "unit_key";
    private static final String SPEED_KEY = "speed_key";
    private static final String LOCATION_KEY = "location_key";
    private static final String CALORIES_KEY = "calories_key";
    private static final String DELETE_CALLED = "delete_key";
    private static final String EXERCISE_POSITION = "exercise_position_key";
    private static final int DISPLAY_INTENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        datasource = new ExerciseDataSource(getActivity());
        datasource.open();

        ActivityHistory = datasource.getAllExerciseEntries();

        mExerciseAdapter = new ExerciseAdapter(getActivity(), R.layout.entry_view, ActivityHistory);

        // Assign the adapter to ListView
        setListAdapter(mExerciseAdapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    public void onListItemClick(ListView list, View v, int position, long id) {
        if (mExerciseAdapter.getItem(position).getInputType() == 0){
            Intent displayEntryIntent = new Intent(getActivity(), DisplayEntryActivity.class);
            displayEntryIntent.putExtra(ACTIVITY_KEY, mExerciseAdapter.getItem(position).getActivityType());

            Calendar datetime = mExerciseAdapter.getItem(position).getDateTime();
            String dateText = new SimpleDateFormat("hh:mm:ss MMM dd yyyy").format(datetime.getTime());
            displayEntryIntent.putExtra(DATETIME_KEY, dateText);

            displayEntryIntent.putExtra(DURATION_KEY, mExerciseAdapter.getItem(position).getDuration());
            displayEntryIntent.putExtra(DISTANCE_KEY, mExerciseAdapter.getItem(position).getDistance());
            displayEntryIntent.putExtra(UNIT_KEY, mUnits);
            displayEntryIntent.putExtra(POSITION_KEY, position);

            startActivityForResult(displayEntryIntent, DISPLAY_INTENT);
        }
        else{
            Intent mapEntryIntent = new Intent(getActivity(), MapDisplayEntry.class);
            mapEntryIntent.putExtra(ACTIVITY_KEY, mExerciseAdapter.getItem(position).getActivityType());
            mapEntryIntent.putExtra(SPEED_KEY, mExerciseAdapter.getItem(position).getAvgSpeed());
            mapEntryIntent.putExtra(LOCATION_KEY, mExerciseAdapter.getItem(position).getLocationList());
            mapEntryIntent.putExtra(CALORIES_KEY, mExerciseAdapter.getItem(position).getCalories());
            mapEntryIntent.putExtra(DURATION_KEY, mExerciseAdapter.getItem(position).getDuration());
            mapEntryIntent.putExtra(DISTANCE_KEY, mExerciseAdapter.getItem(position).getDistance());
            mapEntryIntent.putExtra(CLIMB_KEY, mExerciseAdapter.getItem(position).getClimb());
            mapEntryIntent.putExtra(UNIT_KEY, mUnits);
            mapEntryIntent.putExtra(POSITION_KEY, position);

            startActivityForResult(mapEntryIntent, DISPLAY_INTENT);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == DISPLAY_INTENT){
                if (data.getBooleanExtra(DELETE_CALLED, false)){
                    int position = data.getIntExtra(EXERCISE_POSITION, -1);
                    ExerciseEntry exerciseEntry = mExerciseAdapter.getItem(position);
                    datasource.deleteExerciseEntry(exerciseEntry);
                    updateData();
                    updateServer();
                }
            }
        }
    }

    public void updateData(){
        datasource.open();
        ActivityHistory = datasource.getAllExerciseEntries();
        mExerciseAdapter = new ExerciseAdapter(getActivity(), R.layout.entry_view, ActivityHistory);
        setListAdapter(mExerciseAdapter);
    }

    public interface HistListener{
        public int getUnits();
    }

    public void updateServer(){
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

    private class ExerciseAdapter extends ArrayAdapter<ExerciseEntry>{

        public ExerciseAdapter(Context context, int textViewResourceId, ArrayList<ExerciseEntry> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ExerciseEntry entry = getItem(position);
            Calendar datetime = entry.getDateTime();
            String dateText = new SimpleDateFormat("hh:mm:ss MMM dd yyyy").format(datetime.getTime());

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_view, parent, false);
            }

            String unitType;
            Double distance = entry.getDistance();
            mUnits = ((HistListener) getActivity()).getUnits();
            if (entry.getInputType() == 0){
                if (mUnits == 0){
                    unitType = "kilometers";
                    DecimalFormat df = new DecimalFormat("#.##");
                    distance = Double.parseDouble(df.format(distance * 1.61));
                }
                else{
                    DecimalFormat df = new DecimalFormat("#.##");
                    distance = Double.parseDouble(df.format(distance));
                    unitType = "miles";
                }
            }
            else{
                if (mUnits == 0){
                    unitType = "kilometers";
                    DecimalFormat df = new DecimalFormat("#.##");
                    distance = Double.parseDouble(df.format(distance));
                }
                else{
                    DecimalFormat df = new DecimalFormat("#.##");
                    distance = Double.parseDouble(df.format(distance*0.62));
                    unitType = "miles";
                }
            }


            int minutes = (int) Math.floor(entry.getDuration());
            int seconds = (int) Math.round((entry.getDuration()-minutes)*60);

            TextView text = (TextView) convertView.findViewById(R.id.exercise_entry);
            String sourceString = "<b>" + entry.getActivityType() + ", " + dateText + "</b> <br>" + Double.toString(distance) + " " + unitType + ", " + Integer.toString(minutes) + " minutes " + Integer.toString(seconds) + " seconds";
            text.setText(Html.fromHtml(sourceString));

            return convertView;
        }
    }

}
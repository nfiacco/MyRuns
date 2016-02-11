package edu.dartmouth.cs.myruns;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ManualInputActivity extends ListActivity implements DateFragment.DateSetListener, TimeFragment.TimeSetListener, DynamicDialog.PositiveListener {

    private static final String ACTIVITY_NAME = "edu.dartmouth.cs.myruns.ACTIVITY_NAME";
    static final String[] STATISTICS = new String[] { "Date","Time","Duration","Distance","Calories","Heart Rate","Comment"};

    private static String INPUT_KEY = "input_key";
    private static String ACTIVITY_KEY = "activity_key";
    private static String DATE_KEY = "date_key";
    private static String DURATION_KEY = "duration_key";
    private static String DISTANCE_KEY = "distance_key";
    private static String PACE_KEY = "pace_key";
    private static String SPEED_KEY = "speed_key";
    private static String CALORIES_KEY = "calories_key";
    private static String HEART_KEY = "heart_key";
    private static String COMMENT_KEY = "comment_key";


    private int mInputType;
    private String mActivityType;
    private Calendar mDateTime;
    private double mDuration;
    private double mDistance;
    private double mAvgPace;
    private double mAvgSpeed;
    private int mCalories;
    private int mHeartRate;
    private String mComment;

    private ExerciseDataSource datasource;
    private ExerciseEntry exerciseEntry;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        mDateTime.set(year, month, day);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        mDateTime.set(Calendar.HOUR_OF_DAY, hour);
        mDateTime.set(Calendar.MINUTE, minute);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);

        datasource = new ExerciseDataSource(this);
        datasource.open();

        exerciseEntry = new ExerciseEntry();

        mInputType = 0;
        mActivityType = getIntent().getStringExtra(ACTIVITY_NAME);
        mDateTime = Calendar.getInstance();
        mDuration = 0;
        mDistance = 0;

        if (savedInstanceState != null){
            mInputType = savedInstanceState.getInt(INPUT_KEY);
            mActivityType = savedInstanceState.getString(ACTIVITY_KEY);

            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss MMM dd yyyy");
            Date datetime;
            try {
                datetime = format.parse(savedInstanceState.getString(DATE_KEY));
            } catch (ParseException e) {
                datetime = null;
            }

            if (datetime != null){
                mDateTime.setTime(datetime);
            }

            mDuration = savedInstanceState.getFloat(DURATION_KEY);
            mDistance = savedInstanceState.getFloat(DISTANCE_KEY);
            mAvgPace = savedInstanceState.getFloat(PACE_KEY);
            mAvgSpeed = savedInstanceState.getFloat(SPEED_KEY);
            mCalories = savedInstanceState.getInt(CALORIES_KEY);
            mHeartRate = savedInstanceState.getInt(HEART_KEY);
            mComment = savedInstanceState.getString(COMMENT_KEY);
        }

        // Define a new adapter
        final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                R.layout.list_element, STATISTICS);

        // Assign the adapter to ListView
        setListAdapter(mAdapter);

        // Define the listener interface
        AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position){
                    case 0:
                        new DateFragment().show(getFragmentManager(), "tag");
                        break;
                    case 1:
                        new TimeFragment().show(getFragmentManager(), "tag");
                        break;
                    case 2:
                        DynamicDialog durationDialog = new DynamicDialog();
                        durationDialog.setTitle("Duration (minutes):");
                        durationDialog.setReturnType(0);
                        durationDialog.show(getFragmentManager(), "tag");
                        break;
                    case 3:
                        DynamicDialog distanceDialog = new DynamicDialog();
                        distanceDialog.setTitle("Distance (Miles):");
                        distanceDialog.setReturnType(1);
                        distanceDialog.show(getFragmentManager(), "tag");
                        break;
                    case 4:
                        DynamicDialog caloriesDialog = new DynamicDialog();
                        caloriesDialog.setTitle("Calories:");
                        caloriesDialog.setReturnType(2);
                        caloriesDialog.show(getFragmentManager(), "tag");
                        break;
                    case 5:
                        DynamicDialog heartRateDialog = new DynamicDialog();
                        heartRateDialog.setTitle("Average Heart Rate (BPM):");
                        heartRateDialog.setReturnType(3);
                        heartRateDialog.show(getFragmentManager(), "tag");
                        break;
                    case 6:
                        DynamicDialog commentDialog = new DynamicDialog();
                        commentDialog.setTitle("Comment:");
                        commentDialog.setReturnType(4);
                        commentDialog.show(getFragmentManager(), "tag");
                        break;
                    default:
                        break;
                }
            }

        };

        // Get the ListView and wire up the listener
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(mListener);

        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_input_buttons, null, false);
        listView.addFooterView(footerView);

    }

    // to be implemented
    public void saveExercise(View view){
        exerciseEntry.setInputType(mInputType);
        exerciseEntry.setActivityType(mActivityType);
        String date = mDateTime.toString();
        exerciseEntry.setDateTime(mDateTime);
        exerciseEntry.setDuration(mDuration);
        exerciseEntry.setDistance(mDistance);
        if (mDistance != 0 && mDuration != 0){
            exerciseEntry.setAvgPace(mDuration/mDistance);
        }

        if (mDistance != 0 && mDuration != 0){
            exerciseEntry.setAvgSpeed(mDistance/mDuration);
        }
        exerciseEntry.setCalories(mCalories);
        exerciseEntry.setHeartRate(mHeartRate);
        exerciseEntry.setComment(mComment);

        datasource.createExerciseEntry(exerciseEntry);

        Toast.makeText(getApplicationContext(), "Entry #" + Integer.toString(datasource.getRowNumber()) + " saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelExercise(View view){
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INPUT_KEY, mInputType);
        outState.putString(ACTIVITY_KEY, mActivityType);

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss MMM dd yyyy");
        String datetext = format.format(mDateTime.getTime());
        outState.putString(DATE_KEY, datetext);

        outState.putDouble(DURATION_KEY, mDuration);
        outState.putDouble(DISTANCE_KEY, mDistance);
        outState.putDouble(PACE_KEY, mAvgPace);
        outState.putDouble(SPEED_KEY, mAvgSpeed);
        outState.putInt(CALORIES_KEY, mCalories);
        outState.putInt(HEART_KEY, mHeartRate);
        outState.putString(COMMENT_KEY, mComment);
    }

    public void setDuration(double value){
        mDuration = value;
    }
    public void setDistance(double value){
        mDistance = value;
    }
    public void setCalories(int value){
        mCalories = value;
    }
    public void setHeartRate(int value){
        mHeartRate = value;
    }
    public void setComment(String value){
        mComment = value;
    }

}

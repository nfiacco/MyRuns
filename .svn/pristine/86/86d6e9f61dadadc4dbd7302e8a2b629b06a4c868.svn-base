package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class DisplayEntryActivity extends Activity {

    private static final String ACTIVITY_KEY = "activity_key";
    private static final String DATETIME_KEY = "date_time_key";
    private static final String DURATION_KEY = "duration_key";
    private static final String DISTANCE_KEY = "distance_key";
    private static final String POSITION_KEY = "position_key";
    private static final String UNIT_KEY = "unit_key";
    private static final String DELETE_CALLED = "delete_key";
    private static final String EXERCISE_POSITION = "exercise_position_key";

    private int position;
    private int mUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        TextView activity_type = (TextView) findViewById(R.id.activity_type);
        TextView date_time = (TextView) findViewById(R.id.date_time);
        TextView duration = (TextView) findViewById(R.id.duration);
        TextView distance = (TextView) findViewById(R.id.distance);

        mUnits = getIntent().getIntExtra(UNIT_KEY,0);
        String unitType;

        double mDistance = getIntent().getDoubleExtra(DISTANCE_KEY,0);
        DecimalFormat df = new DecimalFormat("#.##");
        if (mUnits == 0){
            unitType = " kilometers";
            mDistance = Double.parseDouble(df.format(mDistance * 1.61));
        }
        else{
            unitType = " miles";
            mDistance = Double.parseDouble(df.format(mDistance));
        }

        activity_type.setText(getIntent().getStringExtra(ACTIVITY_KEY));
        date_time.setText(getIntent().getStringExtra(DATETIME_KEY));

        int minutes = (int) Math.floor(getIntent().getDoubleExtra(DURATION_KEY,0));
        int seconds = (int) Math.round((getIntent().getDoubleExtra(DURATION_KEY, 0)-minutes)*60);

        duration.setText(Integer.toString(minutes) + " minutes " + Integer.toString(seconds) + " seconds");

        distance.setText(Double.toString(mDistance) + unitType);

        position = getIntent().getIntExtra(POSITION_KEY, -1);

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
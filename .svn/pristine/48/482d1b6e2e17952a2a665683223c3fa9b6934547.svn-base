package edu.dartmouth.cs.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nick on 1/31/15.
 */
public class ExerciseDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_INPUT_TYPE, MySQLiteHelper.COLUMN_ACTIVITY, MySQLiteHelper.COLUMN_DATE_TIME,
            MySQLiteHelper.COLUMN_DURATION, MySQLiteHelper.COLUMN_DISTANCE, MySQLiteHelper.COLUMN_PACE, MySQLiteHelper.COLUMN_SPEED, MySQLiteHelper.COLUMN_CALORIES,
            MySQLiteHelper.COLUMN_CLIMB, MySQLiteHelper.COLUMN_HEARTRATE, MySQLiteHelper.COLUMN_COMMENT, MySQLiteHelper.COLUMN_PRIVACY, MySQLiteHelper.COLUMN_GPS };

    private static final String TAG = "edu.dartmouth.cs.myruns.ExerciseDataSource";
    public static final String TABLE_ENTRIES = "ENTRIES";

    public ExerciseDataSource(Context context) {
        dbHelper = MySQLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createExerciseEntry(ExerciseEntry exercise) {

        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_INPUT_TYPE, exercise.getInputType());
        values.put(MySQLiteHelper.COLUMN_ACTIVITY, exercise.getActivityType());

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss MMM dd yyyy");
        String datetext = format.format(exercise.getDateTime().getTime());

        values.put(MySQLiteHelper.COLUMN_DATE_TIME, datetext);
        values.put(MySQLiteHelper.COLUMN_DURATION, exercise.getDuration());
        values.put(MySQLiteHelper.COLUMN_DISTANCE, exercise.getDistance());
        values.put(MySQLiteHelper.COLUMN_PACE, exercise.getAvgPace());
        values.put(MySQLiteHelper.COLUMN_SPEED, exercise.getAvgSpeed());
        values.put(MySQLiteHelper.COLUMN_CALORIES, exercise.getCalories());
        values.put(MySQLiteHelper.COLUMN_CLIMB, exercise.getClimb());
        values.put(MySQLiteHelper.COLUMN_HEARTRATE, exercise.getHeartRate());
        values.put(MySQLiteHelper.COLUMN_COMMENT, exercise.getComment());
        //values.put(MySQLiteHelper.COLUMN_PRIVACY, exercise.getPrivacy());

        if (exercise.getLocationList() != null) {
            byte[] bytes = exercise.getLocationByteArray();
            values.put(MySQLiteHelper.COLUMN_GPS, bytes);
        }
        return database.insert(MySQLiteHelper.TABLE_ENTRIES, null, values);
    }

    public void deleteExerciseEntry(ExerciseEntry exercise) {
        long id = exercise.getId();
        Log.d(TAG, "delete exercise = " + id);
        System.out.println("ExerciseEntry deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ENTRIES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteEntryById(long id){
        System.out.println("ExerciseEntry deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ENTRIES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllExerciseEntries() {
        System.out.println("ExerciseEntry deleted all");
        Log.d(TAG, "delete all = ");
        database.delete(MySQLiteHelper.TABLE_ENTRIES, null, null);
    }

    public ArrayList<ExerciseEntry> getAllExerciseEntries() {
        ArrayList<ExerciseEntry> exercises = new ArrayList<ExerciseEntry>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ENTRIES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ExerciseEntry exercise = cursorToExerciseEntry(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return exercises;
    }

    private ExerciseEntry cursorToExerciseEntry(Cursor cursor) {
        ExerciseEntry exercise = new ExerciseEntry();

        exercise.setId(cursor.getLong(0));
        exercise.setInputType(cursor.getInt(1));
        exercise.setActivityType(cursor.getString(2));

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss MMM dd yyyy");
        Date datetime;
        try {
            datetime = format.parse(cursor.getString(3));
        } catch (ParseException e) {
            datetime = null;
        }

        Calendar calendar = Calendar.getInstance();

        if (datetime != null){
            calendar.setTime(datetime);
        }

        exercise.setDateTime(calendar);
        exercise.setDuration(cursor.getFloat(4));
        exercise.setDistance(cursor.getFloat(5));
        exercise.setAvgPace(cursor.getFloat(6));
        exercise.setAvgSpeed(cursor.getFloat(7));
        exercise.setCalories(cursor.getInt(8));
        exercise.setClimb(cursor.getFloat(9));
        exercise.setHeartRate(cursor.getInt(10));
        exercise.setComment(cursor.getString(11));
        // exercise.setPrivacy(cursor.getInt(12));
        ArrayList<Location> locList = new ArrayList<Location>();
        if (cursor.getBlob(13) != null) {
            exercise.setLocationListFromByteArray(cursor.getBlob(13));
        }
        return exercise;
    }

    public static byte[] createByteArray(ArrayList<Location> locList) throws IOException {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        for (Location l : locList) {
            dout.writeDouble(l.getLatitude());
            dout.writeDouble(l.getLongitude());
        }
        dout.close();
        byte[] asBytes = bout.toByteArray();

        return asBytes;
    }

    public static ArrayList<Location> readFromDB(byte[] bytes) throws IOException, SQLException{
        ArrayList<Location> locList = new ArrayList<Location>();
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        DataInputStream din = new DataInputStream(bin);
        for(int i = 0; i<bytes.length-2; i+=2){
            double lat = din.readDouble();
            double lon = din.readDouble();
            Location location = new Location("");
            location.setLatitude(lat);
            location.setLongitude(lon);
            locList.add(location);
        }
        return locList;
    }

    public int getRowNumber(){
        String countQuery = "SELECT  * FROM " + TABLE_ENTRIES;
        Cursor cursor = database.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
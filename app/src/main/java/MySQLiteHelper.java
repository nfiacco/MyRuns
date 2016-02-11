package edu.dartmouth.cs.myruns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nick on 1/31/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static MySQLiteHelper mInstance;

    public static final String TABLE_ENTRIES = "ENTRIES";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INPUT_TYPE = "input_type";
    public static final String COLUMN_ACTIVITY = "activity_type";
    public static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_PACE = "avg_pace";
    public static final String COLUMN_SPEED = "avg_speed";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_CLIMB = "climb";
    public static final String COLUMN_HEARTRATE = "heartrate";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_PRIVACY = "privacy";
    public static final String COLUMN_GPS = "gps_data";

    private static final String DATABASE_NAME = "entries.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ENTRIES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_INPUT_TYPE + " INTEGER NOT NULL, "
            + COLUMN_ACTIVITY + " TEXT NOT NULL, "
            + COLUMN_DATE_TIME + " TEXT NOT NULL, "
            + COLUMN_DURATION + " FLOAT NOT NULL, "
            + COLUMN_DISTANCE + " FLOAT NOT NULL, "
            + COLUMN_PACE + " FlOAT, "
            + COLUMN_SPEED + " FlOAT, "
            + COLUMN_CALORIES + " INTEGER, "
            + COLUMN_CLIMB + " FlOAT, "
            + COLUMN_HEARTRATE + " INTEGER, "
            + COLUMN_COMMENT + " TEXT, "
            + COLUMN_PRIVACY + " INTEGER, "
            + COLUMN_GPS + " BLOB )";

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MySQLiteHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

}
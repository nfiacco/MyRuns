package edu.dartmouth.cs.myruns;

import android.location.Location;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nick on 1/30/15.
 */
public class ExerciseEntry {
    private Long id;

    private int mInputType;        // Manual, GPS or automatic
    private String mActivityType;     // Running, cycling etc.
    private Calendar mDateTime;    // When does this entry happen
    private double mDuration;         // Exercise duration in seconds
    private double mDistance;      // Distance traveled. Either in meters or feet.
    private double mAvgPace;       // Average pace
    private double mAvgSpeed;      // Average speed
    private double mCurSpeed;
    private int mCalories;          // Calories burnt
    private double mClimb;         // Climb. Either in meters or feet.
    private int mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private ArrayList<Location> mLocationList; // Location list
    private Location mLastLocation;
    private String mCurrentActivity;

    private static final String[] ACTIVITY_LIST = {"Standing", "Walking", "Running", "Sitting", "Hiking", "Biking", "Swimming", "Alpine Skiing", "XC Skiing", "Skating", "Elliptical","Other"};

    private int[] mActivityCount = new int[4];
    private double[] mActivityWeight = new double[4];

    public ExerciseEntry() {
        mLocationList = new ArrayList<Location>();

        mActivityCount = new int[4];
        mActivityWeight = new double[4];

        mActivityWeight[0] = 0.6D;
        mActivityWeight[1] = 0.6D;
        mActivityWeight[2] = 1D;
        mActivityWeight[3] = 1D;

        mActivityCount[0] = 0;
        mActivityCount[1] = 0;
        mActivityCount[2] = 0;
        mActivityCount[3] = 0;
    }

    // Getters

    public long getId() {
        return id;
    }

    public int getInputType() {
        return mInputType;
    }

    public String getActivityType() {
        return mActivityType;
    }

    public Calendar getDateTime() {
        return mDateTime;
    }

    public double getDuration() {
        return mDuration;
    }

    public double getDistance() {
        return mDistance;
    }

    public double getAvgPace() {
        return mAvgPace;
    }

    public double getAvgSpeed() {
        return mAvgSpeed;
    }

    public int getCalories() {
        return mCalories;
    }

    public double getClimb() {
        return mClimb;
    }

    public int getHeartRate() {
        return mHeartRate;
    }

    public String getComment() {
        return mComment;
    }

    public ArrayList<Location> getLocationList() {
        return mLocationList;
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    public double getCurSpeed() {
        return mCurSpeed;
    }

    public String getCurrentActivity() {
        return mCurrentActivity;
    }

    //Setters

    public void setId(long id) {
        this.id = id;
    }

    public void setInputType(int mInputType) {
        this.mInputType = mInputType;
    }

    public void setActivityType(String mActivityType) {
        this.mActivityType = mActivityType;
    }

    public void setCurrentActivity(String mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    public void setDateTime(Calendar mDateTime) {
        this.mDateTime = mDateTime;
    }

    public void setDuration(double mDuration) {
        this.mDuration = mDuration;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public void setAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public void setAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public void setCalories(int mCalories) {
        this.mCalories = mCalories;
    }

    public void setClimb(double mClimb) {
        this.mClimb = mClimb;
    }

    public void setHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public void setLocationList(ArrayList<Location> mLocationList) {
        this.mLocationList = mLocationList;
    }

    public void updateDuration(){
        mDuration = (System.currentTimeMillis() - mDateTime.getTimeInMillis()) / 60000D;
        double dur = mDuration * 60;
        if (mDuration != 0) {
            mAvgSpeed = (mDistance / dur);
        }
    }

    public void insertLocation(Location location){
        try{
            mLocationList.add(location);

            if (mLastLocation == null){
                setAvgSpeed(0);
                setClimb(0);
                setAvgSpeed(0);
                setClimb(0);
                setDistance(0);
                setCalories(0);
            }
            else{
                updateDuration();
                mCurSpeed = location.getSpeed();
                mDistance += Math.abs(location.distanceTo(mLastLocation))/1000;
                mClimb += (location.getAltitude() - mLastLocation.getAltitude())/1000;
                mCalories = ((int)(mDistance / 15.0D));
            }

            mLastLocation = location;
        }
        finally{
        }
    }

    public byte[] getLocationByteArray()
    {
        int[] arrayOfInt = new int[2 * mLocationList.size()];
        for (int i = 0; i < this.mLocationList.size(); i++)
        {
            arrayOfInt[(2 * i)] = ((int)(1000000.0D * (mLocationList.get(i)).getLatitude()));
            arrayOfInt[(1 + 2 * i)] = ((int)(1000000.0D * (mLocationList.get(i)).getLongitude()));
        }
        ByteBuffer localByteBuffer = ByteBuffer.allocate(32 * arrayOfInt.length);
        localByteBuffer.asIntBuffer().put(arrayOfInt);
        return localByteBuffer.array();
    }

    public void setLocationListFromByteArray(byte[] bytes)
    {
        IntBuffer localIntBuffer = ByteBuffer.wrap(bytes).asIntBuffer();
        int[] arrayOfInt = new int[bytes.length / 32];
        localIntBuffer.get(arrayOfInt);
        int i = arrayOfInt.length / 2;
        for (int j = 0; j < i; j++)
        {
            Location location = new Location("");
            location.setLatitude(arrayOfInt[(2 * j)] / 1000000.0D);
            location.setLongitude(arrayOfInt[(1 + 2 * j)] / 1000000.0D);
            mLocationList.add(location);
        }
    }

    public void updateActivityWeighted(int paramInt)
    {
        if ((paramInt >= 0) && (paramInt < mActivityCount.length)){
            mActivityCount[paramInt] = (1 + mActivityCount[paramInt]);
        }

        double max = 0.0;
        int i = -1;
        for (int j = 0; j < mActivityCount.length; j++)
        {
            double curWeight = mActivityCount[j] * mActivityWeight[j];
            if (curWeight > max)
            {
                max = curWeight;
                i = j;
            }
        }
        if (i >= 0) {
            setActivityType(ACTIVITY_LIST[i]);
        }
    }
}
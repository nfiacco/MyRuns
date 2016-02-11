package edu.dartmouth.cs.myrunsserver.server.data;

import com.google.appengine.api.datastore.Key;

public class PostEntity {
	public static String ENTITY_KIND_PARENT = "PostParent";
	public static String ENTITY_PARENT_KEY = ENTITY_KIND_PARENT;
	public static String ENTITY_KIND_POST = "Post";

	public static String FIELD_NAME_ID = "ID";
	public static String FIELD_NAME_INPUT = "Input";
	public static String FIELD_NAME_ACTIVITY = "Activity";
	public static String FIELD_NAME_DATE = "Date";
	public static String FIELD_NAME_DURATION = "Duration";
	public static String FIELD_NAME_DISTANCE = "Distance";
	public static String FIELD_NAME_SPEED = "Speed";
	public static String FIELD_NAME_CALORIES = "Calories";
	public static String FIELD_NAME_CLIMB = "Climb";
	public static String FIELD_NAME_HEARTRATE = "Heartrate";
	public static String FIELD_NAME_COMMENT = "Comment";
	
	private String mId;
	private String mInputType;
	private String mActivityType;
	private String mDateTime;
    private String mDuration;
    private String mDistance;
    private String mAvgSpeed;
    private String mCalories;
    private String mClimb;
    private String mHeartRate;
    private String mComment;
    
    private Key mKey;

	public PostEntity() {
	}
	
	public void setKey(Key key){
		this.mKey = key;
	}

    public void setId(String id) {
        this.mId = id;
    }

    public void setInputType(String mInputType) {
        this.mInputType = mInputType;
    }
    
    public void setActivityType(String mActivityType){
    	this.mActivityType = mActivityType;
    }
    
	public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public void setDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public void setAvgSpeed(String mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public void setCalories(String mCalories) {
        this.mCalories = mCalories;
    }

    public void setClimb(String mClimb) {
        this.mClimb = mClimb;
    }

    public void setHeartRate(String mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }
    
    public Key getKey(){
    	return mKey;
    }
    
    public String getId() {
        return mId;
    }

    public String getInputType() {
        return mInputType;
    }

    public String getActivityType() {
        return mActivityType;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getDistance() {
        return mDistance;
    }

    public String getAvgSpeed() {
        return mAvgSpeed;
    }

    public String getCalories() {
        return mCalories;
    }

    public String getClimb() {
        return mClimb;
    }

    public String getHeartRate() {
        return mHeartRate;
    }

    public String getComment() {
        return mComment;
    }
}

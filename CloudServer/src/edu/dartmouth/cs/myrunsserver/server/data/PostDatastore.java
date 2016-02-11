package edu.dartmouth.cs.myrunsserver.server.data;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class PostDatastore {
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getParentKey() {
		return KeyFactory.createKey(PostEntity.ENTITY_KIND_PARENT,
				PostEntity.ENTITY_PARENT_KEY);
	}

	private static void createParentEntity() {
		Entity entity = new Entity(getParentKey());

		mDatastore.put(entity);
	}

	public static boolean add(PostEntity post) {
		Key parentKey = getParentKey();
		try {
			mDatastore.get(parentKey);
		} catch (Exception ex) {
			createParentEntity();
		}

		Entity entity = new Entity(PostEntity.ENTITY_KIND_POST, parentKey);

		entity.setProperty(PostEntity.FIELD_NAME_ID, post.getId());
		entity.setProperty(PostEntity.FIELD_NAME_INPUT, post.getInputType());
		entity.setProperty(PostEntity.FIELD_NAME_ACTIVITY, post.getActivityType());
		entity.setProperty(PostEntity.FIELD_NAME_DATE, post.getDateTime());
		entity.setProperty(PostEntity.FIELD_NAME_DURATION, post.getDuration());
		entity.setProperty(PostEntity.FIELD_NAME_DISTANCE, post.getDistance());
		entity.setProperty(PostEntity.FIELD_NAME_SPEED, post.getAvgSpeed());
		entity.setProperty(PostEntity.FIELD_NAME_CALORIES, post.getCalories());
		entity.setProperty(PostEntity.FIELD_NAME_CLIMB, post.getClimb());
		entity.setProperty(PostEntity.FIELD_NAME_HEARTRATE, post.getHeartRate());
		entity.setProperty(PostEntity.FIELD_NAME_COMMENT, post.getComment());

		mDatastore.put(entity);

		return true;
	}

	public static ArrayList<PostEntity> query() {
		ArrayList<PostEntity> resultList = new ArrayList<PostEntity>();
		Query query = new Query(PostEntity.ENTITY_KIND_POST);
		query.setFilter(null);
		query.setAncestor(getParentKey());
		query.addSort(PostEntity.FIELD_NAME_DATE, SortDirection.ASCENDING);
		PreparedQuery pq = mDatastore.prepare(query);

		for (Entity entity : pq.asIterable()) {
			PostEntity post = new PostEntity();
			
			post.setKey(entity.getKey());
			
			post.setId(entity.getProperty(PostEntity.FIELD_NAME_ID).toString());
			post.setInputType(entity.getProperty(PostEntity.FIELD_NAME_INPUT).toString());
			post.setActivityType(entity.getProperty(PostEntity.FIELD_NAME_ACTIVITY).toString());
			post.setDateTime(entity.getProperty(PostEntity.FIELD_NAME_DATE).toString());
			post.setDuration(entity.getProperty(PostEntity.FIELD_NAME_DURATION).toString());
			post.setDistance(entity.getProperty(PostEntity.FIELD_NAME_DISTANCE).toString());
			post.setAvgSpeed(entity.getProperty(PostEntity.FIELD_NAME_SPEED).toString());
			post.setCalories(entity.getProperty(PostEntity.FIELD_NAME_CALORIES).toString());
			post.setClimb(entity.getProperty(PostEntity.FIELD_NAME_CLIMB).toString());
			post.setHeartRate(entity.getProperty(PostEntity.FIELD_NAME_HEARTRATE).toString());
			post.setComment(entity.getProperty(PostEntity.FIELD_NAME_COMMENT).toString());
			
			resultList.add(post);
		}
		
		return resultList;
		
	}
	
	public static void clear(){
		ArrayList<PostEntity> postList= query();
		for (PostEntity post : postList){
			mDatastore.delete(post.getKey());
		}
	}
	
	public static void delete(Key key){
		mDatastore.delete(key);
	}

}

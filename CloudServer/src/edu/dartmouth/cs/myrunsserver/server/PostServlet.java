package edu.dartmouth.cs.myrunsserver.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.myrunsserver.server.data.PostDatastore;
import edu.dartmouth.cs.myrunsserver.server.data.PostEntity;

public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String id = req.getParameter("id_key");
		String input_type = req.getParameter("input_key");
		String activity_type = req.getParameter("activity_key");
		String date = req.getParameter("date_key");
		String duration = req.getParameter("duration_key");
		String distance = req.getParameter("distance_key");
		String speed = req.getParameter("speed_key");
		String calories = req.getParameter("calories_key");
		String climb = req.getParameter("climb_key");
		String heartrate = req.getParameter("heartrate_key");
		String comment = req.getParameter("comment_key");
		
		PostEntity entity = new PostEntity();
		entity.setId(id);
		entity.setInputType(input_type);
		entity.setDateTime(date);
		entity.setActivityType(activity_type);
		entity.setDistance(distance);
		entity.setDuration(duration);
		entity.setAvgSpeed(speed);
		entity.setCalories(calories);
		entity.setClimb(climb);
		entity.setHeartRate(heartrate);
		entity.setComment(comment);

		PostDatastore.add(entity);
		
		resp.sendRedirect("/query.do");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
}

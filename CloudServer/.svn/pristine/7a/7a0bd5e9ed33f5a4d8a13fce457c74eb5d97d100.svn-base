package edu.dartmouth.cs.myrunsserver.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;

import edu.dartmouth.cs.myrunsserver.gcm.Message;
import edu.dartmouth.cs.myrunsserver.gcm.Sender;
import edu.dartmouth.cs.myrunsserver.server.data.PostDatastore;
import edu.dartmouth.cs.myrunsserver.server.data.PostEntity;
import edu.dartmouth.cs.myrunsserver.server.data.RegDatastore;

public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_RETRY = 5;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		String id = req.getParameter("id_key");
		
		ArrayList<PostEntity> postList = PostDatastore.query();
		
		Key mKey = null;
		
		for (PostEntity post : postList){
			
			if (post.getId().equals(id)){
				mKey = post.getKey();
			}
		}
		
		if (mKey != null){
			System.out.println("good");
			PostDatastore.delete(mKey);
		}

		// notify
		List<String> devices = RegDatastore.getDevices();

		Message message = new Message(devices);
		message.addData("id_key", id);

		// Have to hard-code the API key when creating the Sender
		Sender sender = new Sender(Globals.GCMAPIKEY);
		
		// Send the message to device, at most retrying MAX_RETRY times
		sender.send(message, MAX_RETRY);
		
		resp.sendRedirect("/query.do");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
}

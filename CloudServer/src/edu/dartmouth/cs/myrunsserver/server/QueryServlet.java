package edu.dartmouth.cs.myrunsserver.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.myrunsserver.server.data.PostDatastore;
import edu.dartmouth.cs.myrunsserver.server.data.PostEntity;

public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<PostEntity> postList = PostDatastore.query();
		
		req.setAttribute("postList", postList);
		
		getServletContext().getRequestDispatcher("/main.jsp").forward(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
	
}

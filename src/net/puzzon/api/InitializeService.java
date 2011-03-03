//                                                      
//Copyright (c) 2010 PuzzOn.net
//

package net.puzzon.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * 
 * Initialize Service
 * 
 */
public class InitializeService extends HttpServlet {
	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 1506149126993031478L;

	/**
	 * GET /api/init/items/*
	 * 
	 * /api/init/items?start=...&results=...
	 */
	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		final PrintWriter out = resp.getWriter();

		try {
			final int start = Integer.parseInt(req.getParameter("start"));
			final int results = Integer.parseInt(req.getParameter("results"));

			out.write(getItems(start, results));
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * json: [ {id:...}, {id:...}, ... ]
	 */
	private String getItems(int start, int results) {
		final JSONArray json = new JSONArray();

		if (start < 1)  start = 1;
		if (start > 12)	start = 12;
		
		if (results < 0) results = 0;
		if (results + start > 12 + 1) results = 12 + 1 - start;

		try {

			for (int i = start; i < start + results; ++i) {
				json.put((new JSONObject().put("id", i)));
			}
		} catch (final JSONException ex) {
			ex.printStackTrace();
		}

		return json.toString();
	}
}
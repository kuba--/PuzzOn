//                                                      
//Copyright (c) 2010 PuzzOn.net
//

package net.puzzon.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.puzzon.T_Puzzle;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * 
 * Puzzle Service
 * 
 */
public class PuzzleService extends HttpServlet {
	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 8632812917289131826L;

	/**
	 * GET /api/puzzle/*
	 * 
	 * /api/puzzle/items?start=...&results=...
	 * 
	 * /api/puzzle?id=...
	 */
	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");		
		
		final PrintWriter out = resp.getWriter();
		String path = req.getPathInfo();		

		if ("/items".equalsIgnoreCase(path)) {
			
			try {
				int start = Integer.parseInt(req.getParameter("start"));
				int results = Integer.parseInt(req.getParameter("results"));
				
				out.write(getItems(start, results));
			}
			catch(NumberFormatException ex){
				ex.printStackTrace();
			}
					
		} else if (null == path || path.isEmpty()) {
			final String item = getItem(req.getParameter("id"));

			if (null != item && !item.isEmpty())
					out.write(item);
			else
				resp.setStatus(400);
		}
	}

	/**
	 * json: [ {id:...}, {id:...}, ... ]
	 */
	private String getItems(int start, int results) {
		final JSONArray json = new JSONArray();
		
		if (start < 1) start = 1;  if (start > 12) start = 12;
		if (results < 0) results = 0; if (results + start > 12 + 1) results = 12 + 1 - start;	
		
		try {

			for (int i = start; i < start + results; ++i) {
				json.put((new JSONObject().put("id", i)));
			}
		} catch (final JSONException ex) {
			ex.printStackTrace();
		}

		return json.toString();
	}

	/**
	 * json: { id: ..., polys: [ {name: '...', points: [ {x:.., y:...}, {x:..., y:...}, ...]}, ... ]
	 */
	private String getItem(final String id) {
		try {
			return T_Puzzle.create(id);

		} catch (final JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}

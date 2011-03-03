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

import com.google.appengine.repackaged.org.json.JSONException;

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
	 * /api/puzzle?id=...
	 */
	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		final PrintWriter out = resp.getWriter();

		final String item = getItem(req.getParameter("id"));

		if (null != item && !item.isEmpty())
			out.write(item);
		else
			resp.setStatus(400);

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

package net.puzzon.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.puzzon.T_Puzzle;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * 
 * Score Service
 *
 */
public class ScoreService  extends HttpServlet {

	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 2273095853433173698L;
	
	/**
	 * GET /api/score 
	 * 
	 * /api/score?id=...
	 * 
	 */
	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp)	throws ServletException, IOException {		
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		final PrintWriter out = resp.getWriter();
				
		final String idParam = req.getParameter("id");		
		
		final HttpSession session = req.getSession();
		final Object atr = session.getAttribute("scores");
		
		try {
			@SuppressWarnings("unchecked")
			final
			HashMap<String, String> scores = (HashMap<String, String>)atr;
			if (null == idParam || idParam.isEmpty()) {
		
				final JSONArray json = new JSONArray();
				for (final String id : scores.keySet()) {
					json.put(new JSONObject("{id:" + id + ", ticks:" + scores.get(id) + "}"));
				}
				out.write(json.toString());
				
			}else {
				final String ticks = scores.get(idParam);
				
				final JSONObject json = new JSONObject("{id:" + idParam + ", ticks:" + ticks + "}");
				out.write(json.toString());
			}						
		}
		catch (final Exception e) {
			e.printStackTrace();
			resp.setStatus(400);
		}
	}
	
	/**
	 * POST /api/score?id=...&ticks=...
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)	throws ServletException, IOException {
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		final PrintWriter out = resp.getWriter();
		
		final String id = req.getParameter("id");
		final String next_id = T_Puzzle.nextId(id);
		final String ticks = req.getParameter("ticks");
		
		final HttpSession session = req.getSession();
		final Object atr = session.getAttribute("scores");
		
		HashMap<String, String> scores = null;
		try {
			if (null != atr) scores = (HashMap<String, String>)atr;			
			else scores = new HashMap<String, String>();
			
			scores.put(id, ticks);
			session.setAttribute("scores", scores);
						
			final JSONObject json = new JSONObject("{id:" + id + ", next_id:" + next_id + "}");			
			out.write(json.toString());
		}
		catch (final Exception e) {
			resp.setStatus(400);
		}
	}
}

package net.puzzon.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.puzzon.T_Puzzle;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * 
 * Validate Service
 * 
 */
public class ValidateService extends HttpServlet {
	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 2655393030213807167L;

	/**
	 * POST /api/valid?polyform
	 */
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)	throws ServletException, IOException {
		
		final String polyform = req.getParameter("polyform");
		
		if (!T_Puzzle.isValid(polyform)) {
			//
			// Not Acceptable - 406 error
			resp.setStatus(406);
		}
		else {						
			resp.setStatus(200); // OK
			resp.setContentType("application/json; charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			final PrintWriter out = resp.getWriter();
					
			try {
				final String id = new JSONObject(polyform).getString("id");
				final JSONObject json = new JSONObject("{id:" + id + ", is_valid:true}");

				out.write(json.toString());
			} catch (final JSONException e) {			
				e.printStackTrace();
			}
			
		}
	}

}

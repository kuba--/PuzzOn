//                                                      
//Copyright (c) 2010 PuzzOn.net
//

package net.puzzon;

import java.util.HashMap;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * Constructor - t-Puzzle polyforms
 */
public class T_Puzzle {
	
	// triangle Tx(100, 100, X, Y)
	final static String t = "{ name: t,  points: [ {x: 100, y: 100}, {x: 150, y: 100}, {x: 100, y: 150} ] }";
	// triangle Tx(100, 100, -X, Y)
	final static String t_x = "{ name: t,  points: [ {x: 100, y: 100}, {x: 50, y: 100}, {x: 100, y: 150} ] }";

	// arrow Tx(250, 100, X, Y)
	final static String a = "{ name: a, points: [ {x: 250, y: 100}, {x: 350, y: 200}, {x: 300, y: 200}, {x: 300, y: 220}, {x: 250, y: 170} ] }";
	// arrow Tx(250, 100, -X, Y)
	final static String a_x = "{ name: a, points: [ {x: 250, y: 100}, {x: 150, y: 200}, {x: 200, y: 200}, {x: 200, y: 220}, {x: 250, y: 170} ] }";

	// small trapez Tx(100, 300, X, Y)
	final static String ts = "{ name: ts, points: [ {x: 100, y: 300}, {x: 150, y: 300}, {x: 150, y: 380}, {x: 100, y: 330} ] }";
	// small trapez Tx(100, 300, -X, Y)
	final static String ts_x = "{ name: ts, points: [ {x: 100, y: 300}, {x: 50, y: 300}, {x: 50, y: 380}, {x: 100, y: 330} ] }";

	// large trapez Tx(300, 300, X, Y)
	final static String tl = "{ name: tl, points: [ {x: 300, y: 300}, {x: 350, y: 300}, {x: 350, y: 400}, {x: 300, y: 450} ] }";
	// large trapez Tx(300, 300, -X, Y)
	final static String tl_x = "{ name: tl, points: [ {x: 300, y: 300}, {x: 250, y: 300}, {x: 250, y: 400}, {x: 300, y: 450} ] }";

	// default constructor
	protected T_Puzzle() { }

	/**
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 * @throws NumberFormatException
	 */
	public static String create(final String id) throws JSONException, NumberFormatException {

		JSONObject json = null;
		try {
			json = new JSONObject("{id:" + id + "}");

			switch (Integer.parseInt(id)) {
			case 1:
			case 4:
				json.accumulate("polys", new JSONObject(t));
				json.accumulate("polys", new JSONObject(a));
				json.accumulate("polys", new JSONObject(ts));
				json.accumulate("polys", new JSONObject(tl));
				break;

			case 2:
			case 8:
			case 12:
				json.accumulate("polys", new JSONObject(t));
				json.accumulate("polys", new JSONObject(a_x));
				json.accumulate("polys", new JSONObject(ts_x));
				json.accumulate("polys", new JSONObject(tl));
				break;

			case 3:
				json.accumulate("polys", new JSONObject(t_x));
				json.accumulate("polys", new JSONObject(a_x));
				json.accumulate("polys", new JSONObject(ts));
				json.accumulate("polys", new JSONObject(tl));
				break;

			case 5:
				json.accumulate("polys", new JSONObject(t_x));
				json.accumulate("polys", new JSONObject(a));
				json.accumulate("polys", new JSONObject(ts_x));
				json.accumulate("polys", new JSONObject(tl));
				break;

			case 6:
			case 7:
				json.accumulate("polys", new JSONObject(t_x));
				json.accumulate("polys", new JSONObject(a_x));
				json.accumulate("polys", new JSONObject(ts_x));
				json.accumulate("polys", new JSONObject(tl_x));
				break;

			case 9:
			case 10:
			case 11:
				json.accumulate("polys", new JSONObject(t));
				json.accumulate("polys", new JSONObject(a));
				json.accumulate("polys", new JSONObject(ts));
				json.accumulate("polys", new JSONObject(tl_x));
				break;

			default:
				return null;
			}
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final NumberFormatException e) {
			e.printStackTrace();
		}

		return (null == json) ? null : json.toString();
	}

	public static String nextId(final String id) {
		Integer i = new Integer(id) + 1;
		if (i > 12) i = 1;
		
		return i.toString();
	}
	
	public static String prevId(final String id) {
		Integer i = new Integer(id) - 1;
		if (i < 1) i = 12;
		
		return i.toString();		
	}
	
	public static boolean isValid(final String json) {
		final HashMap<String, XY[]> map = new HashMap<String, XY[]>(4);

		try {
			final int id = splitPolys(json, map);

			final XY[] t = map.get("t");
			final XY[] a = map.get("a");
			final XY[] ts = map.get("ts");
			final XY[] tl = map.get("tl");

			switch (id) {
			case 1:
				return t[1].equals(tl[3]) && t[2].equals(a[0])
				&& tl[2].equals(a[1]) && a[3].equals(ts[3])
				&& ts[2].equals(a[4]);
			case 2:
				return t[2].equals(ts[0]) && ts[3].equals(a[0])
				&& a[1].equals(tl[3]) && tl[2].equals(ts[2])
				&& t[0].equals(ts[1]);

			case 3:
				return tl[3].equals(a[4]) && a[0].equals(t[2])
				&& a[1].equals(ts[0]) && a[2].equals(ts[1]);
			case 4:
				return (equals(a[4], ts[2]) && equals(ts[1], tl[0])
						&& equals(tl[2], t[2]) && equals(tl[1], ts[0]) && equals(
								ts[3], a[3]));
			case 5:
				return equals(tl, 2, a, 4) && equals(ts, 0, tl, 3)
				&& equals(ts, 0, a, 3) && equals(ts, 3, t, 1)
				&& equals(t, 2, a, 1) && equals(t, 0, a, 2);
			case 6:
				return (equals(t, 1, tl, 3) && equals(ts, 0, tl, 0)
						&& equals(ts, 2, a, 4) && equals(t, 2, tl, 2))
						|| (equals(t, 2, tl, 2) && equals(t, 0, ts, 0)
								&& equals(ts, 2, a, 4)
								&& equals(ts, 3, a, 3)
								&& equals(t, 1, a, 2) && equals(t, 1, tl, 3));
			case 7:
				return equals(tl, 1, ts, 1) && equals(ts, 2, a, 4)
				&& equals(a, 2, t, 1) && equals(ts, 3, a, 3)
				&& equals(t, 0, ts, 0) && equals(t, 2, tl, 2);
			case 8:
				return equals(tl, 2, a, 0) && equals(a, 1, t, 2)
				&& equals(a, 2, t, 0) && equals(t, 1, ts, 0)
				&& equals(ts, 3, a, 3) && equals(ts, 2, a, 4);
			case 9:
				return (equals(tl, 0, a, 0) && equals(tl, 3, ts, 1)
						&& equals(ts, 2, a, 4) && equals(t, 2, ts, 1)
						&& equals(t, 0, ts, 0) && equals(ts, 3, a, 3))
						|| (equals(tl, 0, a, 0) && equals(tl, 3, ts, 3)
								&& equals(ts, 0, t, 0)
								&& equals(t, 1, a, 4)
								&& equals(ts, 1, t, 2) && equals(t, 2, a, 3));
			case 10:
				final XY tl0 = tl[0],	tl1 = tl[1], tl2 = tl[2], tl3 = tl[3];
				final XY tl12 = new XY((tl1.x + tl2.x) / 2, (tl1.y + tl2.y) / 2);
				final XY tl03 = new XY((tl0.x + tl3.x) / 2, (tl0.y + tl3.y) / 2);

				return equals(a, 0, tl, 0) && equals(t, 1, tl, 2)
				&& equals(tl, 3, ts, 1) && equals(ts, 3, a, 3)
				&& equals(ts, 2, a, 4) && tl12.equals(t[0])
				&& tl03.equals(a[4]);

			case 11:
				return equals(a, 1, tl, 3) && equals(ts, 3, a, 0)
				&& equals(ts, 2, tl, 2) && equals(a, 4, t, 1)
				&& equals(t, 2, a, 3);
			case 12:
				return equals(a, 1, tl, 0)
				&& equals(a, 2, tl, 1)
				&& equals(ts, 1, t, 1)
				&& equals(ts, 0, t, 0)
				&& ((equals(a, 3, ts, 3) && equals(a, 4, ts, 2) && equals(
						t, 2, tl, 2)) || equals(a, 3, ts, 1)
						&& equals(ts, 2, tl, 2)
						&& equals(t, 2, a, 4));

			default:
				return false;
			}

		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static int splitPolys(final String json, final HashMap<String, XY[]> map) throws JSONException {
		final JSONObject polyform = new JSONObject(json);
		
		final int id = polyform.getInt("id");
		final JSONArray polys = polyform.getJSONArray("polys");
		for (int i = 0, n = polys.length(); i < n; ++i) {
			final JSONObject poly = polys.getJSONObject(i);
			final String name = poly.getString("name");

			final JSONArray points = poly.getJSONArray("points");
			final int m = points.length();
			final XY[] arr = new XY[m];
			for (int j = 0; j < m; ++j) {
				final JSONObject xy = points.getJSONObject(j);
				final int x = xy.getInt("x"), y = xy.getInt("y");

				arr[j] = new XY(x, y);
			}

			map.put(name, arr);
		}
		return id;
	}
	
	static boolean equals(final XY xy1, final XY xy2) {
		return xy1.equals(xy2);
	}

	static boolean equals(final XY[] p1, final int i1, final XY[] p2, final int i2) {
		return equals(p1[i1], p2[i2]);
	}
}

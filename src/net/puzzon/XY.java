package net.puzzon;

public class XY {
	private final static int EPS = 9;
	public int x, y;
	
	public XY() { this.x = 0; this.y = 0; }
	public XY(int x, int y) { this.x = x; this.y = y; }
		
	@Override
	public boolean equals(Object obj) {
		try {
			XY xy = (XY)obj;
			return (Math.abs(this.x - xy.x) < EPS) && (Math.abs(this.y - xy.y) < EPS);
		}
		catch(Exception e) {
			return false;
		}
	}
}


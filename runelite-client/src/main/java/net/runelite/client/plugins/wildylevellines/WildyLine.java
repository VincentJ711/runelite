package net.runelite.client.plugins.wildylevellines;

import java.awt.geom.Line2D;
import java.awt.Rectangle;

public class WildyLine
{
	private int x1;
	private int x2;
	private int y;
	private int plane;
	private Line2D line;

	WildyLine(int plane, int x1, int x2, int y)
	{
		this.x1 = x1;
		this.y = y;
		this.x2 = x2;
		this.plane = plane;
		this.line = new Line2D.Float(x1, y, x2, y);
	}

	int getX1()
	{
		return x1;
	}

	int getX2()
	{
		return x2;
	}

	int getY()
	{
		return y;
	}

	int getPlane()
	{
		return plane;
	}

	boolean isInPlane(int plane)
	{
		return this.plane == plane;
	}

	boolean intersectsRectangle(Rectangle r)
	{
		return line.intersects(r);
	}

	WildyLine subtractFromRectangle(Rectangle r)
	{
		if (!intersectsRectangle(r))
		{
			return null;
		}

		return new WildyLine(plane, Math.max(r.x, x1), Math.min(r.x + r.width, x2), y);
	}
}
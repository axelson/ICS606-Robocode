package com.jaxelson;

import java.awt.geom.Point2D;
import java.io.Serializable;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class ExtendedPoint2D extends Point2D.Double implements Serializable{

	private static final long serialVersionUID = 74324L;
	public ExtendedPoint2D(double x, double y) {
		super(x, y);
	}
	
	public ExtendedPoint2D(Point2D location, double angle, double dist) {
		double enemyX = location.getX() + Math.sin(angle) * dist;
		double enemyY = location.getY() + Math.cos(angle) * dist;
		x = enemyX;
		y = enemyY;
	}
	
	public void setVectorLocation(double angle, double dist) {
		double enemyX = x + Math.sin(angle) * dist;
		double enemyY = y + Math.cos(angle) * dist;
		x = enemyX;
		y = enemyY;
	}
	
	public double angleTo(Point2D location) {
		double ex = location.getX();
		double ey = location.getY();
		return Math.atan2(ex - x, ey - y);
	}
	
	public double angleTo(double x, double y) {
		return angleTo(new Point2D.Double(x, y));
	}

}

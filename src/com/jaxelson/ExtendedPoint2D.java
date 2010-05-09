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
	
	public double bearingTo(Point2D location, AdvancedRobot robot) {
//		double absoluteAngle = angleTo(location);
		double absoluteAngle = angleToGood(location);
		double bearing = absoluteAngle - robot.getHeadingRadians();
		System.out.println("Plain: "+ bearing);
//		return bearing + Math.PI;
		return (bearing > 0) ? bearing - Math.PI : bearing + Math.PI;
//		Utils.
//		return Utils.normalAbsoluteAngle(bearing);
	}
	
	public double angleToGood(Point2D location) {
		ExtendedPoint2D loc = (ExtendedPoint2D) location;
		return Utils.normalAbsoluteAngle(Math.atan2(loc.x - x,
				loc.y - y));
	}
	
	public double angleTo(Point2D location) {
		double ox = location.getX();
		double oy = location.getY();
		double rx = ox - x;
		final double ry = oy - y;
		System.out.println("location: "+ location + " x: "+ x + " y: "+ y);
		double relativeAngle = Math.atan2(rx, ry);
		System.out.println("relative: "+ relativeAngle);
		return relativeAngle;
//		if((rx>0) && (ry>0)) { 		//Upper Right
//			return relativeAngle;
//		} else if((rx>0) && (ry<0)) { //Lower Right
//			return relativeAngle + Math.PI/4;
//		} else if((rx<0) && (ry<0)) { // Lower Left
//			return relativeAngle + Math.PI/2;
//		} else if((rx<0) && (ry>0)) { // Upper Left
//			return relativeAngle + Math.PI*3/4;
//		} else {
//			System.out.println("AngleTo: Error, returning 0");
//			return 0;
//		}
	}
	
	public double angleTo(double x, double y) {
		return angleTo(new Point2D.Double(x, y));
	}

}

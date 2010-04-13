package com.jaxelson;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class ExtendedBotUH extends AdvancedRobot {

    public void drawCenteredCircle(Graphics2D g, Integer radius) {
    	double x = getX() - radius;
        double y = getY() - radius;
		Shape circle = new Ellipse2D.Double(x, y, radius*2, radius*2);
        g.draw(circle);
    }
    
    public Double getDistanceToRightWall() {
    	double botRightEdge = getX() + 18;		// Bot is 36 pixels wide
    	double rightWallLocation = getBattleFieldWidth();
    	return rightWallLocation - botRightEdge;
    }
    
    public Double getDistanceToLeftWall() {
    	double botLeftEdge = getX() - 18;		// Bot is 36 pixels wide
    	return botLeftEdge;
    }
    /**
     * Turns to the desired angle
     * @param desiredAngle the desired angle in radians
     * @return how far is needed to turn
     */
	public Double turnTo(double desiredAngle) {
		double currentAngle = this.getHeadingRadians();
		double turnDistanceRad = currentAngle - desiredAngle;
		turnDistanceRad = Utils.normalRelativeAngle(turnDistanceRad);
		this.setTurnLeftRadians(turnDistanceRad);
		
		return Math.abs(turnDistanceRad);
	}
	
	/**
	 * Turns radar to the desired angle
	 * @param desiredAngle
	 * @return radians needed to turn
	 */
	public Double turnRadarTo(double desiredAngle) {
		double currentAngle = this.getRadarHeadingRadians();
		double turnDistanceRad = currentAngle - desiredAngle;
		turnDistanceRad = Utils.normalRelativeAngle(turnDistanceRad);
		this.setTurnRadarLeftRadians(turnDistanceRad);
		
		return Math.abs(turnDistanceRad);
	}
	
	/**
	 * Turns the gun to face in the desired absolute angle
	 * @param desiredAngle for gun to face in radians
	 * @return distance gun needs to turn in radians
	 */
	public Double turnGunTo(double desiredAngle) {
		double currentAngle = this.getGunHeadingRadians();
		double turnDistanceRad = currentAngle - desiredAngle;
		turnDistanceRad = Utils.normalRelativeAngle(turnDistanceRad);
		this.setTurnGunLeftRadians(turnDistanceRad);
		
		return Math.abs(turnDistanceRad);
	}
	
	/**
	 * Turns the gun to face the given target (not doing any prediction for now)
	 * @param target to face gun at
	 * @return distance gun needs to travel
	 */
	public Double turnGunTo(EnemyBot target) {
		double targetBearing = target.getBearingRadians();
		double desiredAngle = targetBearing + this.getHeadingRadians();
		return turnGunTo(desiredAngle);
	}
	
	public Double getCenterX() {
		return new Double(this.getX());
	}
	
	public Double getCenterY() {
		return new Double(this.getY());
	}
	
	public void narrowRadarLock(ScannedRobotEvent event) {
		narrowRadarLock(event, 1.9);
	}
	
	/**
	 * Executes a narrow radar lock<br>
	 * http://robowiki.net/wiki/Radar#Narrow_lock
	 * @param event scanned robot event (may be replaced by an enemy in the future)
	 * @param factor narrow lock factor (how "narrow" lock is), typical values 1.0, 1.9, 2.0
	 */
	public void narrowRadarLock(ScannedRobotEvent event, Double factor) {
		double radarTurn =
			// Absolute bearing to target
			this.getHeadingRadians() + event.getBearingRadians()
			// Subtract current radar heading to get turn required
			- this.getRadarHeadingRadians();

		this.setTurnRadarRightRadians(factor * Utils.normalRelativeAngle(radarTurn));
	}
}

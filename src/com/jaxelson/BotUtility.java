package com.jaxelson;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class BotUtility {	
	/**
	 * Returns the amount of damage inflicted by a bullet using the specified
	 * power (not factoring in the life of the target).<br>
	 * NOTE: A shotPower value greater than 3 will return an invalid result
	 * @param shotPower A double specifying the power with which the bullet
	 *                  was fired
	 * @return A double specifying the amount of damage inflicted by the
	 *         bullet
	 */
	public static Double calculateDamage(double shotPower) {
		double damage = (shotPower * 4);
		if (shotPower > 1) {
			damage += ((shotPower - 1) * 2);
		}
		return damage;
	}
	
	public static Double turnTo(double desiredAngle, AdvancedRobot robot) {
		double currentAngle = robot.getHeadingRadians();
		double turnDistanceRad = currentAngle - desiredAngle;
		turnDistanceRad = Utils.normalRelativeAngle(turnDistanceRad);
		robot.setTurnLeftRadians(turnDistanceRad);
		
		return Math.abs(turnDistanceRad);
	}
	
	public static Double getCenterX(AdvancedRobot robot) {
		return new Double(robot.getX());
	}
	
	public static Double getCenterY(AdvancedRobot robot) {
		return new Double(robot.getY());
	}
}


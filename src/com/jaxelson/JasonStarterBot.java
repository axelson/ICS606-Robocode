package com.jaxelson;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;


public class JasonStarterBot extends AdvancedRobot {
	public void run() {
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }
 
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
    
    public void printRobot(ScannedRobotEvent e) {
    	System.out.println("Name: "+ e.getName());
    	System.out.println("Bearing: "+ e.getBearing());
    	System.out.println("Bearing (radians): "+ e.getBearingRadians());
    	System.out.println("Distance: "+ e.getDistance());
    	System.out.println("Energy: "+ e.getEnergy());
    	System.out.println("Heading: "+ e.getHeading());
    	System.out.println("Heading (radians)"+ e.getHeadingRadians());
    	System.out.println("Velocity: "+ e.getVelocity());
    	System.out.println("Time: "+ e.getTime());
    	System.out.println("Priority: "+ e.getPriority());
    }
    
}

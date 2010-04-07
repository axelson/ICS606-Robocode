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
    
    /**
     * Prints information about a robot scanned by the radar
     * @param e the ScannedRobotEvent that was seen by the radar
     */
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
    
    /**
     * Prints information about myself
     */
    public void printMyInfo() {
    	System.out.println("Heading: "+ this.getHeading());
    	System.out.println("Heading (radians): "+ this.getHeadingRadians());
    	System.out.println("Distance remaining: "+ this.getDistanceRemaining());
    }
    
    public void onPaint(Graphics2D g) {
        // Set the paint color to red
        g.setColor(java.awt.Color.RED);
        // Paint a filled rectangle at (50,50) at size 100x150 pixels
        g.fillRect(50, 50, 100, 150);
    }
}

package com.jaxelson;

import java.awt.Graphics2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;


public class JasonStarterBot extends AdvancedRobot {
	private Integer debug = 1;
	private Integer state = 0;
	
	public void run() {
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
//		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
        while (true) {
        	
//        	System.out.println("Looping!");
        	if(debug >= 1) {
        		printMyInfo();
        	}
        	switch(state) {
        	case 0:	// Do a loop
        		System.out.println("State 0");
        		setMaxVelocity(5);
        		setAhead(600);
        		setTurnRight(360);
        		state++;
        		break;
        	case 1:
        		System.out.println("State 1");
        		setFire(1.0);
        		if(getDistanceRemaining() <= 0) {
        			state++;
        		}
        		break;
        	case 2:
        		//setTurnGunRightRadians(3);
        	}
            
//            waitFor(new TurnCompleteCondition(this));
        	scan();
            execute();
//            setAhead(-100);
            //setTurnGunRight(360);
            //setBack(100);
            //setTurnGunRight(360);
            // Check for new targets.
            // Only necessary for Narrow Lock because sometimes our radar is already
            // pointed at the enemy and our onScannedRobot code doesn't end up telling
            // it to turn, so the system doesn't automatically call scan() for us
            // [see the javadocs for scan()].
            scan();
        }
    }
 
    public void onScannedRobot(ScannedRobotEvent e) {
    	if(debug >= 2) {
    		System.out.println("Found robot: "+ e.getName());
    		printRobot(e);
    	}
    	double radarTurn =
    		// Absolute bearing to target
    		getHeadingRadians() + e.getBearingRadians()
    		// Subtract current radar heading to get turn required
    		- getRadarHeadingRadians();

    	setTurnRadarRightRadians(2.0 * Utils.normalRelativeAngle(radarTurn));
    	
    	double gunTurn = getHeadingRadians() + e.getBearingRadians() - getGunHeadingRadians();
    	if(state >= 1) {
    		setTurnGunRightRadians(Utils.normalRelativeAngle(gunTurn));
    	}
    	setFire(1.0);
    }
    
    public void onWin(WinEvent e) {
    	System.out.println("I win!");
    	setAhead(200);
    	setTurnRightRadians(Math.PI);
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

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
}

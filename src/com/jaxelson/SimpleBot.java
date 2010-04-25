package com.jaxelson;

import java.awt.Color;
import java.awt.Graphics2D;

import navigation.ExtendedBot;
import robocode.HitByBulletEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

public class SimpleBot extends ExtendedBot {

	/**
	 * run: Chris01's default behavior
	 */
	
	Enemies _enemies = new Enemies(this);
	 
	int state = 0;
	 
	public void run() {
		//setColors(Color.red,Color.blue,Color.green);
		
		setColors(Color.white,Color.white,Color.white);

		
		// Robocode order
		// Battle view (re)painted
		// All robots execute code until the take action (paused)
		// Time updated (by 1)
		// All bullets move and check for collision (includes firing bullets)
		// All robots move
		// All robots perform scans (and collect team messages)
		// All robots are resumed to take new action
		// Each robot processes its event queue
	
		do
		{
			quickestScan(0, true);
			execute();
			
		} while(true);
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		_enemies.update(e);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {

	}
	
	public void onMessageReceived(MessageEvent e)
	{
		if(e.getMessage().equals("Attack"))
			fire(1);
	}
	
	public void onPaint(Graphics2D g) {
		 // Set the paint color to a red half transparent color
	     g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
	 
		_enemies.paintAll(g);
	}
}

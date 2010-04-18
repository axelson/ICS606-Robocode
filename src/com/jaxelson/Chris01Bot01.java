package com.jaxelson;

import robocode.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import navigation.ExtendedBot;

public class Chris01Bot01 extends ExtendedBot {

	/**
	 * run: Chris01's default behavior
	 */
	
	 ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
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
		if((state++) == 0)
		{
			EnemyBot temp = new EnemyBot(e, this);
			enemies.add(temp);
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}
	
	public void onMessageReceived(MessageEvent e)
	{
		if(e.getMessage().equals("Attack"))
			fire(1);
		
	}
	
	public void onPaint(Graphics2D g) {
		// Set the paint color to a red half transparent color
	     g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
	 
	     int x = (int)enemies.get(0).getLocation().x;
	     int y = (int)enemies.get(0).getLocation().y;
	     
	     // Draw a line from our robot to the scanned robot
	     g.drawLine(x, y, (int)getX(), (int)getY());
	 
	     // Draw a filled square on top of the scanned robot that covers it
	     g.fillRect(x - 20, y - 20, 40, 40);
	}
	
	
}

package com.jaxelson;

import robocode.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Hashtable;

import navigation.ExtendedBot;

public class Chris01Bot01 extends ExtendedBot {

	/**
	 * run: Chris01's default behavior
	 */
	
	final int MAX_NO_ENEMIES = 10;
	
	 //ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
	 Hashtable<String,EnemyBot> enemies = new Hashtable<String,EnemyBot>();
	 
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
		if(isNewEnemy(e))
		{
			EnemyBot temp = new EnemyBot(e, this);
			enemies.put(e.getName(),temp);
		}
		else
			updateEnemy(e);
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
		for(EnemyBot enemy : enemies.values()) {
			paintTrackingRectangle(enemy,g);
		}
	}
	
	public void paintTrackingRectangle(EnemyBot robot, Graphics2D g) {
		// Set the paint color to a red half transparent color
	     g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
	 
	     int x = (int)robot.getLocation().x;
	     int y = (int)robot.getLocation().y;
	     
	     // Draw a line from our robot to the scanned robot
	     g.drawLine(x, y, (int)getX(), (int)getY());
	 
	     // Draw a filled square on top of the scanned robot that covers it
	     g.fillRect(x - 20, y - 20, 40, 40);
	}
	
	public void updateEnemy(ScannedRobotEvent e) {
		for(int i = 0; i < enemies.size(); i++)
			if(enemies.get(i).getName() == e.getName())
			{
				enemies.get(i).update(e);
				break;
			}
	}
	
	public Boolean isNewEnemy(ScannedRobotEvent e) {
		int noEnemies = enemies.size();
		String eName = e.getName();
		if(noEnemies > MAX_NO_ENEMIES)
			return false;
		else
		{
			for(int i = 0; i < noEnemies; i++)
				if(enemies.get(i).getName() == eName)
					return false;
			return true;
		}
	}
	
	
}

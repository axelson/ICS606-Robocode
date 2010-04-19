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
	 Hashtable<String,EnemyBot> _enemies = new Hashtable<String,EnemyBot>();
	 
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
		if(_enemies.containsKey(e.getName())) {
			updateEnemies(e, _enemies);
		} else {
			EnemyBot temp = new EnemyBot(e, this);
			_enemies.put(e.getName(),temp);
		}
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
	 
		for(EnemyBot enemy : _enemies.values()) {
			enemy.paintTrackingRectangle(this,g);
		}
	}
	
	public void updateEnemies(ScannedRobotEvent e, Hashtable<String, EnemyBot> enemies) {
		String enemyName = e.getName();
		
		if(_enemies.containsKey(enemyName)) {
			enemies.put(enemyName, new EnemyBot(e, this));
		} else {
			enemies.get(enemyName).update(e);
		}
	}
}

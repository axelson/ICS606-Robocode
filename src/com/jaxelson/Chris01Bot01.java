package com.jaxelson;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;

import navigation.ExtendedBot;
import robocode.HitByBulletEvent;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;


public class Chris01Bot01 extends ExtendedBot implements Serializable {

	private static final long serialVersionUID = 7742997431699620804L;

	/**
	 * run: Chris01's default behavior
	 */
	
	Enemies _enemies = new Enemies(this);
	Boolean target = false;
	EnemyBot temp;
	
	int state = 0;
	Boolean leader = false;
	long fireTime = 0;
	
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
	
		if(this.getEnergy() >= 200) {
			leader = true;
		}

		
		do
		{
			if(leader) {
				quickestScan(0, true);
			}
			else if(target){
			linearTargeting(temp,3);
			}
			execute();
			
		} while(true);
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		_enemies.update(e);
		
			String enemyName = BotUtility.fixName(e.getName());
			System.out.println("****\n" + enemyName + "\n****");
/*
			System.out.println("TEAM MATES:");
			System.out.println(this.getTeammates()[0]);
			System.out.println(this.getTeammates()[1]);
*/
			if(!this.isTeammate(enemyName)) {
				if(leader) {
					try {
						broadcastMessage(_enemies.get(enemyName));
						System.out.println("SENT MESSAGE:");
						System.out.println(_enemies.get(enemyName));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		
	}
		

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {

	}
	
	public void onRobotDeath(RobotDeathEvent e)
	{
		
		_enemies.update(e);
	}
	
	public void onMessageReceived(MessageEvent e)
	{
		target = true;
		System.out.println(e);
		if(!leader) {
			temp = (EnemyBot)e.getMessage();
			temp.setRobot(this);
			System.out.println(temp);
		}
	}
	
	public void onPaint(Graphics2D g) {
		 // Set the paint color to a red half transparent color
	     g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
	 
		_enemies.paintAll(g);
	}
}

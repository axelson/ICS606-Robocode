package com.jaxelson;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
/**
 * Holds the information about all the enemies in the game
 * @author jason
 */
public class Enemies {
	private Hashtable<String,EnemyBot> _enemyTable = new Hashtable<String,EnemyBot>();
	private TeamRobot _robot;
	private EnemyBot _target;
	
	/**
	 * 
	 */
	public Enemies(TeamRobot robot) {
		_robot = robot;
	}
	
	public Collection<EnemyBot> getEnemies() {
		return _enemyTable.values();
	}
	
	/**
	 * Updates records of enemies
	 * @param e ScannedRobotEvent info about a robot
	 * @param enemies list of all known enemies
	 */
	public void update(ScannedRobotEvent e) {
		// Hack because of asterisk error
		String enemyName = e.getName().replace(" (", "* (");
		
		EnemyBot enemyBot = new EnemyBot(e, _robot);
		if(_target == null) {
			_target = enemyBot;
		}

		// Don't track team-mates
		if (_robot.isTeammate(enemyName)) {
			return;
		}
		
		// Update enemy database
		if(_enemyTable.containsKey(enemyName)) {
			_enemyTable.get(enemyName).update(e);
		} else {
			_enemyTable.put(enemyName, enemyBot);
		}
	}
	
	public void update(RobotDeathEvent e) {
		// Hack because of asterisk error
		String enemyName = e.getName().replace(" (", "* (");
		System.out.println("Removing: "+ enemyName);
		_enemyTable.remove(enemyName);
		
		if(_target.getName().equals(enemyName)) {
			_target = pickRandomTarget();
			if(_target != null) System.out.println("Target is now: "+ _target.getName());
		}
	}
	
	private EnemyBot pickRandomTarget() {
		if(_enemyTable.size() >= 1) {
			Enumeration<String> enemyNames = _enemyTable.keys();
			EnemyBot target = _enemyTable.get(enemyNames.nextElement()); 
			return target;
		} else {
			return null;
		}
	}
	
	public EnemyBot get(ScannedRobotEvent e) {
		return get(BotUtility.fixName(e.getName()));
	}
	
	public EnemyBot get(String enemyName) {
		return _enemyTable.get(enemyName);
	}
	
	public void remove(RobotDeathEvent e) {
		_enemyTable.remove(BotUtility.fixName(e.getName()));
	}
	
	public void setTarget(EnemyBot target) {
		_target = target;
	}
	
	public EnemyBot getTarget() {
		return _target;
	}
	
	public int size() {
		return _enemyTable.size();
	}
	
	/**
	 * Paint all enemies on the battlefield
	 * @param g GraphicsObject
	 * @param enemies All known enemies
	 */
	public void paintAll(Graphics2D g) {
		for(EnemyBot enemy : _enemyTable.values()) {
			enemy.paintTrackingRectangle(_robot,g);
		}
	}
	

	public boolean isCurrentTarget(ScannedRobotEvent e) {
		String targetName = getTarget().getName();
		String enemyName = BotUtility.fixName(e.getName());
		return (targetName.equals(enemyName));
	}
}

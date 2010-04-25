/**
 * 
 */
package com.jaxelson;

import java.awt.Graphics2D;
import java.util.Hashtable;

import navigation.ExtendedBot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
/**
 * Holds the information about all the enemies in the game
 * @author jason
 */
public class Enemies {
	private Hashtable<String,EnemyBot> _enemies = new Hashtable<String,EnemyBot>();
	private ExtendedBot _robot;
	
	/**
	 * 
	 */
	public Enemies(ExtendedBot robot) {
		_robot = robot;
	}
	/**
	 * Updates records of enemies
	 * @param e ScannedRobotEvent info about a robot
	 * @param enemies list of all known enemies
	 */
	public void update(ScannedRobotEvent e) {
		// Hack because of asterisk error
		String enemyName = e.getName().replace(" (", "* (");
		
		// Don't track team-mates
		if (_robot.isTeammate(enemyName)) {
			return;
		}
		
		// Update enemy database
		if(_enemies.containsKey(enemyName)) {
			_enemies.get(enemyName).update(e);
		} else {
			_enemies.put(enemyName, new EnemyBot(e, _robot));
		}
	}
	
	public EnemyBot get(ScannedRobotEvent e) {
		return get(e.getName());
	}
	
	public EnemyBot get(String enemyName) {
		return _enemies.get(enemyName);
	}
	
	public void remove(RobotDeathEvent e) {
		_enemies.remove(e.getName());
	}
	
	public int size() {
		return _enemies.size();
	}
	
	/**
	 * Paint all enemies on the battlefield
	 * @param g GraphicsObject
	 * @param enemies All known enemies
	 */
	public void paintAll(Graphics2D g) {
		for(EnemyBot enemy : _enemies.values()) {
			enemy.paintTrackingRectangle(_robot,g);
		}
	}
}

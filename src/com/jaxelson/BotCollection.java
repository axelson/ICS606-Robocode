package com.jaxelson;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import navigation.ExtendedBot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
/**
 * Holds the information about all the enemies in the game
 * @author jason
 */
public class BotCollection implements Serializable {
	private static final long serialVersionUID = -6523798889576108941L;

	private Hashtable<String,BotInfo> _botTable = new Hashtable<String,BotInfo>();
	private ExtendedBot _robot;
	private BotInfo _target;
	private int _debug = 0;
	private boolean _storesOnlyTeammates = false;
	
	/**
	 * 
	 */
	public BotCollection(ExtendedBot robot) {
		_robot = robot;
	}
	
	public BotCollection(BotCollection another) {
		_robot = another._robot;
		_target = another._target;
		_debug = another._debug;
		_botTable = new Hashtable<String, BotInfo>(another._botTable);
	}
	
	/**
	 * @param extendedBot
	 * @param storesOnlyTeammates if true this BotCollection will only store teammates
	 */
	public BotCollection(ExtendedBot robot, boolean storesOnlyTeammates) {
		_robot = robot;
		_storesOnlyTeammates = storesOnlyTeammates;
	}

	public Collection<BotInfo> getEnemiesAsCollection() {
		return _botTable.values();
	}

	private boolean shouldTrack(BotInfo bot) {
		String botName = bot.getName();
		if(_storesOnlyTeammates) {
			return _robot.isTeammate(botName);
		} else {
			return _robot.isEnemy(botName);
		}
	}
	
	/**
	 * Updates records of stored bots
	 * @param e ScannedRobotEvent info about a robot
	 */
	public void update(ScannedRobotEvent e) {
		String botName = BotUtility.fixName(e.getName());
		
		BotInfo bot = new BotInfo(e, _robot);
		if(_target == null) {
			_target = bot;
		}

		if (!shouldTrack(bot)) {
			return;
		}
		
		// Update robot database
		if(_botTable.containsKey(botName)) {
			_botTable.get(botName).update(e);
		} else {
			_botTable.put(botName, bot);
		}
	}
	
	public void update(RobotDeathEvent e) {
		String botName = BotUtility.fixName(e.getName());
		if(_debug >= 1) System.out.println("Removing: "+ botName);
		_botTable.remove(botName);
		
		if(_target.getName().equals(botName)) {
			_target = pickRandomTarget();
			if(_target != null) System.out.println("Target is now: "+ _target.getName());
		}
	}
	
	public BotInfo pickRandomTarget() {
		if(_botTable.size() >= 1) {
			Enumeration<String> enemyNames = _botTable.keys();
			BotInfo target = _botTable.get(enemyNames.nextElement()); 
			return target;
		} else {
			return null;
		}
	}
	
	/**
	 * Chooses target with lowest energy
	 * @return BotInfo with lowest energy, or null if no known enemies
	 */
	public BotInfo pickByLowestEnergy() {
		List<BotInfo> enemies = new ArrayList<BotInfo>(_botTable.values());
		Collections.sort(enemies);
		for(BotInfo enemy : enemies) {
			if(_debug >= 1) System.out.println("Enemy "+ enemy.getName() + ": "+ enemy.getEnergy());
		}
		if(enemies.size() > 0) {
			return enemies.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Removes bots from collection that exceed given range
	 * @param c collection to filter
	 * @param range to filter robots that exceed the range
	 */
	public void filterBotsByRange(double range) {
		Collection<BotInfo> c = _botTable.values();
	    for (Iterator<BotInfo> it = c.iterator(); it.hasNext(); ) {
	        if (it.next().getDistance() > range) {
	            it.remove();
	        }
	    }
	}
	
	/**
	 * Removes bots from collection that exceed given range
	 * @param absoluteAngle to look for bots in the direction of
	 * @param offsetAngle angle (in degrees) to look for bots in. Split on both sides of absoluteAngle
	 * @param range to filter robots that exceed the range
	 */
	public void filterBotsByAngle(double absoluteAngle, double offsetAngle) {
		Collection<BotInfo> c = _botTable.values();
	    for (Iterator<BotInfo> it = c.iterator(); it.hasNext(); ) {
	    	double botAngle = it.next().getAngle();
	        if (!BotUtility.inRange(absoluteAngle - offsetAngle, botAngle, absoluteAngle + offsetAngle)) {
	            it.remove();
	        }
	    }
	}
	
	public void printNames() {
		System.out.print("Bots: ");
		for(BotInfo bot: _botTable.values()) {
			System.out.print(bot.getName() + " ");
		}
		System.out.println();
	}

	public BotInfo get(ScannedRobotEvent e) {
		return get(BotUtility.fixName(e.getName()));
	}
	
	public BotInfo get(String enemyName) {
		return _botTable.get(enemyName);
	}
	
	public void remove(RobotDeathEvent e) {
		_botTable.remove(BotUtility.fixName(e.getName()));
	}
	
	public void setTarget(BotInfo target) {
		_target = target;
	}
	
	public BotInfo getTarget() {
		return _target;
	}
	
	public int size() {
		return _botTable.size();
	}
	
	/**
	 * Paint all enemies on the battlefield
	 * @param g GraphicsObject
	 * @param enemies All known enemies
	 */
	public void paintAll(Graphics2D g) {
		for(BotInfo enemy : _botTable.values()) {
			enemy.paintTrackingRectangle(_robot,g);
		}
	}
	

	public boolean isCurrentTarget(ScannedRobotEvent e) {
		String targetName = getTarget().getName();
		String enemyName = BotUtility.fixName(e.getName());
		return (targetName.equals(enemyName));
	}
}

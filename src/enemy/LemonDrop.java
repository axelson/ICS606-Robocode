package enemy;

import java.awt.Color;

import com.jaxelson.BotUtility;

import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.WinEvent;
import robocode.util.Utils;

public class LemonDrop extends TeamRobot {

	/* LemonDrop made by Exauge
	 * Current Version 1.4.104 - 22:26, 05-08-2010
	 * This robot uses a Pattern Matching Gun, Narrow Lock Radar, and Oscillator Movement
	 * Credits to Robar for Gun (BlackWidow)
	 */

	/* Version History:
	 * 1.0 - Pattern Matching, Random Movement, Infinity lock
	 * 1.1 - changed to oscillator, stronger movement algorithm strengthened wall avoidance
	 * 1.2 - strengthened movement even more, changed gun a wee bit ;), corrected small glitch
	 * 1.3 - small gun tweaking, better wall avoidance, stronger movement, narrow lock radar
	 * 1.4 - multi-mode movement, fixed "sticky-wall glitch" by reverting to previous wall avoidance
	 */

	static final int patDep = 28; // number of ticks to record enemy movement
	static String eLog = "00000000000000000000000000000088888888888887654321007543218800"; // symbolic state of the enemy
	static double eEner; // enemy energy
	double randHit = 1; // random number for movement
	int backDir = 1;
	int moveNeg = 1; // change direction (for movement)
	static int moveNum = 0; // number of movement to use
	static int bestMove = 1; // tells if the best movement has been found (1 = no, 0 = yes)
	static double bestMove1 = 0; // tells how effective movement 1 was
	static double bestMove2 = 0; // tells how effective movement 2 was
	int startStop; // records whether to go or stop
	static int win1 = 0; // movement 1 win counter

	public void run() {
		setAdjustGunForRobotTurn(true); // if the robot is turned, don't turn the gun
		setAdjustRadarForRobotTurn(true); // if the robot is turned, don't turn the radar
		setAdjustRadarForGunTurn(true); // if the gun is turned, don't turn the radar
		setColors(Color.yellow.darker(), Color.yellow, Color.yellow.darker()); // Colors
		setTurnRadarRight(Double.POSITIVE_INFINITY); // turn the radar right an infinite amount
		do { // do the following:
			
			//---Edit---//
			if(this.getRadarTurnRemainingRadians() < 0.001) {
	    		this.setTurnRadarRightRadians(Math.PI);
	    	}
			this.scan();
			//---End edit---//
			
			scan(); // scan for the enemy
		}while(true); // forever
	}

	public void onHitByBullet(HitByBulletEvent e){ // if a bullet hits us
		moveNeg *= -1; // change direction
		randHit *= (Math.random() + 5) * moveNeg; // random number if hit
	}

	public void onHitWall(HitWallEvent event){ // if we hit a wall,
		backDir *= -1; // go the other way
	}


	public void onScannedRobot(ScannedRobotEvent e) { // if we see a robot:
		// Local variables
		int i; // integer
		double absB; // absolute bearing
		int mLen = patDep; // number of ticks to record
		int indX; // pattern index
		double mvAmt = ((getX() + getY()) / 2 + 700) / 2; // movement number
		double mxMv = Math.min(Math.min(getX(), getY()), Math.min(getBattleFieldWidth() - getX(), getBattleFieldHeight() - getY())); // distance from nearest wall
		double bPow = Math.min(eEner - 2.4 / 4, 2.2); // bullet power, use 2.2 or smallest power to kill

		//---Edit---//
		
		String enemyName = BotUtility.fixName(e.getName());
		
		if(this.isTeammate(enemyName)) {
			return;
		}
		//---End edit---//
		
		
		/* Movement */

		// Oscillator
		if(moveNum < 105){ // If it is still first movement
			setTurnRightRadians(Math.cos(absB = e.getBearingRadians())); // "square off" against the enemy
			if(eEner > (eEner = e.getEnergy())) { // if the enemy fires
				setMaxVelocity(16*Math.random()+5); // make the velocity randomized, but no less than 5
				setAhead((Math.random()*mvAmt-(mvAmt*.5))*randHit); // go ahead (or behind) a random amount
			}
			if(mxMv < 50){ // if we get close to a wall
				setBack(100 * backDir); // go back a little bit
				moveNeg *= -1; // when we continue, go the other way
			}
		}

		// Stop & Go
		if(moveNum >= 105 && moveNum < 210){ // If it is the second movement
			setTurnRightRadians(Math.cos(absB = e.getBearingRadians())); // "square off" against the enemy
			setAhead(25 * moveNeg * startStop); // move a bit
			if(eEner > (eEner = e.getEnergy())) { // if the enemy fires
				if(startStop == 0){ // and we arn't moving,
					startStop = 1; // move.
				}
				else { // but if we are moving,
					startStop = 0; // stop.
				}
			}
			if(mxMv < 28){ // if we get close to a wall
				setBack(100 * backDir); // go back a little bit
				moveNeg *= -1; // when we continue, go the other way
			}
		}

		// Determining Most Effective Movement
		if(moveNum >= 210){ // if we have died 3 times with both movements,
			bestMove = 0; // make it so the movement type won't change
			if(bestMove2 > bestMove1){ // else if movement 2 performed the best,
				moveNum = 150; // use movement 2
			}
			else{ // else if movement 1 performed the best,
				moveNum = 0; // use movement 1
			}
		}

		/* Symbolic Pattern Matcher */

		absB = e.getBearingRadians(); // absolute bearing
		eLog = String.valueOf( (char)Math.round(e.getVelocity() * Math.sin(e.getHeadingRadians() - ( absB+=getHeadingRadians() )))).concat(eLog); // record enemy pattern into symbolic state
		while((indX = eLog.indexOf(eLog.substring(0, mLen--), (i = (int)(e.getDistance()/(20-(3*bPow)))))) < 0); // decide where to fire
		do{
			absB += Math.asin(((byte)eLog.charAt(indX--))/e.getDistance()); // correcting absolute bearing
		}
		while(--i > 0); // if int is greater than zero
		setTurnGunRightRadians(Utils.normalRelativeAngle(absB-getGunHeadingRadians())); // turn gun toward the enemy predicted position,
		if(getEnergy() > 5){ // and if we have enough energy,
			setFire(bPow); // FIRE!
		}

		/* Radar Lock */
		double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians(); // how far to turn the radar the other way
		setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn)); // turn the radar the other way to stay locked on enemy
	}

	// If movement is unsuccessful, switch to a new movement.
	public void onDeath(DeathEvent event){ // if we die
		if (win1 < 3){ // we'll assume if we win 3 before we lose 3, the current movement is fine
			moveNum += (35 * bestMove); // add a little bit to movement number
		}
		if(moveNum < 110){ // and if we are using movement 1,
			bestMove1 = bestMove1 - eEner; // record how much energy the enemy had left
		}
		else{ // and if we are using movement 2,
			bestMove2 = bestMove2 - eEner; // record how much energy the enemy had left
		}
	}
	public void onWin(WinEvent event){ // if we win,
		if(moveNum < 1){ // and are using movement 1,
			bestMove1 = bestMove1 + eEner; // record how much energy we had left
			win1 += 1; // add a win to the movement 1 counter
		}
		else{ // and if we are using movement 2,
			bestMove2 = bestMove2 + eEner; // record how much energy we had left
		}
	}
}
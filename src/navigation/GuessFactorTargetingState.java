package navigation;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import com.jaxelson.BotCollection;

/**
 * @author Jason Axelson
 */
public class GuessFactorTargetingState
        extends State {

	BotCollection _enemies = new BotCollection(robot);
	
	private static final double BULLET_POWER = 1.9;
	
	private static double lateralDirection;
	private static double lastEnemyVelocity;
	
    // CONSTRUCTORS

    public GuessFactorTargetingState(ExtendedBot robot) {
        super(robot);
    }

    // PUBLIC METHODS

    /**
     * Returns the statistics of how this state has performed against the
     * current target.
     * @return The NavigationStateStatistics object for the current state
     *         and target
     */
    public Statistics getStatistics() {
        if (statistics == null) {
            statistics = new Statistics();
        }        
        return statistics;
    }

    /**
     * Returns the name of this State.
     * @return A String containing the name of this State object
     */
    public String getName() {
        return "GuessFactorTargetingState";
    }

    /**
     * Returns whether the this state is valid (may be used under the
     * current circumstances).
     * @return A boolean indicating whether this State should be used
     */
    public boolean isValid() {
        return (robot.getNumEnemies() <= 1);
    }

    /**
     * This method will be called to indicate the CommandListener should free
     * all resources and cease execution.
     */
    public void disable() {
        robot.removeEventListener(ON_HIT_BY_BULLET, this);
        robot.removeEventListener(ON_SCANNED_ROBOT, this);
        robot.removeEventListener(ON_ROBOT_DEATH, this);
        updateStatistics();
    }

    /**
     * This method will be called to indicate the CommandListener is free to
     * begin execution.
     */
    public void enable() {
        startTime = robot.getTime();
        damageTaken = 0;
        robot.addEventListener(ON_HIT_BY_BULLET, this);
        robot.addEventListener(ON_SCANNED_ROBOT, this);
        robot.addEventListener(ON_ROBOT_DEATH, this);
        
        lateralDirection = 1;
		lastEnemyVelocity = 0;
    }

    /**
     * This method will be called each turn to allow the DodgeState to
     * execute turn based instructions.
     */
    public void execute() {
    }

    /**
     * This method will be called when your robot is hit by a bullet.
     * @param event A HitByBulletEvent object containing the details of your
     *              robot being hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {
        damageTaken += BotMath.calculateDamage(e.getPower());
    }
    
    public void onRobotDeath(RobotDeathEvent e) {
    	_enemies.update(e);
    }

    /**
     * This method will be called when your robot sees another robot.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A ScannedRobotEvent object containing the details of your
     *              robot's sighting of another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        _enemies.update(e);
        
        double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		if (enemyVelocity != 0) {
			lateralDirection = GFTUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(robot);
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		GFTWave.targetLocation = GFTUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;
		robot.setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - robot.getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		robot.setFire(wave.bulletPower);
		if (robot.getEnergy() >= BULLET_POWER) {
			robot.addCustomEvent(wave);
		}
    }

    // PRIVATE METHODS

    /**
     * Recalculates state statistics.
     */
    private void updateStatistics() {
        statistics.update(robot.getOthers(),
                          damageTaken,
                          (robot.getTime() - startTime),
                          robot);
    }

    // INSTANCE VARIABLES

    // Ordinarily I would use accessor methods exclusively to access instance
    // variables, but in the interest of speed I have allowed direct access.

    /**
     * The total energy lost from bullet hits while this state has been
     * in use
     */
    private double damageTaken;
    /**
     * The time when this state was enabled.
     */
    private long startTime;
    /**
     * Used to track statistics of this state in battle
     */
    private static Statistics statistics;

}


class GFTWave extends Condition {
	static Point2D targetLocation;

	double bulletPower;
	Point2D gunLocation;
	double bearing;
	double lateralDirection;

	private static final double MAX_DISTANCE = 1000;
	private static final int DISTANCE_INDEXES = 5;
	private static final int VELOCITY_INDEXES = 5;
	private static final int BINS = 25;
	private static final int MIDDLE_BIN = (BINS - 1) / 2;
	private static final double MAX_ESCAPE_ANGLE = 0.7;
	private static final double BIN_WIDTH = MAX_ESCAPE_ANGLE / (double)MIDDLE_BIN;
	
	private static int[][][][] statBuffers = new int[DISTANCE_INDEXES][VELOCITY_INDEXES][VELOCITY_INDEXES][BINS];

	private int[] buffer;
	private AdvancedRobot robot;
	private double distanceTraveled;
	
	GFTWave(AdvancedRobot _robot) {
		this.robot = _robot;
	}
	
	public boolean test() {
		advance();
		if (hasArrived()) {
			buffer[currentBin()]++;
			robot.removeCustomEvent(this);
		}
		return false;
	}

	double mostVisitedBearingOffset() {
		return (lateralDirection * BIN_WIDTH) * (mostVisitedBin() - MIDDLE_BIN);
	}
	
	void setSegmentations(double distance, double velocity, double lastVelocity) {
		int distanceIndex = (int)(distance / (MAX_DISTANCE / DISTANCE_INDEXES));
		int velocityIndex = (int)Math.abs(velocity / 2);
		int lastVelocityIndex = (int)Math.abs(lastVelocity / 2);
		buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
	}

	private void advance() {
		distanceTraveled += GFTUtils.bulletVelocity(bulletPower);
	}

	private boolean hasArrived() {
		return distanceTraveled > gunLocation.distance(targetLocation) - 18;
	}
	
	private int currentBin() {
		int bin = (int)Math.round(((Utils.normalRelativeAngle(GFTUtils.absoluteBearing(gunLocation, targetLocation) - bearing)) /
				(lateralDirection * BIN_WIDTH)) + MIDDLE_BIN);
		return GFTUtils.minMax(bin, 0, BINS - 1);
	}
	
	private int mostVisitedBin() {
		int mostVisited = MIDDLE_BIN;
		for (int i = 0; i < BINS; i++) {
			if (buffer[i] > buffer[mostVisited]) {
				mostVisited = i;
			}
		}
		return mostVisited;
	}	
}

class GFTUtils {
	static double bulletVelocity(double power) {
		return 20 - 3 * power;
	}
	
	static Point2D project(Point2D sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
				sourceLocation.getY() + Math.cos(angle) * length);
	}
	
	static double absoluteBearing(Point2D source, Point2D target) {
		return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
	}

	static int sign(double v) {
		return v < 0 ? -1 : 1;
	}
	
	static int minMax(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}
}
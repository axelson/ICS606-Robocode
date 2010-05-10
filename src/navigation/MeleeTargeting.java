package navigation;

import java.awt.Graphics2D;

import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import com.jaxelson.BotCollection;
import com.jaxelson.BotInfo;

/**
 * @author Jason Axelson
 */
public class MeleeTargeting
        extends State {

	BotCollection _enemies = new BotCollection(robot);
	double targetingRange = 300;
	
    // CONSTRUCTORS

    public MeleeTargeting(ExtendedBot robot) {
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
        return "MeleeTargetingState";
    }

    /**
     * Returns whether the this state is valid (may be used under the
     * current circumstances).
     * @return A boolean indicating whether this State should be used
     */
    public boolean isValid() {
        return (robot.getNumEnemies() > 1);
    }

    /**
     * This method will be called to indicate the CommandListener should free
     * all resources and cease execution.
     */
    public void disable() {
        robot.removeEventListener(ON_HIT_BY_BULLET, this);
        robot.removeEventListener(ON_SCANNED_ROBOT, this);
        robot.removeEventListener(ON_ROBOT_DEATH, this);
        robot.removeEventListener(ON_PAINT, this);
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
        robot.addEventListener(ON_PAINT, this);
    }

    /**
     * This method will be called each turn to allow the DodgeState to
     * execute turn based instructions.
     */
    public void execute() {
        // Do absolutely nothing
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
        
    	BotCollection closeEnemies = new BotCollection(_enemies);
    	closeEnemies.filterBotsByRange(targetingRange);

    	// Choose target
    	BotInfo target;
    	if(closeEnemies.size() > 0) {
    		target = closeEnemies.pickByLowestEnergy();
    	} else {
    		target = _enemies.pickByLowestEnergy();
    	}

    	// Choose firepower
    	double firepower = 3;
        if(robot.getEnergy() < 20) {
        	firepower = 1.0;
        } else if(target.getDistance() > 200) {
        	firepower = 2.0;
        } else {
        	firepower = 3.0;
        }
        robot.linearTargeting(target, firepower);
    }
    
    public void onPaint(Graphics2D g) {
    	if(_debug >= 1) System.out.println("Now Painting");
        // Set the paint color to red
        g.setColor(java.awt.Color.BLUE);

    	// Draw range currently looking for enemies
        robot.drawCircleAroundBot(g, targetingRange);
        
        // Draw nearby enemies
        BotCollection closeEnemies = new BotCollection(_enemies);
    	closeEnemies.filterBotsByRange(targetingRange);
    	closeEnemies.paintAll(g);
    	
    	BotCollection mightHitEnemies = new BotCollection(_enemies);
    	mightHitEnemies.filterBotsByAngle(robot.getGunHeadingRadians(), Math.PI/6);
    	g.setColor(java.awt.Color.GREEN);
    	mightHitEnemies.paintAll(g);
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

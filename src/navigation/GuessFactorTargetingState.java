package navigation;

import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import com.jaxelson.BotCollection;

/**
 * @author Jason Axelson
 */
public class GuessFactorTargetingState
        extends State {

	BotCollection _enemies = new BotCollection(robot);
	
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
        return true;
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

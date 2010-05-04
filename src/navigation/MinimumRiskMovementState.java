package navigation;

import java.util.ArrayList;

import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import com.jaxelson.EnemyBot;
import com.jaxelson.ExtendedPoint2D;

/**
 * @author Jason Axelson
 */
public class MinimumRiskMovementState
        extends State {

    ArrayList<GravPoint> _gravpoints = new ArrayList<GravPoint>();
    // CONSTRUCTORS

    public MinimumRiskMovementState(ExtendedBot robot) {
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
        return "MinimumRiskMovementState";
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
        energy = 0;
        updateStatistics();
    }

    /**
     * This method will be called to indicate the CommandListener is free to
     * begin execution.
     */
    public void enable() {
        startTime = robot.getTime();
        energy = robot.getEnergy();
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
    	antiGravMove();
    }

    /**
     * This method will be called when your robot is hit by a bullet.
     * @param event A HitByBulletEvent object containing the details of your
     *              robot being hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent event) {
        damageTaken += BotMath.calculateDamage(event.getPower());
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
        targetBearing = e.getBearingRadians();
        
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
    
    //TODO Finish anti gravity movement    
    void antiGravMove() {
        double xforce = 0;
        double yforce = 0;
        double force;
        double ang;
        ExtendedPoint2D loc = robot.getLocation();
        
        for(EnemyBot enemy: _enemies.getEnemies()) {
        	GravPoint p = enemy.getGravPoint();
        	if(_debug >= 1) System.out.println("antiGrav: enemypoint: "+ p + " strength: "+ p.power);
        	if(_debug >= 2) System.out.println("xforce: "+ xforce + " yforce: "+ yforce);

        	//Calculate the total force from this point on us
            force = p.power/Math.pow(loc.distance(p),2);
            robot.getLocation();
            
            ang = loc.angleTo(p);

            //Add the components of this force to the total force in their 
            //respective directions
            xforce += Math.sin(ang) * force;
            yforce += Math.cos(ang) * force;
        }
        
        if(_debug >= 2) System.out.println("after bots: xforce: "+ xforce + " yforce: "+ yforce);
        
        /* The following four lines add wall avoidance.  They will only 
        affect us if the bot is close to the walls due to the
        force from the walls decreasing at a power 3.*/
        xforce += 5000/Math.pow(loc.distance(robot.getBattleFieldWidth(), loc.y), 3);
        xforce -= 5000/Math.pow(loc.distance(0, loc.y), 3);
        yforce += 5000/Math.pow(loc.distance(loc.x, robot.getBattleFieldHeight()), 3);
        yforce -= 5000/Math.pow(loc.distance(loc.x, 0), 3);
        
        if(_debug >= 2) System.out.println("after walls: xforce: "+ xforce + " yforce: "+ yforce);
        
        //Move in the direction of our resolved force.
        robot.moveToward(loc.x-xforce,loc.y-yforce);
    }

    // INSTANCE VARIABLES

    // Ordinarily I would use accessor methods exclusively to access instance
    // variables, but in the interest of speed I have allowed direct access.

	/**
     * Last known bearing to the target bot
     */
    @SuppressWarnings("unused")
	private double targetBearing;
    /**
     * The energy of the bot when this state was chosen
     */
    @SuppressWarnings("unused")
	private double energy;
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

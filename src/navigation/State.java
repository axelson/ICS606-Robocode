package navigation;

import com.jaxelson.BotCollection;

//import droid.data_types.NavigationStateStatistics;

/**
 * Defines methods required for a State object.
 * @author David McCoy
 */
public abstract class State
        extends EventListener
        implements CommandListener {

	public int _debug = 0;
	protected BotCollection _enemies = null;
    // CONSTRUCTORS

    /**
     * Creates a new State with the specified controller.<br>
     * NOTE: Inherited classes should load persisted data from their
     *       constructors.
     * @param robot The ExtendedRobot object used to provide data and execute
     *              commands
     */
    public State(ExtendedBot robot) {
        this.robot = robot;
        _enemies = new BotCollection(robot);
    }

    // PUBLIC METHODS

    /**
     * This method will be called to indicate the CommandListener should free
     * all resources and cease execution.<br>
     * NOTE: This class provides a blank instantiation of this method.
     */
    public void disable() {
    }

    /**
     * This method will be called to indicate the CommandListener is free to
     * begin execution.<br>
     * NOTE: This class provides a blank instantiation of this method.
     */
    public void enable() {
    }

    /**
     * This method will be called each turn to allow the CommandListener to
     * execute turn based instructions.<br>
     * NOTE: This class provides a blank instantiation of this method.
     */
    public void execute() {
    }

    /**
     * Returns the statistics for this state for the current target.
     * NOTE: This class provides a default instantiation of this method - it
     *       always returns null
     * @return The NavigationStateStatistics object for the current state and
     *         target
     */
    public Statistics getStatistics() {
        return null;
    }

    /**
     * Returns the name of this State.
     * @return A String containing the name of this State object
     */
    public abstract String getName();

    /**
     * Returns whether the this State is valid (may be used under the
     * current circumstances).
     * @return A boolean indicating whether this State should be used
     */
    public abstract boolean isValid();

    // INSTANCE VARIABLES

    // Ordinarily I would use accessor methods exclusively to access instance
    // variables, but in the interest of speed I have allowed direct access.

    // Protected Variables

    /**
     * The ExtendedRobot object used to provide data and execute commands.
     */
    protected ExtendedBot robot;

}

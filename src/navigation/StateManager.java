package navigation;

import java.util.ArrayList;
import robocode.*;

/**
 * Contains basic functionality for managing states.
 * @author David McCoy
 */
public class StateManager
        extends EventListener
        implements CommandListener {

    // CONSTRUCTORS

    /**
     * Creates a new StateManager with the specified controller.
     * @param robot The ExtendedRobot object used to provide data and execute
     *              commands
     */
    public StateManager(ExtendedRobot robot) {
        this.robot = robot;
        states = new ArrayList();
    }

    // PUBLIC METHODS

    /**
     * Adds a State to the list of available States.<br>
     * Note: There is no failsafe to prevent duplicates from being added
     *       to the list.
     * @param aState The State object to be added
     */
    public void addState(State aState) {
        if (aState != null) {
            states.add(aState);
        }
    }

    /**
     * This method will be called to indicate the state manager should free
     * all resources and cease execution.<br>
     * NOTE: If this method is overridden, the overriding method should
     *       call this method.
     */
    public void disable() {
        if (currentState != null) {
            currentState.disable();
        }
    }

    /**
     * This method will be called to indicate the state manager is free to
     * begin execution.<br>
     * NOTE: If this method is overridden, the overriding method should
     *       call this method.
     * @throws NullPointerException If an attempt is made to enable this
     *                              StateManager without having
     *                              added any States or if no State is
     *                              eligible for the current circumstances
     */
    public void enable() {
        // Verify there are states available
        if (states.size() == 0) {
            throw new NullPointerException(
                    "[StateManager.enable] " +
                    "Manager enabled with no states added");
        }

        // Select our first state
        selectNewState();

        // Verify a state was selected
        if (currentState == null) {
            throw new NullPointerException(
                    "[StateManager.enable] " +
                    "There are no valid states for the circumstances");
        }
    }

    /**
     * This method will be called each turn to allow the state manager to
     * execute turn based instructions.<br>
     * NOTE: If this method is overridden, it will be the responsibility
     *       of the overriding method to choose new states and pass the
     *       execute event on to the current State object.
     */
    public void execute() {
        if (currentState.isValid() == false) {
            selectNewState();
        }
        currentState.execute();
    }

    // PROTECTED METHODS

    /**
     * Selects the best State from the list of valid States.<br>
     * This method attempts to establish the best state to use by first
     * checking wins/losses.  If all states have the same win/loss
     * ratio, state is selected by which one has taken the least damage
     * over time.
     */
    protected void selectNewState() {
        int totalStates = states.size();  // Number of states available
        State damageCandidate = null; // Reference for best damage/time candidate
        State winLossCandidate = null; // Reference for best win/loss candidate
        double candidateDamageRatio = 9999; // Default damage ratio
        double candidateWinLossRatio = -9999; // Default win/loss ratio

        State indexedState; // Temporary reference for iterating through the
                            // candidate states
        Statistics indexedStatistics; // Temporary reference for iterating
                                      // through the statistics object for each
                                      // candidate state
        double indexedStateDamageRatio; // Temporary holder for the damage ratio
                                        // of the current state
        double indexedStateWinLossRatio; // Temporary holder for the win/loss
                                         // ratio of the current state

        // Find the best candidate for both categories
        for (int stateIndex = 0; stateIndex < totalStates; stateIndex++) {
            indexedState = (State)states.get(stateIndex);
            if (indexedState.isValid()) {
                indexedStatistics = indexedState.getStatistics();
                if (indexedStatistics == null) {
                    indexedStateDamageRatio = 100;
                    indexedStateWinLossRatio = 0;
                } else {
                    indexedStateDamageRatio =
                            indexedStatistics.getDamageRatio();
                    indexedStateWinLossRatio =
                            indexedStatistics.getWinLossRatio();
                }
                if (indexedStateDamageRatio < candidateDamageRatio) {
                    damageCandidate= indexedState;
                    candidateDamageRatio = indexedStateDamageRatio;
                }
                if (indexedStateWinLossRatio > candidateWinLossRatio) {
                    winLossCandidate= indexedState;
                    candidateWinLossRatio = indexedStateWinLossRatio;
                }
            }
        }

        // Use win/loss candidate if available, otherwise use damage/time
        // candidate
        if (winLossCandidate != null) {
            if (currentState != null) {
                currentState.disable();
            }
            if (candidateWinLossRatio > 0) {
                currentState = winLossCandidate;
            } else {
                currentState = damageCandidate;
            }
            currentState.enable();
            robot.out.println("New State Selected: " + currentState.getName());
        }
    }

    // INSTANCE VARIABLES

    // Ordinarily I would use accessor methods exclusively to access instance
    // variables, but in the interest of speed I have allowed direct access.

    // Protected Variables

    /**
     * The ExtendedRobot object used to provide data and execute commands.
     */
    protected ExtendedRobot robot;
    /**
     * The State currently in use.
     */
    protected volatile State currentState;
    /**
     * Collection of available states.
     */
    protected ArrayList states;

}

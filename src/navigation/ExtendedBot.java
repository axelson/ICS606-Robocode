package navigation;

import java.util.ArrayList;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.CustomEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.WinEvent;

import com.jaxelson.ExtendedBotUH;

/**
 * Extension to the standard AdvancedRobot class to facilitate the
 * distribution of events.
 * @author David McCoy
 */
public class ExtendedBot extends ExtendedBotUH
	implements EventRegistry {

    // CONSTRUCTORS

    /**
     * Creates a new ExtendedRobot object.<br>
     * This constructor initializes the array of listener collections.
     */
    public ExtendedBot() {
        // Initialize Listeners table
        listeners[ON_BULLET_HIT] = bulletHitListeners;
        listeners[ON_BULLET_HIT_BULLET] =
                bulletHitBulletListeners;
        listeners[ON_BULLET_MISSED] = bulletMissedListeners;
        listeners[ON_CUSTOM_EVENT] = customEventListeners;
        listeners[ON_DEATH] = deathListeners;
        listeners[ON_HIT_BY_BULLET] = hitByBulletListeners;
        listeners[ON_HIT_ROBOT] = hitRobotListeners;
        listeners[ON_HIT_WALL] = hitWallListeners;
        listeners[ON_ROBOT_DEATH] = robotDeathListeners;
        listeners[ON_SCANNED_ROBOT] = scannedRobotListeners;
        listeners[ON_SKIPPED_TURN] = skippedTurnListeners;
        listeners[ON_WIN] = winListeners;
    }

    // PUBLIC METHODS

    /**
     * Adds a CommandListener to the collection of listeners registered to
     * receive commands.
     * @param listener The CommandListener object to receive commands
     * @throws IllegalArgumentException If listener is of type State (State
     *                                  objects receive commands through their
     *                                  StateManager)
     */
    public void addCommandListener(CommandListener listener) {
        if (listener instanceof State) {
            throw new IllegalArgumentException(
                    "[Droid.addCommandListener] " +
                    "Cannot add listener of type State - State objects " +
                    "receive commands from their StateManager");
        }
        if (listener != null) {
            synchronized (commandListeners) {
                commandListeners.add(listener);
            }
        }
    }
    
    public void testConnection() {
    	System.out.println("connection successfull!");
    	fire(3);
    }

    /**
     * Removes a CommandListener from the collection of listeners registered
     * to receive commands.
     * @param listener The CommandListener object to stop issuing commands
     */
    public void removeCommandListener(CommandListener listener) {
        if (listener != null) {
            synchronized (commandListeners) {
                commandListeners.remove(listener);
            }
        }
    }

    /**
     * This method should be called at the end of each round to deactivate
     * any registered CommandListeners.
     */
    public void disable() {
        synchronized (commandListeners) {
            int listenerCount = commandListeners.size();
            CommandListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = commandListeners.get(listenerIndex);
                listener.disable();
            }
        }
    }

    /**
     * This method should be called at the beginning of each round to activate
     * any registered CommandListeners.
     */
    public void enable() {
        synchronized (commandListeners) {
            int listenerCount = commandListeners.size();
            CommandListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = commandListeners.get(listenerIndex);
                listener.enable();
            }
        }
    }

    /**
     * This method should be called each turn to allow any registered
     * CommandListeners to perform turn-specific functions.
     */
    public void executeTurn() {
        synchronized (commandListeners) {
            int listenerCount = commandListeners.size();
            CommandListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = commandListeners.get(listenerIndex);
                listener.execute();
            }
        }
    }

    /**
     * Adds an EventListener to the specified collection of listeners.
     * See the EventRegistry class for events.
     * @param event An int specifying the event to register for
     * @param listener The EventListener object to notify of game events
     * @throws ArrayIndexOutOfBoundsException If event parameter is not valid
     */
    public void addEventListener(int event, EventListener listener) {
        if (listener != null) {
            ArrayList<EventListener> listenerCollection = listeners[event];
            synchronized (listenerCollection) {
                listenerCollection.add(listener);
            }
        }
    }

    /**
     * Removes an EventListener from the collection of listeners registered
     * to receive game events.
     * See the EventRegistry class for events.
     * @param event An int specifying the event to unregister from
     * @param listener The EventListener object to stop notifying of game
     *                 events
     * @throws ArrayIndexOutOfBoundsException If event parameter is not valid
     */
    public void removeEventListener(int event, EventListener listener) {
        if (listener != null) {
            ArrayList<EventListener> listenerCollection = listeners[event];
            synchronized (listenerCollection) {
                listenerCollection.remove(listener);
            }
        }
    }

    /**
     * This method will be called when one of your bullets hits another robot.
     * @param event A BulletHitEvent object containing the details of the
     *              bullet hitting the other robot
     */
    public void onBulletHit(BulletHitEvent event) {
        synchronized (bulletHitListeners) {
            int listenerCount = bulletHitListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener =
                        bulletHitListeners.get(listenerIndex);
                listener.onBulletHit(event);
            }
        }
    }

    /**
     * This method will be called when one of your bullets hits another bullet.
     * @param event A BulletHitBulletEvent object containing the details of the
     *              bullet hitting the other bullet
     */
    public void onBulletHitBullet(BulletHitBulletEvent event) {
        synchronized (bulletHitBulletListeners) {
            int listenerCount = bulletHitBulletListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = bulletHitBulletListeners.get(listenerIndex);
                listener.onBulletHitBullet(event);
            }
        }
    }

    /**
     * This method will be called when one of your bullets misses
     * (hits a wall).
     * @param event A BulletMissedEvent object containing the details of the
     *              bullet miss
     */
    public void onBulletMissed(BulletMissedEvent event) {
        synchronized (bulletMissedListeners) {
            int listenerCount = bulletMissedListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = bulletMissedListeners.get(listenerIndex);
                listener.onBulletMissed(event);
            }
        }
    }

    /**
     * This method will be called when a custom condition is met.
     * @param event A CustomEvent object containing the details of the event
     */
    public void onCustomEvent(CustomEvent event) {
        synchronized (customEventListeners) {
            int listenerCount = customEventListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = customEventListeners.get(listenerIndex);
                listener.onCustomEvent(event);
            }
        }
    }

    /**
     * This method will be called if your robot dies.
     * @param event A DeathEvent object containing the details of your robot
     *              death
     */
    public void onDeath(DeathEvent event) {
        synchronized (deathListeners) {
            int listenerCount = deathListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = deathListeners.get(listenerIndex);
                listener.onDeath(event);
            }
        }
    }

    /**
     * This method will be called when your robot is hit by a bullet.
     * @param event A HitByBulletEvent object containing the details of your
     *              robot being hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent event) {
        synchronized (hitByBulletListeners) {
            int listenerCount = hitByBulletListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = hitByBulletListeners.get(listenerIndex);
                listener.onHitByBullet(event);
            }
        }
    }

    /**
     * This method will be called when your robot collides with another robot.
     * @param event A HitRobotEvent object containing the details of your
     *              robot's collision with another robot
     */
    public void onHitRobot(HitRobotEvent event) {
        synchronized (hitRobotListeners) {
            int listenerCount = hitRobotListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = hitRobotListeners.get(listenerIndex);
                listener.onHitRobot(event);
            }
        }
    }

    /**
     * This method will be called when your robot collides with a wall.
     * @param event A HitWallEvent object containing the details of your
     *              robot's collision with a wall
     */
    public void onHitWall(HitWallEvent event) {
        synchronized (hitWallListeners) {
            int listenerCount = hitWallListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = hitWallListeners.get(listenerIndex);
                listener.onHitWall(event);
            }
        }
    }

    /**
     * This method will be called if another robot dies.
     * @param event A RobotDeathEvent object containing the details of the
     *              other robot's death
     */
    public void onRobotDeath(RobotDeathEvent event) {
        synchronized (robotDeathListeners) {
            int listenerCount = robotDeathListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = robotDeathListeners.get(listenerIndex);
                listener.onRobotDeath(event);
            }
        }
    }

    /**
     * This method will be called when your robot sees another robot.
     * @param event A ScannedRobotEvent object containing the details of your
     *              robot's sighting of another robot
     */
    public void onScannedRobot(ScannedRobotEvent event) {
        synchronized (scannedRobotListeners) {
            int listenerCount = scannedRobotListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = scannedRobotListeners.get(listenerIndex);
                listener.onScannedRobot(event);
            }
        }
    }

    /**
     * This method will be called if your robot is taking an extremely long
     * time between actions. If you receive 30 of these, your robot will be
     * removed from the round. You will only receive this event after taking
     * an action... so a robot in an infinite loop will not receive any
     * events, and will simply be stopped. No correctly working,
     * reasonable robot should ever receive this event.
     * @param event A SkippedTurnEvent object containing the details of the
     *              skipped turn
     */
    public void onSkippedTurn(SkippedTurnEvent event) {
        synchronized (skippedTurnListeners) {
            int listenerCount = skippedTurnListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = skippedTurnListeners.get(listenerIndex);
                listener.onSkippedTurn(event);
            }
        }
    }

    /**
     * This method will be called if your robot wins a round.
     * @param event A WinEvent object containing the details of your robot's
     *              win
     */
    public void onWin(WinEvent event) {
        synchronized (winListeners) {
            int listenerCount = winListeners.size();
            EventListener listener;
            for (int listenerIndex = 0;listenerIndex < listenerCount;
                    listenerIndex++) {
                listener = winListeners.get(listenerIndex);
                listener.onWin(event);
            }
        }
    }

    // Instance Variables

    /**
     * Collection of CommandListener objects registered to receive commands.
     */
    protected ArrayList<CommandListener> commandListeners = new ArrayList<CommandListener>();
    /**
     * The listener table used to register EventListeners by event.
     */
    protected ArrayList<EventListener>[] listeners = new ArrayList[EVENT_COUNT];
    /**
     * Collection of EventListener objects registered to receive bullet hit
     * events.
     */
    protected ArrayList<EventListener> bulletHitListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive bullet hit
     * bullet events.
     */
    protected ArrayList<EventListener> bulletHitBulletListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive bullet missed
     * events.
     */
    protected ArrayList<EventListener> bulletMissedListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive custom
     * events.
     */
    protected ArrayList<EventListener> customEventListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive death
     * events.
     */
    protected ArrayList<EventListener> deathListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive hit by bullet
     * events.
     */
    protected ArrayList<EventListener> hitByBulletListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive hit robot
     * events.
     */
    protected ArrayList<EventListener> hitRobotListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive hit wall
     * events.
     */
    protected ArrayList<EventListener> hitWallListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive robot death
     * events.
     */
    protected ArrayList<EventListener> robotDeathListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive scanned robot
     * events.
     */
    protected ArrayList<EventListener> scannedRobotListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive skipped turn
     * events.
     */
    protected ArrayList<EventListener> skippedTurnListeners = new ArrayList<EventListener>();
    /**
     * Collection of EventListener objects registered to receive win
     * events.
     */
    protected ArrayList<EventListener> winListeners = new ArrayList<EventListener>();

}

package navigation;

/**
 * Defines a numeric constant for every event.  These constants will be used
 * to subscribe to events published by the ExtendedRobot class.
 * @author David McCoy
 */
public interface EventRegistry {

    /**
     * Total number of events available
     */
    public static final int EVENT_COUNT = 12;
    /**
     * Used to subscribe to onBulletHit events.
     */
    public static final int ON_BULLET_HIT = 0;
    /**
     * Used to subscribe to onBulletHitBullet events.
     */
    public static final int ON_BULLET_HIT_BULLET = 1;
    /**
     * Used to subscribe to onBulletMissed events.
     */
    public static final int ON_BULLET_MISSED = 2;
    /**
     * Used to subscribe to onCustomEvent events.
     */
    public static final int ON_CUSTOM_EVENT = 3;
    /**
     * Used to subscribe to onDeath events.
     */
    public static final int ON_DEATH = 4;
    /**
     * Used to subscribe to onHitByBullet events.
     */
    public static final int ON_HIT_BY_BULLET = 5;
    /**
     * Used to subscribe to onHitRobot events.
     */
    public static final int ON_HIT_ROBOT = 6;
    /**
     * Used to subscribe to onHitWall events.
     */
    public static final int ON_HIT_WALL = 7;
    /**
     * Used to subscribe to onRobotDeath events.
     */
    public static final int ON_ROBOT_DEATH = 8;
    /**
     * Used to subscribe to onScannedRobot events.
     */
    public static final int ON_SCANNED_ROBOT = 9;
    /**
     * Used to subscribe to onSkippedTurn events.
     */
    public static final int ON_SKIPPED_TURN = 10;
    /**
     * Used to subscribe to onWin events.
     */
    public static final int ON_WIN = 11;

}

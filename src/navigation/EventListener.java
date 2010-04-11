package navigation;

import robocode.*;

/**
 * Provides basic functionality for EventListener classes including
 * blank instantiations of all required event methods.
 * @author David McCoy
 */
public abstract class EventListener
        implements EventRegistry {

    // PUBLIC METHODS

    /**
     * This method will be called when one of your bullets hits another
     * robot.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A BulletHitEvent object containing the details of the
     *              bullet hitting the other robot
     */
    public void onBulletHit(BulletHitEvent event) {
    }

    /**
     * This method will be called when one of your bullets hits another
     * bullet.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A BulletHitBulletEvent object containing the details of
     *              the bullet hitting the other bullet
     */
    public void onBulletHitBullet(BulletHitBulletEvent event) {
    }

    /**
     * This method will be called when one of your bullets misses
     * (hits a wall).<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A BulletMissedEvent object containing the details of the
     *              bullet miss
     */
    public void onBulletMissed(BulletMissedEvent event) {
    }

    /**
     * This method will be called when a custom condition is met.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A CustomEvent object containing the details of the event
     */
    public void onCustomEvent(CustomEvent event) {
    }

    /**
     * This method will be called if your robot dies.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A DeathEvent object containing the details of your robot
     *              death
     */
    public void onDeath(DeathEvent event) {
    }

    /**
     * This method will be called when your robot is hit by a bullet.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A HitByBulletEvent object containing the details of your
     *              robot being hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent event) {
    }

    /**
     * This method will be called when your robot collides with another
     * robot.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A HitRobotEvent object containing the details of your
     *              robot's collision with another robot
     */
    public void onHitRobot(HitRobotEvent event) {
    }

    /**
     * This method will be called when your robot collides with a wall.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A HitWallEvent object containing the details of your
     *              robot's collision with a wall
     */
    public void onHitWall(HitWallEvent event) {
    }

    /**
     * This method will be called if another robot dies.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A RobotDeathEvent object containing the details of the
     *              other robot's death
     */
    public void onRobotDeath(RobotDeathEvent event) {
    }

    /**
     * This method will be called when your robot sees another robot.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A ScannedRobotEvent object containing the details of your
     *              robot's sighting of another robot
     */
    public void onScannedRobot(ScannedRobotEvent event) {
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
    }

    /**
     * This method will be called if your robot wins a round.<br>
     * NOTE: This class provides a blank instantiation of this method.
     * @param event A WinEvent object containing the details of your robot's
     *              win
     */
    public void onWin(WinEvent event) {
    }

}

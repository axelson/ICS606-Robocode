package navigation;

import robocode.ScannedRobotEvent;

/**
 * NavigationStateExample - An of switching navigation states based on
 *                          past performance.<br>
 * This is designed for one-on-one encounters only, but could be
 * extended with a little effort to include melee battles.
 */
public class NavigationStateExample extends ExtendedBot {

	/**
	 * run: NavigationStateExample's default behavior
	 */
	public void run() {
        try {
            // Set up and enable the state manager
            StateManager navigation = new StateManager(this);
//            navigation.addState(new CannonFodderState(this));
//            navigation.addState(new TrackState(this));
            navigation.addState(new WaveSurfing(this));
            navigation.addState(new MoveLeftRightState(this));
            addCommandListener(navigation);
            
            StateManager radar = new StateManager(this);
//            radar.addState(new MeleeRadarState(this));
            radar.addState(new SpinningRadarState(this));
            radar.addState(new NarrowRadarLockState(this));
            addCommandListener(radar);
            
//            StateManager gun = new StateManager(this);
//            gun.addState(new MeleeRadarState(this));
//            addCommandListener(radar);
            
            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);
            setAdjustRadarForRobotTurn(true);
            
            enable();

            // Main bot execution loop
		    while(true) {
                // Spin gun
                //setTurnGunRightRadians(Math.PI);
                // Allow StateManager to do it's thing
		    	executeTurn();
                // Finish the turn
                execute();
	    	}
        } finally {
            // Disable the State Manager
            disable();
        }
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent event) {
		//fire(1);
        super.onScannedRobot(event);
	}

}

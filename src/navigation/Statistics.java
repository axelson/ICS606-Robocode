package navigation;

import java.io.Serializable;

/**
 * Container class for statistic data about a state's performance.
 * @author David McCoy
 */
public class Statistics
        implements Serializable {

    // CONSTANTS

    /**
     * The damage ratio to be returned if data is not available
     */
    public static final double DEFAULT_DAMAGE_RATIO = 0;
    /**
     * The win/loss ratio to be returned if data is not available
     */
    public static final double DEFAULT_WIN_LOSS_RATIO = 9999;
    /**
     * The minimum number of times a bot must be encountered before
     * data is available
     */
     public static final long MINIMUM_ENCOUNTERS = 3;

    // PUBLIC METHODS

    /**
     * Returns the damage ratio received from the opponent represented
     * by this data object.
     * @return A double containing the damage ratio (damage/time)
     */
    public double getDamageRatio() {
        if (encounters < MINIMUM_ENCOUNTERS) {
            return DEFAULT_DAMAGE_RATIO;
        } else {
            return damageRatio;
        }
    }

    /**
     * Returns the win/loss ratio for the opponent represented by
     * this data object.
     * @return A double containing the win/loss ratio
     */
    public double getWinLossRatio() {
        if (encounters < MINIMUM_ENCOUNTERS) {
            return DEFAULT_WIN_LOSS_RATIO;
        } else if (losses > 0) {
            return (wins / losses);
        } else {
            return 9999; // Undefeated
        }
    }

    /**
     * Updates encounter data for this Data object.
     * @param others An int specifying how many opponents are left
     * @param damage A double containing the damage received while the state
     *               was running
     * @param time A long containing the amount of time the state was running
     */
    public void update(int others, double damage, double time) {
        if (time > 50) {
            encounters++;
            if (others == 0) {
                wins++;
            } else {
                losses++;
            }
            damageRatio = ((damageRatio + (damage / time)) / 2);
        }
    }

    // INSTANCE VARIABLES

    // Ordinarily I would use accessor methods exclusively to access instance
    // variables, but in the interest of speed I have allowed direct access.

    // Public Variables

    /**
     * The average damage ratio (damage/time) received from the opponent
     * represented by this data
     */
    public double damageRatio;
    /**
     * The number of encounters with the opponent represented by this data.
     */
    public long encounters;
    /**
     * The number of losses to the opponent represented by this data.
     */
    public long losses;
    /**
     * The number of wins against the opponent represented by this data
     */
    public long wins;

}

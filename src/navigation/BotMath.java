package navigation;

/**
 * Commonly used math algorithms.
 * @author David McCoy
 */
public class BotMath {

    // PUBLIC METHODS

    /**
     * Returns the amount of damage inflicted by a bullet using the specified
     * power (not factoring in the life of the target).<br>
     * NOTE: A shotPower value greater than 3 will return an invalid result
     * @param shotPower A double specifying the power with which the bullet
     *                  was fired
     * @return A double specifying the amount of damage inflicted by the
     *         bullet
     */
    public static double calculateDamage(double shotPower) {
        double damage = (shotPower * 4);
        if (shotPower > 1) {
            damage += ((shotPower - 1) * 2);
        }
        return damage;
    }

}

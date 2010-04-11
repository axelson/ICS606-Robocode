package navigation;

/**
 * Defines functionality required to receive commands.
 * @author David McCoy
 */
public interface CommandListener {

    // PUBLIC METHODS

    /**
     * This method will be called to indicate the CommandListener should free
     * all resources and cease execution.
     */
    public void disable();

    /**
     * This method will be called to indicate the CommandListener is free to
     * begin execution.
     */
    public void enable();

    /**
     * This method will be called each turn to allow the CommandListener to
     * execute turn based instructions.
     */
    public void execute();

}

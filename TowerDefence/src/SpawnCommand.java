/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * SpawnCommand class:
 * Defines a single command used by the SpawnManager.
 */
public class SpawnCommand {
    /**
     * A command id used for spawning a tower or changing the timer delay.
     */
    private int command;
    /**
     * Value associated with the command.
     */
    private int value;

    /**
     * Stores the properties of the spawn command.
     *
     * @param command A command id used for spawning a tower or changing the timer delay.
     * @param value Value associated with the command.
     */
    public SpawnCommand(int command, int value) {
        this.command = command;
        this.value = value;
    }

    /**
     * Gets the command id.
     *
     * @return An int representing the command.
     */
    public int getCommand() {
        return command;
    }

    /**
     * Gets the value associated with the command.
     *
     * @return The value associated with this command.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the command to the specified value.
     *
     * @param value new value to change the value to.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Converts from a String to a command.
     *
     * @param command A string version of the command to convert.
     * @return An int to map the command string to or 0 if none can be mapped.
     */
    public static int convertStringToCommand(String command) {
        switch (command) {
            case "N": return 1;
            case "F": return 2;
            case "B": return 3;
            case "T": return 4;
        }
        return 0;
    }
}
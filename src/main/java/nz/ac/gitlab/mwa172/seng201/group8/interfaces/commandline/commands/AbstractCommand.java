package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;

public abstract class AbstractCommand {

    /**
     * A string representation of this command.
     */
    private final String commandString;

    /**
     * Constructs a command by setting the command string.
     *
     * @param commandString The command string.
     */
    protected AbstractCommand(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Runs the command.
     *
     * @param context Context in which this command is executed.
     * @return Returns the result of running this command.
     * @throws GameOver Thrown if running this command causes the game to end.
     */
    public abstract CommandResult run(CommandContext context) throws GameOver;

    /**
     * Determines if the given string is a command in the given
     * context.
     *
     * @param context Context in which this command is attempting to be executed.
     * @param input   String input to test.
     * @return Returns true if the input is a valid command in the current context,
     * false otherwise.
     */
    public boolean isCommand(CommandContext context, String input) {
        return input.equalsIgnoreCase(commandString);
    }

    /**
     * Gets a string representation of this command.
     *
     * @return Returns a string representation of this command.
     */
    @Override
    public String toString() {
        return commandString;
    }
}

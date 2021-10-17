package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

/**
 * Provides implementation for exiting the game.
 */
public class ExitCommand extends IslandCommand {

    /**
     * Constructor for exit commands.
     * Sets the command string to "Exit".
     */
    protected ExitCommand() {
        super("Exit");
    }

    /**
     * Runs the exit command. Exit commands do nothing but return a wait
     * result.
     *
     * @param context Context in which this command is run.
     * @return A command result that instructs the thread to sleep for 1 second.
     * @throws GameOver Thrown if running this command causes the game to end.
     * Game over is never thrown by exit commands.
     */
    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        System.exit(0);
        return CommandResult.getResultWithWait(1);
    }
}

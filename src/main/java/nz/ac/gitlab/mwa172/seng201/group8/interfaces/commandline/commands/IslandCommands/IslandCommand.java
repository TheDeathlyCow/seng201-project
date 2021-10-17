package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.AbstractCommand;

/**
 * Abstract class for island commands. Does nothing but define
 * the class in which island commands exist.
 */
public abstract class IslandCommand extends AbstractCommand {

    /**
     * Constructs an island command by setting the command string.
     * @param commandString The command string.
     */
    protected IslandCommand(String commandString) {
        super(commandString);
    }
}

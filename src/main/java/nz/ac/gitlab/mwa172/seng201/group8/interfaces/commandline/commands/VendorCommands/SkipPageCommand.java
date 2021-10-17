package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

public class SkipPageCommand extends VendorCommand {

    private final int SKIP_PAGES;

    /**
     * Constructs a skip pages command with a specified command string.
     *
     * @param commandString The command string.
     * @param toSkip How many pages to skip when this command is run.
     */
    protected SkipPageCommand(String commandString, int toSkip) {
        super(commandString);
        SKIP_PAGES = toSkip;
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        return new CommandResult.Builder()
                .skipPages(SKIP_PAGES)
                .build();
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands;

import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class TravelCommand extends AbstractCommand {

    private static final List<TravelCommand> commands = new ArrayList<>();

    protected TravelCommand(final String commandString) {
        super(commandString);
    }

}

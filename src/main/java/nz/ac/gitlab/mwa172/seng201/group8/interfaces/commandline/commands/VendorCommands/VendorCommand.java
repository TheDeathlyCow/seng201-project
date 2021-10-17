package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.AbstractCommand;

import java.util.*;
import java.util.stream.Collectors;

public abstract class VendorCommand extends AbstractCommand {
    /**
     * Constructs a vendor command with a specified command string.
     * @param commandString The command string.
     */
    protected VendorCommand(String commandString) {
        super(commandString);
    }
}

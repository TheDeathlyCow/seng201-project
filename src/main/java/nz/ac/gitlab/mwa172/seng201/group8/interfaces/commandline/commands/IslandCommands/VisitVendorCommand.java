package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands.VendorCommands;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands.TravelCommands;

public class VisitVendorCommand extends IslandCommand {

    /**
     * Constructs a visit docks command by setting the command
     * string to "Visit Vendor".
     */
    protected VisitVendorCommand() {
        super("Visit Vendor");
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        CommandContext vendorContext = new CommandContext.Builder()
                .withPlayer(context.getExecutingPlayer())
                .build();
        Vendor vendor = context.getExecutingPlayer().getShip().dockedAt().getVendor();
        System.out.println(vendor.getRandomGreeting(context.getExecutingPlayer()));
        return VendorCommands.SEE_VENDOR_STOCK.run(vendorContext);
    }
}

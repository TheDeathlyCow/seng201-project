package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.Selector;

import java.util.List;
import java.util.regex.Pattern;

public class SellItemCommand extends VendorCommand implements Selector<Tradeable> {
    protected SellItemCommand() {
        super("Select");
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        Tradeable toSell = (Tradeable)context.getSelection();
        if (toSell != null) {
            Player player = context.getExecutingPlayer();
            Vendor vendor = player.getShip().dockedAt().getVendor();

            if (vendor.canPurchase(toSell)) {
                player.sell(toSell, vendor);
                System.out.printf("You sold %s for %.2f Doubloons!%n",
                        toSell.getName(),
                        toSell.getValueAtVendor(vendor)
                );
            } else {
                System.out.println("Sorry, you cannot sell that item!");
            }
        } else {
            System.out.println("\nInvalid selection!");
        }

        return CommandResult.getResultWithWait(0);
    }

    @Override
    public Tradeable select(CommandContext context, List<Tradeable> selectFrom, String selection) {
        if (this.isCommand(context, selection)) {
            int selectedIndex = Integer.parseInt(selection) - 1;

            if (selectedIndex >= 0 && selectedIndex < selectFrom.size()) {
                return selectFrom.get(selectedIndex);
            }
        }
        return null;
    }

    @Override
    public boolean isCommand(CommandContext context, String input) {
        Player.Location location = context.getExecutingPlayer().getLocation();
        if (location.equals(Player.Location.VENDOR_SELLING)) {
            return InputValidator.isNumeric(input);
        }
        return false;
    }
}

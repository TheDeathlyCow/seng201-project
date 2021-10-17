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

/**
 * Class that provides implementation for purchasing goods
 * from the island vendors.
 */
public class PurchaseItemCommand extends VendorCommand implements Selector<Tradeable> {
    /**
     * Basic constructor for purchase item commands.
     */
    protected PurchaseItemCommand() {
        super("Select");
    }

    /**
     * Purchases the selected item.
     * @param context Context in which this command is executed.
     * @return The result of executing this command.
     * @throws GameOver Thrown if the game ends as a result of this command's execution.
     */
    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        Tradeable toBuy = (Tradeable)context.getSelection();
        if (toBuy != null) {
            Player player = context.getExecutingPlayer();

            if (context.getExecutingPlayer().canPurchase(toBuy)) {
                Vendor vendor = player.getShip().dockedAt().getVendor();
                player.purchase(toBuy, vendor);
                System.out.printf("You purchased %s for %.2f Doubloons!%n",
                        toBuy.getName(),
                        toBuy.getValueAtVendor(vendor)
                );
            } else {
                System.out.println("Sorry, you cannot purchase that item!");
            }
        } else {
            System.out.println("\nInvalid selection!");
        }

        return CommandResult.getResultWithWait(0);
    }

    /**
     * Selects the item that the user wants to purchase.
     * @param context The context in which to select.
     * @param selectFrom The list to select from.
     * @param selection The item the user wants to select.
     * @return The users item selection.
     */
    @Override
    public Tradeable select(CommandContext context, List<Tradeable> selectFrom, String selection) {
        if (this.isCommand(context, selection)) {
            int selectedIndex = Integer.parseInt(selection) - 1;

            if (selectedIndex >= 0 && selectedIndex < selectFrom.size())
                return selectFrom.get(selectedIndex);
        }
        return null;
    }

    /**
     * Takes a player input and checks if it is valid (is numeric).
     * @param context Context in which this command is attempting to be executed.
     * @param input  String input to test.
     * @return True if the input is valid, false if not.
     */
    @Override
    public boolean isCommand(CommandContext context, String input) {
        Player.Location playerLocation = context.getExecutingPlayer().getLocation();
        if (playerLocation.equals(Player.Location.VENDOR_BUYING)) {
            return InputValidator.isNumeric(input);
        }

        return false;
    }
}

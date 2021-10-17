package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.Selector;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands.TravelCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeeVendorStock extends ViewStockCommand {

    protected SeeVendorStock() {
        super("Purchase Cargo", BUY_PROMPT);
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        context.getExecutingPlayer().setLocation(Player.Location.VENDOR_BUYING);
        CommandContext purchaseContext = new CommandContext.Builder()
                .withPlayer(context.getExecutingPlayer())
                .build();

        return super.run(purchaseContext, (ctx) -> {
            Vendor vendor = ctx.getExecutingPlayer().getShip().dockedAt().getVendor();
            List<Map<Tradeable, Integer>> pages = getPagesWithQuantity(vendor.getCargoStock());
            pages.addAll(getPagesWithQuantity(vendor.getUpgradeStock()));
            return pages;
        });
    }

    @Override
    protected void printHeader(CommandContext context) {
        Island island = context.getExecutingPlayer().getShip().dockedAt();
        System.out.println();
        System.out.printf("Current balance: %.2f Doubloons%n", context.getExecutingPlayer().getBalance());
        System.out.println("===== " + island.getName() + " Vendor Stock =====");
    }

    @Override
    protected Selector<Tradeable> getSelector() {
        return VendorCommands.PURCHASE_CARGO;
    }

    /**
     * A string containing the prompt to display to the player when they
     * want to buy something.
     */
    protected static final String BUY_PROMPT = "Type the number of the item you wish to buy, or type 'next page' or 'previous page' to see what else this vendor has.\n"
            + "If you wish to sell your cargo, type 'sell cargo'.\n"
            + "Type 'return' if you wish to go back.\n";
}

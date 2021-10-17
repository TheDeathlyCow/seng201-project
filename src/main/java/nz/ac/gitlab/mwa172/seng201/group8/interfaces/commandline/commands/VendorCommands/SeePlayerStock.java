package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.Selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeePlayerStock extends ViewStockCommand {

    protected SeePlayerStock() {
        super("Sell Cargo", SELL_PROMPT);
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        context.getExecutingPlayer().setLocation(Player.Location.VENDOR_SELLING);
        return super.run(context, (ctx) -> {
            Player player = ctx.getExecutingPlayer();
            List<Map<Tradeable, Integer>> pages = getPagesWithQuantity(player.getShip().getCargo());
            pages.addAll(getPagesWithQuantity(player.getShip().getUpgrades()));
            return pages;
        });
    }

    protected void printHeader(CommandContext context) {
        System.out.println();
        System.out.printf("Current balance: %.2f Doubloons%n", context.getExecutingPlayer().getBalance());
        System.out.println("===== Your Stock =====");
    }

    @Override
    protected Selector<Tradeable> getSelector() {
        return VendorCommands.SELL_CARGO;
    }

    /**
     * A string containing the prompt to display to the player when they
     * want to sell something.
     */
    protected static final String SELL_PROMPT = "Type the number of the item you wish to sell, or type 'next page' or 'previous page' to see what else you have.\n"
            + "Type 'return' if you wish to go back.\n";
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

import java.util.ArrayList;
import java.util.List;


/**
 * View Cargo class. Allows the Player to view their cargo and various
 * information regarding it such as price.
 */

public class ViewCargoCommand extends IslandCommand {
    /**
     * Constructs the View Cargo Command.
     */
    protected ViewCargoCommand() {
        super("View Cargo");
    }

    /**
     * Provides the result of the executed command.
     * @param context Context in which this command is executed.
     * @return Displays the Cargo
     * @throws GameOver If running this command causes the game to end.
     */
    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        List<Tradeable> items = new ArrayList<>();
        items.addAll(context.getExecutingPlayer().getShip().getCargo());
        items.addAll(context.getExecutingPlayer().getShip().getSoldHistory());
        if (items.size() == 0) {
            System.out.println("You have not purchased any cargo yet!");
            return new CommandResult.Builder().withWait(1).build();
        }

        for (Tradeable item : items) {
            String itemMessage = String.format("- Paid %.2f Doubloons for %s",
                    item.getPurchaseValue(),
                    item.getName());
            if (item.isSold()) {
                itemMessage += String.format(", sold for %.2f Doubloons on %s",
                        item.getSoldValue(),
                        item.getSoldAt().getName()
                );
            }
            System.out.println(itemMessage);
        }
        return CommandResult.getResultWithWait(3);
    }
}

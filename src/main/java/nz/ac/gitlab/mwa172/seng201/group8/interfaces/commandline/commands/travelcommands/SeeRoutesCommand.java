package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

import java.util.List;

/**
 * Class that provides the implementation for seeing the possible routes
 * from the current island to other islands.
 */
public class SeeRoutesCommand extends TravelCommand {

    /**
     * Basic constructor for see routes commands.
     */
    protected SeeRoutesCommand() {
        super("See Routes");
    }

    /**
     * Allows the player to see the routes from their current island
     * to the selected island.
     * @param context Context in which this command is executed.
     * @return  The result of executing this command.
     * @throws GameOver Thrown if the game ends as a result of this command's execution.
     */
    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        Player executing = context.getExecutingPlayer();

        GameWorld world = context.getWorld();
        Island dockedAt = executing.getShip().dockedAt();
        List<Connection> connections = world.getConnectionsOf(dockedAt);

        String nextIsland = getInput(context, connections);

        TravelCommand command = TravelCommands.REGISTRY.getCommand(context, nextIsland);

        assert command != null;
        Object selection = TravelCommands.SET_SAIL.select(context, connections, nextIsland);

        CommandContext nextContext = new CommandContext.Builder()
                .withWorld(context.getWorld())
                .withSelection(selection)
                .build();

        command.run(nextContext);

        return CommandResult.getResultWithWait(0);
    }

    /**
     * Displays a list of connections and prompts the user to select the destination
     * they would like to travel to.
     *
     * @param context Context in which this command is executed.
     * @param connections The connections from the current island to all other possible islands.
     * @return The island that the player wants to travel to,
     * or returns the player to their current island.
     */
    private String getInput(CommandContext context, List<Connection> connections) {
        System.out.println("Available Routes:");
        PlayerShip ship = context.getExecutingPlayer().getShip();
        for (int i = 0; i < connections.size(); i++) {
            String description = connections.get(i)
                    .getDestination().getVendor().getDesireDescription();
            description += "\n";
            description += connections.get(i).getSailingLabel(ship);
            description += "\t" + connections.get(i).getDescription();
            System.out.printf(" - %d: %s%n", i + 1, description);
        }

        return IslandTraderCommandApp.getInput(
                "Type the number of the island you want to go to, or type 'return' to return to the island.\n",
                (input) -> TravelCommands.REGISTRY.isCommand(context, input)
        );
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.Selector;

import java.util.List;

/**
 * Class that provides the implementation for sailing to another
 * island via the command line.
 */
public class SetSailCommand extends TravelCommand implements Selector<Connection> {

    /**
     * Basic constructor for set sail commands.
     */
    protected SetSailCommand() {
        super("Set Sail");
    }

    /**
     * Sets sail from the island the player currently docked at
     * to the selected island.
     *
     * @param context The context in which this command is executed.
     * @return The result of executing this command.
     * @throws GameOver Thrown if the game ends as a result of this command's execution.
     */
    @Override
    public CommandResult run(CommandContext context) throws GameOver {

        if (context.getSelection() == null) {
            System.out.println("\nInvalid selection!");
            return CommandResult.getResultWithWait(2);
        }

        Player player = context.getExecutingPlayer();
        PlayerShip ship = player.getShip();
        Connection route = (Connection) context.getSelection();

        int waitTime = 5;

        if (ship.canSailRoute(route)) {
            System.out.printf("You have set sail for the island of %s!%n", route.getDestination().getName());

            TravellingEvent<?> event = ship.travel(route);
            if (event != null) {
                runEvent(event, ship);
            } else {
                System.out.println("You safely made it to your destination!");
            }

            System.out.printf("Welcome to %s!%n", route.getDestination().getName());
        } else {
            System.out.println("You cannot sail this route!");
            waitTime = 2;
        }

        return CommandResult.getResultWithWait(waitTime);
    }

    /**
     * Runs a given event, targeting the player's ship.
     * If the event causes the game to end, this method will print a message
     * before throwing the game over context back up the call stack.
     *
     * @param event  Event to run.
     * @param target Target of event to run.
     * @throws GameOver Thrown if the game ends. Contains context for how the game ended.
     */
    public void runEvent(TravellingEvent<?> event, PlayerShip target) throws GameOver {
        String result;
        try {
            result = event.runEvent(target,
                    IslandTraderCommandApp.DICE_ROLLER_1_6,
                    e -> System.out.println(e.getAttackMessage())
            );
        } catch (GameOver over) {
            System.out.println("Game Over!");
            throw over;
        }
        System.out.println(result);
    }

    /**
     * Determines if an input string matches the format for a set sail command.
     *
     * @param input The input string.
     * @return Returns true if the input string is only a positive integer, false otherwise.
     */
    @Override
    public boolean isCommand(CommandContext context, String input) {
        return InputValidator.isNumeric(input);
    }

    /**
     * Selects an item from a list of connections based on some player input string.
     *
     * @param selectFrom The list of connections to select from.
     * @param selection  The connection the player wants to select.
     * @return Returns the selected connection if the input selection is valid,
     * otherwise it will return null.
     */
    @Override
    public Connection select(CommandContext context, List<Connection> selectFrom, String selection) {
        if (this.isCommand(context, selection)) {
            int selectedIndex = Integer.parseInt(selection) - 1;

            if (selectedIndex >= 0 && selectedIndex < selectFrom.size())
                return selectFrom.get(selectedIndex);
        }
        return null;
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

import java.util.regex.Pattern;

/**
 * Provides the implementation for hiring crew from an island.
 */
public class HireCrewCommand extends IslandCommand {

    /**
     * Constructs a hire crew command with the command string
     * "Hire Crew".
     */
    protected HireCrewCommand() {
        super("Hire Crew");
    }

    /**
     * Continuously prompts the user to enter a number of crew they want to hire
     * until they enter a valid amount, then adds that amount to their crew.
     * If the player already has a full crew, then the command just returns.
     *
     * @param context Context in which this command is executed.
     * @return Returns the result of executing this command.
     * @throws GameOver Game over is never thrown by hire crew commands.
     */
    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        PlayerShip ship = context.getExecutingPlayer().getShip();

        if (ship.getNumCrew() >= ship.getAttributes().getNumBeds()) {
            System.out.println("You cannot any more crew!");
            return CommandResult.getResultWithWait(2);
        }

        String amountString = IslandTraderCommandApp.getInput(
                "Enter the number of crew you'd like to hire: ",
                (input) -> {
                    if (InputValidator.isNumeric(input)) {
                        int amount = Integer.parseInt(input);
                        return ship.canHireCrew(amount);
                    }
                    return false;
                }
        );

        int amount = Integer.parseInt(amountString);
        double cost = amount * IslandTraderConfig.CONFIG.getHireCost();
        System.out.printf("You hired %d crew for %.2f Doubloons!%n", amount, cost);
        ship.addCrew(amount);
        context.getExecutingPlayer().removeFunds(cost);

        return CommandResult.getResultWithWait(3);
    }
}

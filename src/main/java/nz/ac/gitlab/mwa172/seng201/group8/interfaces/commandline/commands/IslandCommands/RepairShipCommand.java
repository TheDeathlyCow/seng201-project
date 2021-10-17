package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

public class RepairShipCommand extends IslandCommand {

    /**
     * Constructs an repair ship command. The command string
     * for repair ship commands is "Repair Ship".
     */
    protected RepairShipCommand() {
        super("Repair Ship");
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        PlayerShip ship = context.getExecutingPlayer().getShip();
        if (ship.getHealth() < ship.getAttributes().getMaxHealth()) {
            double repairCost = ship.getRepairCost();
            if (ship.repairShip()) {
                System.out.printf("Your ship was repaired, costing you %.2f Doubloons!%n",
                        repairCost);
            } else {
                System.out.println("You cannot afford to repair your ship!");
            }
        } else {
            System.out.println("You do not need to repair your ship!");
        }

        return CommandResult.getResultWithWait(2);
    }
}

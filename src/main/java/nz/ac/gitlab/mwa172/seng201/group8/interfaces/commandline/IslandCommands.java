package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands.TravelCommand;

import java.util.Arrays;

@Deprecated
enum IslandCommands {

    VIEW_SHIP("View Ship", ViewCommands::viewShip),
    VISIT_VENDOR("Visit Vendor", VendorCommands::visitVendor),
    VIEW_CARGO("View Cargo", ViewCommands::viewCargo),
    REPAIR_SHIP("Repair Ship", IslandCommands::repairShip),
    EXIT("Exit", IslandCommands::exit),
    UNKNOWN("unknown", IslandCommands::unknown);

    private final String actionName;
    private final CommandRunner runner;

    @Deprecated
    IslandCommands(String actionName, CommandRunner runner) {
        this.actionName = actionName;
        this.runner = runner;
    }

    @Override
    public String toString() {
        return actionName;
    }

    @Deprecated
    static IslandCommands[] getCommands() {
        int length = IslandCommands.values().length;
        return Arrays.copyOfRange(IslandCommands.values(), 0, length - 1);
    }

    @Deprecated
    static IslandCommands getCommand(String commandString) {
        for (IslandCommands command : IslandCommands.values()) {
            if (commandString.equalsIgnoreCase(command.toString())) {
                return command;
            }
        }
        return UNKNOWN;
    }

    @Deprecated
    static boolean isValidCommand(String input) {
        try {
            return getCommand(input) != UNKNOWN;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    static IslandCommands getInput() {
        System.out.println(IslandCommands.getBasicCommandsString());
        String selectActionString = IslandTraderCommandApp.getInput(
                "What would you like to do? ",
                IslandCommands::isValidCommand
        );

        return IslandCommands.getCommand(selectActionString);
    }

    @Deprecated
    static String getBasicCommandsString() {
        StringBuilder actionBuilder = new StringBuilder();
        actionBuilder.append("Available actions:\n");
        for (IslandCommands command : getCommands()) {
            actionBuilder.append("- ");
            actionBuilder.append(command.toString());
            actionBuilder.append("\n");
        }
        return actionBuilder.toString();
    }

    //* Running Methods *//

    /**
     * Runs this command.
     * @param executing The player executing the command.
     * @return Returns the number of second the thread should wait until beginning the next tick.
     *      If a negative number is returned, then the application should exit.
     */
    @Deprecated
    int run(Player executing) throws GameOver {
        return runner.run(executing);
    }

    //* General Command Methods *//

    @Deprecated
    static int repairShip(Player executing) throws GameOver {
        PlayerShip ship = executing.getShip();
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

        return 2;
    }

    @Deprecated
    static int exit(Player executing) {
        System.out.println("Exiting...");
        return -1;
    }

    @Deprecated
    static int unknown(Player executing) {
        System.out.println("Unknown command, please try again.");
        return 0;
    }

}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

public class ViewShipCommand extends IslandCommand {
    protected ViewShipCommand() {
        super("View Ship");
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        PlayerShip ship = context.getExecutingPlayer().getShip();
        String status =  "Name: " + ship.getName() + "\n" +
                String.format("Current health: %d / %d (%.0f%%)%n",
                        ship.getHealth(),
                        ship.getAttributes().getMaxHealth(),
                        ship.getHealthAsPercentage()) +
                "Crew: " + ship.getNumCrew() + "\n" +
                "Number of beds: " + ship.getAttributes().getNumBeds() + "\n" +
                "Minimum crew to sail: " + ship.getMinCrewToSail() + "\n" +
                String.format("Wages per day: %.2f Doubloons%n", ship.getCrewWagesPerDay()) +
                String.format("Speed: %d km / day%n", (int)ship.getAttributes().getSpeed()) +
                String.format("Armor: %d, Damage: %d%n",
                        ship.getAttributes().getArmour(),
                        ship.getAttributes().getDamage()) +
                "Current weight: " + ship.getCurrentWeight() + "\n" +
                "Max weight: " + ship.getAttributes().getMaxWeight() + "\n" +
                String.format("Cargo: %d / %d slots filled%n",
                        ship.getCargoManager().size(),
                        ship.getCargoManager().getMaxSize()) +
                String.format("Upgrades (%d / %d slots filled)",
                        ship.getUpgrades().size(),
                        ship.getUpgrades().getMaxSize());
        StringBuilder builder = new StringBuilder();
        if (ship.getUpgrades().size() > 0) {
            builder.append(": ");
        }
        ship.getUpgrades()
                .forEach(u -> builder.append(" - ").append(u.toString()));

        System.out.println(status + builder.toString());
        return new CommandResult.Builder().withWait(3).build();
    }
}

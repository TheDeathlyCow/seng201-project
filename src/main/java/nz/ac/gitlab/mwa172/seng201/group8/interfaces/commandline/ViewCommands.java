package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.util.List;

@Deprecated
class ViewCommands {

    @Deprecated
    static int viewShip(Player executing) {
        PlayerShip ship = executing.getShip();
        System.out.println("Name: " + ship.getName());
        System.out.println("Crew: " + ship.getNumCrew());
        System.out.println("Wages per day: " + ship.getCrewWages(1));
        System.out.println("Current weight: " + ship.getCurrentWeight());
        System.out.println("Max weight: " + ship.getAttributes().getMaxWeight());
        System.out.printf("Cargo: %d / %d slots filled%n",
                ship.getCargoManager().size(),
                ship.getCargoManager().getMaxSize()
        );
        System.out.printf("Current health: %d / %d (%.0f%%)%n",
                ship.getHealth(),
                ship.getAttributes().getMaxHealth(),
                ship.getHealthAsPercentage());
        System.out.printf("Upgrades (%d/%d slots filled):%n",
                ship.getUpgrades().size(),
                ship.getUpgrades().getMaxSize());

        ship.getUpgrades()
                .forEach(s -> System.out.println(s.toString()));
        return 3;
    }

    @Deprecated
    static int viewCargo(Player executing) {
        List<CargoItem> cargoHold = executing.getShip().getCargoManager().getCargo();
        if (cargoHold.size() == 0) {
            System.out.println("You have not purchased any cargo yet!");
            return 1;
        }

        for (CargoItem item : cargoHold) {
            String itemMessage = String.format("- Paid %.2f for %s",
                    item.getPurchaseValue(),
                    item.getName());
            if (item.isSold()) {
                itemMessage += String.format(", sold for %.2f at %s",
                        item.getSoldValue(),
                        item.getSoldAt().getName()
                );
            }
            System.out.println(itemMessage);
        }
        return 3;
    }

}

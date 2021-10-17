package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.cargo;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.TestLoader;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameOverTests {

    GameWorld testWorld;

    @BeforeEach
    void setup() throws FileNotFoundException {
        testWorld = TestLoader.getTestWorld();
        PlayerShip ship = testWorld.getPlayer().getShip();
        double balance = testWorld.getPlayer().getBalance();
        testWorld.getPlayer().removeFunds(balance);
        ship.addCrew(ship.getAttributes().getNumBeds() - ship.getNumCrew());
    }

    @Test
    void gameEndsWhenTimeIsUp() {
        assertDoesNotThrow(() -> testWorld.addDays(testWorld.getDaysRemaining() - 1));
        assertThrows(GameOver.class, () -> testWorld.addDays(1));
    }

    @Test
    void bankruptPlayerEndsGameWhenRepairingShip() {
        PlayerShip ship = testWorld.getPlayer().getShip();
        ShipAttackContext context = new ShipAttackContext.Builder(ship)
                .withDamageDealt(ship.getHealth()).build();
        ship.onDamaged(context);
        assertThrows(GameOver.class, ship::repairShip);
    }

    @Test
    void wealthyPlayerCanRepairShip() {
        PlayerShip ship = testWorld.getPlayer().getShip();
        ShipAttackContext context = new ShipAttackContext.Builder(ship)
                .withDamageDealt(ship.getHealth()).build();
        ship.onDamaged(context);
        testWorld.getPlayer().addFunds(1000000);
        assertDoesNotThrow(ship::repairShip);
    }

    @Test
    void playerWithCargoCanRepairShip() {
        PlayerShip ship = testWorld.getPlayer().getShip();
        ShipAttackContext context = new ShipAttackContext.Builder(ship)
                .withDamageDealt(ship.getHealth()).build();
        ship.onDamaged(context);

        double repairCost = ship.getRepairCost();

        CargoItem item = new CargoItem("Test", CargoItem.CargoType.COMMON, repairCost * 2, 1);
        ship.getCargoManager().addCargo(item);
        assertDoesNotThrow(ship::repairShip);
    }

    @Test
    void playerWithUpgradesCanRepairShio() {
        PlayerShip ship = testWorld.getPlayer().getShip();
        ShipAttackContext context = new ShipAttackContext.Builder(ship)
                .withDamageDealt(ship.getHealth()).build();
        ship.onDamaged(context);

        double repairCost = ship.getRepairCost();

        ShipUpgrade upgrade = new ShipUpgrade.ShipUpgradeBuilder("Test")
                .withBaseValue(repairCost * 5)
                .build();

        ship.upgrade(upgrade);


        assertDoesNotThrow(ship::repairShip);
    }

    @Test
    void sailingMinRouteDoesNotEndGameWhenPoor() {
        Player player = testWorld.getPlayer();
        PlayerShip ship = player.getShip();
        Island island = ship.dockedAt();
        Connection minConnection = testWorld.getConnectionsOf(island).get(0);
        for (Connection c : testWorld.getConnectionsOf(island)) {
            if (c.getDistance() < minConnection.getDistance()) {
                minConnection = c;
            }
        }

        player.addFunds(ship.getCrewWages(minConnection.getDistance()) + 0.1);

        Connection finalMinConnection = minConnection;
        assertDoesNotThrow(() -> ship.canSailRoute(finalMinConnection));

    }

    @Test
    void sailingDoesNotEndGameWithOnlyCargo() {
        Player player = testWorld.getPlayer();
        PlayerShip ship = player.getShip();
        Island island = ship.dockedAt();
        Connection minConnection = testWorld.getConnectionsOf(island).get(0);
        for (Connection c : testWorld.getConnectionsOf(island)) {
            if (c.getDistance() < minConnection.getDistance()) {
                minConnection = c;
            }
        }

        double wageCost = ship.getCrewWages(minConnection.getDistance());
        CargoItem item = new CargoItem("Test", CargoItem.CargoType.COMMON, wageCost * 2, 1);
        ship.getCargoManager().addCargo(item);

        Connection finalMinConnection = minConnection;

        assertDoesNotThrow(() -> ship.canSailRoute(finalMinConnection));

    }

    @Test
    void sailingDoesNotEndGameWithOnlyUpgrades() {
        Player player = testWorld.getPlayer();
        PlayerShip ship = player.getShip();
        Island island = ship.dockedAt();
        Connection minConnection = testWorld.getConnectionsOf(island).get(0);
        for (Connection c : testWorld.getConnectionsOf(island)) {
            if (c.getDistance() < minConnection.getDistance()) {
                minConnection = c;
            }
        }

        double wageCost = ship.getCrewWages(minConnection.getDistance());

        ShipUpgrade upgrade = new ShipUpgrade.ShipUpgradeBuilder("Test")
                .withBaseValue(wageCost * 5)
                .build();

        ship.upgrade(upgrade);

        Connection finalMinConnection = minConnection;
        assertDoesNotThrow(() -> ship.canSailRoute(finalMinConnection));

    }

    @Test
    void sailingMaxRouteDoesNotEndGameWhenPoor() {
        Player player = testWorld.getPlayer();
        PlayerShip ship = player.getShip();
        Island island = ship.dockedAt();
        Connection minConnection = testWorld.getConnectionsOf(island).get(0);
        for (Connection c : testWorld.getConnectionsOf(island)) {
            if (c.getDistance() < minConnection.getDistance()) {
                minConnection = c;
            }
        }

        Connection maxConnection = testWorld.getConnectionsOf(island).get(0);
        for (Connection c : testWorld.getConnectionsOf(island)) {
            if (c.getDistance() > maxConnection.getDistance()) {
                maxConnection = c;
            }
        }

        player.addFunds(ship.getCrewWages(minConnection.getDistance()) + 0.1);

        Connection finalMaxConnection = maxConnection;
        assertDoesNotThrow(() -> ship.canSailRoute(finalMaxConnection));
    }
}

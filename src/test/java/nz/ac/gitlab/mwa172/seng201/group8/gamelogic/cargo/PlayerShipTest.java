package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.cargo;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerShipTest {

    GameWorld gameWorld;
    ShipUpgrade testUpgrade;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        FileManager.load();
        gameWorld = new GameWorld(new Player("Test Player"), 0, 12345);
        gameWorld.generateIslands();
        gameWorld.getPlayer().setShip(FileManager.getShips().get(0));

        testUpgrade = new ShipUpgrade.ShipUpgradeBuilder("test")
                .withUpgradeSlotModifier(5)
                .withArmourModifier(1)
                .withBaseValue(1)
                .withCargoSlotModifier(1)
                .withDamageModifier(1)
                .withMaxHealthModifier(1)
                .withSpeedModifier(1)
                .withNumBedsModifier(1)
                .withMaxWeightModifier(0)
                .build();
    }

    @Test
    void cannotApplyNegativeUpgrades() {
        PlayerShip ship = gameWorld.getPlayer().getShip();

        // max health
        ShipUpgrade negativeUpgrade = new ShipUpgrade.ShipUpgradeBuilder("negative health")
                .withMaxHealthModifier(Integer.MIN_VALUE)
                .build();
        ShipUpgrade borderlineInvalid = new ShipUpgrade.ShipUpgradeBuilder("zero health")
                .withMaxHealthModifier(-ship.getAttributes().getMaxHealth())
                .build();
        assertFalse(ship.upgrade(borderlineInvalid));
        assertFalse(ship.getUpgrades().contains(borderlineInvalid));
        assertFalse(ship.upgrade(negativeUpgrade));
        assertFalse(ship.getUpgrades().contains(negativeUpgrade));

        // armour
        negativeUpgrade = new ShipUpgrade.ShipUpgradeBuilder("negative armour")
                .withArmourModifier(Integer.MIN_VALUE)
                .build();
        borderlineInvalid = new ShipUpgrade.ShipUpgradeBuilder("-1 armour")
                .withArmourModifier(-ship.getAttributes().getArmour() - 1)
                .build();
        assertFalse(ship.upgrade(borderlineInvalid));
        assertFalse(ship.getUpgrades().contains(borderlineInvalid));
        assertFalse(ship.upgrade(negativeUpgrade));
        assertFalse(ship.getUpgrades().contains(negativeUpgrade));
    }

    @Test
    void testRemoveUpgrade() {
        PlayerShip ship = gameWorld.getPlayer().getShip();
        assertTrue(ship.upgrade(testUpgrade));
        assertTrue(ship.upgrade(testUpgrade));
        assertTrue(ship.removeUpgrade(testUpgrade));
    }

    @Test
    void fullHealthShipDoesNotRepair() throws GameOver {
        gameWorld.getPlayer().addFunds(10000);
        PlayerShip ship = gameWorld.getPlayer().getShip();
        assertFalse(ship.repairShip());
    }

    @Test
    void damagedShipDoesRepairWithEnoughMoney() throws GameOver {
        gameWorld.getPlayer().addFunds(10000);
        PlayerShip ship = gameWorld.getPlayer().getShip();
        ship.onDamaged(new ShipAttackContext.Builder(ship)
                .withDamageDealt(5)
                .build());
        assertTrue(ship.repairShip());
        assertEquals(ship.getHealth(), ship.getAttributes().getMaxHealth());
    }

    @Test
    void fundsRemovedWhenRepaired() throws GameOver {
        gameWorld.getPlayer().addFunds(10000);
        PlayerShip ship = gameWorld.getPlayer().getShip();
        ship.onDamaged(new ShipAttackContext.Builder(ship)
                .withDamageDealt(5)
                .build());
        double preBalance = gameWorld.getPlayer().getBalance();
        double repairCost = ship.getRepairCost();
        ship.repairShip();
        double postBalance = gameWorld.getPlayer().getBalance();
        assertEquals(postBalance + repairCost, preBalance);
    }

}
package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.TestLoader;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.PirateShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PirateAttackTest {
    PirateAttack attack;
    PlayerShip playerShip;
    PirateShip pirateShip;
    GameWorld testWorld;

    @BeforeEach
    void setup() throws FileNotFoundException {
        testWorld = TestLoader.getTestWorld();
        Player testPlayer = testWorld.getPlayer();
        ShipUpgrade baseUpgrade = new ShipUpgrade.ShipUpgradeBuilder("test")
                .withUpgradeSlotModifier(3)
                .withArmourModifier(7)
                .withBaseValue(500.)
                .withCargoSlotModifier(15)
                .withDamageModifier(10)
                .withMaxHealthModifier(100)
                .withSpeedModifier(1)
                .withNumBedsModifier(10)
                .withMaxWeightModifier(100)
                .build();
        playerShip = FileManager.getShips().get(0);
        pirateShip = new PirateShip("Pirates", baseUpgrade, 5);
        attack = new PirateAttack("Pirate Attack", pirateShip, 1);
        testPlayer.setShip(playerShip);
    }


    @RepeatedTest(10)
    void runAttack10Times() throws GameOver {
        try {
            attack.runEvent(playerShip);
        } catch (GameOver over) {
            System.out.println("Player died!");
        }
    }

    @Test
    void damagingShipReducesHealth() {
        int preHealth = pirateShip.getHealth();
        ShipAttackContext context = new ShipAttackContext.Builder(playerShip)
                .withDamageDealt(pirateShip.getHealth())
                .build();
        playerShip.onDamaged(context);
        assertNotEquals(playerShip.getHealth(), preHealth);
    }

    @Test
    void testRollMultipliers() {
        Map<Integer, Double> rollValues = new LinkedHashMap<>();

        rollValues.put(1, 1.0);
        rollValues.put(2, 1.0);
        rollValues.put(3, 1.5);
        rollValues.put(4, 1.5);
        rollValues.put(5, 2.0);
        rollValues.put(6, 2.0);

        for (Integer roll : rollValues.keySet()) {
            double multiplier = attack.getMultiplier(roll);
            assertEquals(rollValues.get(roll), multiplier);
        }
    }
}
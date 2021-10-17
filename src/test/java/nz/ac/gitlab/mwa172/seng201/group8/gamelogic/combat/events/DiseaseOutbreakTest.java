package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem.CargoType;
import static org.junit.jupiter.api.Assertions.*;

class DiseaseOutbreakTest {

    PlayerShip testTarget;
    DiseaseOutbreak testOutbreak;

    @BeforeEach
    void setUp() {
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
        testTarget = new PlayerShip("HMS Tester", baseUpgrade, 5);

        testOutbreak = new DiseaseOutbreak("COVID-19", 100, 0.5);
    }

    @Test
    void testWithMedicine() throws GameOver {
        CargoItem funny = new CargoItem("Sodium hypochlorite",
                CargoType.MEDICINE,
                0.0,
                0);

        int preCrew = testTarget.getNumCrew();
        testTarget.getCargoManager().addCargo(funny);
        testOutbreak.runEvent(testTarget);
        int postCrew = testTarget.getNumCrew();
        assertEquals(preCrew, postCrew);
    }

    @Test
    void testNoMedicine() throws GameOver {
        int preCrew = testTarget.getNumCrew();
        testOutbreak.runEvent(testTarget);
        int postCrew = testTarget.getNumCrew();
        assertNotEquals(preCrew, postCrew);
    }
}
package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShipUpgradeTest {

    @Test
    void testToString() {
        ShipUpgrade upgrade = new ShipUpgrade.ShipUpgradeBuilder("test a")
                .withMaxHealthModifier(1)
                .build();
        assertEquals(upgrade.toString(), "test a (+1 max health)");
    }

    @Test
    void defaultUpgradeHasNoNullValues() {
        ShipUpgrade upgrade = new ShipUpgrade.ShipUpgradeBuilder("default").build();

        List<Integer> modifiers = new ArrayList<>();
        modifiers.add(upgrade.getMaxHealthModifier());
        modifiers.add(upgrade.getSpeedModifier());
        modifiers.add(upgrade.getDamageModifier());
        modifiers.add(upgrade.getArmourModifier());
        modifiers.add(upgrade.getNumBedsModifier());
        modifiers.add(upgrade.getMaxWeightModifier());
        modifiers.add(upgrade.getUpgradeSlotModifier());
        modifiers.add(upgrade.getCargoSlotModifier());

        modifiers.forEach(i -> assertNotNull(i));
    }

    @Test
    void defaultUpgradeHasOnlyZeroValues() {
        ShipUpgrade upgrade = new ShipUpgrade.ShipUpgradeBuilder("default").build();

        List<Integer> modifiers = new ArrayList<>();
        modifiers.add(upgrade.getMaxHealthModifier());
        modifiers.add(upgrade.getSpeedModifier());
        modifiers.add(upgrade.getDamageModifier());
        modifiers.add(upgrade.getArmourModifier());
        modifiers.add(upgrade.getNumBedsModifier());
        modifiers.add(upgrade.getMaxWeightModifier());
        modifiers.add(upgrade.getUpgradeSlotModifier());
        modifiers.add(upgrade.getCargoSlotModifier());

        modifiers.forEach(i -> assertEquals(0, i));
    }
}

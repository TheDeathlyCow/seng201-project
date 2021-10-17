package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import org.junit.jupiter.api.BeforeEach;

public class CommandAppTest {

    GameManager game;

    @BeforeEach
    void setupGameManager() {
        game = new GameManager(
                new GameWorld(
                        new Player("Test Player"), 50, 0
                )
        );
        game.getWorld().getPlayer().setShip(
                new PlayerShip("Voyaging Canoe",
                        new ShipUpgrade.ShipUpgradeBuilder("Voyager")
                                .withBaseValue(0.0)
                                .withUpgradeSlotModifier(5)
                                .withArmourModifier(10)
                                .withDamageModifier(15)
                                .withCargoSlotModifier(25)
                                .withMaxHealthModifier(100)
                                .withSpeedModifier(15)
                                .withNumBedsModifier(25)
                                .withMaxWeightModifier(150)
                                .build(),
                        10
                )
        );
        Player player = game.getWorld().getPlayer();
        player.addFunds(100);
        player.getShip().addCrew(5);
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {

    GameManager game;
    Vendor vendor;
    Player player;

    @BeforeEach
    void setupGameManager() throws FileNotFoundException {
        FileManager.load();
        game = new GameManager(
                new GameWorld(
                        new Player("Test Player"), 50, 0
                )
        );
        game.getWorld().generateIslands();
        game.getWorld().getPlayer().setShip(
                FileManager.getShips().get(0)
        );
        player = game.getWorld().getPlayer();
        player.addFunds(100000);
        player.getShip().addCrew(5);
        vendor = game.getWorld().getPlayer().getShip().dockedAt().getVendor();
    }

    @Test
    void playerBalanceGoesDownAfterPurchase() {

        Tradeable toBuy = vendor.getCargoStock().get(0);

        double playerPreBalance = player.getBalance();
        double value = toBuy.getValueAtVendor(vendor);
        player.purchase(toBuy, vendor);

        double playerAfterBalance = player.getBalance();

        assertEquals(playerAfterBalance + value, playerPreBalance, 0.001);
    }

    @Test
    void vendorBalanceGoesDownAfterPurchase() {

        Tradeable toBuy = vendor.getCargoStock().get(0);

        double vendorPreBalance = vendor.getBalance();
        double value = toBuy.getValueAtVendor(vendor);
        player.purchase(toBuy, vendor);

        double vendorAfterBalance = vendor.getBalance();

        assertEquals(vendorAfterBalance - value, vendorPreBalance, 0.001);
    }

    @Test
    void playerShipWeightIncreasesByPurchasedWeight() {
        CargoItem toBuy = vendor.getCargoStock().get(0);

        int preWeight = player.getShip().getCurrentWeight();
        int weight = toBuy.getWeight();

        player.purchase(toBuy, vendor);

        int afterWeight = player.getShip().getCurrentWeight();

        assertEquals(afterWeight - weight, preWeight);
    }

    @Test
    void playerGainsItemAfterPurchase() {
        CargoItem toBuy = vendor.getCargoStock().get(0);
        long preQuantity = player.getShip().getCargoManager().getCargo().stream()
                .filter(x -> x.equals(toBuy)).count();
        player.purchase(toBuy, vendor);
        long postQuantity = player.getShip().getCargoManager().getCargo().stream()
                .filter(x -> x.equals(toBuy)).count();
        assertEquals(postQuantity - 1, preQuantity);
    }

    @Test
    void vendorLosesItemAfterPurchase() {
        CargoItem toBuy = vendor.getCargoStock().get(0);
        long preQuantity = vendor.getCargoStock().stream()
                .filter(x -> x.equals(toBuy)).count();
        player.purchase(toBuy, vendor);
        long postQuantity = vendor.getCargoStock().stream()
                .filter(x -> x.equals(toBuy)).count();
        assertEquals(postQuantity + 1, preQuantity);
    }

    @Test
    void brokePlayerCannotBuy() {
        CargoItem toBuy = vendor.getCargoStock().get(0);
        player.removeFunds(player.getBalance());
        assertThrows(IllegalStateException.class, () -> player.purchase(toBuy, vendor));
    }

    @Test
    void overweightPlayerCannotBuy() {
        CargoItem toBuy = vendor.getCargoStock().get(0);
        int leftOverWeight = player.getShip().getAttributes().getMaxWeight() - player.getShip().getCurrentWeight();
        ShipUpgrade leadWalls = new ShipUpgrade.ShipUpgradeBuilder("Lead Walls")
                .withWeight(leftOverWeight)
                .build();
        player.getShip().upgrade(leadWalls);
        assertThrows(IllegalStateException.class, () -> player.purchase(toBuy, vendor));
        player.getShip().removeUpgrade(leadWalls);
    }
}

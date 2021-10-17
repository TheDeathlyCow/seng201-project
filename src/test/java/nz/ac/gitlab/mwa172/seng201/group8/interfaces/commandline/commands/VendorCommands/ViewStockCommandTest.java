package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ViewStockCommandTest {

    GameWorld testWorld;

    /**
     * Create a new world and generate the island graph of that world.
     *
     * @throws IOException Thrown if json files cannot be loaded.
     */
    @BeforeEach
    void setup() throws IOException {
        FileManager.load();
        testWorld = new GameWorld(new Player("Test Player"), 50, 0);
        testWorld.getPlayer().setShip(FileManager.getShips().get(0));
        testWorld.generateIslands();
    }

    @Test
    void pagesAreCorrectSize() {
        List<String> stock = new ArrayList<>();
        for (int i = 0; i < IslandTraderConfig.CONFIG.getMaxItemsPerPage() * 2; i++) {
            stock.add("" + i);
        }
        List<Map<String, Integer>> pages = VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(stock);

        pages.forEach((page) -> {
            assertEquals(IslandTraderConfig.CONFIG.getMaxItemsPerPage(), page.size());
        });
    }

    @Test
    void onlyOnePageOfStock() {
        List<String> stock = new ArrayList<>();
        for (int i = 0; i < IslandTraderConfig.CONFIG.getMaxItemsPerPage(); i++) {
            stock.add("" + i);
        }
        List<Map<String, Integer>> pages = VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(stock);

        assertEquals(1, pages.size());
    }

    @Test
    void trailingPage() {
        List<String> stock = new ArrayList<>();
        for (int i = 0; i <= IslandTraderConfig.CONFIG.getMaxItemsPerPage(); i++) {
            stock.add("" + i);
        }

        List<Map<String, Integer>> pages = VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(stock);

        assertEquals(2, pages.size());
    }

    @Test
    void itemsInSplitPagesAreUnique() {
        Vendor vendor = testWorld.getPlayer().getShip().dockedAt().getVendor();

        Set<Tradeable> uniqueItems = new LinkedHashSet<>();

        List<Map<Tradeable, Integer>> pages = VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(vendor.getCargoStock());
        pages.addAll(VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(vendor.getUpgradeStock()));

        for (Map<Tradeable, Integer> page : pages) {
            for (Tradeable key : page.keySet()) {
                assertFalse(uniqueItems.contains(key));
                uniqueItems.add(key);
            }
        }
    }

    @Test
    void splitPagesCountsCorrectly() {
        Vendor vendor = testWorld.getPlayer().getShip().dockedAt().getVendor();

        Set<Tradeable> uniqueItems = new LinkedHashSet<>();

        List<Map<Tradeable, Integer>> pages = VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(vendor.getCargoStock());
        pages.addAll(VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(vendor.getUpgradeStock()));

        Set<Tradeable> uniqueStock = new LinkedHashSet<>();
        uniqueStock.addAll(vendor.getCargoStock());
        uniqueStock.addAll(vendor.getUpgradeStock());

        for (Map<Tradeable, Integer> page : pages) {
            uniqueItems.addAll(page.keySet());
        }

        List<Tradeable> tradeables = uniqueItems.stream().sorted(
                Comparator.comparing(Tradeable::toString)
        ).collect(Collectors.toList());

        List<Tradeable> stonks = uniqueStock.stream().sorted(
                Comparator.comparing(Tradeable::toString)
        ).collect(Collectors.toList());

        assertArrayEquals(tradeables.toArray(), stonks.toArray());
    }

    @Test
    void splitPageQuantityIsEqualToStockQuantity() {
        Vendor vendor = testWorld.getPlayer().getShip().dockedAt().getVendor();

        Set<Tradeable> uniqueItems = new LinkedHashSet<>();

        List<Map<Tradeable, Integer>> pages = VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(vendor.getCargoStock());
        pages.addAll(VendorCommands.SEE_VENDOR_STOCK.getPagesWithQuantity(vendor.getUpgradeStock()));

        int splitTotal = 0;

        for (Map<Tradeable, Integer> page : pages) {
            for (Tradeable key : page.keySet()) {
                splitTotal += page.get(key);
            }
        }

        assertEquals(splitTotal, vendor.getCargoStock().size() + vendor.getUpgradeStock().size());
    }
}
package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldGeneratorTest {

    GameWorld testWorld;

    @BeforeEach
    void setUp() throws IOException {
        FileManager.load();
        testWorld = new GameWorld(new Player("Test Player"), 50, 0);
        testWorld.generateIslands();
    }

    @Test
    void loadedUpgrades() {
        assertTrue(FileManager.getUpgradePool().size() > 0);
    }

    @Test
    void generatedAnyIslands() {
        assertTrue(testWorld.getIslands().size() > 0);
    }

    @Test
    void islandsHaveNoLoops() {
        for (Island source : testWorld.getIslands()) {
            for (Connection nextEdge : testWorld.getConnectionsOf(source)) {
                assertNotEquals(source, nextEdge.getDestination());
            }
        }
    }

    @Test
    void islandsHaveNoParallelEdges() {
        for (Island source : testWorld.getIslands()) {
            List<Island> islands = new ArrayList<>();
            for (Connection connection : testWorld.getConnectionsOf(source)) {
                assertFalse(islands.contains(connection.getDestination()));
                islands.add(connection.getDestination());
            }
        }
    }

    @Test
    void islandsIsComplete() {
        int numIslands = testWorld.getIslands().size();
        for (Island source : testWorld.getIslands()) {
            assertEquals(testWorld.getConnectionsOf(source).size(), numIslands - 1);
        }
    }

    @Test
    void shipsWereLoaded() {
        assertTrue(FileManager.getShips().size() > 0);
    }

    @Test
    void shipsDockedAtIsland() {
        for (PlayerShip ship : FileManager.getShips()) {
            assertNotNull(ship.dockedAt());
        }
    }
}
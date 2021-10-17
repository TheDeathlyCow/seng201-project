package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.TestLoader;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.BermudaTriangleEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BermudaTests {

    final String BERMUDA = "Bermuda";
    GameWorld testWorld;

    @BeforeEach
    void setup() throws FileNotFoundException {
        FileManager.load();

        FileManager.islandNames = new ArrayList<>();
        FileManager.islandNames.add(BERMUDA);
        for (int i = 0; i < IslandTraderConfig.CONFIG.getNumIslands() - 1; i++)
            FileManager.islandNames.add("Island " + i);

        testWorld = TestLoader.getTestWorld();
    }

    @Test
    void connectionsToBermudaHaveDisappear() {
        for (Island is : testWorld.getIslands()) {
            for (Connection c : testWorld.getConnectionsOf(is)) {
                if (c.getDestination().getName().equalsIgnoreCase(BERMUDA)) {
                    assertTrue(c.getEvents().contains(BermudaTriangleEvent.EVENT));
                }
            }
        }
    }

    @Test
    void onlyConnectionsToBermudaHaveDisappear() {
        for (Island is : testWorld.getIslands()) {
            for (Connection c : testWorld.getConnectionsOf(is)) {
                if (!c.getDestination().getName().equalsIgnoreCase(BERMUDA) && !is.getName().equalsIgnoreCase(BERMUDA)) {
                    assertFalse(c.getEvents().contains(BermudaTriangleEvent.EVENT));
                }
            }
        }
    }

    @Test
    void onlyOneBermudaEvent() {
        for (Island is : testWorld.getIslands()) {
            for (Connection c : testWorld.getConnectionsOf(is)) {
                if (c.getDestination().getName().equalsIgnoreCase(BERMUDA)) {
                    int totalEvents = 0;
                    for (TravellingEvent<?> e : c.getEvents())
                        if (e.equals(BermudaTriangleEvent.EVENT))
                            totalEvents++;
                    assertEquals(1, totalEvents);
                }
            }
        }
    }
}
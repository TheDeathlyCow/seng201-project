package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.TestLoader;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {

    GameWorld testWorld;
    Island a;
    Island b;
    List<Island> islandList;

    Connection forward;
    Connection backward;

    @BeforeEach
    void setup() throws FileNotFoundException {
        FileManager.load();
        testWorld = TestLoader.getTestWorld();
        islandList = testWorld.getIslands();
        a = islandList.get(0);
        b = islandList.get(1);
        forward = testWorld.getConnectionBetween(a, b);
        backward = testWorld.getConnectionBetween(b, a);
    }

    @Test
    void reversedConnectionHasSameEvents() {
        assertArrayEquals(forward.getEvents().toArray(), backward.getEvents().toArray());
    }

    @Test
    void reversedConnectionsAreSameDistance() {
        assertEquals(forward.getDistance(), backward.getDistance());
    }

    @Test
    void reversedConnectionsAreOppositeDestinations() {
        assertEquals(forward.getDestination(), b);
        assertEquals(backward.getDestination(), a);
    }

    @Test
    void reversedConnectionsHaveSameProbability() {
        assertEquals(forward.getEventProbability(), backward.getEventProbability());
    }

}
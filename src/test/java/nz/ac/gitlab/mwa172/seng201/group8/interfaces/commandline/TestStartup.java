package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.TestLoader;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TestStartup {

    GameWorld testWorld;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        testWorld = TestLoader.getTestWorld();
    }

    @Test
    void islandsSetTheirWorld() {
        Player player = testWorld.getPlayer();
        Island docked = player.getShip().dockedAt();
        assertNotNull(docked);
    }

    @Test
    void testInvalidNames() {
        String[] invalidNames = new String[] {
                "",
                " ",
                "   ",
                "a",
                "aa",
                "a  a",
                "aa ",
                " aa",
                "aa1",
                "aaa1",
                "aa aa1",
                "lol aß",
                "a1a",
                "aa1a",
                "aa 1aa",
                "lol ßa",
                "aaa aaa aaa aaa aaa",
                "aaa aaa aaa aaa aaa aaa aaa aaa aaa aaa",
                "A",
                "AA",
                "A  A",
                "AA ",
                " AA",
                "A1A",
                "AA1A",
                "AA A1A",
                "lol ßA",
                "1AA",
                "1AAA",
                "1AA AA",
                "ßlol A",
                "AAA AAA AAA AAA AAA",
                "AAA AAA AAA AAA AAA AAA AAA AAA AAA AAA"
        };

        for (String invalidName : invalidNames) {
            assertFalse(InputValidator.ASCII_NAME_VALIDATOR.isValid(invalidName));
        }
    }

    @Test
    void testValidNames() {

        String[] validNames = new String[] {
                "aaa",
                "abab",
                "AAA",
                "ABAB",
                "aAa",
                "Joao III",
                "ThisStringIsFif",
                "ThisFif"
        };

        for (String validName : validNames) {
            assertTrue(InputValidator.ASCII_NAME_VALIDATOR.isValid(validName));
        }
    }
}
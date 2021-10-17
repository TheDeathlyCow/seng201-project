package nz.ac.gitlab.mwa172.seng201.group8.gamelogic;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;

import java.io.FileNotFoundException;

public class TestLoader {

    public static GameWorld getTestWorld() throws FileNotFoundException {
        FileManager.load();
        GameWorld testWorld = new GameWorld(new Player("Test Player"), 50, 0);
        testWorld.generateIslands();
        testWorld.getPlayer().setShip(FileManager.getShips().get(0));
        return testWorld;
    }
}

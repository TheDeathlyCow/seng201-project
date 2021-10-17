package nz.ac.gitlab.mwa172.seng201.group8;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GuiApp;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Main class, contains the main method.
 *
 * @author Michael Alpenfels
 * @author Jakib Isherwood
 */
public class Main {

    /**
     * Runs the application normally.
     *
     * @param args Runtime arguments. If the first argument is "nogui", runs the command line app.
     * @throws IOException Thrown if the file manager cannot load a file.
     */
    public static void main(String[] args) throws IOException {
        FileManager.load();
        if (args.length > 0 && args[0].equalsIgnoreCase("nogui")) {
            runCommandLineApp();
        } else {
            FlatDarkPurpleIJTheme.install();
            runGuiApp();
        }
    }

    /**
     * Runs the command line application.
     */
    private static void runCommandLineApp() {
        IslandTraderCommandApp commandApp = new IslandTraderCommandApp();
        commandApp.start();
        commandApp.run();
    }

    /**
     * Runs the gui application.
     */
    private static void runGuiApp() {
        GuiApp guiApp = new GuiApp();
        guiApp.start();
    }

    /**
     * Creates a game for debugging.
     *
     * @return Returns the new game.
     * @throws FileNotFoundException Thrown if there is an error loading files.
     */
    public static GameManager debugSetup() throws FileNotFoundException {
        FileManager.load();
        FlatDarkPurpleIJTheme.install();
        Player player = new Player("Test Player");
        player.addFunds(10000);
        player.setShip(FileManager.getShips().get(0));
        for (int i = 0; i < 7; i++)
            player.getShip().upgrade(FileManager.getUpgradePool().get(0));

        GameWorld world = new GameWorld(player, 50, 1);
        world.generateIslands();
        return new GameManager(world);
    }

    /**
     * Outputs a message to the console prefixed with "[IslandTrader]: ".
     *
     * @param message Message to output.
     */
    public static void log(String message) {
        System.out.println("[IslandTrader]: " + message);
    }
}

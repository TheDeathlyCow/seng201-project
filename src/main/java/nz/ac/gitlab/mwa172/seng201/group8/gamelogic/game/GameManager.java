package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.time.Instant;

/**
 * The primary purpose of the game manager is handle requests from other modules
 * (ie the command line and GUI interfaces and the file manager).
 */
public class GameManager {
    /**
     * The saved game world.
     */
    private final GameWorld world;
    /**
     * The game name for a save file.
     */
    private String gameName;

    /**
     * Constructor for GameManager. Requires a game world.
     *
     * @param world The current game world.
     */
    public GameManager(GameWorld world) {
        this.world = world;
        this.gameName = world.getPlayer().getName() + "-" + Instant.now().toString();
    }

    /**
     * @return The game world.
     */
    public GameWorld getWorld() {
        return world;
    }

    /**
     * @return Returns the game player.
     */
    public Player getPlayer() {
        return world.getPlayer();
    }

    /**
     * @return Returns the player's ship.
     */
    public PlayerShip getPlayerShip() {
        return getPlayer().getShip();
    }

    /**
     * @return Returns the island the player is currently docked at.
     */
    public Island currentlyDockedAt() {
        return getPlayerShip().dockedAt();
    }

    /**
     * @return The games name which is also the players.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @param gameName Sets the games name.
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Saves the game.
     */
    public void save() {

    }

    /**
     * Loads the game.
     */
    public void load() {

    }

    /**
     * Closes the application.
     *
     * @param context The context in which the player ends the game.
     */
    public void gameOver(GameOver context) {
        System.exit(1);
    }
}

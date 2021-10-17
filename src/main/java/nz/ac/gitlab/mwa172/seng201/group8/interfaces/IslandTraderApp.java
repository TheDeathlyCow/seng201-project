package nz.ac.gitlab.mwa172.seng201.group8.interfaces;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;

/**
 * Abstract application class.
 */
public abstract class IslandTraderApp {

    /**
     * Title of the application.
     */
    public static final String TITLE = "Island Trader";

    /**
     * The game manager is how the GUI communicates with the game logic.
     */
    private GameManager gameManager;

    /**
     * @return Returns the game manager.
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Sets the game manager only if the current manager is null.
     *
     * @param gameManager Game manager to set.
     */
    public void setGameManager(GameManager gameManager) {
        if (this.gameManager == null)
            this.gameManager = gameManager;
    }

    /**
     * Starts the application.
     */
    public abstract void start();

    /**
     * Called when the game is over.
     *
     * @param over Context for how the game ended.
     */
    public abstract void gameOver(GameOver over);

}

package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

/**
 * A class that ends the game.
 */
public class GameOver extends Throwable {

    /**
     * The reasons that the game could be ending.
     */
    public enum GameOverReason {
        BANKRUPTCY("Bankruptcy"),
        KILLED_BY_PIRATES("Killed by Pirates"),
        TIME_UP("Time up"),
        VANISHED("Vanished into the Bermuda Triangle");

        private final String reason;

        GameOverReason(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return reason;
        }
    }

    /**
     * The reason the game is ending.
     */
    private final GameOverReason gameOverReason;
    /**
     * The player.
     */
    private final Player player;
    /**
     * The message that the player receives.
     */
    private String message;
    /**
     * The players end balance.
     */
    private final double endBalance;
    /**
     * The players end score.
     */
    private final int endScore;
    /**
     * Whether or not this round was a high score.
     */
    private boolean isMaxScore;

    /**
     * Constructs the game over sequence.
     *
     * @param gameOverReason Why the game is over.
     * @param player         The players details which includes their name, balance, and score.
     */
    public GameOver(GameOverReason gameOverReason, Player player) {
        this.gameOverReason = gameOverReason;
        this.player = player;
        this.endBalance = player.getBalance();
        this.endScore = this.setScore();
        this.message = "";
    }

    /**
     * Constructs the game over message.
     *
     * @param gameOverReason Why the game is over.
     * @param player         The players details which includes their name, balance, and score.
     * @param message        The message given to the player.
     */
    public GameOver(GameOverReason gameOverReason, Player player, String message) {
        this(gameOverReason, player);
        this.message = message;
    }

    /**
     * @return A string message describing what is occurring.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The reason the game is over.
     */
    public GameOverReason getGameOverReason() {
        return gameOverReason;
    }

    /**
     * @return The players name.
     */
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Gets the number of days that elapsed during the game.
     *
     * @return Returns the current day of the world.
     */
    public int getDaysElapsed() {
        GameWorld world = player.getShip().dockedAt().getWorld();

        return world.getCurrentDay();
    }

    /**
     * @return The players end balance.
     */
    public double getEndBalance() {
        return endBalance;
    }

    /**
     * @return Returns whether or not this game led to a high score.
     */
    public boolean isMaxScore() {
        return isMaxScore;
    }

    /**
     * Calculates and returns the score of the game. The final score of the game will
     * be the final balance divided by the number of days elapsed plus the number
     * of crew members currently on board the ship.
     *
     * @return Returns the score of the game.
     */
    private int setScore() {
        PlayerShip ship = player.getShip();
        int score = (int) (getEndBalance() / getDaysElapsed() + ship.getNumCrew());
        this.isMaxScore = FileManager.writeScore(player.getName(), score);
        return score;
    }

    /**
     * @return Returns the final score of the game.
     */
    public int getScore() {
        return this.endScore;
    }

    /**
     * @return Returns a string representation of game over objects.
     */
    @Override
    public String toString() {
        return "GameOver{" +
                "gameOverReason=" + gameOverReason +
                ", player=" + player +
                ", message='" + message + '\'' +
                ", endBalance=" + endBalance +
                ", endScore=" + endScore +
                ", isMaxScore=" + isMaxScore +
                '}';
    }
}

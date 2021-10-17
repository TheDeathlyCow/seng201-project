package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;

import java.util.*;

/**
 * Defines the game world. Stores the player and current day,
 * and also a graph representation of the islands and the connections
 * between them using an adjacency list.
 */
public class GameWorld {

    /**
     * The player in this world.
     */
    private final Player player;
    /**
     * An graph of the islands and the connections between them,
     * represented with an adjacency list.
     */
    private Map<Island, ArrayList<Connection>> islands;
    /**
     * The current day of the world, starts at 0.
     */
    private int currentDay = 0;
    /**
     * The maximum number of days that may elapse before the game ends.
     */
    private final int TOTAL_DAYS;
    /**
     * The game world generator, will generate a graph of the islands that
     * is simple, complete, and undirected.
     */
    private GameWorldGenerator gameWorldGenerator;

    /**
     * The absolute minimum number of days the player may
     * select.
     * ? Maybe make this a config thing?
     */
    public static final int MIN_DAYS = 20;
    /**
     * The absolute maximum number of days the player may
     * select.
     * ? Maybe make this a config thing?
     */
    public static final int MAX_DAYS = 50;

    /**
     * Creates a new game world.
     *
     * @param player    The game's player.
     * @param totalDays The maximum number of days that may elapse in this world.
     * @param seed      The world generator seed.
     */
    public GameWorld(Player player, final int totalDays, final long seed) {
        this.player = player;
        this.TOTAL_DAYS = totalDays;
        islands = new HashMap<>();
        gameWorldGenerator = new GameWorldGenerator(seed);
        System.out.println("World seed is: " + seed);
    }

    /**
     * Generates the island graph for this world, and sets
     * the world each island to this, then moves each ship to
     * a random island.
     */
    public void generateIslands() {
        this.islands = gameWorldGenerator.generateIslandGraph();
        for (Island island : islands.keySet()) {
            island.setWorld(this);
        }
        FileManager.getShips().forEach((ship) -> ship.moveTo(this.getRandomIsland()));
    }

    /**
     * Gets a random island from the islands key set.
     *
     * @return Returns a random island.
     */
    public Island getRandomIsland() {
        Random rand = gameWorldGenerator.getRandom();
        List<Island> islandList = new ArrayList<>(this.islands.keySet());
        int randomIsland = rand.nextInt(islandList.size());
        return islandList.get(randomIsland);
    }

    /**
     * Gets a list of the islands.
     *
     * @return Returns an unmodifiable list of the islands.
     */
    public List<Island> getIslands() {
        ArrayList<Island> list = new ArrayList<>(islands.keySet());
        return Collections.unmodifiableList(list);
    }

    /**
     * Finds the connections an island has to the other islands.
     *
     * @param island The source island to find the connections of.
     * @return Returns an array list of the connections of the island.
     */
    public ArrayList<Connection> getConnectionsOf(Island island) {
        return islands.get(island);
    }

    /**
     * Gets the connection between two islands.
     *
     * @param source      The source island of the connection.
     * @param destination The destination island of the connection.
     * @return Returns the connection from source to destination. If no connection
     * can be found, returns null.
     */
    public Connection getConnectionBetween(Island source, Island destination) {
        List<Connection> sourceConnections = islands.get(source);
        for (Connection connection : sourceConnections) {
            if (connection.getDestination().equals(destination))
                return connection;
        }
        return null;
    }

    /**
     * @return Returns the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the number of days remaining. If the current day is somehow after the
     * last day, then returns 0.
     *
     * @return Returns the total number of days remaining.
     */
    public int getDaysRemaining() {
        return Math.max(0, TOTAL_DAYS - currentDay);
    }

    /**
     * @return Returns the current day of the world.
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * @return Gets the maximum number of days that may elapse in this world
     * before the game ends.
     */
    public final int getTotalDays() {
        return TOTAL_DAYS;
    }

    /**
     * Adds a specified number of days to the world.
     *
     * @param moreDays The amount of days to add.
     * @throws GameOver Thrown if the current day goes beyond the last day.
     */
    public void addDays(int moreDays) throws GameOver {
        if (moreDays <= 0)
            throw new IllegalArgumentException("Must add a positive number of days!");
        currentDay += moreDays;
        if (currentDay >= TOTAL_DAYS) {
            throw new GameOver(GameOver.GameOverReason.TIME_UP, getPlayer());
        }
    }
}

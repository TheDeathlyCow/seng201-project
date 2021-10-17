package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.BermudaTriangleEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;

import java.util.*;

/**
 * Class for generating the game world.
 * Contains methods for generating the world seed, islands, cargo items, vendor desire, vendor items,
 * and connections to other islands.
 */
public class GameWorldGenerator {
    /**
     * The generators random number generator.
     */
    private final Random random;
    /**
     * The seed used by the random number generatr.
     */
    private final long SEED;

    /**
     * Constructor for GameWorldGenerator. Requires a seed.
     *
     * @param seed The game seed.
     */
    GameWorldGenerator(long seed) {
        this.random = new Random(seed);
        this.SEED = seed;
        FileManager.shuffleEvents(random);
    }

    /**
     * @return The random number generator.
     */
    Random getRandom() {
        return random;
    }

    /**
     * @return Returns the world seed.
     */
    long getSEED() {
        return SEED;
    }

    /**
     * Generates the islands and connections between them. The
     * returned map will be an adjacency list of a complete,
     * connected, and simple graph.
     *
     * @return A graph implemented using an adjacency list of Islands to
     * Connections.
     */
    Map<Island, ArrayList<Connection>> generateIslandGraph() {
        Map<Island, ArrayList<Connection>> islandGraph = new HashMap<>();
        List<Island> islandList = generateIslands();

        for (Island sourceIsland : islandList) {

            islandGraph.computeIfAbsent(sourceIsland, k -> new ArrayList<>());
            ArrayList<Connection> sourceConnections = islandGraph.get(sourceIsland);

            for (Island destination : islandList) {
                boolean shouldConnect = !sourceIsland.equals(destination)
                        && !inConnections(destination, islandGraph.get(sourceIsland));

                if (shouldConnect) {
                    islandGraph.computeIfAbsent(destination, k -> new ArrayList<>());

                    ArrayList<Connection> destinationConnections = islandGraph.get(destination);
                    Connection connection = generateNewConnection(destination);

                    boolean isConnectingToBermuda = sourceIsland.getName().equalsIgnoreCase("Bermuda")
                            || destination.getName().equalsIgnoreCase("Bermuda");
                    if (isConnectingToBermuda && !connection.getEvents().contains(BermudaTriangleEvent.EVENT))
                        connection.addEvent(BermudaTriangleEvent.EVENT);

                    sourceConnections.add(connection);

                    Connection reverse = connection.reverseConnection(sourceIsland);
                    destinationConnections.add(reverse);
                    islandGraph.put(destination, destinationConnections);
                }
            }

            islandGraph.put(sourceIsland, sourceConnections);
        }
        return islandGraph;
    }

    /**
     * Generates a list of islands. The islands will not have their
     * connections.
     *
     * @return A list of generated islands.
     */
    private List<Island> generateIslands() {
        List<Island> islands = new ArrayList<>();
        List<String> islandNames = FileManager.getIslandNames();
        Collections.shuffle(islandNames, random);
        for (int i = 0; i < IslandTraderConfig.CONFIG.getNumIslands(); i++) {
            String name = islandNames.get(i);
            islands.add(generateIsland(name));
        }
        return islands;
    }

    /**
     * Generates a single island.
     *
     * @param name The name of the generated island.
     * @return The generated island.
     */
    private Island generateIsland(String name) {
        if (name.equalsIgnoreCase("Bermuda"))
            System.out.println("Mysterious things...");
        Vendor islandVendor = generateVendor(name);
        Island newIsland = new Island(name, islandVendor);
        islandVendor.setIsland(newIsland);
        return newIsland;
    }

    /**
     * Generates a vendor and sets their desire table and stock.
     *
     * @param islandName The name of the island.
     * @return The vendor along with its properties.
     */
    private Vendor generateVendor(String islandName) {
        Vendor vendor = new Vendor(islandName + " vendor");

        // set desire table
        for (CargoItem.CargoType type : CargoItem.CargoType.values()) {
            double minDesire = 0.5;
            double maxDesire = 1.9;
            double desire = minDesire + (maxDesire - minDesire) * random.nextDouble();
            vendor.setDesireForType(type, desire);
        }

        // generate stock
        for (CargoItem item : FileManager.getCargoPool()) {
            double shouldInclude = random.nextDouble();
            if (shouldInclude < 0.4) {
                CargoItem toAdd = item.copy();
                double desire = vendor.getDesireFor(toAdd.getType());
                int maxStock = IslandTraderConfig.CONFIG.getAverageVendorStock();
                int amount = (int) (random.nextInt(maxStock) * 1.0 / desire);
                if (amount > 0) {
                    for (int i = 0; i < amount; i++) {
                        vendor.addCargoToStock(toAdd);
                    }
                }
            }
        }
        // generate upgrade stock
        for (ShipUpgrade upgrade : FileManager.getUpgradePool()) {
            double shouldInclude = random.nextDouble();
            if (shouldInclude < 0.5) {
                vendor.addUpgradeToStock(upgrade);
            }
        }

        // set a basic balance
        vendor.setBalance(1000 + random.nextInt(1000));

        return vendor;
    }

    /**
     * Determines if an island is in the connections of another island.
     *
     * @param destination Island to check.
     * @param connections List of connections to look through.
     * @return Returns true if the destination is in the connections list, false otherwise.
     */
    private boolean inConnections(Island destination, List<Connection> connections) {
        for (Connection connection : connections) {
            if (destination.equals(connection.getDestination()))
                return true;
        }
        return false;
    }

    /**
     * Generates a new connection to an island. Will randomly assign distance,
     * event probability, and events, but destination must be specified.
     *
     * @param destination The destination island of the new connection.
     * @return The generated connection.
     */
    private Connection generateNewConnection(Island destination) {
        int distance = random.nextInt(191) + 10;
        double eventProbability = random.nextDouble();
        Connection newConnection = new Connection(distance, destination, eventProbability);
        List<TravellingEvent<?>> events = generateEvents();
        newConnection.addEvents(events);
        if (destination.getName().equalsIgnoreCase("Bermuda")) {
            newConnection.addEvent(BermudaTriangleEvent.EVENT);
        }
        return newConnection;
    }

    /**
     * Generates a list of travelling events for a connection.
     *
     * @return An array list of the generated events. The size of the list
     * will be somewhere between 1 and the max events specified in the config.
     */
    private ArrayList<TravellingEvent<?>> generateEvents() {
        ArrayList<TravellingEvent<?>> events = new ArrayList<>();
        int numEvents = random.nextInt(IslandTraderConfig.CONFIG.getMaxEvents()) + 1;
        int totalEvents = 0;
        int currEventIndex = random.nextInt(FileManager.getEvents().size());
        for (int i = currEventIndex; totalEvents < numEvents; i = (i + 1) % FileManager.getEvents().size()) {
            events.add(FileManager.getEvents().get(i));
            totalEvents++;
        }
        return events;
    }
}

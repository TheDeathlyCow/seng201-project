package nz.ac.gitlab.mwa172.seng201.group8.file_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.*;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Manages JSON files for Island Trader.
 */
public class FileManager {

    /**
     * A generic Gson object.
     */
    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    /**
     * Every possible cargo item that a vendor may have in their stock.
     */
    private static final List<CargoItem> CARGO_POOL = new ArrayList<>();
    /**
     * Every possible upgrade that a vendor may have in their stock.
     */
    private static final List<ShipUpgrade> UPGRADE_POOL = new ArrayList<>();
    /**
     * Every possible travelling event that may occur when sailing.
     */
    private static final List<TravellingEvent<?>> EVENTS = new ArrayList<>();
    /**
     * Every possible ship the player may captain.
     */
    private static final List<PlayerShip> SHIPS = new ArrayList<>();
    /**
     * Every possible name an island might have.
     */
    public static List<String> islandNames = new ArrayList<>();

    /**
     * Loads all of the pools into memory.
     *
     * @throws FileNotFoundException Thrown if any file cannot be found.
     */
    public static void load() throws FileNotFoundException {
        readIslandNames();
        readCargoList();
        readEvents();
        readUpgrades();
        readShips();
    }

    /**
     * Reads all of the cargo items into a variable.
     *
     * @throws FileNotFoundException Thrown if the cargo items file cannot be found.
     */
    private static void readCargoList() throws FileNotFoundException {
        String filename = "data/islands/cargo.json";
        CARGO_POOL.clear();
        Gson cargoGson = new GsonBuilder()
                .registerTypeAdapter(CargoItem.class, new CargoItem())
                .disableHtmlEscaping()
                .create();

        List<JsonElement> jsonElementList = gson.fromJson(getReader(filename), new TypeToken<List<JsonElement>>() {
        }.getType());
        for (JsonElement jsonElement : jsonElementList) {
            CargoItem cargoItem = cargoGson.fromJson(jsonElement, new TypeToken<CargoItem>() {
            }.getType());
            CARGO_POOL.add(cargoItem);
        }
    }

    /**
     * Reads all of the travelling events into a variable.
     *
     * @throws FileNotFoundException Thrown if any of the event files cannot be found.
     */
    private static void readEvents() throws FileNotFoundException {
        String filename = "data/events/";
        EVENTS.clear();
        EVENTS.addAll(readList(filename + "disease_events.json",
                new GsonBuilder()
                        .registerTypeAdapter(DiseaseOutbreak.class, new DiseaseOutbreak("error", 1, 1))
                        .create(),
                DiseaseOutbreak.class
        ));
        EVENTS.addAll(readList(filename + "pirate_events.json",
                new GsonBuilder()
                        .registerTypeAdapter(PirateAttack.class, new PirateAttack("error", null, 0))
                        .create(),
                PirateAttack.class
        ));
        EVENTS.addAll(readList(filename + "rescue_events.json",
                new GsonBuilder()
                        .registerTypeAdapter(RescueSailors.class, new RescueSailors("error", 0, 0))
                        .create(),
                RescueSailors.class
        ));
        EVENTS.addAll(readList(filename + "weather_events.json",
                new GsonBuilder()
                        .registerTypeAdapter(SevereWeather.class, new SevereWeather("error", 0, 0))
                        .create(),
                SevereWeather.class
        ));
    }

    /**
     * Reads all of the ships into a variable.
     *
     * @throws FileNotFoundException Thrown if the ships file cannot be found.
     */
    private static void readShips() throws FileNotFoundException {
        String filename = "data/ships.json";
        SHIPS.clear();
        SHIPS.addAll(readList(filename,
                PlayerShipDeserializer.GSON,
                PlayerShip.class
        ));
    }

    /**
     * Reads all of the ship upgrades into a variable.
     *
     * @throws FileNotFoundException Thrown if the upgrades file cannot be found.
     */
    private static void readUpgrades() throws FileNotFoundException {
        String filename = "data/islands/upgrades.json";
        UPGRADE_POOL.clear();
        UPGRADE_POOL.addAll(readList(filename,
                ShipUpgradeDeserializer.GSON,
                ShipUpgrade.class
        ));
    }

    /**
     * Reads a list of a type from a JSON file.
     *
     * @param filename The file path of the JSON file.
     * @param gson     The gson instance to use in reading.
     * @param type     The class of the object to read into a list, NOT the list type itself.
     * @param <E>      The generic type of the object list to read.
     * @return Returns a list of all of the items read into a variable.
     * @throws FileNotFoundException Thrown if the file cannot be found.
     */
    private static <E> List<E> readList(String filename, Gson gson, Class<E> type) throws FileNotFoundException {
        List<E> events = new ArrayList<>();
        List<JsonElement> elements = new Gson().fromJson(getReader(filename), new TypeToken<List<JsonElement>>() {
        }.getType());
        for (JsonElement element : elements) {
            events.add(gson.fromJson(element, type));
        }
        return events;
    }

    /**
     * Reads the config from a file and returns the config.
     * If the file does not exist or cannot be read, returns a default config.
     *
     * @return Returns a config read from the config file, or a default config
     * if the config file cannot be read for any reason.
     */

    static IslandTraderConfig readConfig() {
        String filename = "config.json";
        IslandTraderConfig config;
        try {
            File configFile = new File(filename);

            if (!configFile.exists()) {
                FileWriter writer = new FileWriter(configFile);
                IslandTraderConfig.GSON.toJson(new IslandTraderConfig(), writer);
                writer.flush();
                writer.close();
            }

            FileReader reader = new FileReader(configFile);
            config = IslandTraderConfig.GSON.fromJson(reader, IslandTraderConfig.class);
            reader.close();
            System.out.println("Loaded config: " + config.toString());
        } catch (Exception e) {
            System.err.println("Unable to load config, using default config instead.");
            System.err.println("Config error: " + e);
            config = new IslandTraderConfig();
        }
        return config;
    }

    /**
     * Reads the list of item names from a file into a variable.
     */
    private static void readIslandNames() {
        String filename = "data/islands/names.json";
        islandNames.clear();
        try {
            islandNames = getStringList(filename);
        } catch (IOException e) {
            System.err.println("Error loading " + filename + ": " + e);
            List<String> names = new ArrayList<>();
            for (int i = 0; i < IslandTraderConfig.CONFIG.getNumIslands(); i++) {
                names.add("Island " + i);
            }
            islandNames = names;
        }
    }

    /**
     * Gets all of the possible names an island might be able to have.
     *
     * @return Returns an modifiable list of the island names pool.
     */
    public static List<String> getIslandNames() {
        return islandNames;
    }

    /**
     * Gets a list of vendor greetings from a JSON file in the resources.
     *
     * @return Returns a list of greetings a vendor may present to the player in various languages.
     */
    public static List<String> getVendorGreetings() {
        String filename = "data/islands/vendor_greetings.json";
        try {
            return getStringList(filename);
        } catch (IOException e) {
            System.err.println("Error loading " + filename + ": " + e);
            return Arrays.asList("Hello", "Welcome");
        }
    }

    /**
     * Parses a list of strings from a JSON file.
     *
     * @param filename The file to parse from.
     * @return Returns the list of strings contained in that file.
     * @throws FileNotFoundException Thrown if the file cannot be found.
     */
    private static List<String> getStringList(String filename) throws FileNotFoundException {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(getReader(filename), listType);
    }

    /**
     * Gets the reader for a particular file from the resources folder.
     *
     * @param filename The file to look for.
     * @return Returns a buffered reader of the file.
     * @throws FileNotFoundException Thrown if the file cannot be found.
     */
    private static BufferedReader getReader(String filename) throws FileNotFoundException {
        ClassLoader classLoader = FileManager.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(filename);
        if (stream == null)
            throw new FileNotFoundException("Count not find file: " + filename);
        return new BufferedReader(new InputStreamReader(stream));
    }

    /**
     * Writes the score for a given player name to file, and determines
     * if that score was a high score.
     * The player's score is only written to the scores file if it is a
     * high score for that player, and the scores file is kept in the same
     * directory as the jar file.
     *
     * @param playerName The name of the player.
     * @param score      The player's score.
     * @return Returns true if the score was a max score, false otherwise.
     */
    public static boolean writeScore(String playerName, int score) {
        String filename = "scores.json";

        File scoreFile = new File(filename);
        Map<String, Integer> scores;
        try {
            FileReader reader = new FileReader(scoreFile);
            Type mapType = new TypeToken<Map<String, Integer>>() {
            }.getType();
            scores = gson.fromJson(reader, mapType);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            scores = new LinkedHashMap<>();
        }
        Map<String, Integer> finalScores = scores;
        Optional<Map.Entry<String, Integer>> maxEntry = scores.entrySet().stream()
                .max(Comparator.comparingInt(score2 -> finalScores.get(score2.getKey())));

        int maxScore = maxEntry.isPresent() ? maxEntry.get().getValue() : -1;

        if (scores.get(playerName) == null || scores.get(playerName) < score) {
            scores.put(playerName, score);
        }

        String toWrite = gson.toJson(scores);
        try {
            FileWriter writer = new FileWriter(scoreFile);
            writer.write(toWrite);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxScore < score;
    }

    /**
     * Gets all of the possible cargo items a vendor might have in their stock.
     *
     * @return Returns an unmodifiable list of the cargo items pool.
     */
    public static List<CargoItem> getCargoPool() {
        return Collections.unmodifiableList(CARGO_POOL);
    }

    /**
     * Gets all of the possible upgrades a vendor might have in their stock.
     *
     * @return Returns an unmodifiable list of the upgrades pool.
     */
    public static List<ShipUpgrade> getUpgradePool() {
        return Collections.unmodifiableList(UPGRADE_POOL);
    }

    /**
     * Gets all of the possible events that may be encountered
     * when sailing any route.
     *
     * @return Returns an unmodifiable list of the events pool.
     */
    public static List<TravellingEvent<?>> getEvents() {
        return Collections.unmodifiableList(EVENTS);
    }

    /**
     * Shuffles the events list with a particular random object.
     *
     * @param random The random generator to shuffle with.
     */
    public static void shuffleEvents(Random random) {
        Collections.shuffle(EVENTS, random);
    }

    /**
     * Gets the possible ships the player can captain.
     *
     * @return Returns an unmodifiable list of ships.
     */
    public static List<PlayerShip> getShips() {
        return Collections.unmodifiableList(SHIPS);
    }

}

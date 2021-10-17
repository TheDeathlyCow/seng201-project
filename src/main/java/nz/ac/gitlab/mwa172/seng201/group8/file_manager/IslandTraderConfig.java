package nz.ac.gitlab.mwa172.seng201.group8.file_manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles the config for this application.
 * The config file is located in the directory this application is run in.
 * <p>
 * Implements jsondeserializer and jsonserializer for island trader config.
 */
public class IslandTraderConfig implements JsonDeserializer<IslandTraderConfig>, JsonSerializer<IslandTraderConfig> {

    /**
     * The GSON encoder and decoder with island trader config registered as its
     * type adapter.
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(IslandTraderConfig.class, new IslandTraderConfig())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    /**
     * The current config of this application.
     */
    public static final IslandTraderConfig CONFIG;

    // reads the config when this class is loaded into memory
    static {
        CONFIG = FileManager.readConfig();
    }

    /**
     * The number of islands to generate/
     */
    private int numIslands;
    /**
     * Whether or not unicode names are allowed for the player.
     */
    private boolean useUnicode;
    /**
     * The average number of a single type of item the vendor should have.
     */
    private int averageVendorStock;
    /**
     * The maximum number of items to be displayed in a stock page in the command
     * line application.
     */
    private int maxItemsPerPage;
    /**
     * The wage of a single crew member per day of sailing.
     */
    private double wageCostPerDay;
    /**
     * The amount it costs to hire a single crew member.
     */
    private double hireCost;
    /**
     * The maximum number of events a route can have in its pool.
     */
    private int maxEvents;
    /**
     * The weight of a single crew member.
     */
    private int personWeight;

    /**
     * Creates a new default config.
     */
    public IslandTraderConfig() {
        numIslands = 5;
        useUnicode = false;
        averageVendorStock = 5;
        maxItemsPerPage = 10;
        wageCostPerDay = 0.13;
        hireCost = 0.5;
        maxEvents = 6;
        personWeight = 1;
    }

    /**
     * @return Returns the maximum number of events a route can have in its pool.
     */
    public int getMaxEvents() {
        return maxEvents;
    }

    /**
     * @return Returns the number of islands to generate.
     */
    public int getNumIslands() {
        return numIslands;
    }

    /**
     * @return Returns whether or not the players name can use any unicode character.
     */
    public boolean isUsingUnicode() {
        return useUnicode;
    }

    /**
     * @return Returns the average number of a single type of item a vendor can
     * have in their stock.
     */
    public int getAverageVendorStock() {
        return averageVendorStock;
    }

    /**
     * @return Returns the maximum number of items to display on a page in the command
     * line app.
     */
    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    /**
     * @return Returns the wage cost of a single crew member per day of sailing.
     */
    public double getWageCostPerDay() {
        return wageCostPerDay;
    }

    /**
     * @return Returns the cost to hire a single crew member from an island.
     */
    public double getHireCost() {
        return hireCost;
    }

    /**
     * @return Returns the weight of a crew member on board a ship.
     */
    public int getPersonWeight() {
        return personWeight;
    }

    /**
     * Creates a string representation of the config.
     *
     * @return Returns a string representation of the config.
     */
    @Override
    public String toString() {
        return "IslandTraderConfig{" +
                "numIslands=" + numIslands +
                ", useUnicode=" + useUnicode +
                ", averageVendorStock=" + averageVendorStock +
                ", maxItemsPerPage=" + maxItemsPerPage +
                ", wageCostPerDay=" + wageCostPerDay +
                ", hireCost=" + hireCost +
                ", maxEvents=" + maxEvents +
                ", personWeight=" + personWeight +
                '}';
    }

    /**
     * Deserializes a json element into this config.
     * The default config looks like this:
     * <pre>
     *     {
     *          "num_islands": 5,
     *          "use_unicode": false,
     *          "avg_vendor_stock": 5,
     *          "wage_cost_per_day": 0.13,
     *          "hire_cost": 0.5,
     *          "max_events": 6,
     *          "max_items_per_page": 10,
     *          "person_weight": 1
     *     }
     * </pre>
     *
     * @param jsonElement                Element to deserialize.
     * @param type                       The type to deserialize to.
     * @param jsonDeserializationContext The context of deserialization,
     * @return Returns this config, updated with the values in jsonElement.
     * @throws JsonParseException Thrown if the jsonElement is invalid.
     */
    @Override
    public IslandTraderConfig deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        numIslands = Math.max(1, jsonObject.get("num_islands").getAsInt());
        useUnicode = jsonObject.get("use_unicode").getAsBoolean();
        averageVendorStock = Math.max(1, jsonObject.get("avg_vendor_stock").getAsInt());
        wageCostPerDay = jsonObject.get("wage_cost_per_day").getAsDouble();
        hireCost = jsonObject.get("hire_cost").getAsDouble();
        maxEvents = jsonObject.get("max_events").getAsInt();
        maxItemsPerPage = jsonObject.get("max_items_per_page").getAsInt();
        personWeight = jsonObject.get("person_weight").getAsInt();

        return this;
    }

    /**
     * Serializes a config into a json element.
     * The default output element looks like this:
     * <pre>
     *     {
     *          "num_islands": 5,
     *          "use_unicode": false,
     *          "avg_vendor_stock": 5,
     *          "wage_cost_per_day": 0.13,
     *          "hire_cost": 0.5,
     *          "max_events": 6,
     *          "max_items_per_page": 10,
     *          "person_weight": 1
     *     }
     * </pre>
     *
     * @param islandTraderConfig                Config to serialize.
     * @param type                       The type to serialize to.
     * @param context The context of serialization,
     * @return The serialized version of the given config.
     */
    @Override
    public JsonElement serialize(IslandTraderConfig islandTraderConfig, Type type, JsonSerializationContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("num_islands", numIslands);
        map.put("use_unicode", useUnicode);
        map.put("avg_vendor_stock", averageVendorStock);
        map.put("wage_cost_per_day", wageCostPerDay);
        map.put("hire_cost", hireCost);
        map.put("max_events", maxEvents);
        map.put("max_items_per_page", maxItemsPerPage);
        map.put("person_weight", personWeight);

        return context.serialize(map, new TypeToken<Map<String, Object>>() {
        }.getType());
    }
}

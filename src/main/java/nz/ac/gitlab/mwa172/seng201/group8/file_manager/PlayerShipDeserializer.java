package nz.ac.gitlab.mwa172.seng201.group8.file_manager;

import com.google.gson.*;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;

import java.lang.reflect.Type;

/**
 * Handles deserialization for player ships.
 */
public class PlayerShipDeserializer implements JsonDeserializer<PlayerShip> {

    /**
     * Public Gson with registered type adapter for
     * player ships.
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(PlayerShip.class, new PlayerShipDeserializer())
            .create();

    /**
     * Deserializes a json element into a player ship.
     * <p>
     * The json element should look like this:
     * <pre>
     * {
     *      "name": "Treasure Ship",
     *      "base_weight": 75,
     *      "attributes": {
     *          "name": "Treasure Ship",
     *          "max_health": 250,
     *          "speed": 45,
     *          "damage": 15,
     *          "armour": 40,
     *          "num_beds": 100,
     *          "max_weight": 2000,
     *          "upgrade_slots": 7,
     *          "cargo_slots": 200
     *          }
     *  }
     *  </pre>
     *
     * @param jsonElement                Element to deserialize.
     * @param type                       The type to deserialize to.
     * @param jsonDeserializationContext The context of deserialization,
     * @return Returns the player ship deserialized from jsonElement.
     * @throws JsonParseException Thrown if the jsonElement is invalid.
     */
    @Override
    public PlayerShip deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int baseWeight = jsonObject.get("base_weight").getAsInt();
        JsonElement attributesElement = jsonObject.get("attributes");
        ShipUpgrade baseUpgrade = ShipUpgradeDeserializer.GSON.fromJson(attributesElement, ShipUpgrade.class);

        return new PlayerShip(name, baseUpgrade, baseWeight);
    }
}

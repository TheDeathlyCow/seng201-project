package nz.ac.gitlab.mwa172.seng201.group8.file_manager;

import com.google.gson.*;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.PirateShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;

import java.lang.reflect.Type;

/**
 * Handles deserialization for pirate ships.
 */
public class PirateShipDeserializer implements JsonDeserializer<PirateShip> {

    /**
     * Public Gson with registered type adapter for
     * pirate ships.
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(PirateShip.class, new PirateShipDeserializer())
            .create();

    /**
     * Deserializes a json element into a pirate ship.
     * The json element should look like this:
     * <pre>
     *  {
     *      "name": "Black Beard Pirates",
     *      "weight": 4,
     *      "ship": {
     *          "name": "Queen Anne's Revenge",
     *          "base_weight": 1,
     *          "attributes": {
     *              "name": "Pirate Ship",
     *              "max_health": 350,
     *              "speed": 1,
     *              "damage": 25,
     *              "armour": 40,
     *              "num_beds": 100,
     *              "max_weight": 500,
     *              "upgrade_slots": 1,
     *              "cargo_slots": 100
     *          }
     *      }
     *  }
     * </pre>
     *
     * @param jsonElement                Element to deserialize.
     * @param type                       The type to deserialize to.
     * @param jsonDeserializationContext The context of deserialization,
     * @return Returns the pirate ship deserialized from jsonElement.
     * @throws JsonParseException Thrown if the jsonElement is invalid.
     */
    @Override
    public PirateShip deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int baseWeight = jsonObject.get("base_weight").getAsInt();
        JsonElement attributesElement = jsonObject.get("attributes");
        ShipUpgrade baseUpgrade = ShipUpgradeDeserializer.GSON.fromJson(attributesElement, ShipUpgrade.class);
        return new PirateShip(name, baseUpgrade, baseWeight);
    }
}

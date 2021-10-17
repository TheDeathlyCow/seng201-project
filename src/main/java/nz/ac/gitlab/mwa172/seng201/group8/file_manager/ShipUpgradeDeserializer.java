package nz.ac.gitlab.mwa172.seng201.group8.file_manager;

import com.google.gson.*;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;

import java.lang.reflect.Type;

import static nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade.ShipUpgradeBuilder;


/**
 * Handles deserialization for ship upgrades.
 */
public class ShipUpgradeDeserializer implements JsonDeserializer<ShipUpgrade> {

    /**
     * Public Gson with registered type adapter for
     * ship upgrades.
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ShipUpgrade.class, new ShipUpgradeDeserializer())
            .create();

    /**
     * Deserializes a json element into a ship upgrade.
     * Ship upgrades should have the following json format:
     * <pre>
     *     {
     *         "name": "Cannons",
     *         "max_heath": 10,
     *         "speed": 1,
     *         "damage": 20,
     *         "armour": 15,
     *         "upgrade_slots": 0,
     *         "cargo_slots": 0,
     *         "base_value": 12.5,
     *         "weight": 15
     *     }
     * </pre>
     * However, all values are optional EXCEPT for the name.
     *
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     * @throws JsonParseException
     */
    @Override
    public ShipUpgrade deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        ShipUpgradeBuilder builder = new ShipUpgradeBuilder(jsonObject.get("name").getAsString());
        builder.withMaxHealthModifier(addOptionalModifier(jsonObject.get("max_health")));
        builder.withSpeedModifier(addOptionalModifier(jsonObject.get("speed")));
        builder.withDamageModifier(addOptionalModifier(jsonObject.get("damage")));
        builder.withArmourModifier(addOptionalModifier(jsonObject.get("armour")));
        builder.withNumBedsModifier(addOptionalModifier(jsonObject.get("num_beds")));
        builder.withMaxWeightModifier(addOptionalModifier(jsonObject.get("max_weight")));
        builder.withUpgradeSlotModifier(addOptionalModifier(jsonObject.get("upgrade_slots")));
        builder.withCargoSlotModifier(addOptionalModifier(jsonObject.get("cargo_slots")));
        builder.withWeight(addOptionalModifier(jsonObject.get("weight")));
        JsonElement valueElement = jsonObject.get("base_value");
        if (valueElement != null)
            builder.withBaseValue(valueElement.getAsDouble());

        return builder.build();
    }

    private int addOptionalModifier(JsonElement modifierElement) {
        return modifierElement != null ? modifierElement.getAsInt() : 0;
    }
}

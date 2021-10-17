package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.lang.reflect.Type;

/**
 * Event that sometimes occurs when a player sails a connection.
 */
public class RescueSailors extends TravellingEvent<RescueSailors> {

    private final int NUM_SAILORS;

    /**
     * Constructs a TravellingEvent with a name, a target, and a chance weight.
     *
     * @param shipName     The name of the ship where the sailors come from.
     * @param chanceWeight The chance weight of any of the particular events occurring.
     * @param numSailors   The number of sailors to be rescued by this event.
     */
    public RescueSailors(String shipName, int chanceWeight, int numSailors) {
        super(shipName, chanceWeight);
        this.NUM_SAILORS = numSailors;
    }

    /**
     * Adds a random number of sailors to the player's crew, if they have enough
     * space in their ship. If they do not, the sailors are left to drown at sea.
     *
     * @param target     The target of this event.
     * @param diceRoller The dice roller for this event. Unused for Rescue events.
     * @param feedback   How the event should send feedback to the player mid
     *                   way through execution.
     * @return Returns a status string, detailing the outcome of the event.
     */
    @Override
    public String runEvent(PlayerShip target, DiceRoller diceRoller, EventFeedback feedback) {
        if (target.addCrew(NUM_SAILORS)) {
            double reward = NUM_SAILORS * 1.2;
            target.getOwner().addFunds(reward);
            return String.format("You rescued %d of the %s sailors and they joined your crew! " +
                            "They also gave you a gift of %.2f Doubloons!",
                    NUM_SAILORS,
                    getName(),
                    reward
            );
        }
        return String.format("You found %d sailors from the %s adrift in the sea, but unfortunately you don't have enough room for them on board and so they were left to drown.",
                NUM_SAILORS,
                getName());
    }

    /**
     * Provides a more accurate name for this event.
     *
     * @return Returns the name of this event, in the format:
     * "Rescue Sailors of the [shipName]"
     */
    @Override
    public String getName() {
        return "Rescue Sailors of the " + super.getName();
    }

    /**
     * Gets the proper name of this event.
     *
     * @return Returns the raw name of this event. For example, "Scurvy".
     */
    @Override
    public String getProperName() {
        return super.getName();
    }

    /**
     * Deserializes a JSON object into a Rescue Sailors event.
     * A JSON object for a Weather event must look something like this:
     * <pre>
     *     {
     *         "name": "RMS Titanic",
     *         "weight": 2,
     *         "severity": 5
     *     }
     * </pre>
     *
     * @param jsonElement                element to deserialize
     * @param type                       The type of the object to deserialize
     * @param jsonDeserializationContext context of deserialization
     * @return A new travelling event deserialized from the json object.
     * @throws JsonParseException Thrown if there is an error in the deserialization.
     */
    @Override
    public RescueSailors deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String shipName = jsonObject.get("ship_name").getAsString();
        int weight = jsonObject.get("weight").getAsInt();
        int numSailors = jsonObject.get("num_sailors").getAsInt();
        return new RescueSailors(shipName, weight, numSailors);
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.lang.reflect.Type;
import java.util.Random;

/**
 * A severe weather event that damage the players ship.
 */
public class SevereWeather extends TravellingEvent<SevereWeather> {

    /**
     * How severe this event is. Must be a double between 0 and 1,
     * reduces the amount of damage the event can do as a percentage.
     */
    private final double SEVERITY;

    /**
     * Constructs a TravellingEvent with a name, a target, and a chance weight.
     *
     * @param name         The name of the event.
     * @param chanceWeight The chance weight of any of the particular events occurring.
     * @param severity The severity of the event.
     */
    public SevereWeather(String name, int chanceWeight, double severity) {
        super(name, chanceWeight);
        if (severity > 1.0)
            severity = 1.0;
        if (severity < 0.0)
            severity = 0.0;
        this.SEVERITY = severity;
    }

    /**
     * Randomly damages the players ship.
     *
     * @param target     The target of this event.
     * @param diceRoller The dice roller for this event. Unused for Disease events.
     * @param feedback   How the event should send feedback to the player mid
     *                   way through execution.
     * @return Returns a status string, detailing the outcome of the event.
     */
    @Override
    public String runEvent(PlayerShip target, DiceRoller diceRoller, EventFeedback feedback) {
        int bound = (int) (target.getHealth() * SEVERITY);

        int damage = new Random().nextInt(bound);
        String message = String.format("Your ship sailed into a %s and took %d damage!",
                this.getName(),
                damage);
        ShipAttackContext context = new ShipAttackContext.Builder(target)
                .withDamageDealt(damage)
                .withAttackMessage(message)
                .build();
        return target.onDamaged(context);
    }

    /**
     * Deserializes a JSON object into a Severe Weather event.
     * A JSON object for a Weather event must look something like this:
     * <pre>
     *     {
     *         "name": "Hurricane",
     *         "weight": 1,
     *         "severity": 0.9
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
    public SevereWeather deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int weight = jsonObject.get("weight").getAsInt();
        double severity = jsonObject.get("severity").getAsDouble();
        return new SevereWeather(name, weight, severity);
    }
}

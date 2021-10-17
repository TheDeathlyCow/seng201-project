package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Disease outbreak event that can occur when the player travels.
 * Kills a random amount of crew if they do not have any medicine.
 */
public class DiseaseOutbreak extends TravellingEvent<DiseaseOutbreak> {

    /**
     * How severe this event is. Must be a double between 0 and 1.
     * Represents the percentage of the crew to kill if this event is not
     * prevented with medicine.
     */
    private final double severity;

    /**
     * Constructs a TravellingEvent with a name, a target, and a chance weight.
     *
     * @param name         The name of the event.
     * @param chanceWeight The chance weight of any of the particular events occurring.
     * @param severity     What percentage of the crew this disease will kill.
     */
    public DiseaseOutbreak(String name, int chanceWeight, double severity) {
        super(name, chanceWeight);
        this.severity = severity;
    }

    /**
     * Creates a disease outbreak on a ship. If the target has medicine on
     * their ship, the medicine is consumed and the event skips. Otherwise,
     * it will kill a random number of crew.
     *
     * @param target     The target of this event.
     * @param diceRoller The dice roller for this event. Unused for Disease events.
     * @param feedback   How the event should send feedback to the player mid
     *                   way through execution.
     * @return Returns a status string, detailing the outcome of the event.
     * @throws GameOver Thrown if this event causes the game to end
     */
    @Override
    public String runEvent(PlayerShip target, DiceRoller diceRoller, EventFeedback feedback) throws GameOver {

        List<CargoItem> cargoItems = target.getCargoManager().getCargo();
        for (CargoItem item : cargoItems) {
            if (item.getType().equals(CargoItem.CargoType.MEDICINE)) {
                target.getCargoManager().removeCargo(item);
                return String.format("There was an outbreak of %s on your ship, but you were able to stop it with %s!",
                        this.getName(),
                        item.getName());
            }
        }

        int crewKilled = (int) (target.getNumCrew() * severity);
        target.removeCrew(crewKilled);
        return String.format("There was an outbreak of %s on your ship, and it killed %d sailors!",
                this.getName(),
                crewKilled);
    }

    /**
     * Deserializes a JSON object into a Severe Weather event.
     * A JSON object for a Weather event must look something like this:
     * <pre>
     *     {
     *         "name": "Scurvy",
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
    public DiseaseOutbreak deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int weight = jsonObject.get("weight").getAsInt();
        double severity = jsonObject.get("severity").getAsDouble();
        return new DiseaseOutbreak(name, weight, severity);
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.lang.reflect.Type;

/**
 * A special event that is not kept in any files. Instantly causes the game
 * to end if run, but will only occur when travelling to an island named
 * "Bermuda".
 */
public class BermudaTriangleEvent extends TravellingEvent<BermudaTriangleEvent> {

    /**
     * The only instance of the Bermuda Triangle event, can only be applied
     * to connections that sail to an island named "Bermuda".
     */
    public static final BermudaTriangleEvent EVENT;

    static {
        EVENT = new BermudaTriangleEvent();
    }

    /**
     * Constructs a BermudaTriangleEvent. The name and chance weight
     * of a bermuda triangle event are always the same.
     */
    private BermudaTriangleEvent() {
        super("Mysterious Disappearance", 1);
    }

    /**
     * The player vanishes into the Bermuda Triangle and the game ends.
     *
     * @param target   The target of this event.
     * @param dice     The dice roller for this event.
     * @param feedback How the event should send feedback to the player mid
     *                 way through execution.
     * @return Returns a string outcome of this event.
     * @throws GameOver Thrown with a special condition for this event and this
     *                  event only.
     */
    @Override
    public String runEvent(PlayerShip target, DiceRoller dice, EventFeedback feedback) throws GameOver {
        throw new GameOver(
                GameOver.GameOverReason.VANISHED,
                target.getOwner(),
                "You vanished into the Bermuda Triangle, never to be seen again..."
        );
    }

    /**
     * Bermuda Triangle events cannot be read from a file.
     *
     * @param jsonElement                Element to deserialize.
     * @param type                       The type of the object to deserialize.
     * @param jsonDeserializationContext The context of deserialization.
     * @return Returns null, Bermuda Triangle events cannot be read from files.
     * @throws JsonParseException Not thrown by this method.
     */
    @Override
    public BermudaTriangleEvent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}

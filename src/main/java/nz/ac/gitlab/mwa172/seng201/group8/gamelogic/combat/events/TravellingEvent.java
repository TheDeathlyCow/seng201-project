package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import com.google.gson.JsonDeserializer;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;

import java.util.Random;

/**
 * Abstract TravellingEvent class. Defines basic properties and methods used by PirateAttack,
 * SevereWeather, DiseaseOutbreak, and RescueCrew.
 */
public abstract class TravellingEvent<E> implements JsonDeserializer<E> {

    /**
     * The name of the event.
     */
    private final String NAME;

    /**
     * The chance weight of any of the particular events occurring.
     */
    private final int chanceWeight;

    /**
     * Dice that returns a random integer
     * between 1 and 6 (inclusive) when rolled.
     */
    public static final DiceRoller DICE_1_6 = (isAttacking) -> new Random().nextInt(6) + 1;

    /**
     * DnD-like dice that returns a random integer
     * between 1 and 20 (inclusive) when rolled.
     */
    public static final DiceRoller DICE_1_20 = (isAttacking) -> new Random().nextInt(20) + 1;

    /**
     * Constructs a TravellingEvent with a name and a chance weight.
     *
     * @param name         The name of the event.
     * @param chanceWeight The chance weight of any of the particular events occurring.
     */
    public TravellingEvent(String name, int chanceWeight) {
        this.NAME = name;
        this.chanceWeight = chanceWeight;
    }

    /**
     * @return Gets the name of the event.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Gets the proper name of this event. If you override
     * this.getName(), then this should be overridden to.
     *
     * @return Returns the name of this event.
     */
    public String getProperName() {
        return this.getName();
    }

    /**
     * @return Gets the chance weight of the possible events that could occur.
     */
    public int getChanceWeight() {
        return chanceWeight;
    }

    /**
     * Runs the event with a default dice.
     *
     * @param target The target of this event.
     * @return Returns a status string describing the outcome of the event.
     * @throws GameOver Thrown if this event causes the game to end.
     */
    public String runEvent(PlayerShip target) throws GameOver {
        return runEvent(target, DICE_1_6, c -> {
        });
    }

    /**
     * Runs the even with a specific dice roller and custom feedback.
     *
     * @param target   The target of this event.
     * @param dice     The dice roller for this event.
     * @param feedback How the event should send feedback to the player mid
     *                 way through execution.
     * @return Returns a status string describing the outcome of the event.
     * @throws GameOver Thrown if this event causes the game to end.
     */
    public abstract String runEvent(PlayerShip target, DiceRoller dice, EventFeedback feedback) throws GameOver;

    /**
     * Gets a string representation of this event.
     *
     * @return Returns the name of the event.
     */
    @Override
    public String toString() {
        return NAME;
    }
}

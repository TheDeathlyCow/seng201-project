package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.PirateShipDeserializer;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.PirateShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Ship;

import java.lang.reflect.Type;

/**
 * Handles the pirate attacks. When the event is run, prompts player to
 * roll a dice in a DND-like minigame.
 */
public class PirateAttack extends TravellingEvent<PirateAttack> {
    private PirateShip pirates;

    /**
     * Constructs a pirate attack.
     *
     * @param name         Name of the pirates
     * @param pirates      The Pirate's Ship
     * @param chanceWeight The probability weight of this event.
     */
    public PirateAttack(String name, PirateShip pirates, int chanceWeight) {
        super(name, chanceWeight);
        this.pirates = pirates;
    }

    /**
     * Runs the event. When a ship reaches 0 health, they are boarded by the other ship.
     * This may cause the game to end.
     *
     * @param player     The target of this attack.
     * @param diceRoller The dice roller for this event. Should get an input dice roll from the user.
     * @param feedback   How the event should send feedback to the player mid
     *                   way through execution.
     * @return Returns a status string, detailing the outcome of the event.
     * @throws GameOver Thrown if the player loses the battle.
     */
    @Override
    public String runEvent(PlayerShip player, DiceRoller diceRoller, EventFeedback feedback) throws GameOver {
        while (pirates.getHealth() > 0 && player.getHealth() > 0) {
            battle(pirates, player, diceRoller, feedback);
            battle(player, pirates, diceRoller, feedback);
        }

        Ship boarding;
        Ship boarded;
        if (pirates.getHealth() <= 0) {
            boarding = player;
            boarded = pirates;
        } else {
            boarding = pirates;
            boarded = player;
        }

        String boardedName = boarded instanceof PlayerShip ? "you" : boarded.getName();
        String boardingName = boarding instanceof PlayerShip ? "You" : boarding.getName();

        ShipAttackContext context = new ShipAttackContext.Builder(boarded)
                .withAttackingShip(boarding)
                .withAttackMessage(boardingName + " boarded " + boardedName + "!")
                .build();

        return boarded.onBoarded(context);
    }

    /**
     * Commences a battle between two ships.
     *
     * @param attacker   Ship on the offencive.
     * @param target     Ship on the defence.
     * @param diceRoller Dice roller for this event.
     * @param feedback Provides feedback to the player during the battle.
     */
    private void battle(Ship attacker, Ship target, DiceRoller diceRoller, EventFeedback feedback) {

        int defenceRoll = target.rollDice(diceRoller, false);
        int attackRoll = attacker.rollDice(diceRoller, true);

        int targetDefence = (int) (getMultiplier(defenceRoll) * target.getAttributes().getArmour());
        int attackerOffence = (int) (getMultiplier(attackRoll) * target.getAttributes().getDamage());

        int damageDealt = (int) ((100.0 / (100.0 + targetDefence)) * attackerOffence);

        ShipAttackContext pirateAttackContext = new ShipAttackContext.Builder(target)
                .withRolls(attackerOffence, targetDefence)
                .withAttackingShip(attacker)
                .withDamageDealt(damageDealt)
                .build();
        attacker.attackShip(pirateAttackContext);
        feedback.sendFeedback(pirateAttackContext);
    }

    public double getMultiplier(int roll) {
        return (double) ((roll - 1) / 2) / 2 + 1;
    }


    /**
     * Provides a more accurate name for this event.
     *
     * @return Returns the name of this event, in the format:
     * "Attack from the [name]"
     */
    @Override
    public String getName() {
        return "Attack from the " + super.getName();
    }

    /**
     * Gets the proper name of this event.
     *
     * @return Returns the raw name of this event. For example, "Black Beard Pirates".
     */
    @Override
    public String getProperName() {
        return super.getName();
    }

    @Override
    public PirateAttack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String name = object.get("name").getAsString();
        int weight = object.get("weight").getAsInt();
        JsonElement shipElement = object.get("ship");
        PirateShip ship = PirateShipDeserializer.GSON.fromJson(shipElement, PirateShip.class);

        return new PirateAttack(name, ship, weight);
    }
}

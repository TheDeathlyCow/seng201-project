package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.DiceRoller;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;

/**
 * Provides methods for combat between the player and the pirates.
 */
public interface Fighter {

    /**
     * Rolls a dice with a specific dice roller.
     *
     * @param roller      The dice roller.
     * @param isAttacking Whether or not we are rolling to attack.
     * @return Returns a dice roll.
     */
    int rollDice(DiceRoller roller, boolean isAttacking);

    /**
     * On damage sends message detailing damage taken and health remaining.
     *
     * @param context The context of this attack.
     * @return Returns a string representation of the outcome of this method.
     */
    String onDamaged(ShipAttackContext context);

    /**
     * Sends a message that the player is being attacked or attacking.
     *
     * @param context The context of this attack.
     */
    void attackShip(ShipAttackContext context);

    /**
     * Sends a message that a ship is being boarded.
     *
     * @param context The context of the attack that caused the boarding.
     * @return Returns a string representation of the outcome of this boarding.
     * @throws GameOver Thrown if running this method causes the game to end.
     */
    String onBoarded(ShipAttackContext context) throws GameOver;

    /**
     * The proper name of a fighter is either the name of the pirates themselves,
     * or in the case of the player ship, the owner's name.
     *
     * @return Returns the proper name of the fighter.
     */
    String getProperName();
}

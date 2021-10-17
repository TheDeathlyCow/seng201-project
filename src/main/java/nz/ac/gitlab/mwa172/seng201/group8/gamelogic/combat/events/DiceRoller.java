package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

/**
 * Interface that defines behaviour for dice rolls.
 */
public interface DiceRoller {

    /**
     * Rolls a dice.
     *
     * @param isAttacking Determines whether or not this dice roll is for attacking or defending.
     * @return Return a random integer in the range 1-N (inclusive),
     * where N is a positive integer and N > 1.
     */
    int roll(boolean isAttacking);

}

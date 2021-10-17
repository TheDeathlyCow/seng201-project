package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;

/**
 * Interfaces that defines behaviour for sending feedback to the player
 * in the middle of an event.
 */
public interface EventFeedback {

    /**
     * Sends feedback to the player after they are attacked.
     *
     * @param context Context in which the player was attacked.
     */
    void sendFeedback(ShipAttackContext context);

}

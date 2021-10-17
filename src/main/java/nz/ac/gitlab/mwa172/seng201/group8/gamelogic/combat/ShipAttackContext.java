package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Ship;

/**
 * Stores information related to ship attacks, such as target,
 * attacker, rolls, and damage dealt.
 */
public class ShipAttackContext {

    /**
     * Ship that is defending.
     * This field must always be specified.
     */
    private final Ship targetShip;

    /**
     * The ship that is attacking.
     * By default, this is the same as the target ship.
     */
    private final Ship attackingShip;

    /**
     * The roll to attack made by the attacking ship.
     * By default this is 0.
     */
    private final int attackRoll;

    /**
     * The roll to defend made by the target ship.
     * By default this is 0.
     */
    private final int defenceRoll;

    /**
     * How much damage was dealt.
     * By default this is 0.
     */
    private final int damageDealt;

    /**
     * A message that is displayed when this attack occurs.
     * If not set in the builder, this will be dynamically
     * created.
     */
    private final String attackMessage;

    /**
     * Whether or not this context has a custom attack message.
     */
    private final boolean hasCustomAttackMessage;


    /**
     * Constructs a ShipAttackContext with an attacking ship, and damage dealt.
     *
     * @param builder Builder for this attack.
     */
    private ShipAttackContext(Builder builder) {
        this.attackMessage = builder.attackMessage;
        this.attackingShip = builder.attackingShip;
        this.targetShip = builder.targetShip;
        this.attackRoll = builder.attackRoll;
        this.defenceRoll = builder.defenceRoll;
        this.damageDealt = builder.damageDealt;
        hasCustomAttackMessage = builder.hasCustomAttackMessage;
    }

    /**
     * @return Returns the target ship.
     */
    public Ship getTargetShip() {
        return targetShip;
    }

    /**
     * @return Returns the attacking ship.
     */
    public Ship getAttackingShip() {
        return attackingShip;
    }

    /**
     * @return Returns the attacker's attack roll.
     */
    public int getAttackRoll() {
        return attackRoll;
    }

    /**
     * @return Returns the target's defence roll.
     */
    public int getDefenceRoll() {
        return defenceRoll;
    }

    /**
     * @return Returns the damage dealt by the attacking ship.
     */
    public int getDamageDealt() {
        return damageDealt;
    }

    /**
     * If the context has a custom attack message, then the message will simply be returned.
     * Otherwise, this will dynamically create a message that better describes what
     * happened.
     *
     * @return Returns an attack message.
     */
    public String getAttackMessage() {

        if (hasCustomAttackMessage)
            return attackMessage;

        Object damageAmount = damageDealt > 0 ? damageDealt : "no";

        if (attackingShip == null) {
            return String.format("%s was attacked for %s damage!", targetShip.getName(), damageAmount);
        }

        return String.format("%s attacked %s for %s damage!",
                attackingShip.getProperName(), targetShip.getProperName(), damageAmount
        );
    }

    @Override
    public String toString() {
        return "ShipAttackContext{" +
                "targetShip=" + targetShip +
                ", attackingShip=" + attackingShip +
                ", attackRoll=" + attackRoll +
                ", defenceRoll=" + defenceRoll +
                ", damageDealt=" + damageDealt +
                ", attackMessage='" + attackMessage + '\'' +
                '}';
    }

    /**
     * Builder class for ShipAttackContext. Handles the default values of the
     * parent class above.
     */
    public static class Builder {
        private final Ship targetShip;
        private Ship attackingShip;
        private int damageDealt = 0;
        private String attackMessage;
        private int attackRoll = 0;
        private int defenceRoll = 0;
        private boolean hasCustomAttackMessage = false;

        /**
         * Constructor for the builder. Requires the target ship to be specified,
         * and sets the attacking ship to be the same as the target ship.
         *
         * @param targetShip Ship being targeted for an attack.
         */
        public Builder(Ship targetShip) {
            this.targetShip = targetShip;
            attackMessage = "";
        }

        /**
         * Sets the attack and defence rolls of the attack.
         *
         * @param attackRoll  The roll for attack made by the attacking ship.
         * @param defenceRoll The roll for defence made by the target ship.
         * @return Returns this builder.
         */
        public Builder withRolls(int attackRoll, int defenceRoll) {
            this.attackRoll = attackRoll;
            this.defenceRoll = defenceRoll;
            return this;
        }

        /**
         * Specifies the attacking ship.
         *
         * @param attackingShip Ship that is attacking.
         * @return Returns this builder.
         */
        public Builder withAttackingShip(Ship attackingShip) {
            this.attackingShip = attackingShip;
            return this;
        }

        /**
         * Specifies the damage dealt by this attack.
         *
         * @param damageDealt Damage dealt by this attack.
         * @return Returns this builder.
         */
        public Builder withDamageDealt(int damageDealt) {
            this.damageDealt = damageDealt;
            return this;
        }

        /**
         * Sets the attack message to a more specific value.
         *
         * @param attackMessage The attack message.
         * @return Returns this builder.
         */
        public Builder withAttackMessage(String attackMessage) {
            hasCustomAttackMessage = true;
            this.attackMessage = attackMessage;
            return this;
        }

        /**
         * Builds and returns the attack context.
         *
         * @return The context built by this builder.
         */
        public ShipAttackContext build() {
            return new ShipAttackContext(this);
        }
    }
}

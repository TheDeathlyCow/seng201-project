package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Upgrade class for Ships. Implements Tradeable interface.
 * Stores modifiers for ship attributes like max health, speed, and damage.
 * Modifiers can mean an increase or decrease in the attribute. However, there
 * may be certain limits for each attribute defined in the ShipAttributes class
 * in Ship.java.
 */
public class ShipUpgrade implements Tradeable {

    //* ShipUpgrade properties *//

    /**
     * Name of the upgrade, e.g. "Cannons".
     */
    private final String NAME;

    /**
     * HashMap that stores the modifiers
     * of this upgrade.
     */
    private final Map<String, Integer> MODIFIER_MAP = new LinkedHashMap<>();

    /**
     * The weight of this upgrade. By default is 0.
     */
    private final int WEIGHT;

    /**
     * The base value of this upgrade.
     */
    private double baseValue;
    /**
     * The value of this upgrade when it was purchased. Defaults to 0.
     */
    private double purchaseValue = 0;
    /**
     * The value of this upgrade when it was sold. Defaults to 0.
     */
    private double soldValue = 0;
    /**
     * Whether or not the player has sold this item.
     * If the player has this item, it is not sold.
     */
    private boolean isSold;

    private Island soldAt;

    //* Constructor method *//

    /**
     * Constructs a ShipUpgrade using a ShipUpgradeBuilder.
     * See below for details on ShipUpgradeBuilder
     *
     * @param builder ShipUpgradeBuilder - Builder to construct this ShipUpgrade with
     */
    private ShipUpgrade(ShipUpgradeBuilder builder) {
        this.NAME = builder.NAME;
        MODIFIER_MAP.put("max health", builder.maxHealthModifier);
        MODIFIER_MAP.put("speed", builder.speedModifier);
        MODIFIER_MAP.put("damage", builder.damageModifier);
        MODIFIER_MAP.put("armour", builder.armourModifier);
        MODIFIER_MAP.put("beds", builder.numBedsModifier);
        MODIFIER_MAP.put("max weight", builder.maxWeightModifier);
        MODIFIER_MAP.put("upgrade slots", builder.upgradeSlotsModifier);
        MODIFIER_MAP.put("cargo slots", builder.cargoSlotModifier);
        this.WEIGHT = builder.weight;
        this.baseValue = builder.baseValue;
        this.isSold = false;
    }

    //* Getter methods *//

    public String getName() {
        return NAME;
    }

    public int getMaxHealthModifier() {
        return MODIFIER_MAP.get("max health");
    }

    public int getSpeedModifier() {
        return MODIFIER_MAP.get("speed");
    }

    public int getDamageModifier() {
        return MODIFIER_MAP.get("damage");
    }

    public int getArmourModifier() {
        return MODIFIER_MAP.get("armour");
    }

    public int getNumBedsModifier() {
        return MODIFIER_MAP.get("beds");
    }

    public int getMaxWeightModifier() {
        return MODIFIER_MAP.get("max weight");
    }

    public int getUpgradeSlotModifier() {
        return MODIFIER_MAP.get("upgrade slots");
    }

    public int getCargoSlotModifier() {
        return MODIFIER_MAP.get("cargo slots");
    }

    public int getWeight() {
        return WEIGHT;
    }

    //* Tradeable methods *//

    /**
     * Determines the value of this upgrade on a given Island.
     * Different islands will value this upgrade differently.
     * TODO: implement island class so this can work.
     *
     * @param vendor Island - Island to check the value of item on
     * @return The value of this item as a double
     */
    @Override
    public double getValueAtVendor(Vendor vendor) {
        return getBaseValue();
    }

    /**
     * This is the value used to calculate the value of the item on
     * different islands, and does not change from island to island.
     *
     * @return A double representing the base value of this upgrade.
     */
    @Override
    public double getBaseValue() {
        return this.baseValue;
    }

    /**
     * @return True if the player has sold this upgrade. If the player has this upgrade on their ship,
     * they have not sold it.
     */
    @Override
    public boolean isSold() {
        return this.isSold;
    }

    /**
     * Event method called when the upgrade is purchased by the player.
     *
     * @param purchasedFrom the island this upgrade was purchased on
     */
    @Override
    public void onPurchase(Vendor purchasedFrom) {
        this.purchaseValue = getValueAtVendor(purchasedFrom);
        this.soldValue = 0.0;
        this.baseValue /= 2.0;
        isSold = false;
    }

    /**
     * Event method called when the upgrade is sold by the player.
     *
     * @param soldTo the island this upgrade was purchased on
     */
    @Override
    public void onSold(Vendor soldTo) {
        this.soldValue = getValueAtVendor(soldTo);
        isSold = true;
        this.soldAt = soldTo.getIsland();
    }

    /**
     * The purchase value is the value of the item when the player
     * purchased this upgrade and added it to their ship.
     *
     * @return The purchase value of the upgrade.
     */
    @Override
    public double getPurchaseValue() {
        return purchaseValue;
    }

    /**
     * Similar to the purchase value, the sold value is when the
     * player sold this item.
     *
     * @return The sold value of the upgrade.
     */
    @Override
    public double getSoldValue() {
        return soldValue;
    }

    @Override
    public Island getSoldAt() {
        return soldAt;
    }

    @Override
    public ShipUpgrade copy() {

        ShipUpgradeBuilder builder = new ShipUpgradeBuilder(NAME)
                .withMaxHealthModifier(getMaxHealthModifier())
                .withSpeedModifier(getSpeedModifier())
                .withArmourModifier(getArmourModifier())
                .withDamageModifier(getDamageModifier())
                .withNumBedsModifier(getNumBedsModifier())
                .withMaxWeightModifier(getMaxWeightModifier())
                .withCargoSlotModifier(getCargoSlotModifier())
                .withUpgradeSlotModifier(getUpgradeSlotModifier())
                .withWeight(getWeight())
                .withBaseValue(getBaseValue());

        ShipUpgrade copy = new ShipUpgrade(builder);
        copy.isSold = this.isSold;
        copy.purchaseValue = this.purchaseValue;
        copy.soldValue = this.soldValue;
        copy.soldAt = this.soldAt;
        return copy;
    }


    /**
     * Returns a new ShipUpgrade that has the negative of all of the modifiers
     * of the given upgrade, but the same base value.
     *
     * @param upgrade Upgrade to get the inverse of.
     * @return The new, inverted upgrade.
     */
    public static ShipUpgrade getInvertedUpgrade(ShipUpgrade upgrade) {
        return new ShipUpgrade.ShipUpgradeBuilder(upgrade.getName())
                .withMaxHealthModifier(-upgrade.getMaxHealthModifier())
                .withSpeedModifier(-upgrade.getSpeedModifier())
                .withArmourModifier(-upgrade.getArmourModifier())
                .withDamageModifier(-upgrade.getDamageModifier())
                .withNumBedsModifier(-upgrade.getNumBedsModifier())
                .withMaxWeightModifier(-upgrade.getMaxWeightModifier())
                .withCargoSlotModifier(-upgrade.getCargoSlotModifier())
                .withUpgradeSlotModifier(-upgrade.getUpgradeSlotModifier())
                .withWeight(-upgrade.getWeight())
                .withBaseValue(upgrade.getBaseValue())
                .build();
    }

    /**
     * Compares this upgrade to another upgrade by looking at their names, modifiers, and
     * base value. The purchase history of the upgrades are not compared.
     *
     * @param other The ShipUpgrade being compared to.
     * @return True if <b>other</b> is this upgrade or if the modifiers match, and false if
     * <b>other</b> is not a ShipUpgrade or if the modifiers do not match.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ShipUpgrade that = (ShipUpgrade) other;
        return MODIFIER_MAP.equals(that.MODIFIER_MAP)
                && WEIGHT == that.WEIGHT
                && Double.compare(that.baseValue, baseValue) == 0
                && NAME.equals(that.NAME);
    }

    /**
     * Hashs the name, modifiers, weight, and base value of this upgrade.
     *
     * @return The hash value of this upgrade.
     */
    @Override
    public int hashCode() {
        return Objects.hash(NAME,
                MODIFIER_MAP,
                WEIGHT,
                baseValue);
    }

    /**
     * @return A string representation of all of the upgrade's properties.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(NAME).append(" (");
        StringJoiner joiner = new StringJoiner(", ");
        for (String key : MODIFIER_MAP.keySet()) {
            if (MODIFIER_MAP.get(key) != 0) {
                joiner.add(String.format("%s%d %s",
                        MODIFIER_MAP.get(key) > 0 ? "+" : "",
                        MODIFIER_MAP.get(key),
                        key
                ));
            }
        }
        builder.append(joiner.toString());
        builder.append(")");
        return builder.toString();
    }

    /**
     * A builder class to construct a ShipUpgrade. The name of the upgrade is required,
     * but all other values will have a default value of 0.
     */
    public static class ShipUpgradeBuilder {
        /**
         * The name of this upgrade, e.g. "Cannons"
         */
        private final String NAME;
        /**
         * How much this upgrade will increase or decrease the maximum health of the ship is it applied to.
         */
        private int maxHealthModifier = 0;
        /**
         * How much this upgrade will increase or decrease the speed of the ship is it applied to.
         */
        private int speedModifier = 0;
        /**
         * How much this upgrade will increase or decrease the damage of the ship is it applied to.
         */
        private int damageModifier = 0;
        /**
         * How much this upgrade will increase or decrease the armour of the ship is it applied to.
         */
        private int armourModifier = 0;
        /**
         * How much this upgrade will increase or decrease the number of beds on the ship is it applied to.
         */
        private int numBedsModifier = 0;
        /**
         * How much this upgrade will increase or decrease the maximum weight capacity of the ship is it applied to.
         */
        private int maxWeightModifier = 0;
        /**
         * How much this upgrade will increase or decrease the number of upgrade slots on the ship is it applied to.
         */
        private int upgradeSlotsModifier = 0;
        /**
         * How much this upgrade will increase or decrease the number of cargo slots on the ship is it applied to.
         */
        private int cargoSlotModifier = 0;
        /**
         * The base value of this upgrade, used to calculate the purchase and selling value of this ship on different islands.
         */
        private double baseValue = 0.0;
        /**
         * The weight of this upgrade. Some upgrades may have weight associated with them
         * (however base upgrades typically will not).
         */
        private int weight = 0;

        /**
         * Constructor for ShipUpgradeBuilder. Requires a name to be set, but takes
         * no other parameters.
         *
         * @param name The name of the upgrade, e.g. "Cannons".
         */
        public ShipUpgradeBuilder(final String name) {
            this.NAME = name;
        }

        /**
         * @param maxHealthModifier How much the maximum health of the ship
         *                          will increase or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withMaxHealthModifier(int maxHealthModifier) {
            this.maxHealthModifier = maxHealthModifier;
            return this;
        }

        /**
         * @param speedModifier How much the speed of the ship will increase
         *                      or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withSpeedModifier(int speedModifier) {
            this.speedModifier = speedModifier;
            return this;
        }

        /**
         * @param damageModifier How much the damage of the ship will increase
         *                       or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withDamageModifier(int damageModifier) {
            this.damageModifier = damageModifier;
            return this;
        }

        /**
         * @param armourModifier How much the armour of the ship will increase
         *                       or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withArmourModifier(int armourModifier) {
            this.armourModifier = armourModifier;
            return this;
        }

        /**
         * @param numBedsModifier How much the number of beds on the ship will
         *                        increase or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withNumBedsModifier(int numBedsModifier) {
            this.numBedsModifier = numBedsModifier;
            return this;
        }

        /**
         * @param maxWeightModifier How much the maximum weight of the ship will
         *                          increase or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withMaxWeightModifier(int maxWeightModifier) {
            this.maxWeightModifier = maxWeightModifier;
            return this;
        }

        /**
         * @param upgradeSlotModifier How much the number of upgrade slots on the
         *                            ship will increase or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withUpgradeSlotModifier(int upgradeSlotModifier) {
            this.upgradeSlotsModifier = upgradeSlotModifier;
            return this;
        }

        /**
         * @param cargoSlotModifier How much the number of cargo slots on the ship
         *                          will increase or decrease by when this upgrade is applied.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withCargoSlotModifier(int cargoSlotModifier) {
            this.cargoSlotModifier = cargoSlotModifier;
            return this;
        }

        /**
         * @param baseValue the base value of this upgrade.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withBaseValue(double baseValue) {
            this.baseValue = baseValue;
            return this;
        }

        /**
         * @param weight The weight of this upgrade.
         * @return This ShipUpgradeBuilder.
         */
        public ShipUpgradeBuilder withWeight(int weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Builds the ShipUpgrade based on properties specified during construction.
         *
         * @return The ShipUpgrade that has all of the values specified by this builder.
         */
        public ShipUpgrade build() {
            return new ShipUpgrade(this);
        }
    }
}

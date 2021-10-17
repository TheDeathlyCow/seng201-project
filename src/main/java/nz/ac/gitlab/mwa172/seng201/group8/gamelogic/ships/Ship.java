package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.MaximumList;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.Fighter;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Abstract Ship class. Defines basic properties and methods used by Pirate Ships
 * and the Player's Ship.
 */
public abstract class Ship implements Fighter {
    /**
     * The name of the ship.
     */
    private final String NAME;

    /**
     * The current health of the ship. Will be somewhere between 0 and the
     * maximum health of the ship.
     */
    private int health;

    /**
     * The number of people on board this ship. Each person takes up 1
     * bed and has a small amount of weight.
     * This is the attribute used to track the number of people
     * in the ship's prison hold (for pirates), or the number
     * of crew members the player has.
     */
    private int peopleOnBoard;

    /**
     * The current weight of the ship. The weight must between the base weight
     * and the maximum weight of the ship.
     */
    private int currentWeight;

    /**
     * Stores attributes of the ship such as maximum health, damage, armour, and
     * maximum weight.
     */
    protected ShipAttributes attributes;

    /**
     * A MaximumList of all the upgrades this ship has acquired. The maximum number of items
     * this list can have is equal to the number of upgrade slots in the ship.
     */
    protected MaximumList<ShipUpgrade> upgrades;

    /**
     * A list of items that the player has sold.
     */
    private List<Tradeable> soldItems;

    /**
     * Special nested object used to manage the ship's cargo hold.
     */
    private CargoManager cargoManager;

    /**
     * Constructs a Ship with a name, base upgrade, and base weight.
     *
     * @param NAME        String - The name of the ship.
     * @param baseUpgrade ShipUpgrade - A base upgrade for the ship. Most values for this upgrade must be greater than 0.
     * @param baseWeight  final int - The base weight of the ship.
     */
    public Ship(final String NAME, ShipUpgrade baseUpgrade, final int baseWeight) {
        this.NAME = NAME;
        upgrades = new MaximumList<>(1);
        this.cargoManager = new CargoManager();
        attributes = new ShipAttributes(baseWeight);
        attributes.applyUpgrade(baseUpgrade);
        upgrades.setMaxSize(baseUpgrade.getUpgradeSlotModifier());
        this.health = attributes.maxHealth;
        this.currentWeight = attributes.getBaseWeight();
        addPeople((int) (attributes.getNumBeds() * 0.85));
        soldItems = new ArrayList<>();
    }

    /**
     * Damages the ship.
     *
     * @param context The context in which the ship is damaged.
     *                Includes things like amount of damage taken, the attacking ship, etc.
     * @return A string message describing what happened to the ship.
     */
    @Override
    public String onDamaged(ShipAttackContext context) {
        if (context.getDamageDealt() < 0)
            throw new IllegalArgumentException("Ships cannot take negative damage!");
        health -= context.getDamageDealt();
        if (health <= 0)
            health = 0;
        return context.getAttackMessage();
    }

    /**
     * Returns the cost of the ship to be repaired.
     *
     * @return Returns a double representing the cost to repair the ship.
     */
    public double getRepairCost() {
        return 1.5 * (attributes.getMaxHealth() - getHealth());
    }

    /**
     * Sets the current health of the ship to the max health, regardless of whether or not
     * the owner can afford it.
     *
     * @return Returns true.
     * @throws GameOver Never thrown.
     */
    public boolean repairShip() throws GameOver {
        this.health = attributes.getMaxHealth();
        return true;
    }

    /**
     * @param vendor Vendor to get the value of the upgrades at.
     * @return The total value of all of the upgrades at a specific vendor.
     */
    public double getTotalUpgradeValue(Vendor vendor) {
        double value = 0;
        for (ShipUpgrade upgrade : upgrades) {
            value += upgrade.getValueAtVendor(vendor);
        }
        return value;
    }

    /**
     * Applies an upgrade to the ship.
     *
     * @param upgrade Upgrade to be applied.
     * @return boolean - Returns true if the upgrade was successfully applied, false otherwise.
     */
    public boolean upgrade(ShipUpgrade upgrade) {
        if (upgrades.isFull()) {
            return false;
        }
        try {
            attributes.applyUpgrade(upgrade);
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return false;
        }
        upgrades.add(upgrade);
        return true;
    }

    /**
     * Removes a certain number of upgrades from this ship.
     *
     * @param upgrade     upgrade to be removed.
     * @param maxToRemove The maximum number of upgrades to be removed.
     * @return Returns true if any upgrades were removed, false otherwise.
     */
    public boolean removeUpgrade(ShipUpgrade upgrade, int maxToRemove) {
        int removed = 0;
        while (removed < maxToRemove && upgrades.remove(upgrade)) {
            attributes.removeUpgrade(upgrade);
            removeWeight(upgrade.getWeight());
            removed++;
        }

        return removed > 0;
    }

    /**
     * Removes 1 of the specified upgrade.
     *
     * @param upgrade Upgrade to be removed.
     * @return Returns true if the upgrade was removed, false otherwise.
     */
    public boolean removeUpgrade(ShipUpgrade upgrade) {
        return removeUpgrade(upgrade, 1);
    }

    /**
     * Adds some weight to the ship. Will only add the weight if the new weight
     * is within the range [attributes.getBaseWeight(), attributes.getMaxWeight()].
     *
     * @param moreWeight The amount of weight to be added.
     * @return Returns true if the weight could be added, false otherwise.
     */
    protected boolean addWeight(int moreWeight) {
        if (currentWeight + moreWeight <= attributes.getMaxWeight()
                && currentWeight + moreWeight >= attributes.getBaseWeight()) {
            currentWeight += moreWeight;
            return true;
        }
        return false;
    }

    /**
     * Removed some weight from the ship. Will only remove the weight if the new weight
     * is within the range [attributes.getBaseWeight(), attributes.getMaxWeight()].
     *
     * @param lessWeight The amount of weight to be removed.
     * @return Returns true if the weight could be removed, false otherwise.
     */
    protected boolean removeWeight(int lessWeight) {
        return addWeight(-lessWeight);
    }

    /**
     * Adds more people to the ship. Will only add them if there are enough beds for them.
     *
     * @param morePeople The amount of people to be added.
     * @return Returns true if the people could be added, false otherwise.
     */
    public boolean addPeople(int morePeople) {
        if (peopleOnBoard + morePeople <= attributes.getNumBeds()) {
            if (peopleOnBoard <= -morePeople)
                morePeople = peopleOnBoard;
            if (!addWeight(IslandTraderConfig.CONFIG.getPersonWeight() * morePeople)) {
                return false;
            }
            peopleOnBoard += morePeople;
            return true;
        }
        return false;
    }

    /**
     * Removes some people from the ship. If it attempts to remove more people
     * than are currently on board, then will just set the number of people to 0.
     *
     * @param lessPeople The number of people to be removed.
     * @return Returns true if the people could be removed, false otherwise.
     */
    public boolean removePeople(int lessPeople) {
        return addPeople(-lessPeople);
    }

    /**
     * Get the health of the ship as a percentage. Will be somewhere between 0% and 100%.
     *
     * @return Returns a double representing the health of the ship as a percentage.
     */
    public double getHealthAsPercentage() {
        return 100.0 * ((double) getHealth() / (double) attributes.getMaxHealth());
    }

    /**
     * Returns the attributes of this ship. Includes things such as
     * the maximum health, speed, damage, and armour. See ShipAttributes
     * class for more details.
     *
     * @return The attributes of this ship in a ShipAttributes object.
     */
    public ShipAttributes getAttributes() {
        return attributes;
    }

    /**
     * @return Returns the a MaximumList of the Ship's upgrades.
     */
    public MaximumList<ShipUpgrade> getUpgrades() {
        return upgrades;
    }

    /**
     * See <b>CargoManager</b> class in Ship.java for details on the cargo manager.
     *
     * @return Returns the ship's cargo manager.
     */
    public CargoManager getCargoManager() {
        return cargoManager;
    }

    /**
     * Adds an item to the sold history.
     *
     * @param item Item being added to sold history.
     */
    private void addToSoldHistory(Tradeable item) {
        soldItems.add(item);
    }

    /**
     * @return Returns the name of the ship.
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return Returns the current health of the ship.
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Returns the current weight of the ship.
     */
    public int getCurrentWeight() {
        return currentWeight;
    }

    /**
     * @return Gets the number of people on board this ship.
     */
    public int getPeopleOnBoard() {
        return peopleOnBoard;
    }

    /**
     * Returns the cargo hold.
     *
     * @return Returns an unmodifiable list of all of the items
     * currently in the cargo hold.
     */
    public List<CargoItem> getCargo() {
        return cargoManager.getCargo();
    }

    /**
     * Gets the sold history of the ship.
     *
     * @return Returns an unmodifiable list of items that have ever
     * been sold by the player.
     */
    public List<Tradeable> getSoldHistory() {
        return Collections.unmodifiableList(soldItems);
    }


    /**
     * Protected nested class that handles the ship's attributes including maximum health,
     * speed, damage, armour, number of beds, maximum weight, number of upgrade slots, number of
     * cargo slots, and the base weight. Contains methods to apply and remove upgrades to these
     * attributes.
     */
    public class ShipAttributes {
        /**
         * The maximum health of the ship.
         */
        private int maxHealth = 0;
        /**
         * The speed of the ship
         */
        private double speed = 0.0;
        /**
         * The amount of damage the ship can deal to other ships.
         */
        private int damage = 0;
        /**
         * How much armour this ship has.
         */
        private int armour = 0;
        /**
         * The number of beds for crew and prisoners on this ship.
         */
        private int numBeds = 0;
        /**
         * The maximum weight of this ship.
         */
        private int maxWeight = 0;
        /**
         * The base weight of this ship.
         */
        private final int BASE_WEIGHT;

        private ShipAttributes(final int baseWeight) {
            this.BASE_WEIGHT = baseWeight;
        }

        /**
         * Applies a given upgrade to the Ship's attributes, if it can be applied.
         * If the upgrade cannot be applied, throws an exception.
         *
         * @param upgrade Upgrade to apply
         * @throws IllegalStateException Thrown if the upgrade cannot be applied.
         */
        public void applyUpgrade(ShipUpgrade upgrade) throws IllegalStateException {
            if (canApplyUpgrade(upgrade)) {
                this.maxHealth += upgrade.getMaxHealthModifier();
                this.speed += upgrade.getSpeedModifier();
                this.damage += upgrade.getDamageModifier();
                this.armour += upgrade.getArmourModifier();
                this.numBeds += upgrade.getNumBedsModifier();
                this.maxWeight += upgrade.getMaxWeightModifier();
                addWeight(upgrade.getWeight());
                upgrades.setMaxSize(upgrades.getMaxSize() + upgrade.getUpgradeSlotModifier());
                cargoManager.cargoHold.setMaxSize(cargoManager.getMaxSize() + upgrade.getCargoSlotModifier());
                health = this.maxHealth;
            } else {
                throw new IllegalStateException("Cannot apply upgrade '"
                        + upgrade.getName() + "' to ship '" + NAME + "'");
            }
        }

        /**
         * Removes a given upgrade from a ship. Inverts many of the values of the upgrade,
         * then applies it normally. Will only remove the upgrade if the ship has it in
         * it's upgrade list.
         *
         * @param upgrade Upgrade to be removed
         * @throws IllegalStateException Thrown if upgrade cannot be removed.
         */
        public void removeUpgrade(ShipUpgrade upgrade) throws IllegalStateException {
            if (upgrades.contains(upgrade))
                applyUpgrade(ShipUpgrade.getInvertedUpgrade(upgrade));
        }

        /**
         * Determines if a given upgrade can be applied to this Ship's attributes.
         * Does not modify the attributes of the ship.
         *
         * @param upgrade Upgrade to be checked,
         * @return Returns true if the upgrade can be applied, false otherwise.
         */
        public boolean canApplyUpgrade(ShipUpgrade upgrade) {
            return upgrades.size() + 1 <= upgrades.getMaxSize()
                    && maxHealth + upgrade.getMaxHealthModifier() > 0
                    && speed + upgrade.getSpeedModifier() > 0.0
                    && damage + upgrade.getDamageModifier() > 0
                    && armour + upgrade.getArmourModifier() >= 0
                    && numBeds + upgrade.getNumBedsModifier() >= peopleOnBoard
                    && maxWeight + upgrade.getMaxWeightModifier() >= attributes.getBaseWeight()
                    && currentWeight + upgrade.getWeight() <= maxWeight + upgrade.getMaxWeightModifier()
                    && upgrades.getMaxSize() + upgrade.getUpgradeSlotModifier() > upgrades.size()
                    && cargoManager.getMaxSize() + upgrade.getCargoSlotModifier() > cargoManager.size();
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public double getSpeed() {
            return speed * (1.0 - (double) getCurrentWeight() / (2 * getMaxWeight()));
        }

        public int getDamage() {
            return damage;
        }

        public int getArmour() {
            return armour;
        }

        public int getNumBeds() {
            return numBeds;
        }

        public int getMaxWeight() {
            return maxWeight;
        }

        public final int getBaseWeight() {
            return BASE_WEIGHT;
        }
    }

    /**
     * Nested class that handles the ship's cargo hold.
     * Contains methods to add and remove cargo, as well as
     * get the number of items and maximum size of the cargo hold.
     */
    public class CargoManager {
        /**
         * A MaximumList of all the items stored in this ship's cargo hold.
         */
        private MaximumList<CargoItem> cargoHold;

        /**
         * Creates a cargo hold that cannot hold any items.
         */
        private CargoManager() {
            this.cargoHold = new MaximumList<>(0);
        }

        public boolean canAddCargoItem(CargoItem cargoItem) {
            return cargoHold.size() + 1 < cargoHold.getMaxSize()
                    && getCurrentWeight() + cargoItem.getWeight() <= getAttributes().getMaxWeight();
        }

        public boolean addCargo(CargoItem item) {
            if (cargoHold.add(item)) {
                if (addWeight(item.getWeight())) {
                    return true;
                }
                cargoHold.remove(item);
            }
            return false;
        }

        public boolean removeCargo(CargoItem item) {
            if (cargoHold.remove(item)) {
                if (removeWeight(item.getWeight())) {
                    addToSoldHistory(item);
                    return true;
                }
                cargoHold.add(item);
            }
            return false;
        }

        /**
         * Sums all the value of all of the cargo at a vendor.
         *
         * @param vendor Vendor to get the value at.
         * @return Returns a double with the sum of the base values of
         * all of the items in the ship's cargo hold at a specific vendor.
         */
        public double getTotalCargoValue(Vendor vendor) {
            double totalValue = 0;
            for (CargoItem item : cargoHold) {
                totalValue += item.getValueAtVendor(vendor);
            }
            return totalValue;
        }

        /**
         * @return Returns the number of items in the cargo hold.
         */
        public int size() {
            return cargoHold.size();
        }

        /**
         * @return Returns the maximum number of items that can be
         * in the ship's cargo hold.
         */
        public int getMaxSize() {
            return cargoHold.getMaxSize();
        }

        /**
         * Determines whether or not there is an item of the specified
         * type in the cargo hold.
         *
         * @param type The type to look for.
         * @return Returns true if the type is found, false otherwise.
         */
        public boolean hasCargoOfType(CargoItem.CargoType type) {
            for (CargoItem item : cargoHold) {
                if (item.getType().equals(type))
                    return true;
            }
            return false;
        }

        /**
         * Removes a random item from the cargo hold.
         *
         * @return Returns the item that was removed.
         */
        public CargoItem removeRandomItem() {
            int removeIndex = new Random().nextInt(size());
            return cargoHold.remove(removeIndex);
        }

        /**
         * @return Returns an unmodifiable list of cargo items.
         */
        public List<CargoItem> getCargo() {
            return Collections.unmodifiableList(cargoHold);
        }
    }
}

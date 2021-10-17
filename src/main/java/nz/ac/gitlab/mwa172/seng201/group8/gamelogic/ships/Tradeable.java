package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;

/**
 * Provides methods for objects which the player can trade.
 * Note that any class with implements this interface should
 * specify its equals and hashCode methods.
 */
public interface Tradeable {

    /**
     * Gets the name of a Tradeable item.
     * @return Returns the name of a Tradeable item.
     */
    String getName();

    /**
     * Gets the value of the item at a given vendor.
     *
     * @param vendor Vendor to determine the value of this item at.
     * @return The value of the item at the vendor.
     */
    double getValueAtVendor(Vendor vendor);

    /**
     * Gets the base value of the item. This is the value used to calculate
     * the value of the item on different islands, and does not change from
     * island to island.
     *
     * @return Returns a double representing the base value of this item.
     */
    double getBaseValue();

    /**
     * Returns the value of this item when the player purchases it.
     *
     * @return a double of the price of the item when the player purchased it. Returns 0.0 if it has not been purchased.
     */
    double getPurchaseValue();

    /**
     * Returns the value of this item when the player sells it.
     *
     * @return a double of the price of the item when the player sold it. Returns 0.0 if it has not been sold.
     */
    double getSoldValue();

    /**
     * Called when this item is purchased.
     *
     * @param purchasedFrom The vendor this item was purchased from.
     */
    void onPurchase(Vendor purchasedFrom);

    /**
     * Called when this item is sold by a player.
     *
     * @param soldTo The vendor this item was sold to.
     */
    void onSold(Vendor soldTo);

    /**
     * Check if the player has sold this item.
     *
     * @return true if the item has been sold, false otherwise.
     */
    boolean isSold();

    /**
     * Gets the island this Tradeable was sold at.
     *
     * @return The island this Tradeable was sold at.
     */
    Island getSoldAt();

    /**
     * Returns a new instance of this tradeable with all of the same
     * attributes as the original.
     *
     * @return Returns a new tradeable.
     */
    Tradeable copy();

}

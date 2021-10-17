package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;

/**
 * The player class stores information related to the player.
 */
public class Player {
    /**
     * The name of the player.
     */
    private final String name;
    /**
     * The amount of money the player currently has.
     */
    private double balance;
    /**
     * The ship the player is currently captaining.
     */
    private PlayerShip ship;
    /**
     * The present location of the player.
     * ? This could be moved to the command context, as it is only used there.
     */
    private Location location;

    /**
     * Creates a new player with a name.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.balance = 0;
        location = Location.ISLAND;
        this.addFunds(750);
    }

    /**
     * Creates a new player with a name and starting ship.
     *
     * @param name         The name of the player.
     * @param startingShip The player's starting ship.
     */
    public Player(String name, PlayerShip startingShip) {
        this(name);
        this.setShip(startingShip);
    }

    /**
     * Gets the ship the player is currently captaining.
     *
     * @return Returns the player's ship.
     */
    public PlayerShip getShip() {
        return ship;
    }

    /**
     * Sets the ship of the player, and sets the owner of that ship
     * to this player.
     *
     * @param ship Player's new ship.
     */
    public void setShip(PlayerShip ship) {
        this.ship = ship;
        if (ship != null)
            this.ship.setOwner(this);
    }

    /**
     * @return Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Determines whether or not the player can purchase an item. Will check the player
     * has enough money, and if the item will fit into the ship.
     *
     * @param wantsToBuy The item the player wants to buy. Must either be a CargoItem or ShipUpgrade.
     * @return Returns true if the player can purchase the item.
     */
    public boolean canPurchase(Tradeable wantsToBuy) {
        boolean hasFunds = getBalance() >= wantsToBuy.getValueAtVendor(ship.dockedAt().getVendor());
        boolean canApplyUpgrade = true;
        boolean canAddCargoItem = true;

        if (ShipUpgrade.class.equals(wantsToBuy.getClass())) {
            canApplyUpgrade = ship.getAttributes().canApplyUpgrade((ShipUpgrade) wantsToBuy);
        } else if (CargoItem.class.equals(wantsToBuy.getClass())) {
            canAddCargoItem = ship.getCargoManager().canAddCargoItem((CargoItem) wantsToBuy);
        }

        return hasFunds && canApplyUpgrade && canAddCargoItem;

    }

    /**
     * Purchases an item from a vendor. Removes the value of the item
     * from the player's balance and adds the item either to their
     * ship or cargo hold.
     *
     * @param buying     The item the player is buying.
     * @param buyingFrom The vendor the player is buying from.
     * @throws IllegalStateException    Thrown if the player cannot purchase the item.
     * @throws IllegalArgumentException Thrown if the item the player wants to buy
     *                                  is not a cargo item or ship upgrade.
     */
    public void purchase(Tradeable buying, Vendor buyingFrom) throws IllegalStateException, IllegalArgumentException {
        if (!canPurchase(buying)) {
            throw new IllegalStateException("Player cannot purchase " + buying.getName() + "!");
        }
        removeFunds(buying.getValueAtVendor(buyingFrom));
        buying.onPurchase(ship.dockedAt().getVendor());
        buyingFrom.sellItem(buying);
        if (buying.getClass().equals(CargoItem.class)) {
            ship.getCargoManager().addCargo((CargoItem) buying);
        } else if (buying.getClass().equals(ShipUpgrade.class)) {
            ship.upgrade((ShipUpgrade) buying);
        } else {
            throw new IllegalArgumentException("Player cannot purchase " + buying.getClass().getSimpleName());
        }

    }

    /**
     * Sells an item to a vendor.
     *
     * @param selling   Item to sell.
     * @param sellingTo Vendor to sell to.
     */
    public void sell(Tradeable selling, Vendor sellingTo) {
        if (sellingTo.purchaseItem(selling)) {
            addFunds(selling.getValueAtVendor(sellingTo));
            selling.onSold(sellingTo);
            if (selling.getClass().equals(CargoItem.class)) {
                ship.getCargoManager().removeCargo((CargoItem) selling);
            } else if (selling.getClass().equals(ShipUpgrade.class)) {
                ship.removeUpgrade((ShipUpgrade) selling);
            }
        }
    }

    /**
     * @return Returns the current balance of the player.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Adds more money to the player's balance. If the player would end up having
     * negative money from this, they will instead have their balance set to 0.
     *
     * @param moreFunds The amount of money to add.
     */
    public void addFunds(double moreFunds) {
        if (this.balance + moreFunds >= 0)
            this.balance += moreFunds;
        else
            this.balance = 0;
    }

    /**
     * Removes money from the player's balance. If the player would end up having
     * negative money from this, they will instead have their balance set to 0.
     *
     * @param lessFunds The amount of money to remove.
     */
    public void removeFunds(double lessFunds) {
        addFunds(-lessFunds);
    }

    /**
     * @return Returns the current location of the player.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Moves the player to a new location.
     *
     * @param location The new location of the player.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * An enum of all the places the player can be.
     */
    public enum Location {
        ISLAND,
        DOCKS,
        VENDOR_BUYING,
        VENDOR_SELLING
    }
}

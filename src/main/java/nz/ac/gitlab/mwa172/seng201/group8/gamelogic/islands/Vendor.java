package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;

import java.util.*;
import java.util.stream.Collectors;

import static nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem.CargoType;

/**
 * Class for handling the vendors that the players interact with.
 * Contains methods for the vendors greetings, desire, stock, gold, and
 * the purchasing and selling of stock.
 */
public class Vendor {
    /**
     * The name of the vendor.
     */
    private final String NAME;
    /**
     * The list of greetings that the vendor can use.
     */
    private final List<String> greetings;
    /**
     * The cargo stock that the vendor has to sell.
     */
    private final List<CargoItem> cargoStock;
    /**
     * The stock that the vendor has to sell that upgrades
     * the players ship.
     */
    private final List<ShipUpgrade> upgradeStock;
    /**
     * The amount of money that the vendor has.
     */
    private double balance;
    /**
     * The table that has the vendors desire for goods.
     */
    private final Map<CargoType, Double> desireTable;
    /**
     * The island that the vendor is on.
     */
    private Island island;

    //* Constructors *//

    /**
     * Constructs a vendor.
     *
     * @param name The name of the vendor.
     */
    public Vendor(String name) {
        this.NAME = name;
        greetings = FileManager.getVendorGreetings();
        cargoStock = new ArrayList<>();
        upgradeStock = new ArrayList<>();
        desireTable = new HashMap<>();
    }


    //* Greetings *//

    /**
     * Adds the list of greetings that the vendor can say.
     *
     * @param greetings A list of all of the greetings the vendor can say.
     */
    public void addGreetings(List<String> greetings) {
        this.greetings.addAll(greetings);
    }

    /**
     * Gets a random greeting from the greeting list. If the greeting
     * in written in a right to left script, makes sure that the greeting
     * is still displayed in a left to right format.
     *
     * @param greeting A possible greeting for the vendor.
     * @return A random greeting for the vendor to say to the player.
     */
    public String getRandomGreeting(Player greeting) {
        int randGreeting = new Random().nextInt(greetings.size());
        return /* "\u200E" + */ greetings.get(randGreeting) + " " + greeting.getName() + "!";
    }

    //* Purchasing methods *//

    /**
     * Purchases an item from the player. The item is copied before it
     * is added to this vendor's inventory.
     *
     * @param item The item that is going to be purchased by the vendor.
     * @return Returns true if the vendor can purchase the item false otherwise.
     */
    public boolean purchaseItem(Tradeable item) {
        if (canPurchase(item)) {
            Tradeable newItem = item.copy();
            if (item.getClass().equals(CargoItem.class))
                cargoStock.add((CargoItem) newItem);
            else if (item.getClass().equals(ShipUpgrade.class))
                upgradeStock.add((ShipUpgrade) newItem);
            else
                return false;
            balance -= item.getValueAtVendor(this);
            return true;
        }
        return false;
    }

    /**
     * Sells an item to the player.
     *
     * @param item The item that is being sold by the vendor.
     * @return Returns true if the vendor can sell the item false otherwise.
     */
    public boolean sellItem(Tradeable item) {
        if (item.getClass().equals(CargoItem.class))
            cargoStock.remove(item);
        else if (item.getClass().equals(ShipUpgrade.class))
            upgradeStock.remove(item);
        else
            return false;
        balance += item.getValueAtVendor(this);
        return true;
    }

    /**
     * Determines whether or not the vendor can purchase an item.
     *
     * @param item The item to be purchased.
     * @return Returns true if the vendor can purchase the item false otherwise.
     */
    public boolean canPurchase(Tradeable item) {
        return balance >= item.getValueAtVendor(this);
    }

    //* Desire methods *//

    /**
     * Creates a description of the types of items this vendor
     * desire.
     *
     * @return Returns a string of the types of the vendor desires and how much,
     * in sorted order from most desirable to least desireable.
     */
    public String getDesireDescription() {
        Map<CargoType, Double> desires = new LinkedHashMap<>();

        this.desireTable.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(i -> desires.put(i.getKey(), i.getValue()));

        StringBuilder description = new StringBuilder();

        for (Map.Entry<CargoType, Double> entry : desires.entrySet()) {
            description.append(entry.getKey().toString());
            description.append(": ");
            description.append(String.format("%.2f", entry.getValue()));
            description.append("\n");
        }

        return description.toString();
    }

    /**
     * Sets the vendors desire for all item types.
     *
     * @param cargoType The type of cargo to set desire for.
     * @param desire    The desire that will be placed on the type to influence price.
     */
    public void setDesireForType(CargoType cargoType, double desire) {
        desireTable.put(cargoType, desire);
    }

    /**
     * Gets the desire table of the vendor.
     *
     * @param cargoType The type of cargo to get.
     * @return The cargo types desire table.
     */
    public double getDesireFor(CargoType cargoType) {
        return desireTable.get(cargoType);
    }

    //* Stock methods *//

    /**
     * Adds an item to the players stock.
     *
     * @param cargoItem The item to be added.
     */
    public void addCargoToStock(CargoItem cargoItem) {
        cargoStock.add(cargoItem);
    }

    /**
     * Adds a ship upgrade to the players stock.
     *
     * @param upgrade The upgrade to be added.
     */
    public void addUpgradeToStock(ShipUpgrade upgrade) {
        upgradeStock.add(upgrade);
    }

    //* Getters and Setters *//

    /**
     * Gets the players cargo stock.
     *
     * @return The cargo stock.
     */
    public List<CargoItem> getCargoStock() {
        return cargoStock;
    }

    /**
     * Gets the players ship upgrade stock.
     *
     * @return The shop upgrade stock.
     */
    public List<ShipUpgrade> getUpgradeStock() {
        return upgradeStock;
    }

    /**
     * Sets the balance of the vendor.
     *
     * @param balance The money that the vendor has.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the balance of the vendor.
     *
     * @return The vendors balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Gets the island that the vendor is on.
     *
     * @return The island.
     */
    public Island getIsland() {
        return island;
    }

    /**
     * Sets the island that the vendor is on.
     *
     * @param island The island.
     */
    public void setIsland(Island island) {
        this.island = island;
    }
}

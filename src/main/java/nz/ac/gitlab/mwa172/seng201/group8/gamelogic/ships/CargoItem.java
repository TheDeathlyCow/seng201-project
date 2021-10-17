package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships;

import com.google.gson.*;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Stores information related to cargo and vendors.
 */
public class CargoItem implements Tradeable, JsonDeserializer<CargoItem> {
    /**
     * The name of the cargo item.
     */
    private final String NAME;
    /**
     * The base value of the cargo item.
     */
    private double baseValue;
    /**
     * The value of the cargo item when it was purchased.
     */
    private double purchaseValue;
    /**
     * The value of the cargo item when it was so.d
     */
    private double soldValue;
    /**
     * Whether or not this item has been sold.
     */
    private boolean isSold;
    /**
     * The weight of the cargo item.
     */
    private final int WEIGHT;
    /**
     * The type of the cargo item.
     */
    private final CargoType TYPE;
    /**
     * The island the item was sold on, if it has been sold.
     */
    private Island soldAt;

    /**
     * Constructs a cargo item.
     *
     * @param NAME   The name of the cargo item.
     * @param type   The type of the cargo item.
     * @param value  The value of the cargo item.
     * @param weight The weight of the cargo item.
     */
    public CargoItem(final String NAME, CargoType type, double value, int weight) {
        this.NAME = NAME;
        this.TYPE = type;
        this.baseValue = value;
        this.WEIGHT = weight;
        this.isSold = false;
    }

    /**
     * Creates a generic cargo item, mainly for GSON.
     */
    public CargoItem() {
        // Default Constructor for GSON
        this("Crate", CargoType.COMMON, 2.0, 2);
    }

    /**
     * @return The weight of the cargo item.
     */
    public int getWeight() {
        return WEIGHT;
    }

    /**
     * @return The type of the cargo item.
     */
    public CargoType getType() {
        return TYPE;
    }

    //* Tradeable Methods *//

    /**
     * @return The name of the cargo item.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Takes a cargo item and multiples it by the vendors desire for that type of cargo item.
     *
     * @param vendor The vendor
     * @return The value of the Cargo item after the vendors desire has been applied.
     */
    @Override
    public double getValueAtVendor(Vendor vendor) {
        return baseValue * vendor.getDesireFor(this.TYPE);
    }

    /**
     * @return The base value of the cargo item.
     */
    @Override
    public double getBaseValue() {
        return baseValue;
    }

    /**
     * Called when this item is purchased from a vendor.
     *
     * @param purchasedFrom The vendor this item was purchased from.
     */
    @Override
    public void onPurchase(Vendor purchasedFrom) {
        isSold = false;
        this.purchaseValue = this.getValueAtVendor(purchasedFrom);
    }

    /**
     * Called when this item is sold to a vendor.
     *
     * @param soldTo The vendor this item was sold to.
     */
    @Override
    public void onSold(Vendor soldTo) {
        isSold = true;
        this.soldValue = this.getValueAtVendor(soldTo);
        this.soldAt = soldTo.getIsland();
    }

    /**
     * @return Returns true if this item has been sold, false otherwise.
     */
    @Override
    public boolean isSold() {
        return isSold;
    }

    /**
     * @return Returns the value of this item when it was purchased.
     */
    @Override
    public double getPurchaseValue() {
        return purchaseValue;
    }

    /**
     * @return Returns the value of this item when it was sold.
     */
    @Override
    public double getSoldValue() {
        return soldValue;
    }

    /**
     * @return Returns the island this item was sold at. If isSold is false, then returns null.
     */
    @Override
    public Island getSoldAt() {
        return soldAt;
    }

    /**
     * Creates a copy of this CargoItem.
     *
     * @return Returns a new instance of CargoItem that is identical to this instance.
     */
    @Override
    public CargoItem copy() {
        CargoItem copy = new CargoItem(NAME, TYPE, baseValue, WEIGHT);
        copy.isSold = this.isSold;
        copy.purchaseValue = this.purchaseValue;
        copy.soldValue = this.soldValue;
        copy.soldAt = this.soldAt;
        return copy;
    }

    /**
     * Creates a string representation of this cargo item.
     *
     * @return Returns a string in the format of "name (type)".
     */
    @Override
    public String toString() {
        return String.format("%s (%s)", NAME, TYPE.toString());
    }

    //* CargoType enum *//

    /**
     * Enum that handles the various types a cargo item can be.
     */
    public enum CargoType {
        ALCOHOL("Alcohol"),
        LUXURY("Luxury Item"),
        NATURAL_RESOURCE("Natural Resource"),
        WEAPON("Weapons"),
        FOOD("Food Stuffs"),
        MEDICINE("Medicine"),
        COMMON("Common");

        /**
         * Nicer string representation of this enum.
         */
        private final String TYPE_STRING;

        /**
         * Private constructor that creates a new instance of CargoType.
         *
         * @param typeString The string representation of this instance.
         */
        CargoType(String typeString) {
            this.TYPE_STRING = typeString;
        }

        /**
         * @return Returns the type string.
         */
        @Override
        public String toString() {
            return TYPE_STRING;
        }
    }

    /**
     * Deserializes a json element into a cargo item.
     * Cargo item JSON elements should be of the form:
     * <pre>
     *     {
     *         "name": "Crate of Lemons",
     *         "type": "FOOD",
     *         "base_value": 5.7,
     *         "weight": 3
     *     }
     * </pre>
     *
     * @param jsonElement Json element to deserialize.
     * @param type        The type of the object being deserialized to.
     * @param context     The deserialization context.
     * @return Returns a deserialized instance of cargo item based on jsonElement.
     * @throws JsonParseException Thrown if the jsonElement is not a valid cargo item.
     */
    @Override
    public CargoItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        CargoItem.CargoType cargoType = CargoItem.CargoType.valueOf(jsonObject.get("type").getAsString());
        double baseValue = jsonObject.get("base_value").getAsDouble();
        int weight = jsonObject.get("weight").getAsInt();

        return new CargoItem(name, cargoType, baseValue, weight);
    }

    /**
     * Determines if two cargo items are equal. Does not compare purchase history.
     *
     * @param o Item to compare to.
     * @return Returns true of this and o are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoItem cargoItem = (CargoItem) o;
        return Double.compare(cargoItem.baseValue, baseValue) == 0
                && WEIGHT == cargoItem.WEIGHT
                && NAME.equals(cargoItem.NAME)
                && TYPE == cargoItem.TYPE;
    }

    /**
     * Hashes a cargo item.
     *
     * @return Returns the hash code of this item.
     */
    @Override
    public int hashCode() {
        return Objects.hash(NAME, baseValue, isSold, WEIGHT, TYPE);
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;

import java.util.Objects;

/**
 * Stores information relating to islands. Each island is a vertex
 * on a graph of islands kept by the game world.
 */
public class Island {
    /**
     * The name of the island.
     */
    private final String NAME;
    /**
     * The vendor of this island.
     */
    private final Vendor vendor;
    /**
     * The world this island exists in.
     */
    private GameWorld world;

    /**
     * Creates a new island with a name and vendor.
     *
     * @param name   The name of the island.
     * @param vendor The island's vendor.
     */
    public Island(String name, Vendor vendor) {
        this.NAME = name;
        this.vendor = vendor;
    }

    //* Basic Getters and Setters *//

    /**
     * @return Returns the vendor of this island.
     */
    public Vendor getVendor() {
        return vendor;
    }

    /**
     * @return Returns the name of this island.
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return Returns the world this island exists in.
     */
    public GameWorld getWorld() {
        return world;
    }

    /**
     * Sets the world this island exists in.
     *
     * @param world The new world for this island.
     */
    public void setWorld(GameWorld world) {
        this.world = world;
    }

    /**
     * Islands are compared solely on the basis of their names, therefore
     * every island MUST have a unique name.
     *
     * @param other Island to compare to.
     * @return True if the name of the islands match, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Island island = (Island) other;
        return NAME.equals(island.NAME);
    }

    /**
     * Hashes the island's name.
     *
     * @return The hash value of the island's name.
     */
    @Override
    public int hashCode() {
        return Objects.hash(NAME);
    }

    /**
     * Creates a string representation of this island.
     *
     * @return Returns the name of this island.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}

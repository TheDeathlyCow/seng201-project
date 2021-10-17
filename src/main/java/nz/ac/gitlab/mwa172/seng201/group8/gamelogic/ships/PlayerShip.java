package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.PirateShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.DiceRoller;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;

import java.util.Random;

/**
 * Class for handling ships that the player can use.
 * Contains methods for upgrading, repairing, sailing to islands,
 * and crew.
 */
public class PlayerShip extends Ship {

    /**
     * The player who owns this ship.
     */
    private Player owner;
    /**
     * The island the ship is currently docked at.
     */
    private Island dockedAt;
    /**
     * Whether or not the wages for this ship's crew have been paid.
     */
    private boolean wagesPaid;

    /**
     * Constructor for PlayerShip. Requires a name, base upgrade, and base weight.
     * The base upgrade must increase the attributes of the ship.
     *
     * @param name        The name of the ship.
     * @param baseUpgrade The base upgrade of the ship. Includes things like max health,
     *                    damage, and armour.
     * @param baseWeight  The base weight of the ship. The weight of the ship can never
     *                    be less than this value.
     */
    public PlayerShip(String name, ShipUpgrade baseUpgrade, int baseWeight) {
        super(name, baseUpgrade, baseWeight);
    }

    //* Upgrades and Repairs *//

    /**
     * Repairs the ship if the owner has enough money to do so.
     * Removes the funds from the player's balance if the repair
     * is made.
     *
     * @return Returns true if the ship was repaired, false otherwise.
     * @throws NullPointerException Thrown if the owner is not set.
     * @throws GameOver             Thrown if the player cannot possibly repair their ship.
     */
    @Override
    public boolean repairShip() throws NullPointerException, GameOver {
        double balance = owner.getBalance();
        double repairCost = getRepairCost();
        if (canRepairShip()) {
            super.repairShip();
            owner.removeFunds(repairCost);
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not the ship can be repaired. If the owner
     * does not have enough money to repair, also checks if the player can
     * possibly repair their ship, and ends the game if they cannot.
     *
     * @return Returns true of the player can repair their ship, false otherwise.
     * @throws GameOver Thrown if the player can never repair their ship.
     */
    public boolean canRepairShip() throws GameOver {
        if (getRepairCost() > owner.getBalance()) {
            double totalValue = owner.getBalance();
            totalValue += getCargoManager().getTotalCargoValue(dockedAt.getVendor());
            totalValue += getTotalUpgradeValue(dockedAt.getVendor());
            if (totalValue < getRepairCost())
                throw new GameOver(GameOver.GameOverReason.BANKRUPTCY, owner,
                        "You cannot possibly afford to repair your ship!");
            return false;
        }
        return getHealth() < attributes.getMaxHealth();
    }

    //* Setter and Getter for Owner *//

    /**
     * Sets the owner of this ship. Also assigns this ship to be
     * the current ship of the new owner.
     *
     * @param owner The new owner of this ship.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * @return The owner of this ship.
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Moves the ship to a new island.
     *
     * @param island Island to move the ship to.
     */
    public void moveTo(Island island) {
        this.dockedAt = island;
    }

    //* Travel-related methods *//

    /**
     * Determines whether or not this ship can sail a route.
     *
     * @param connection Route to check if the player can sail.
     * @return Returns true if it can sail the given connection false otherwise.
     * @throws GameOver Thrown if the player cannot afford to sail anywhere.
     */
    public boolean canSailRoute(Connection connection) throws GameOver {
        // null check
        if (connection == null)
            return false;

        // time check
        final int maxDays = dockedAt.getWorld().getTotalDays();
        int currentDay = dockedAt.getWorld().getCurrentDay();
        if (currentDay + getSailTime(connection.getDistance()) > maxDays) {
            checkTimeToSailAnywhere(currentDay);
            return false;
        }

        // health check - we must have full health to sail
        if (getHealth() < attributes.getMaxHealth()) {
            canRepairShip();
            return false;
        }

        // crew check - we must have 90% crew to sail
        if (getNumCrew() < getMinCrewToSail()) {
            if (!canHireCrew(1))
                throw new GameOver(GameOver.GameOverReason.BANKRUPTCY, owner,
                        "You cannot possibly hire any more crew!");

            return false;
        }

        // wage check - final check
        return wagesPaid || canPayCrew(connection.getDistance());
    }

    /**
     * Checks if there is enough time to sail to anywhere on the map.
     *
     * @param currentDay The current day.
     * @throws GameOver Thrown if there is not enough time to sail anywhere
     *                  on the map with reason TIME_UP.
     */
    private void checkTimeToSailAnywhere(final int currentDay) throws GameOver {
        final int maxDays = dockedAt.getWorld().getTotalDays();
        boolean canSailAnywhere = false;
        for (Island island : dockedAt.getWorld().getIslands()) {
            Connection toIsland = dockedAt.getWorld().getConnectionBetween(dockedAt, island);
            if (toIsland != null && currentDay + getSailTime(toIsland.getDistance()) <= maxDays) {
                canSailAnywhere = true;
            }
        }

        if (!canSailAnywhere)
            throw new GameOver(GameOver.GameOverReason.TIME_UP, owner, "There is not enough time to sail anywhere!");
    }

    /**
     * Attempts to travel to an island. Will only travel if it can, otherwise
     * will throw and exception.
     *
     * @param connection Route to sail.
     * @return Returns any events that this ship may have encountered while sailing.
     * @throws IllegalStateException Thrown if this ship cannot sail to the specified island.
     * @throws GameOver              Thrown if the trip takes more than the available number of days left, or if
     *                               the player cannot possibly pay their crew.
     */
    public TravellingEvent<?> travel(Connection connection) throws IllegalStateException, GameOver {
        GameWorld world = dockedAt.getWorld();
        Island destination = connection.getDestination();
        if (!canSailRoute(connection))
            throw new IllegalStateException("PlayerShip " + getName() + " cannot sail to " + destination.getName());
        TravellingEvent<?> event = connection.onSail();
        payCrew(connection.getDistance());
        moveTo(destination);
        wagesPaid = false;
        world.addDays(this.getSailTime(connection.getDistance()));
        return event;
    }

    /**
     * Calculates how long it will take for this ship to sail a distance.
     *
     * @param distance The distance to sail.
     * @return Returns the number of days it will take to sail to a route.
     */
    public int getSailTime(int distance) {
        return (int) Math.ceil(distance / attributes.getSpeed());
    }

    /**
     * @return The island this ship is currently docked at.
     */
    public Island dockedAt() {
        return dockedAt;
    }

    //* Crew Methods *//

    /**
     * Adds more crew members to the ship.
     *
     * @param moreCrew The amount of crew to add.
     * @return Returns true if the crew could be added, false otherwise.
     */
    public boolean addCrew(int moreCrew) {
        return addPeople(moreCrew);
    }

    /**
     * Removes some crew members from the ship.
     *
     * @param lessCrew The amount of crew to remove.
     * @return Returns true if the crew could be removed, false otherwise.
     */
    public boolean removeCrew(int lessCrew) {
        return removePeople(lessCrew);
    }

    /**
     * @return The amount of crew this ship has.
     */
    public int getNumCrew() {
        return getPeopleOnBoard();
    }

    /**
     * A ship must have 90% of its beds filled in order to sail.
     *
     * @return Returns the minimum number of crew members needed to sail.
     */
    public int getMinCrewToSail() {
        return (int) (attributes.getNumBeds() * 0.9);
    }

    /**
     * Determines if the owner can pay the wages of this ships' crew for a
     * trip length. The ship can only travel if the owner has a sufficient
     * amount of money.
     *
     * @param distance The distance the ship wants to sail.
     * @return Returns true if the owner can pay the crew wages, false otherwise.
     * @throws GameOver Thrown if there is no way for the player to pay their crew.
     */
    public boolean canPayCrew(int distance) throws GameOver {
        if (getCrewWages(distance) > owner.getBalance()) {
            checkCanAffordToSailAnywhere();
            return false;
        }
        return true;
    }


    /**
     * Checks if the player can afford to pay their crew to sail anywhere.
     * Throws game over if they cannot afford to sail anywhere with their current
     * crew. Note that this does not account for the player not having enough crew
     * to sail, but that that is still okay because if they cannot sail this this
     * much crew then they definitely cannot sail with more crew.
     *
     * @throws GameOver Thrown if the player cannot afford to sail anywhere.
     */
    private void checkCanAffordToSailAnywhere() throws GameOver {
        double totalValue = owner.getBalance();
        totalValue += owner.getShip().getCargoManager().getTotalCargoValue(dockedAt.getVendor());
        totalValue += owner.getShip().getTotalUpgradeValue(dockedAt.getVendor());

        GameWorld world = dockedAt.getWorld();
        for (Connection connection : world.getConnectionsOf(dockedAt)) {
            if (totalValue >= getCrewWages(connection.getDistance()))
                return;
        }

        throw new GameOver(GameOver.GameOverReason.BANKRUPTCY, owner,
                "You cannot possibly pay your crew!");
    }

    /**
     * How much it will cost for the crew to sail some distance.
     *
     * @param distance The distance the ship wants to sail.
     * @return Returns the wage of all crew members as double.
     */
    public double getCrewWages(int distance) {
        return this.getNumCrew() * IslandTraderConfig.CONFIG.getWageCostPerDay() * getSailTime(distance);
    }

    /**
     * How much it will cost for the crew to sail one day.
     *
     * @return Returns the wage of all crew members per day as double.
     */
    public double getCrewWagesPerDay() {
        return this.getNumCrew() * IslandTraderConfig.CONFIG.getWageCostPerDay();
    }

    /**
     * Pays the crew. Will when setting sail and by a button in the interface.
     * If wages are already paid, then it will not do anything.
     *
     * @param distance The distance the ship will sail.
     * @throws IllegalStateException Thrown if the ship attempts to sail and it cannot
     *                               pay the crew wages.
     * @throws GameOver              Thrown if there is no way for the player to pay their crew.
     */
    public void payCrew(int distance) throws IllegalStateException, GameOver {
        if (wagesPaid)
            return;
        if (!canPayCrew(distance))
            throw new IllegalStateException("Cannot pay crew!");
        wagesPaid = true;
        owner.removeFunds(getCrewWages(distance));
    }

    /**
     * Determines whether or not the player can hire a certain
     * number of crew members.
     *
     * @param moreCrew The number of crew the player wants to hire.
     * @return Returns true if the player can hire this much more crew,
     * false otherwise.
     */
    public boolean canHireCrew(int moreCrew) {

        if (getNumCrew() + moreCrew > attributes.getNumBeds())
            return false;

        if (owner.getBalance() < moreCrew * IslandTraderConfig.CONFIG.getHireCost())
            return false;

        if (getCurrentWeight() + moreCrew * IslandTraderConfig.CONFIG.getPersonWeight() > attributes.getMaxWeight())
            return false;

        return true;
    }

    /**
     * @return Returns a String representation of the Ship.
     */
    @Override
    public String toString() {
        return this.getName();
    }

    //* Fighter methods *//

    /**
     * Rolls a dice.
     *
     * @param roller      The dice roller.
     * @param isAttacking Whether or not we are rolling to attack.
     * @return Returns a random number based on the bounds of the roller.
     */
    @Override
    public int rollDice(DiceRoller roller, boolean isAttacking) {
        return roller.roll(isAttacking);
    }

    /**
     * Attacks another ship.
     *
     * @param context The context of this attack.
     */
    @Override
    public void attackShip(ShipAttackContext context) {
        Ship target = context.getTargetShip();
        target.onDamaged(context);
    }

    /**
     * Called when this ship is boarded by pirates.
     *
     * @param context The context of the attack that caused the boarding.
     * @return Returns a string representation of the outcome of this boarding.
     * @throws GameOver Thrown if the player is killed during this boarding.
     */
    @Override
    public String onBoarded(ShipAttackContext context) throws GameOver {

        PirateShip attackers = (PirateShip) context.getAttackingShip();
        int attackerBeds = attackers.getAttributes().getNumBeds() - attackers.getPeopleOnBoard();
        int hostages = Math.min(attackerBeds, new Random().nextInt(getNumCrew() / 2));
        attackers.addPrisoners(hostages);
        this.removeCrew(hostages);

        double looted = Math.min(attackers.getGoldHunger(), owner.getBalance());

        while (looted < attackers.getGoldHunger()) {
            if (getCargo().size() > 0) {
                CargoItem pillaged = getCargoManager().removeRandomItem();
                attackers.getCargoManager().addCargo(pillaged);
                looted += pillaged.getBaseValue();
            } else {
                String message = String.format(
                        "Your loot did not satisfy the %s pirates, so they took all of your cargo," +
                                "made your crew walk the plank, and destroyed your ship!",
                        attackers.getName()
                );
                throw new GameOver(GameOver.GameOverReason.KILLED_BY_PIRATES, owner, message);
            }
        }

        String message = String.format(
                "The %s Pirates boarded your ship. They took %.2f Doubloons worth of loot and %d prisoners!",
                attackers.getName(),
                looted,
                hostages
        );

        attackers.repairShip();

        return message;
    }

    /**
     * Gets the proper name of this ship.
     *
     * @return Returns the owner's name.
     */
    @Override
    public String getProperName() {
        return owner.getName();
    }
}

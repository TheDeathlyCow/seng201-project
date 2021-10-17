package nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.DiceRoller;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Ship;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * PirateShip class. A pirate ship "is-a" ship, but also has some
 * extra properties.
 * <p>
 * Note: In a pirate ship, the number of people on board is the same
 * as the number of prisoners.
 */
public class PirateShip extends Ship {

    /**
     * How much this pirate ship hungers for gold.
     */
    private final int goldHunger;
    /**
     * A list of attack phrases that will be prompted to the player
     * when they are attacked by pirates.
     */
    private final List<String> attackPhrases;

    /**
     * Constructor for PirateShips. Takes a name, a base upgrade, and base weight.
     * Sets the gold hunger to a random amount between 0 and 1000.
     *
     * @param name        The name of the ship, e.g. "Queen Anne's Revenge".
     * @param baseUpgrade The base upgrade of the ship. Must be able to be applied to the ship.
     * @param baseWeight  The base weight of the ship.
     */
    public PirateShip(final String name, final ShipUpgrade baseUpgrade, final int baseWeight) {
        super(name, baseUpgrade, baseWeight);
        this.goldHunger = new Random().nextInt(1000);
        this.attackPhrases = Arrays.asList("Arrgh!", "Yo ho ho and a bottle of rum!");

        int rand = new Random().nextInt(FileManager.getCargoPool().size());
        this.getCargoManager().addCargo(FileManager.getCargoPool().get(rand));
    }

    //* Pirate Ship-specific methods *//

    /**
     * @return Returns this pirate ship's gold hunger.
     */
    public int getGoldHunger() {
        return goldHunger;
    }

    /**
     * Adds a specified number of prisoners.
     *
     * @param morePrisoners Number of prisoners to add.
     * @return Returns true if the prisoners could be added, false otherwise.
     */
    public boolean addPrisoners(int morePrisoners) {
        return addPeople(morePrisoners);
    }

    /**
     * Removes a specified number of prisoners.
     *
     * @param lessPrisoners Number of prisoners to remove.
     * @return Returns true if the prisoners could be removed, false otherwise.
     */
    public boolean removePrisoners(int lessPrisoners) {
        return removePeople(lessPrisoners);
    }

    /**
     * @return Returns the number of prisoners on board.
     */
    public int getNumPrisoners() {
        return getPeopleOnBoard();
    }

    //* Fighter methods *//

    /**
     * Rolls a dice.
     *
     * @param roller      The dice roller.
     * @param isAttacking Whether or not we are rolling to attack.
     * @return Returns a random number between 1 and 6 (inclusive).
     */
    @Override
    public int rollDice(DiceRoller roller, boolean isAttacking) {
        return TravellingEvent.DICE_1_6.roll(isAttacking);
    }


    /**
     * Damages an enemy ship.
     *
     * @param context Context in which to attack a ship.
     */
    @Override
    public void attackShip(ShipAttackContext context) {
        Ship target = context.getTargetShip();
        target.onDamaged(context);
    }

    /**
     * Event method that is called when this ship is boarded.
     * Frees all of the prisoners and allows the opponent ship to take
     * all of the cargo on board.
     *
     * @param context Context in which this ship was attacked, causing it to be
     *                boarded.
     * @return Returns a status string detailing the outcome of this boarding.
     */
    @Override
    public String onBoarded(ShipAttackContext context) {
        Ship attacker = context.getAttackingShip();
        CargoManager attackerCargoManager = attacker.getCargoManager();
        double looted = 0.0;
        for (CargoItem cargoItem : this.getCargoManager().getCargo()) {
            if (attackerCargoManager.addCargo(cargoItem)) {
                looted += cargoItem.getBaseValue();
            }
        }

        int attackerAvailableBeds = attacker.getAttributes().getNumBeds() - attacker.getPeopleOnBoard();
        int rescued = Math.min(this.getNumPrisoners(), attackerAvailableBeds);
        attacker.addPeople(rescued);
        this.removePrisoners(rescued);

        String message = String.format(
                "You boarded the %s, taking %.2f worth of loot and rescuing %d prisoners!",
                getName(),
                looted,
                rescued
        );

        try {
            this.repairShip();
        } catch (GameOver ignored) {
        }

        int rand = new Random().nextInt(FileManager.getCargoPool().size());
        this.getCargoManager().addCargo(FileManager.getCargoPool().get(rand));

        return message;
    }

    /**
     * Returns the name of these pirates. For example,
     * the Black Beard Pirates.
     *
     * @return Returns the name of the pirates.
     */
    @Override
    public String getProperName() {
        return this.getName();
    }
}

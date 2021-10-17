package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;

import javax.swing.*;

/**
 * Stores methods for hiring crew.
 */
public class HireCrewDialog {
    /**
     * The player hiring crew.
     */
    private final Player player;
    /**
     * The parent component this hire crew is coming from.
     */
    private final JComponent parent;

    /**
     * Creates a new instance of hire crew dialog with a player and parent.
     *
     * @param player Player hiring crew
     * @param parent Parent component
     */
    public HireCrewDialog(Player player, JComponent parent) {
        this.player = player;
        this.parent = parent;
    }

    /**
     * Called when the player enters something into the dialog.
     *
     * @param input Input the player gave.
     */
    private void onEnter(String input) {
        if (InputValidator.isNumeric(input) && input.length() <= 6) {
            int amount = Integer.parseInt(input);
            hireCrew(amount);
        } else {
            JOptionPane.showMessageDialog(parent, "Invalid input!");
            this.showDialog();
        }
    }

    /**
     * Adds an amount of crew to the players ship, if possible.
     *
     * @param amount Amount of crew to hire.
     */
    private void hireCrew(int amount) {
        PlayerShip ship = player.getShip();
        if (ship.canHireCrew(amount)) {
            double cost = amount * IslandTraderConfig.CONFIG.getHireCost();
            ship.addCrew(amount);
            player.removeFunds(cost);
            JOptionPane.showMessageDialog(parent, String.format("You hired %d crew for %.2f Doubloons!", amount, cost));
        } else {
            int moreCrew = (int) (player.getBalance() / IslandTraderConfig.CONFIG.getHireCost());
            int bedsLeft = ship.getAttributes().getNumBeds() - ship.getNumCrew();
            if (moreCrew > bedsLeft) {
                moreCrew = bedsLeft;
            }
            String message = "<html>You cannot hire that many crew!<br/>";
            message += "You may only hire up to " + moreCrew + " more crew!";
            JOptionPane.showMessageDialog(parent, message);
            this.showDialog();
        }

    }

    /**
     * Shows the hire crew dialog.
     */
    public void showDialog() {
        String input = JOptionPane.showInputDialog(parent, "Enter the number of crew you would like to hire");
        if (input != null)
            this.onEnter(input);
    }
}

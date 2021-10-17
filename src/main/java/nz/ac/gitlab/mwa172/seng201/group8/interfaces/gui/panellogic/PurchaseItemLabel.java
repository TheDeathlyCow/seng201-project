package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GameScreen;

import javax.swing.*;

/**
 * Tradeable item label that allows the player to purchase
 * items.
 */
public class PurchaseItemLabel extends TradeableItemLabel {
    /**
     * Constructs a panel that displays information about a Tradeable
     * item and allows the player to make transactions.
     *
     * @param parent      The parent of this panel.
     * @param gameManager The game manager.
     * @param item        The item this label is displaying.
     * @param quantity    The quantity the vendor has of the given item.
     */
    protected PurchaseItemLabel(GameScreen parent, GameManager gameManager, Tradeable item, int quantity) {
        super(parent, gameManager, item, quantity, "Buy");
    }

    /**
     * Confirms that the player really wants to sell the item to the vendor,
     * and if they do, will attempt to make a transaction.
     */
    @Override
    protected void makeTransaction() {
        Vendor vendor = gameManager.currentlyDockedAt().getVendor();

        String message = String.format("Are you sure you want to purchase %s for %.2f Doubloons?",
                item.getName(), item.getValueAtVendor(vendor)
        );

        boolean normalBuy = parent.isNormalBuy();

        int selection = JOptionPane.OK_OPTION;

        if (normalBuy)
            selection = JOptionPane.showConfirmDialog(parent, message);

        boolean canBuy = player.canPurchase(item);
        if (selection == JOptionPane.OK_OPTION && canBuy) {
            player.purchase(item, vendor);
            parent.refresh();

            if (normalBuy)
                JOptionPane.showMessageDialog(parent,
                        String.format("You purchased %s for %.2f Doubloons!",
                                item.getName(), item.getValueAtVendor(vendor))
                );
        } else if (!canBuy) {
            JOptionPane.showMessageDialog(parent, "You cannot purchase that item!");
        }
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GameScreen;

import javax.swing.*;

/**
 * Tradeable item label that allows the player to sell
 * items.
 */
public class SellItemLabel extends TradeableItemLabel {
    /**
     * Constructs a panel that allows the player
     *
     * @param parent      The parent of this panel.
     * @param gameManager The game manager.
     * @param item        The item this label is displaying.
     * @param quantity    The quantity the vendor has of the given item.
     */
    protected SellItemLabel(GameScreen parent, GameManager gameManager, Tradeable item, int quantity) {
        super(parent, gameManager, item, quantity, "Sell");
    }

    /**
     * Confirms that the player really wants to sell the item to the vendor,
     * and if they do, will attempt to make a transaction.
     */
    @Override
    protected void makeTransaction() {
        Vendor vendor = gameManager.currentlyDockedAt().getVendor();

        boolean normalBuy = parent.isNormalBuy();

        String message = String.format("Are you sure you want to sell %s for %.2f Doubloons?",
                item.getName(), item.getValueAtVendor(vendor)
        );
        int selection = JOptionPane.OK_OPTION;

        if (normalBuy)
            selection = JOptionPane.showConfirmDialog(parent, message);

        boolean canSell = vendor.canPurchase(item);
        if (selection == JOptionPane.OK_OPTION && canSell) {
            player.sell(item, vendor);
            parent.refresh();

            if (normalBuy)
                JOptionPane.showMessageDialog(parent,
                        String.format("You sold %s for %.2f Doubloons!",
                                item.getName(), item.getValueAtVendor(vendor))
                );
        } else if (!canSell) {
            JOptionPane.showMessageDialog(parent, "You cannot sell that item!");
        }
    }
}

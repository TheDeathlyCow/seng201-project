package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GameScreen;

import javax.swing.*;
import java.awt.*;

/**
 * Stores a label and a buttons that relate to a single tradeable.
 * The label will have the name, cost, and quantity of the item in
 * the vendor or player's stock. The button will allow the player
 * to buy or sell 1 of that item.
 */
public abstract class TradeableItemLabel extends JPanel {

    /**
     * Parent of this route panel.
     */
    protected final GameScreen parent;
    /**
     * The tradeable this panel is displaying.
     */
    protected final Tradeable item;
    /**
     * How much of the item the vendor has.
     */
    protected final int quantity;
    /**
     * The player.
     */
    protected final Player player;
    /**
     * The game manager.
     */
    protected final GameManager gameManager;
    /**
     * Allows the player to buy an item.
     */
    private final JButton buyButton;
    /**
     * The label of the item.
     */
    private final JLabel itemLabel;
    /**
     * A button that allows the player to see the properties of
     * an item upgrade. Will only display if the item is a ship upgrade.
     */
    private JButton viewModifiers = null;

    /**
     * Constructs a panel that displays information about a Tradeable
     * item and allows the player to make transactions.
     *
     * @param parent      The parent of this panel.
     * @param gameManager The game manager.
     * @param item        The item this label is displaying.
     * @param quantity    The quantity the vendor has of the given item.
     * @param buttonLabel The label to apply to the button.
     */
    protected TradeableItemLabel(GameScreen parent, GameManager gameManager, Tradeable item, int quantity, String buttonLabel) {
        this.parent = parent;
        this.gameManager = gameManager;
        this.item = item;
        this.quantity = quantity;
        this.player = this.gameManager.getPlayer();
        this.itemLabel = new JLabel(getLabelText());
        this.buyButton = new JButton(buttonLabel);
        this.buyButton.addActionListener(e -> this.makeTransaction());

        this.setLayout(new FlowLayout());

        this.add(itemLabel);
        if (viewModifiers != null)
            this.add(viewModifiers);
        this.add(buyButton);

        Font font = itemLabel.getFont();
        font = new Font(font.getName(), Font.PLAIN, 16);
        itemLabel.setFont(font);
        buyButton.setFont(font);
        if (viewModifiers != null)
            viewModifiers.setFont(font);
    }

    /**
     * Creates and returns the text for the item label.
     *
     * @return Returns the text for the item label.
     */
    private String getLabelText() {
        Vendor vendor = gameManager.currentlyDockedAt().getVendor();

        String itemName = item.toString();
        if (item instanceof ShipUpgrade) {
            itemName = item.getName();
            viewModifiers = new JButton("View Upgrade");
            viewModifiers.addActionListener(e ->
                    JOptionPane.showMessageDialog(parent, item.toString(), "Ship Upgrade Properties", JOptionPane.PLAIN_MESSAGE)
            );
        }

        String label = String.format("%s x%d (%.2f Doubloons)",
                itemName, quantity, item.getValueAtVendor(vendor)
        );

        label = label.replace("\n", "<br/>");

        return label;
    }

    /**
     * Transacts the item between the player and the vendor.
     */
    protected abstract void makeTransaction();
}

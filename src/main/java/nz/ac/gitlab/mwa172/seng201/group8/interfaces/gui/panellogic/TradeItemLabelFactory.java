package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GameScreen;

/**
 * Creates and returns concrete implementations of tradeable item labels.
 */
public class TradeItemLabelFactory {

    /**
     * Parent of the label.
     */
    private GameScreen parent;
    /**
     * The game the label is displaying for.
     */
    private GameManager gameManager;
    /**
     * The item the label is displaying.
     */
    private Tradeable item;
    /**
     * The quantity of the item the label is displaying.
     */
    private int quantity;

    /**
     * Creates a new factory for tradeable item labels.
     *
     * @param parent The parent of the label.
     * @param gameManager The game the label is displaying for.
     * @param item The item the label is displaying/
     * @param quantity The quantity of the item the label is displaying.
     */
    public TradeItemLabelFactory(GameScreen parent, GameManager gameManager, Tradeable item, int quantity) {
        this.parent = parent;
        this.gameManager = gameManager;
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Creates a new sell label with the factory's properties.
     *
     * @return Returns a sell item label.
     */
    public SellItemLabel makeSellLabel() {
        return new SellItemLabel(parent, gameManager, item, quantity);
    }

    /**
     * Creates a new purchase label with the factory's properties.
     *
     * @return Returns a purchase item label.
     */
    public PurchaseItemLabel makePurchaseLabel() {
        return new PurchaseItemLabel(parent, gameManager, item, quantity);
    }

    /**
     * Interface that can be implemented as a lambda function to be passed into methods.
     */
    public interface FactoryGetter {
        /**
         * Returns the result of one of the factory methods from the
         * passed in factory.
         *
         * @param factory Factory of the label to get.
         * @return Returns a new tradeable item label.
         */
        TradeableItemLabel getLabel(TradeItemLabelFactory factory);
    }

}

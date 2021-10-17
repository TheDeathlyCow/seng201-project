package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui;

import nz.ac.gitlab.mwa172.seng201.group8.Main;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.AssetManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Island;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Primary screen in which interactions with the GUI version of this application
 * take place. Contains several tabs with different panels and labels for the different
 * locations, such as the island, docks, and vendor.
 */
public class GameScreen extends JFrame {
    private JTabbedPane locations;
    private JPanel content;
    private JLabel daysRemaining;
    private JPanel islandPanel;
    private JPanel vendorPanel;
    private JPanel docksPanel;
    private JLabel welcomeLabel;
    private JButton hireCrewButton;
    private JButton viewCargoButton;
    private JLabel docksLabel;
    private JLabel shipLabel;
    private JPanel routes;
    private JLabel balanceLabel;
    private JButton repairShipButton;
    private JLabel vendorLabel;
    private JPanel playerStock;
    private JPanel vendorStock;
    private JScrollPane vendorScroll;
    private JScrollPane playerScroll;
    private JScrollPane routeScroll;
    private JScrollPane shipScroll;
    private JCheckBox quickBuy;
    private JLabel greetingLabel;
    private GuiApp app;

    /**
     * Constructs a game screen with a title and a gui app.
     *
     * @param title Title of the window.
     * @param app   Instance of the GuiApp to communicate with the game logic.
     */
    public GameScreen(String title, GuiApp app) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(content);
        this.app = app;
        this.setIconImage(AssetManager.ICON.getImage());
        this.pack();
        this.setLocationRelativeTo(null);
        locations.addChangeListener((e) -> refresh());

        int scrollSpeed = 12;

        vendorScroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        playerScroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        routeScroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        shipScroll.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
    }

    /**
     * @return Returns the game manager.
     */
    public GameManager getGame() {
        return app.getGameManager();
    }

    /**
     * Refreshes the window.
     */
    public void refresh() {
        updateTopBar();
        updateIslandPanel();
        updateVendorPanel();
        updateDocksPanel();
    }

    /**
     * Sets the current tab to the island tab and refreshes the window.
     */
    public void onArrived() {
        this.locations.setSelectedIndex(0);
        refresh();
    }

    /**
     * Updates the top bar with the current days remaining.
     */
    private void updateTopBar() {
        GameManager gameManager = app.getGameManager();
        GameWorld world = gameManager.getWorld();

        daysRemaining.setText("<html><center>" +
                "<p style='margin:7px;'> Days Remaining: " + world.getDaysRemaining() +
                "</p></center></html>"
        );
    }

    /**
     * Updates the island panel.
     */
    private void updateIslandPanel() {
        Player player = app.getGameManager().getWorld().getPlayer();
        Island dockedAt = player.getShip().dockedAt();
        welcomeLabel.setText("<html>" +
                "<style>div.welcome { margin: 30px;} </style>" +
                "<center><div class='welcome'><p>" +
                "Welcome to " + dockedAt.getName() + ", " + player.getName() + "!" +
                "</p></div></center></html>"
        );
    }

    /**
     * Updates the vendor panel.
     */
    private void updateVendorPanel() {
        Player player = app.getGameManager().getWorld().getPlayer();
        Island dockedAt = player.getShip().dockedAt();
        Vendor vendor = player.getShip().dockedAt().getVendor();
        balanceLabel.setText(String.format("<html>" +
                        "<style>div.balance { margin: 30px;} </style>" +
                        "<center><div class='balance'><p>" +
                        "Your balance: %.2f Doubloons" +
                        "</p><p>" +
                        "Vendor balance: %.2f Doubloons" +
                        "</p></div></center></html>",
                player.getBalance(),
                vendor.getBalance()
        ));

        greetingLabel.setText(vendor.getRandomGreeting(player));

        vendorLabel.setText("<html><h2 style='margin: 7px;'>" +
                dockedAt.getName() + " Vendor" +
                "</h2></html>");
        updateVendorStock();
    }

    /**
     * Updates the docks panel.
     */
    private void updateDocksPanel() {
        Player player = app.getGameManager().getWorld().getPlayer();
        Island dockedAt = player.getShip().dockedAt();
        docksLabel.setText("<html><center><h3 style='margin: 7px;'>" +
                dockedAt.getName() + " Docks" +
                "</h3></center></html>");
        shipLabel.setText(ViewShipGUI.getStatusString(player.getShip()));
        updateRoutes();
    }

    /**
     * Updates the vendor stock.
     */
    private void updateVendorStock() {
        GameWorld world = app.getGameManager().getWorld();
        Player player = world.getPlayer();
        Vendor vendor = player.getShip().dockedAt().getVendor();

        List<Tradeable> fullVendorStock = new ArrayList<>();
        fullVendorStock.addAll(vendor.getCargoStock());
        fullVendorStock.addAll(vendor.getUpgradeStock());

        vendorStock = updateStockPanel(vendorStock, fullVendorStock, TradeItemLabelFactory::makePurchaseLabel);

        List<Tradeable> fullPlayerCargo = new ArrayList<>();
        fullPlayerCargo.addAll(player.getShip().getCargo());
        fullPlayerCargo.addAll(player.getShip().getUpgrades());

        playerStock = updateStockPanel(playerStock, fullPlayerCargo, TradeItemLabelFactory::makeSellLabel);
    }

    /**
     * Updates the routes panel.
     */
    private void updateRoutes() {
        routes.removeAll();
        routes.setLayout(new BoxLayout(routes, BoxLayout.Y_AXIS));
        GameWorld world = app.getGameManager().getWorld();
        Player player = world.getPlayer();
        Island island = player.getShip().dockedAt();
        List<Connection> connections = world.getConnectionsOf(island);

        for (Connection connection : connections) {
            RoutePanel panel = new RoutePanel(this, app, connection);
            routes.add(panel, BorderLayout.PAGE_END);
        }

    }

    /**
     * Takes a panel of stock items and returns an updated version of it.
     *
     * @param panel     JPanel to update.
     * @param fullStock The full stock to be displayed on that panel.
     * @param getter    Label factory getter that tells the label what type it is.
     * @return Returns the updated panel.
     */
    public JPanel updateStockPanel(JPanel panel, List<Tradeable> fullStock, TradeItemLabelFactory.FactoryGetter getter) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Map<Tradeable, Integer> stock = getStockWithQuantities(fullStock);

        for (Tradeable item : stock.keySet()) {
            int quantity = stock.get(item);
            TradeItemLabelFactory factory = new TradeItemLabelFactory(this, getGame(), item, quantity);
            TradeableItemLabel stockItem = getter.getLabel(factory);
            panel.add(stockItem, BorderLayout.PAGE_END);
        }
        return panel;
    }

    /**
     * Maps items in a given list to their count within that list.
     *
     * @param stock List to count.
     * @param <E>   Type of item that is being counted.
     * @return Returns a linked hash map of the items mapped to their quantities.
     */
    private static <E> Map<E, Integer> getStockWithQuantities(List<? extends E> stock) {
        Map<E, Integer> map = new LinkedHashMap<>();
        for (E item : stock) {
            Integer quantity = map.get(item);
            if (quantity == null)
                quantity = 0;
            map.put(item, quantity + 1);
        }
        return map;
    }

    /**
     * Initiates repair ship dialog.
     */
    private void repairShip() {
        Player player = app.getGameManager().getWorld().getPlayer();
        PlayerShip ship = player.getShip();

        if (ship.getHealth() >= ship.getAttributes().getMaxHealth()) {
            JOptionPane.showMessageDialog(this, "You do not need to repair your ship!");
            return;
        }

        String repairConfirm = String.format("Repair ship for %.2f Doubloons?", ship.getRepairCost());
        int confirmation = JOptionPane.showConfirmDialog(this, repairConfirm);

        if (confirmation == JOptionPane.OK_OPTION) {
            double repairCost = ship.getRepairCost();
            boolean repaired = false;
            try {
                repaired = ship.repairShip();
            } catch (GameOver over) { app.gameOver(over); }

            String message = String.format("You repaired your ship for %.2f Doubloons!", repairCost);
            if (!repaired)
                message = "You could not repair your ship!";
            JOptionPane.showMessageDialog(this, message);
        }
    }

    /**
     * Initiates view cargo dialog.
     */
    private void viewCargo() {
        Player player = app.getGameManager().getWorld().getPlayer();
        PlayerShip ship = player.getShip();

        List<Tradeable> items = new ArrayList<>();
        items.addAll(ship.getCargo());
        items.addAll(ship.getSoldHistory());
        if (items.size() == 0) {
            JOptionPane.showMessageDialog(this, "You have not purchased any cargo yet!");
            return;
        }

        StringBuilder cargoStringBuilder = new StringBuilder();
        cargoStringBuilder.append("<html><style>.item {margin: 3px;}</style><div class=item>");
        for (Tradeable item : items) {
            cargoStringBuilder.append("<p>");
            cargoStringBuilder.append(String.format("- Paid %.2f Doubloons for %s",
                    item.getPurchaseValue(), item.getName()
            ));
            if (item.isSold())
                cargoStringBuilder.append(String.format(", sold for %.2f Doubloons on %s",
                        item.getSoldValue(), item.getSoldAt().getName()
                ));
            cargoStringBuilder.append("</p>");
        }
        cargoStringBuilder.append("</div></html>");

        JLabel cargoLabel = new JLabel(cargoStringBuilder.toString());
        JScrollPane scrollPane = new JScrollPane(cargoLabel);
        scrollPane.setPreferredSize(new Dimension(500, 500));

        JOptionPane.showMessageDialog(this, scrollPane);
    }

    /**
     * Custom creates UI components.
     */
    private void createUIComponents() {
        hireCrewButton = new JButton();
        hireCrewButton.addActionListener(e -> hireCrewListener());

        viewCargoButton = new JButton();
        viewCargoButton.addActionListener(e -> viewCargo());

        repairShipButton = new JButton();
        repairShipButton.addActionListener(e -> this.repairShip());

        vendorStock = new JPanel();
        playerStock = new JPanel();
    }

    /**
     * Initiates hire crew dialog.
     */
    private void hireCrewListener() {
        Player player = app.getGameManager().getWorld().getPlayer();
        if (!player.getShip().canHireCrew(1)) {
            JOptionPane.showMessageDialog(this, "You cannot hire any more crew!");
            return;
        }

        HireCrewDialog dialog = new HireCrewDialog(player, content);
        dialog.showDialog();
    }

    /**
     * Whether or not transactions should be made without any dialog boxes.
     *
     * @return Returns false if the quickbuy check box is selected.
     */
    public boolean isNormalBuy() {
        return !quickBuy.isSelected();
    }

    /**
     * Runs the application in debug mode. Skips directly to game screen, handling
     * setup automatically.
     *
     * @param args Runtime args.
     * @throws Exception Thrown if there is an exception.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Debug Mode Enabled");
        GameManager gameManager = Main.debugSetup();
        GuiApp app = new GuiApp();
        app.setGameManager(gameManager);
        app.getMainScreen().setVisible(true);
        app.getMainScreen().onArrived();
    }
}

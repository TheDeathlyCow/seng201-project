package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.startup;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.AssetManager;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GuiApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic.ViewShipGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Setup screen. Lets the player input their name, select num days,
 * enter a seed, and select a ship.
 */
public class StartupScreen extends JFrame {
    public static String name;
    private JPanel setupScreenPanel;
    private JLabel nameJLabel;
    private JLabel welcomeJLabel;
    private JTextField nameTextField;
    private JSlider gameLengthSlider;
    private JLabel gameLengthJLabel;
    private JLabel selectedShipLabel;
    private JButton traderNameConfirmButton;
    private JButton gameLengthConfirmButton;
    private JComboBox<PlayerShip> shipDropDown;
    private PlayerShip selectedShip;
    private JButton startGameButton;
    private JTextField worldSeedTextField;
    private JLabel worldSeedJLabel;
    private JLabel descLabel;

    /**
     * World seed.
     */
    private long seed;
    /**
     * The GUI application.
     */
    private GuiApp app;

    /**
     * Creates a new startup screen, but does not display it.
     * @param title Title of the screen.
     * @param app GUI application.
     */
    public StartupScreen(String title, GuiApp app) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(setupScreenPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        startGameButton.addActionListener(this::startGameListener);
        this.selectShipListener(null);
        this.app = app;
        setIconImage(AssetManager.ICON.getImage());
    }

    /**
     * Reads and sets the seed from the seed input field.
     */
    private void setSeed() {
        final String seedString = worldSeedTextField.getText();
        if (seedString == null || seedString.length() == 0) {
            seed = new Random().nextLong();
        } else {
            try {
                seed = Long.parseLong(seedString);
            } catch (NumberFormatException e) {
                seed = seedString.hashCode();
            }
        }
    }

    /**
     * Creates a new game and sets that game in the GUI application.
     */
    private void createGame() {

        String playerName = nameTextField.getText();
        int totalDays = gameLengthSlider.getValue();

        Player player = new Player(playerName);
        player.setShip(this.selectedShip);

        GameWorld world = new GameWorld(player, totalDays, seed);
        world.generateIslands();

        app.setGameManager(new GameManager(world));

    }

    /**
     * Custom creates the UI components.
     */
    private void createUIComponents() {
        shipDropDown = new JComboBox<>(FileManager.getShips().toArray(new PlayerShip[0]));
        shipDropDown.addActionListener(this::selectShipListener);
    }

    /**
     * Reads all the information input and validates it. If the information is valid,
     * then creates and starts the game.
     * @param e Action event.
     */
    private void startGameListener(ActionEvent e) {
        setSeed();
        boolean isValid = CreateGameValidator.isValid(nameTextField, gameLengthSlider);
        if (isValid) {
            createGame();
            this.setVisible(false);
            try {
                TimeUnit.MILLISECONDS.sleep(325);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            app.getMainScreen().refresh();
            app.getMainScreen().setVisible(true);
            this.dispose();
        }
    }

    /**
     * Updates the ship info panel when a ship is selected.
     *
     * @param event Action event
     */
    private void selectShipListener(ActionEvent event) {
        selectedShip = (PlayerShip) shipDropDown.getSelectedItem();
        if (selectedShip != null)
            selectedShipLabel.setText(ViewShipGUI.getHTMLDescription(selectedShip));
        else
            selectedShipLabel.setText("ERROR: No ship selected!");
    }
}
package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.ShipAttackContext;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Connection;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Ship;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GameScreen;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GuiApp;

import javax.swing.*;
import java.awt.*;

/**
 * Stores a label and two buttons that relate to a single route.
 * The label will contain the destination island's name, the first
 * button will show a dialog box that shows the user information about
 * the view, including any hazards, and the third button allows the user
 * to sail that route.
 */
public class RoutePanel extends JPanel {

    /**
     * Parent of this route panel.
     */
    private GameScreen parent;
    /**
     * The route this panel is displaying.
     */
    private Connection route;
    /**
     * The player.
     */
    private Player player;
    /**
     * The app this game is running in, primarily for dealing with game over.
     */
    private GuiApp app;
    /**
     * Allows the player to sail a route.
     */
    private JButton sailButton;
    /**
     * Shows the player any hazards along the route.
     */
    private JButton viewEventsButton;
    /**
     * The label of the destination.
     */
    private JLabel destinationLabel;


    /**
     * Constructs a route panel with a parent, game manager, and particular route.
     *
     * @param parent The parent of this panel.
     * @param app    The game app.
     * @param route  The route this label is displaying.
     */
    public RoutePanel(GameScreen parent, GuiApp app, Connection route) {
        this.parent = parent;
        this.app = app;
        this.route = route;
        this.player = parent.getGame().getPlayer();
        this.destinationLabel = new JLabel(route.getSailingLabel(player.getShip()));
        this.viewEventsButton = new JButton("View Route");
        this.viewEventsButton.addActionListener(e -> this.viewEvents());
        this.sailButton = new JButton("Sail!");
        this.sailButton.addActionListener(e -> this.sail());

        this.setLayout(new FlowLayout());

        this.add(destinationLabel);
        this.add(viewEventsButton);
        this.add(sailButton);

        Font font = destinationLabel.getFont();
        font = new Font(font.getName(), Font.PLAIN, 18);
        destinationLabel.setFont(font);
        viewEventsButton.setFont(font);
        sailButton.setFont(font);
    }

    /**
     * Shows the player a dialog box with the description of events and hazards
     * along the route.
     */
    public void viewEvents() {

        String message = "<html><h3>" +
                "The vendor on has the following desire for different types of cargo:" +
                "</h3></html>\n" +
                route.getDestination().getVendor().getDesireDescription();
        message += "<html><br/></html>";
        message += route.getDescription();

        JOptionPane.showMessageDialog(parent, message);
    }

    /**
     * Confirms that the player actually wants to sail the route, then
     * Attempts to sail the route. If the player can sail the route, then
     * they will be shown any information relating to events that occured
     * during their travel. If they cannot sail the route, then they will
     * not be able to. If the game ends while they sail the route, this method
     * will also handle that.
     */
    public void sail() {

        String confirmMsg = String.format("Are you sure you want to travel to %s?", route.getDestination().getName());
        int confirm = JOptionPane.showConfirmDialog(parent, confirmMsg);

        if (confirm != JOptionPane.OK_OPTION)
            return;

        try {
            TravellingEvent<?> event = player.getShip().travel(route);
            String toDisplay = "You safely made it to the island of " + route.getDestination().getName();
            if (event != null) {
                toDisplay = runEvent(event);
            }
            parent.onArrived();
            JOptionPane.showMessageDialog(parent, toDisplay, "Island Trader", JOptionPane.ERROR_MESSAGE);
        } catch (GameOver over) {
            app.gameOver(over);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(parent, "You cannot sail this route!", "Island Trader", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Runs a given event. If the event is null, returns an empty string.
     *
     * @param event The event to run.
     * @return Returns the status string describing the outcome of the event.
     * If the event is null, returns an empty string.
     * @throws GameOver Thrown if the event ends the game.
     */
    private String runEvent(TravellingEvent<?> event) throws GameOver {

        if (event == null)
            return "";

        GuiDiceRoller roller = new GuiDiceRoller(event, TravellingEvent.DICE_1_6, parent);
        String status = event.runEvent(this.player.getShip(), roller, this::sendBattleFeedback);

        roller.dispose();

        return status;
    }

    /**
     * Sends feedback to the player after an attack.
     *
     * @param context Context of the attack.
     */
    private void sendBattleFeedback(ShipAttackContext context) {

        String message = "<html><p style = 'margin:7px;'>";
        message += context.getAttackMessage() + "<br><br>";

        Ship attacker = context.getAttackingShip();
        Ship target = context.getTargetShip();

        message += attacker.getProperName() + " health: " + attacker.getHealth() + "<br>";
        message += target.getProperName() + " health: " + target.getHealth();

        message += "</p></html>";

        JOptionPane.showMessageDialog(parent, message);
    }
}

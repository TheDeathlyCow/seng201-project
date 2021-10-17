package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.panellogic;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.AssetManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.DiceRoller;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.GameScreen;

import javax.swing.*;

/**
 * Dialog box that handles dice rolling for GUIs.
 * This dialog box cannot be closed normally, it will
 * only close once the roll button is clicked.
 */
public class GuiDiceRoller extends JDialog implements DiceRoller {
    /**
     * Content pane of the dialog box.
     */
    private JPanel contentPane;
    /**
     * Button the user presses to roll the dice.
     */
    private JButton rollButton;
    /**
     * A label that describes what is happening.
     */
    private JLabel attackDesc;
    /**
     * The even being run. Should always be a pirate attack for this class.
     */
    private TravellingEvent<?> event;
    /**
     * The default dice roller of this event. Should be something simple that just returns a
     * random number and nothing else.
     */
    private final DiceRoller dice;
    /**
     * The primary game screen that this dialog box will display relative to.
     */
    private final GameScreen parent;

    /**
     * Creates the dialog box with a custom dice roller, but does not display it until
     * the game logic needs a dice to be rolled.
     *
     * @param event Event being run.
     * @param dice The default dice roller.
     * @param parent The parent window of this dialog.
     */
    public GuiDiceRoller(TravellingEvent<?> event, DiceRoller dice, GameScreen parent) {
        this.event = event;
        this.dice = dice;
        this.parent = parent;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(rollButton);

        rollButton.addActionListener(e -> close());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setIconImage(AssetManager.ICON.getImage());
    }

    /**
     * Closes this dialog box when the player rolls a dice.
     */
    private void close() {
        this.setVisible(false);
    }

    /**
     * Shows this dialog box and waits until the user presses roll.
     *
     * @param isAttacking Determines whether or not this dice roll is for attacking or defending.
     * @return Returns the roll of this dice, the bounds of which will be determined by the dice
     * property of this class, but by default is a random number between 1 and 6 (inclusive).
     */
    @Override
    public int roll(boolean isAttacking) {
        String desc = String.format("<html><p style='margin: 14px;'>" +
                        "You have been attacked by the %s! Roll a dice to %s!" +
                        "</p></html>",
                event.getProperName(),
                isAttacking ? "attack" : "defend"
        );
        this.attackDesc.setText(desc);
        this.pack();
        this.setVisible(true);
        int roll = this.dice.roll(isAttacking);
        JOptionPane.showMessageDialog(parent, "You rolled at " + roll);
        return roll;
    }
}

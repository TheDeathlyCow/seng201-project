package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.AssetManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.IslandTraderApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.startup.StartupScreen;

import javax.swing.*;
import java.awt.*;

/**
 * Basic class for the GUI application.
 */
public class GuiApp extends IslandTraderApp {

    /**
     * The main game screen.
     */
    private final GameScreen MAIN_SCREEN;

    /**
     * Creates a new gui application.
     */
    public GuiApp() {
        MAIN_SCREEN = new GameScreen("Island Trader", this);
    }

    /**
     * Starts the gui application by showing the setup screen.
     */
    @Override
    public void start() {
        StartupScreen startup = new StartupScreen("Island Trader Setup", this);
        startup.setVisible(true);
    }

    /**
     * Ends the game.
     *
     * @param context Context in which the game ended.
     */
    @Override
    public void gameOver(GameOver context) {
        StringBuilder message = new StringBuilder("<html><style>over{ margin: 5px; }</style><div class='over'>");
        message.append("<h2>Game Over!</h2>");
        message.append("<h3>Reason: ").append(context.getGameOverReason()).append("</h3>");
        if (context.isMaxScore())
            message.append("<h3>New high score!</h3>");
        message.append("<br/>");
        if (!context.getMessage().equals(""))
            message.append("<p>").append(context.getMessage()).append("</p><p></p>");
        message.append("<p>Days elapsed: ").append(context.getDaysElapsed()).append("</p>");
        message.append("<p>Final Balance: ").append(String.format("%.2f", context.getEndBalance())).append("</p>");
        message.append("<p>Final Score: ").append(context.getScore()).append("</p>");


        Image icon = AssetManager.ICON.getImage();
        Image scaledIcon = icon.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        message.append("</div></html>");
        JOptionPane.showMessageDialog(MAIN_SCREEN,
                message.toString(),
                "Game Over!",
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon(scaledIcon)
        );

        getGameManager().gameOver(context);
    }

    /**
     * @return Returns the main game screen.
     */
    public GameScreen getMainScreen() {
        return MAIN_SCREEN;
    }

}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.DiceRoller;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.combat.events.TravellingEvent;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.IslandTraderApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.AbstractCommand;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands.IslandCommand;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands.IslandCommands;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class IslandTraderCommandApp extends IslandTraderApp {

    static final String lineBreak = "========================";

    public static final DiceRoller DICE_ROLLER_1_6 = (isAttacking) -> {
        String rollInput = getInput(
                "Press Enter to roll a dice.",
                (input) -> true
        );
        return TravellingEvent.DICE_1_6.roll(isAttacking);
    };

    private final IslandTraderCommandStartup startup;

    public IslandTraderCommandApp() {
        this.startup = new IslandTraderCommandStartup();
    }

    @Override
    public void start() {
        super.setGameManager(startup.start());
        Player player = getGameManager().getWorld().getPlayer();
    }

    @Override
    public void gameOver(GameOver over) {
        System.out.println("Game over!");
        System.out.println("Reason: " + over.getGameOverReason().toString());
        getGameManager().gameOver(over);
    }

    public void run() {

        boolean exit = false;
        do {
            GameManager manager = getGameManager();
            GameWorld world = manager.getWorld();
            Player player = world.getPlayer();
            PlayerShip ship = player.getShip();
            System.out.println('\n' + lineBreak);
            System.out.println("Days remaining: " + world.getDaysRemaining());
            System.out.printf("Current balance: %.2f%n", player.getBalance());
            System.out.printf("Docked at: %s%n", ship.dockedAt().getName());

            System.out.println(lineBreak);
            IslandCommand command = IslandCommands.promptInput();
            System.out.println();
            CommandContext context = new CommandContext.Builder()
                    .withPlayer(player)
                    .build();

            CommandResult result = null;
            try {
                result = command.run(context);
            } catch (GameOver over) {
                this.gameOver(over);
            }

            int sleepTime = result.getWait();
            if (sleepTime < 0) {
                exit = true;
            }
        } while(!exit);
    }

    public static String getInput(String message, InputValidator validator) {
        String input;
        boolean validated;
        do {
            Scanner inScanner = new Scanner(System.in);
            System.out.print(message);
            input = inScanner.nextLine();
            validated = validator.isValid(input);
        } while (!isValidInput(validated));

        return input;
    }

    private static boolean isValidInput(boolean isValid) {
        if (isValid)
            return true;
        System.out.println("Invalid entry, please try again.");
        return false;
    }
}

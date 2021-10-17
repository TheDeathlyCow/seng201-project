package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.FileManager;
import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameManager;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.PlayerShip;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.ShipUpgrade;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp.getInput;

class IslandTraderCommandStartup {

    private boolean started = false;

    GameManager start() {
        assert !started;
        started = true;
        GameManager game = createNewGame();
        return game;
    }

    private GameManager loadGame() {
        return createNewGame();
    }

    private GameManager createNewGame() {
        Player player = createPlayer();
        GameWorld world = createWorld(player);
        world.generateIslands();
        PlayerShip ship = getStartingShip(world);
        player.setShip(ship);
        return new GameManager(world);
    }

    private Player createPlayer() {
        InputValidator validator = InputValidator.ASCII_NAME_VALIDATOR;

        if (IslandTraderConfig.CONFIG.isUsingUnicode()) {
            validator = InputValidator.UNICODE_NAME_VALIDATOR;
        }

        final String traderName = getInput("Enter player name (3-15 characters, letters and spaces only): ",
                validator);

        return new Player(traderName);
    }

    private PlayerShip getStartingShip(GameWorld world) {
        String promptMessage = "What ship would you like to captain?\n";
        promptMessage += "Available ships: ";
        StringJoiner joiner = new StringJoiner(", ");
        for (PlayerShip ship : FileManager.getShips())
            joiner.add(ship.getName());
        promptMessage += joiner.toString() + "\n";

        String selectedName = getInput(
                promptMessage,
                (String input) -> {
                    for (PlayerShip ship : FileManager.getShips()) {
                        if (input.equalsIgnoreCase(ship.getName())) {
                            return true;
                        }
                    }
                    return false;
                }
        );

        PlayerShip chosen = FileManager.getShips().stream()
                .filter(ship -> ship.getName().equalsIgnoreCase(selectedName))
                .collect(Collectors.toList())
                .get(0);

        return chosen;
    }

    private GameWorld createWorld(Player player) {
        final int totalDays = getTotalDays();
        final long seed = getSeed();
        return new GameWorld(player, totalDays, seed);
    }

    private int getTotalDays() {
        final String message = String.format("How many days do you want the game to last (%d-%d)?: ",
                GameWorld.MIN_DAYS,
                GameWorld.MAX_DAYS);
        final String totalDaysString = getInput(message,
                (String input) -> {
                    if (!InputValidator.isNumeric(input))
                        return false;
                    int days = Integer.parseInt(input);
                    return days >= 20 && days <= 50;
                });

        return Integer.parseInt(totalDaysString);
    }

    private long getSeed() {
        final String seedString = getInput("Enter a world seed (optional): ", (String input) -> true);
        if (seedString.length() > 0)
            return seedString.hashCode();
        return new Random().nextLong();
    }
}

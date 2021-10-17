package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandRegistry;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles a register of all island commands.
 * Each command can has a static instance that can
 * be referenced outside of this class.
 */
public class IslandCommands implements CommandRegistry<IslandCommand> {

    /**
     * The registry of island commands.
     */
    public static final IslandCommands REGISTRY = new IslandCommands();
    /**
     * List of commands in the registry.
     */
    private final List<IslandCommand> commands = new ArrayList<>();

    /**
     * The view ship command.
     */
    public static final ViewShipCommand VIEW_SHIP;
    /**
     * The view cargo command.
     */
    public static final ViewCargoCommand VIEW_CARGO;
    /**
     * The visit docks command.
     */
    public static final VisitDocksCommand VISIT_DOCKS;
    /**
     * The visit vendor command.
     */
    public static final VisitVendorCommand VISIT_VENDOR_COMMAND;
    /**
     * The hire crew command.
     */
    public static final HireCrewCommand HIRE_CREW;
    /**
     * The exit command.
     */
    public static final ExitCommand EXIT;

    // Initialise all commands on load.
    static {
        VIEW_SHIP = REGISTRY.register(new ViewShipCommand());
        VIEW_CARGO = REGISTRY.register(new ViewCargoCommand());
        VISIT_DOCKS = REGISTRY.register(new VisitDocksCommand());
        VISIT_VENDOR_COMMAND = REGISTRY.register(new VisitVendorCommand());
        HIRE_CREW = REGISTRY.register(new HireCrewCommand());
        EXIT = REGISTRY.register(new ExitCommand());
    }

    /**
     * Only the above registry may instantiate this class.
     */
    private IslandCommands() {
    }

    /**
     * Prompts the user to enter an island command.
     *
     * @return Returns a valid command entered by the user.
     */
    public static IslandCommand promptInput() {

        System.out.println("Available Commands:");
        REGISTRY.commands.forEach(
                (command) -> {
                    System.out.printf("- %s%n", command.toString());
                }
        );

        CommandContext context = new CommandContext.Builder()
                .build();

        String selectActionString = IslandTraderCommandApp.getInput(
                "What would you like to do? ",
                (input) -> REGISTRY.isCommand(context, input)
        );

        return REGISTRY.getCommand(context, selectActionString);
    }

    /**
     * Adds a command to the registry, then returns
     * that same command.
     *
     * @param command Command to be registered.
     * @param <C>     The class of the command to be registered.
     *                Must be a subclass of island command
     * @return Returns the command passed into this method.
     */
    @Override
    public <C extends IslandCommand> C register(C command) {
        commands.add(command);
        return command;
    }

    /**
     * Gets an unmodifiable list of the commands in the
     * registry.
     *
     * @return Returns an unmodifiable list of the commands in the
     * registry.
     */
    @Override
    public List<IslandCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    /**
     * Determines if the input string matches the pattern for any command
     * in the registry.
     *
     * @param context The context in which the command is attempting to be executed.
     * @param input The input string to check.
     * @return Returns true if the command matches any command in the registry,
     * false otherwise.
     */
    @Override
    public boolean isCommand(CommandContext context, String input) {
        return commands.stream()
                .anyMatch(command -> command.isCommand(context, input));
    }

    /**
     * Gets the first matching command of an input string.
     *
     * @param context The context in which the command is attempting to be executed.
     * @param input Input string to get the command of.
     * @return Returns the first match of the input string to a command in the registry.
     * If no matches can be found, returns null.
     */
    @Override
    public IslandCommand getCommand(CommandContext context, String input) {
        return commands.stream()
                .filter(command -> command.isCommand(context, input))
                .findFirst()
                .orElse(null);
    }
}

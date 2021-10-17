package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands;

import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TravelCommands implements CommandRegistry<TravelCommand> {

    public static final TravelCommands REGISTRY = new TravelCommands();
    private final List<TravelCommand> commands = new ArrayList<>();

    public static final SeeRoutesCommand SEE_ROUTES;
    public static final SetSailCommand SET_SAIL;
    public static final TravelReturnCommand RETURN;

    static {
        SEE_ROUTES = REGISTRY.register(new SeeRoutesCommand());
        SET_SAIL = REGISTRY.register(new SetSailCommand());
        RETURN = REGISTRY.register(new TravelReturnCommand());
    }

    private TravelCommands() {
    }

    public TravelCommand getEntryCommand() {
        return SEE_ROUTES;
    }

    @Override
    public <C extends TravelCommand> C register(C command) {
        commands.add(command);
        return command;
    }

    @Override
    public List<TravelCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public boolean isCommand(CommandContext context, String input) {
        return commands.stream()
                .anyMatch(command -> command.isCommand(context, input));
    }

    @Override
    public TravelCommand getCommand(CommandContext context, String input) {
        return commands.stream()
                .filter(command -> command.isCommand(context, input))
                .findFirst()
                .orElse(null);
    }
}

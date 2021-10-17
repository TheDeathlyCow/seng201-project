package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandRegistry;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

import java.util.ArrayList;
import java.util.List;

public class VendorCommands implements CommandRegistry<VendorCommand> {

    public static VendorCommands REGISTRY = new VendorCommands();
    private final List<VendorCommand> commands = new ArrayList<>();

    public static final SeeVendorStock SEE_VENDOR_STOCK;
    public static final SeePlayerStock SEE_PLAYER_STOCK;
    public static final PurchaseItemCommand PURCHASE_CARGO;
    public static final SellItemCommand SELL_CARGO;
    public static final SkipPageCommand NEXT_PAGE;
    public static final SkipPageCommand PREVIOUS_PAGE;
    public static final VendorCommand RETURN;

    static {
        SEE_VENDOR_STOCK = REGISTRY.register(new SeeVendorStock());
        SEE_PLAYER_STOCK = REGISTRY.register(new SeePlayerStock());
        PURCHASE_CARGO = REGISTRY.register(new PurchaseItemCommand());
        SELL_CARGO = REGISTRY.register(new SellItemCommand());
        NEXT_PAGE = REGISTRY.register(new SkipPageCommand("Next Page", 1));
        PREVIOUS_PAGE = REGISTRY.register(new SkipPageCommand("Previous Page", -1));

        RETURN = REGISTRY.register(new VendorCommand("Return") {
            @Override
            public CommandResult run(CommandContext context) throws GameOver {
                context.getExecutingPlayer().setLocation(Player.Location.ISLAND);
                return new CommandResult.Builder()
                        .shouldExitLoop()
                        .build();
            }
        });
    }

    private VendorCommands() {
    }

    @Override
    public <C extends VendorCommand> C register(C command) {
        commands.add(command);
        return command;
    }

    @Override
    public List<VendorCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean isCommand(CommandContext context, String input) {
        return commands.stream().anyMatch(
                (command) -> command.isCommand(context, input)
        );
    }

    @Override
    public VendorCommand getCommand(CommandContext context, String input) {
        return commands.stream()
                .filter(command -> command.isCommand(context, input))
                .findFirst()
                .orElse(null);
    }
}

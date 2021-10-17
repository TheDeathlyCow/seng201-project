package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.IslandCommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands.TravelCommands;

import java.util.regex.Pattern;

public class VisitDocksCommand extends IslandCommand {

    /**
     * Constructs a visit docks command by setting the command
     * string to "Visit Docks".
     */
    protected VisitDocksCommand() {
        super("Visit Docks");
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        context.getExecutingPlayer().setLocation(Player.Location.DOCKS);
        CommandContext docksContext = new CommandContext.Builder()
                .withPlayer(context.getExecutingPlayer())
                .build();

        return TravelCommands.SEE_ROUTES.run(docksContext);
    }
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.travelcommands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;

public class TravelReturnCommand extends TravelCommand {
    protected TravelReturnCommand() {
        super("Return");
    }

    @Override
    public CommandResult run(CommandContext context) throws GameOver {
        context.getExecutingPlayer().setLocation(Player.Location.ISLAND);
        return CommandResult.getResultWithWait(0);
    }
}

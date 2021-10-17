package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;

public interface CommandRunner {
    int run(Player executing) throws GameOver;
}

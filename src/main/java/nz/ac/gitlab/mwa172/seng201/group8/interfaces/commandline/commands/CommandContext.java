package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameWorld;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;

public class CommandContext {
    private final Player executingPlayer;
    private final GameWorld world;
    private final Object selection;

    private CommandContext(Builder builder) {
        this.executingPlayer = builder.executingPlayer;
        this.world = builder.world;
        this.selection = builder.selection;
    }

    public Player getExecutingPlayer() {
        return executingPlayer;
    }

    public GameWorld getWorld() {
        return world;
    }

    public Object getSelection() {
        return selection;
    }

    public static class Builder {
        private Player executingPlayer;
        private GameWorld world;
        private Object selection;

        public Builder withPlayer(Player executingPlayer) {
            this.executingPlayer = executingPlayer;
            this.world = executingPlayer.getShip().dockedAt().getWorld();
            return this;
        }

        public Builder withWorld(GameWorld world) {
            this.world = world;
            this.executingPlayer = world.getPlayer();
            return this;
        }

        public <E> Builder withSelection(E selection) {
            this.selection = selection;
            return this;
        }

        public CommandContext build() {
            return new CommandContext(this);
        }

    }

}

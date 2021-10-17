package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;

import java.util.concurrent.TimeUnit;

public class CommandResult {

    private int wait;
    private boolean exitLoop;
    private int skipPages;

    private CommandResult(Builder builder) {
        wait = builder.wait;
        exitLoop = builder.exitLoop;
        skipPages = builder.skipPages;
        if (wait > 0)
            sleep();
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(wait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getWait() {
        return wait;
    }

    public boolean shouldExitLoop() {
        return exitLoop;
    }

    public int skipPages() {
        return skipPages;
    }

    public static CommandResult getResultWithWait(int wait) {
        return new Builder().withWait(wait).build();
    }

    public static class Builder {

        private int wait = 0;
        private boolean exitLoop = false;
        private int skipPages = 0;

        public Builder withWait(int wait) {
            this.wait = wait;
            return this;
        }

        public Builder shouldExitLoop() {
            exitLoop = true;
            return this;
        }

        public Builder skipPages(int skipPages) {
            this.skipPages = skipPages;
            return this;
        }

        public CommandResult build() {
            return new CommandResult(this);
        }
    }
}

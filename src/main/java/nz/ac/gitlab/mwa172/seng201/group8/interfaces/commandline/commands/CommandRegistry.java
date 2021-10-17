package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands;

import java.util.List;

public interface CommandRegistry<E extends AbstractCommand> {

    <C extends E> C register(C command);

    List<E> getCommands();

    boolean isCommand(CommandContext context, String input);

    E getCommand(CommandContext context, String input);

}

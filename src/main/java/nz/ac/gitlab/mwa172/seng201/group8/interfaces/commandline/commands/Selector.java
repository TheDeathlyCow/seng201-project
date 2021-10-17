package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands;

import java.util.List;

/**
 * Provides behaviour for commands to select items
 * from a list.
 *
 * @param <E> The type of the object to select.
 */
public interface Selector<E> {

    /**
     * Selects an item from a list based on some user input string.
     *
     * @param context The context in which to select.
     * @param selectFrom The list to select from.
     * @param selection The item the user wants to select.
     * @return Returns the selected item if the input selection is valid,
     * otherwise it will return null.
     */
    E select(CommandContext context, List<E> selectFrom, String selection);
}

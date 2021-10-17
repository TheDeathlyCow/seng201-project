package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.MaximumList;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.Player;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.CargoItem;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands.VendorCommand;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands.ViewStockCommand;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides methods for the player to interface with
 * vendors.
 */
@Deprecated
enum VendorCommands {

    NEXT_PAGE("next page"),
    PREVIOUS_PAGE("previous page"),
    SEE_UPGRADES("see upgrades"),
    SELL_CARGO("sell cargo"),
    RETURN("return"),
    SELECT("select"),
    UNKNOWN("unknown");

    //* Instance Methods *//

    /**
     * A string representation of this command.
     */
    private final String COMMAND_STRING;

    /**
     * Constructor for VendorCommands, implicitly private due
     * to this class being an enum.
     *
     * @param commandString The string representation of the command. Should
     *                      look nice.
     */
    VendorCommands(String commandString) {
        COMMAND_STRING = commandString;
    }

    /**
     * Returns the string representation of this command.
     *
     * @return Returns the command string.
     */
    @Override
    public String toString() {
        return COMMAND_STRING;
    }

    /**
     * Parses a string into a command. If the string cannot be parsed
     * into a valid command, returns UNKNOWN.
     * The select command must be only an integer, it cannot
     * be the world "select".
     *
     * @param commandString String to be parsed.
     * @return The command instance to be executed. If the command cannot
     * be parsed, returns the UNKNOWN command.
     */
    @Deprecated
    private static VendorCommands getCommand(String commandString) {
        if (commandString.matches(SELECT.toString()))
            return UNKNOWN;

        if (Pattern.matches("^[0-9]+$", commandString))
            return SELECT;

        for (VendorCommands command : VendorCommands.values()) {
            if (command.toString().equalsIgnoreCase(commandString)) {
                return command;
            }
        }
        return UNKNOWN;
    }

    /**
     * Determines if a given string is a valid command string.
     *
     * @param command Command string to check.
     * @return True if the command string is a valid command, false otherwise.
     */
    @Deprecated
    private static boolean isCommand(String command) {
        return getCommand(command) != UNKNOWN;
    }

    /**
     * Entry point for vendor commands. This method MUST be called before
     * any another VendorCommand methods.
     *
     * @param executing The player executing the command.
     * @return The number of seconds the current thread should wait before continuing after
     * this command is finished. Negative return values indicate the program should exit.
     */
    @Deprecated
    static int visitVendor(Player executing) {
        Vendor vendor = executing.getShip().dockedAt().getVendor();
        System.out.println(vendor.getRandomGreeting(executing));
        purchaseCargo(executing, vendor);
        return 0;
    }


    /**
     * Prompts the player to enter a vendor command.
     *
     * @param promptString String to prompt the player with.
     * @param pages The pages of stock to display.
     * @param pageNum The current page number to display.
     * @param vendor The vendor the player is at.
     * @return Returns a valid command string containing the user's input.
     */
    @Deprecated
    private static String promptInput(String promptString, List<Map<Tradeable, Integer>> pages, int pageNum, Vendor vendor) {

        if (pages.size() == 0) {
            System.out.println("\n\nOut of Stock!\n\n");
            pageNum = -1;
        } else {
            Map<Tradeable, Integer> page = pages.get(pageNum);
//            printStock(page, vendor);
        }
        System.out.println("===== Page " + (pageNum + 1) + " of " + pages.size() + " =====");

        //* Get a command input from the user. *//
        return IslandTraderCommandApp.getInput(
                promptString,
                VendorCommands::isCommand
        );
    }

    /**
     * Allows the user to purchase from a vendor.
     *
     * @param executing The player executing the command
     * @param vendor    The vendor the player is currently at
     */
    @Deprecated
    private static void purchaseCargo(Player executing, Vendor vendor) {
        // List<List<Tradeable>> pages = getPagesFromStock(vendor.getCargoStock());

        boolean exit = false;
        int pageNum = 0;
        do {
            //* Get the pages to display *//
            List<Map<Tradeable, Integer>> pages = getPagesWithQuantity(vendor.getCargoStock());
            pages.addAll(getPagesWithQuantity(vendor.getUpgradeStock()));

            //* Get a command input from the user. *//
            System.out.println();
            System.out.printf("Current balance: %.2f Doubloons%n", executing.getBalance());
            System.out.println("===== " + vendor.getIsland().getName() + " Vendor Stock =====");
            String input = promptInput(BUY_PROMPT, pages, pageNum, vendor);
            VendorCommands command = VendorCommands.getCommand(input);

            //* Execute the input command *//
            switch (command) {
                case NEXT_PAGE -> pageNum = pages.size() > 0 ? (pageNum + 1) % pages.size() : 0;
                case PREVIOUS_PAGE -> pageNum = pages.size() > 0 ? (((pageNum - 1) % pages.size()) + pages.size()) % pages.size() : 0;
                case RETURN -> exit = true;
                case SELL_CARGO -> sellCargo(executing, vendor);
                case SELECT -> {
                    int stockNum = Integer.parseInt(input) - 1;
                    if (pageNum < pages.size())
                        purchaseItem(pages.get(pageNum), executing, stockNum);
                    else
                        System.out.println("There is nothing to buy!");
                }
                default -> System.out.println("Invalid command, please try again.");
            }

        } while (!exit);
    }

    /**
     * Lets the player sell cargo to the vendor.
     *
     * @param executing The player executing the command.
     * @param vendor The vendor the player is selling to.
     */
    @Deprecated
    private static void sellCargo(Player executing, Vendor vendor) {
        boolean exit = false;
        int pageNum = 0;
        do {
            //* Get the pages to display *//
            List<Map<Tradeable, Integer>> pages = getPagesWithQuantity(executing.getShip().getCargo());

            pages.addAll(getPagesWithQuantity(executing.getShip().getUpgrades()));

            //* Get a command input from the user. *//
            System.out.println();
            System.out.printf("Your balance: %.2f Doubloons%n", executing.getBalance());
            System.out.printf("Vendor balance: %.2f Doubloons%n", executing.getBalance());
            System.out.println("===== Your Cargo =====");
            String input = promptInput(SELL_PROMPT, pages, pageNum, vendor);
            VendorCommands command = VendorCommands.getCommand(input);
            //* Execute the input command *//
            switch (command) {
                case NEXT_PAGE -> pageNum = pages.size() > 0 ? (pageNum + 1) % pages.size() : 0;
                case PREVIOUS_PAGE -> pageNum = pages.size() > 0 ? (((pageNum - 1) % pages.size()) + pages.size()) % pages.size() : 0;
                case RETURN -> exit = true;
                case SELECT -> {
                    int stockNum = Integer.parseInt(input) - 1;
                    if (pageNum < pages.size())
                        sellItem(pages.get(pageNum), executing, stockNum);
                    else
                        System.out.println("There is nothing to sell!");
                }
                default -> System.out.println("Invalid command, please try again.");
            }

        } while (!exit);
    }

    /**
     * Returns a list of lists that has been split up
     * into sub lists with a maximum length.
     * Each sublist could be seen as being like a page,
     * it has a maximum length but with a minimum length of 1,
     * (i.e. there must be something in the sublist).
     *
     * @param toSplit List to split.
     * @param maxSize The maximum size of the sub lists.
     * @param <E>     Generic type of the list to split.
     * @return A list of sub lists with a maximum length.
     */
    @Deprecated
    private static <E> List<List<E>> splitList(List<? extends E> toSplit, int maxSize) {
        List<List<E>> pages = new ArrayList<>();
        for (int i = 0; i < toSplit.size(); i++) {
            MaximumList<E> page = new MaximumList<>(maxSize);
            while (i < toSplit.size() && page.add(toSplit.get(i))) {
                i++;
            }
            pages.add(page);
        }
        return pages;
    }

    public static <E> List<Map<E, Integer>> getPagesWithQuantity(List<? extends E> stock) {
        List<Map<E, Integer>> pages = new ArrayList<>();
        Set<E> uniqueItems = new HashSet<>(stock);
        Map<E, Integer> page = new LinkedHashMap<>();
        for (E key : uniqueItems) {
            List<E> sameItems = stock.stream()
                    .filter(item -> item.equals(key))
                    .collect(Collectors.toList());

            if (page.size() < IslandTraderConfig.CONFIG.getMaxItemsPerPage()) {
                page.put(key, sameItems.size());
            } else {
                pages.add(page);
                page = new LinkedHashMap<>();
            }
        }

        if (page.size() > 0 && page.size() < IslandTraderConfig.CONFIG.getMaxItemsPerPage())
            pages.add(page);

        return pages;
    }

    /**
     * Purchases an item from a vendor. Makes sure that the
     * item the player is trying to purchase is valid, and also
     * that they actually can purchase the item.
     *
     * @param stock     The stock to purchase from.
     * @param executing The player purchasing the item.
     * @param stockNum  The index of the item the player wants to buy.
     */
    @Deprecated
    private static void purchaseItem(Map<? extends Tradeable, Integer> stock, Player executing, int stockNum) {
        if (stockNum >= 0 && stockNum < stock.size()) {
            Tradeable toBuy = new ArrayList<>(stock.keySet()).get(stockNum);
            if (!executing.canPurchase(toBuy)) {
                System.out.println("Sorry, you cannot purchase this item. Please try another item.");
                return;
            }
            Vendor vendor = executing.getShip().dockedAt().getVendor();
            executing.purchase(toBuy, vendor);
            System.out.printf("You purchased %s for %.2f%n",
                    toBuy.getName(),
                    toBuy.getValueAtVendor(vendor));
            return;
        }
        System.out.println("Sorry, that is not a valid item. Please try again.");
    }

    /**
     * Sells an item to a vendor. Ensure that the selected item is valid,
     * and that the player can sell that item.
     *
     * @param stock The stock the player is selling from.
     * @param executing The player selling the item.
     * @param stockNum The number of the item in the stock key set to sell.
     */
    @Deprecated
    private static void sellItem(Map<? extends Tradeable, Integer> stock, Player executing, int stockNum) {
        if (stockNum >= 0 && stockNum < stock.size()) {
            Tradeable toSell = new ArrayList<>(stock.keySet()).get(stockNum);
            Vendor vendor = executing.getShip().dockedAt().getVendor();
            if (!vendor.canPurchase(toSell)) {
                System.out.println("Sorry, you cannot sell that item. Please try another item.");
                return;
            }
            executing.sell(toSell, vendor);
            System.out.printf("You sold %s for %.2f%n",
                    toSell.getName(),
                    toSell.getValueAtVendor(vendor));
            return;
        }
        System.out.println("Sorry, that is not a valid item. Please try again.");
    }

    /**
     * A string containing the prompt to display to the player when they
     * want to buy something.
     */
    @Deprecated
    private static final String BUY_PROMPT = "Type the number of the item you wish to buy, or type 'next page' or 'previous page' to see what else this vendor has.\n"
            + "If you wish to sell your cargo, type 'sell cargo'.\n"
            + "Type 'return' if you wish to go back.\n";

    /**
     * A string containing the prompt to display to the player when they
     * want to sell something.
     */
    @Deprecated
    private static final String SELL_PROMPT = "Type the number of the item you wish to sell, or type 'next page' or 'previous page' to see what else you have.\n"
            + "Type 'return' if you wish to go back.\n";
}

package nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.VendorCommands;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.game.GameOver;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.islands.Vendor;
import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.ships.Tradeable;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.IslandTraderCommandApp;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandContext;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.CommandResult;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.commandline.commands.Selector;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ViewStockCommand extends VendorCommand {

    private final String PROMPT;

    protected ViewStockCommand(String commandString, String prompt) {
        super(commandString);
        this.PROMPT = prompt;
    }

    protected CommandResult run(CommandContext context, PageGetter getter) throws GameOver {
        Vendor vendor = context.getExecutingPlayer().getShip().dockedAt().getVendor();
        boolean exit = false;
        int pageNum = 0;

        while (!exit) {

            List<Map<Tradeable, Integer>> pages = getter.getPages(context);
            pageNum = Math.min(pageNum, pages.size() - 1);
            printPrompt(context, pages, pageNum);
            CommandResult result = getNextAction(context, pages, pageNum);

            exit = result.shouldExitLoop();
            pageNum = addPageNum(pageNum, result.skipPages(), pages.size());

        }

        return CommandResult.getResultWithWait(0);
    }

    private CommandResult getNextAction(CommandContext context, List<Map<Tradeable, Integer>> pages, int pageNum) throws GameOver {

        String nextAction = IslandTraderCommandApp.getInput(PROMPT,
                (input) ->  VendorCommands.REGISTRY.isCommand(context, input)
        );

        VendorCommand command = VendorCommands.REGISTRY.getCommand(context, nextAction);

        if (pages.size() == 0) {
//            returnNoPages(context);
            return command.run(context);
        }

        List<Tradeable> page = new ArrayList<>(pages.get(pageNum).keySet());
        Object selection = getSelector().select(context, page, nextAction);

        CommandContext nextContext = new CommandContext.Builder()
                .withPlayer(context.getExecutingPlayer())
                .withSelection(selection)
                .build();

        return command.run(nextContext);
    }

    /**
     * Gets a list of HashMaps that map an item to the amount the vendor has.
     * Each map can be seed as a page, and has a maximum size specified in the config.
     *
     * @param stock The stock of the vendor. For example, the vendor's cargo stock or
     *              upgrade stock.
     * @param <E>   The type of the item to count the quantity of.
     * @return Returns a list of maps that map cargo items to the quantity in the stock.
     */
    public <E> List<Map<E, Integer>> getPagesWithQuantity(List<? extends E> stock) {

        List<Map<E, Integer>> pages = new ArrayList<>();
        Set<E> uniqueItems = new HashSet<>(stock);
        Map<E, Integer> page = new LinkedHashMap<>();
        for (E key : uniqueItems) {
            List<E> sameItems = stock.stream()
                    .filter(item -> item.equals(key))
                    .collect(Collectors.toList());

            if (page.size() >= IslandTraderConfig.CONFIG.getMaxItemsPerPage()) {
                pages.add(page);
                page = new LinkedHashMap<>();
            }
            page.put(key, sameItems.size());
        }

        if (page.size() > 0 && page.size() <= IslandTraderConfig.CONFIG.getMaxItemsPerPage()) {
            pages.add(page);
        }

        return pages;
    }



    /**
     * Prints a single page of stock to the
     * console.
     *
     * @param stock  The stock to print
     * @param vendor The vendor who owns the stock.
     */
    protected void printStock(Map<? extends Tradeable, Integer> stock, Vendor vendor) {

        int itemNum = 1;
        for (Tradeable item : stock.keySet()) {
            StringBuilder builder = new StringBuilder();
            builder.append(itemNum).append(" - ");
            builder.append(item.toString());
            if (stock.get(item) > 1)
                builder.append(" x").append(stock.get(item));
            builder.append(": ");
            builder.append(String.format("%.2f", item.getValueAtVendor(vendor)));
            builder.append(" Doubloons");
            System.out.println(builder.toString());
            itemNum++;
        }
    }

    protected void printPrompt(CommandContext context, List<Map<Tradeable, Integer>> pages, int pageNum) {
        Vendor vendor = context.getExecutingPlayer().getShip().dockedAt().getVendor();
        printHeader(context);
        if (pages.size() == 0) {
            System.out.println("\n\nOut of Stock!\n\n");
        } else {
            printStock(pages.get(pageNum), vendor);
        }
        if (pages.size() == 0)
            pageNum = -1;

        System.out.println("===== Page " + (pageNum + 1) + " of " + pages.size() + " =====");
    }

    protected abstract void printHeader(CommandContext context);

    protected abstract Selector<Tradeable> getSelector();

    public int addPageNum(int currPage, int toAdd, int numPages) {
        return numPages > 0 ? (((currPage + toAdd) % numPages) + numPages) % numPages : 0;
    }

    protected interface PageGetter {
        List<Map<Tradeable, Integer>> getPages(CommandContext context);
    }
}

import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

import java.util.Iterator;

import components.inventory.Inventory;
import components.inventory.Inventory1;
import components.simplereader.SimpleReader;

/**
 * Demo program that tracks item quantities and has an option to reorder that
 * uses the inventory component.
 *
 * Note: For the sake of demonstration, the user will be prompted to include
 * sample data at the beginning of the demo. Choosing yes is encouraged, and
 * the user can still test the component by removing sample items and/or
 * adding their own items.
 *
 * @author Jacob Witt
 */
public final class DemoReorderSystem {

    /**
     * The inventory to use.
     */
    private static Inventory inv = new Inventory1();

    /**
     * No args constructor.
     */
    private DemoReorderSystem() {

    }

    /**
     * Add sample data to inv.
     */
    private static void loadSampleData() {
        inv.add("1001", "Honeycrisp Apple");
        inv.add("1052", "Blueberry");
        inv.add("1198", "Watermellon");
        inv.add("20040", "Grape");
        inv.add("6546", "Blackberry");
        inv.add("7888", "Banana");
        inv.add("99999", "Kiwi");
        final int randomQuantity1 = 10;
        final int randomQuantity2 = 5;
        inv.setQuantity("1001", 2);
        inv.setQuantity("1052", randomQuantity1);
        inv.setQuantity("20040", 1);
        inv.setQuantity("7888", randomQuantity2);
        inv.addAttribute("6546", "Order Status", "3 Reordered");
    }

    /**
     * Get and return a valid integer from the user.
     * @param in the SimpleReader object to get input
     * @param out the output stream to prompt the user
     * @return a valid integer value corresponding to the user's input
     * @ensures getIntegerInput = [a valid integer corresponding to what
     * the user entered]
     * @requires in is open and out is open
     */
    public static int getIntegerInput(SimpleReader in, SimpleWriter out) {
        String orderQuantity = "";
        boolean validNum = false;
        int result = 0;
        while (!validNum) {
            orderQuantity = in.nextLine();
            try {
                result = Integer.parseInt(orderQuantity);
                validNum = true;
            } catch (NumberFormatException n) {
                validNum = false;
                out.print("Invalid input, try again: ");
            }
        }
        return result;
    }

    /**
     * Print the inventory to out in the appropriate format.
     * @param out the output stream to print to
     * @ensures printInventory appropriately prints the inventory
     * @requires out is open
     */
    public static void printInventory(SimpleWriter out) {
        out.println("Barcode             Name                Quantity");
        for (Inventory.InventoryItem i : inv) {
            final int numSpaces = 20;
            out.print(i.attributes().value("barcode"));
            //print barcode
            for (int k = i.attributes().value("barcode").length(); k < numSpaces;
                k++) {
                out.print(" ");
            }
            //print name
            out.print(i.name());
            for (int k = i.name().length(); k < numSpaces; k++) {
                out.print(" ");
            }
            //print quantity
            out.print(i.quantity());
            for (int k = (i.quantity() + "").length(); k < numSpaces / 2; k++) {
                out.print(" ");
            }
            //print reorder status, if needed
            if (i.attributes().hasKey("Order Status")) {
                out.println(i.attributes().value("Order Status"));
            } else {
                out.println();
            }
        }
    }

    /**
     * Return the int at the beginning of str.
     * @param str the string to analyze
     * @ensures getBeginInt = [the integer or string of
     * integers at the beginning of str]
     * @return the integer or string of integers at the beginning of str
     * @requires str begins with an integer or sequence of integers
     */
    public static int getBeginInt(String str) {
        int startLen = 1;
        int maxSuccess = Integer.MIN_VALUE;
        int current = Integer.MIN_VALUE;
        while (startLen < str.length()) {
            try {
                current = Integer.parseInt(str.substring(0, startLen));
            } catch (NumberFormatException n) {
                current = Integer.MIN_VALUE;
            }
            if (maxSuccess < current) {
                maxSuccess = current;
            }
            startLen++;
        }
        return maxSuccess;
    }

    /**
     * The main method.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();
        String sampleData = " ";
        while (!(sampleData.charAt(0) == 'y' || sampleData.charAt(0) == 'n')) {
            out.println("Begin with sample data (y/n)?");
            sampleData = in.nextLine();
        }
        if (sampleData.charAt(0) == 'y') {
            loadSampleData();
        }
        while (true) {
            printInventory(out);
            out.println("Enter a barcode, \"REORDER\", \"NEW ITEM\", or \"QUIT\".");
            String response = in.nextLine();
            if (response.equals("QUIT")) {
                break;
            }
            if (inv.contains(response)) {
                /*
                 * Provide options for the given item
                 */
                boolean moreOrdered = false;
                out.println("\nItem: " + inv.name(response) + " (" + response + ")");
                out.println("Quantity Remaining: " + inv.quantity(response));
                if (inv.hasAttribute(response, "Order Status")) {
                    moreOrdered = true;
                    String order = inv.getAttribute(response, "Order Status");
                    out.println(order.charAt(0) + " of this item were reordered.");
                    out.println("Select an option:");
                    out.println("1\tReturn to inventory");
                    out.println("2\tEdit item quantity");
                    out.println("3\tOrder more");
                    out.println("4\tRemove item from inventory");
                    out.println("5\tMark order received");
                } else {
                    out.println("Select an option:");
                    out.println("1\tReturn to inventory");
                    out.println("2\tEdit item quantity");
                    out.println("3\tOrder more");
                    out.println("4\tRemove item from inventory");
                }
                /*
                 * Get and validate the user's option choice
                 */
                boolean validIn = false;
                String response2 = "";
                char first = ' ';
                while (!validIn) {
                    response2 = in.nextLine();
                    if (response2.length() > 0) {
                        first = response2.charAt(0);
                        if (first == '1' || first == '2' || first == '3'
                        || first == '5' && moreOrdered || first == '4') {
                        validIn = true;
                        } else {
                        out.print("Invalid response, try again: ");
                        }
                    } else {
                        out.print("Invalid response, try again: ");
                    }
                }
                /*
                 * Perform the appropriate action
                 */
                if (first == '2') {
                    /*
                     * Option chosen: edit item quantity
                     */
                    out.print("\nEnter the item's new quantity: ");
                    int newQTYnum = getIntegerInput(in, out);
                    inv.setQuantity(response, newQTYnum);
                } else if (first == '3') {
                    /*
                     * Option chosen: order more
                     */
                    out.print("Enter the amount to order: ");
                    int numToOrder = getIntegerInput(in, out);
                    if (moreOrdered) {
                        /*
                         * Update the order that was already placed
                         */
                        int alreadyOrdered = getBeginInt(inv.getAttribute(response,
                            "Order Status"));
                        numToOrder += alreadyOrdered;
                        inv.setAttribute(response, "Order Status", numToOrder
                            + " Reordered");
                    } else {
                        /*
                         * Create a new order
                         */
                        inv.addAttribute(response, "Order Status", numToOrder
                            + " Reordered");
                    }
                } else if (first == '4') {
                    inv.remove(response);
                } else if (first == '5') {
                    /*
                     * Option chosen: mark order as received.
                     */
                    int numOrdered = getBeginInt(inv.getAttribute(response,
                    "Order Status"));
                    inv.removeAttribute(response, "Order Status");
                    inv.setQuantity(response, inv.quantity(response) + numOrdered);
                    out.println("\nThe order of " + numOrdered + " " + inv.name(response)
                        + " has been received. There are now " + inv.quantity(response)
                        + " " + inv.name(response) + " remaining.");
                }
            } else if (response.equals("REORDER")) {
                out.println("\nThe following items are out of stock without pending "
                + "orders. For each item, enter an amount to reorder.");
                    Iterator<Inventory.InventoryItem> it = inv.iterator();
                    while (it.hasNext()) {
                        Inventory.InventoryItem next = it.next();
                        if (next.quantity() == 0
                            && !next.attributes().hasKey("Order Status")) {
                            out.print(next.name() + ": ");
                            int orderQuantity = getIntegerInput(in, out);
                            if (orderQuantity > 0) {
                                inv.addAttribute(next.attributes().value("barcode"),
                                "Order Status", orderQuantity + " Reordered");
                            }
                        }
                    }
                out.println("Items reordered succesfully!");
            } else if (response.equals("NEW ITEM")) {
                out.print("Enter a barcode: ");
                String bc = in.nextLine();
                out.print("Enter a name: ");
                String nm = in.nextLine();
                out.print("Enter a quantity: ");
                int qtyNew = getIntegerInput(in, out);
                if (inv.contains(bc)) {
                    out.println("Failed to add. This barcode is already in"
                        + " the inventory.");
                } else {
                    inv.add(bc, nm);
                    inv.setQuantity(bc, qtyNew);
                    out.println("Item added succesfully!");
                }
            } else {
                out.println("Invalid Barcode/Command.");
            }
            out.println();
        }
        out.close();
        in.close();
    }
}

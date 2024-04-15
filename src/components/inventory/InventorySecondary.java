package components.inventory;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;

import java.util.Iterator;

import components.map.Map;

/**
 * The abstract class containing secondary and object methods for Inventory.
 *
 * @author Jacob Witt
 */
public abstract class InventorySecondary implements Inventory {

    /**
     * Abstract class for InventoryItem.
     */
    public abstract class InventoryItemSecondary implements InventoryItem {
        @Override
        public String toString() {
            return ("<" + this.name() + ", " + this.quantity()
                + ", Attributes: " + this.attributes() + ">");
        }

        /**
         * Returns whether the two items have the same quantity, name, and attributes.
         * @ensures this.equals(other) = true iff the names, attributes, and
         * quantities are equal
         * @param obj the second item to compare
         * @return whether the two items are equal
         */
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (!(obj instanceof Inventory.InventoryItem)) {
                return false;
            } else {
                Inventory.InventoryItem inv = (Inventory.InventoryItem) obj;
                /*
                 * Compare names
                 */
                if (!inv.name().equals(this.name())) {
                    return false;
                }
                /*
                 * Compare quantities
                 */
                if (inv.quantity() != this.quantity()) {
                    return false;
                }
                /*
                 * Compare attributes
                 */
                if (!inv.attributes().equals(this.attributes())) {
                    return false;
                }
                return true;
            }
        }

        @Override
        public int hashCode() {
            /*
             * The inventoryItem hashCode will be determined by item's name attribute
             */
            return this.name().hashCode();
        }
    }
    /*
     * Implementation of common object methods
     */

    @Override
    public String toString() {
        String result = "<";
        Iterator<Inventory.InventoryItem> it = this.iterator();
        while (it.hasNext()) {
            Inventory.InventoryItem i = it.next();
            result += i;
            result += ", ";
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 2);
        }
        result += ">";
        return result;
    }

    /**
     * Returns whether the two objects have the same quantity for every item.
     * @ensures this.equals(inv) = true iff the barcodes and corresponding
     * quantities are the same within both inventories
     * @param obj the second inventory to compare
     * @return whether the two inventories are equal
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof Inventory)) {
            return false;
        } else {
            Inventory inv = (Inventory) obj;
            if (inv.size() != this.size()) {
                return false;
            }
            /*
             * Check equality with iterator
             */
            Iterator<Inventory.InventoryItem> it1 = this.iterator();
            Iterator<Inventory.InventoryItem> it2 = inv.iterator();
            while (it1.hasNext()) {
                Inventory.InventoryItem item1 = it1.next();
                Inventory.InventoryItem item2 = it2.next();
                if (!item1.equals(item2)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return this.size();
        /*
         * The hashCode will just be the size of the inventory.  It is unlikely
         * that this method will be used often.  The hashCode for items will
         * be used much more often.
         */
    }

    /*
     * Implementation of secondary methods
     */

    @Override
    public String search(String name) {
        /*
         * Remove the items from the inventory and add them to a queue
         */
        Queue<Inventory.InventoryItem> temp = new Queue1L<Inventory.InventoryItem>();
        String result = "";
        while (this.size() > 0) {
            Inventory.InventoryItem current = this.removeAny();
            temp.enqueue(current);
        }
        /*
         * Find the right name and restore this
         */
        while (temp.length() > 0) {
            Inventory.InventoryItem current = temp.dequeue();
            Map<String, String> attr = current.attributes();
            String barcode = attr.value("barcode");
            this.add(barcode);
            for (int i = 0; i < current.quantity(); i++) {
                this.increment(barcode);
            }
            this.setName(barcode, current.name());
            if (current.name().equals(name)) {
                result = barcode;
            }
            while (attr.size() > 0) {
                Map.Pair<String, String> currentAttribute = attr.removeAny();
                this.addAttribute(barcode, currentAttribute.key(),
                currentAttribute.value());
            }
        }
        return result;
    }

    @Override
    public void add(String barcode, String name) {
        this.add(barcode);
        this.setName(barcode, name);
    }

    @Override
    public void setQuantity(String barcode, int quantity) {
        int currentQuantity = this.quantity(barcode);
        while (currentQuantity < quantity) {
            this.increment(barcode);
            currentQuantity = this.quantity(barcode);

        }
        while (currentQuantity > quantity) {
            this.decrement(barcode);
            currentQuantity = this.quantity(barcode);
        }
    }

    @Override
    public void setAttribute(String barcode, String name, String content) {
        this.removeAttribute(barcode, name);
        this.addAttribute(barcode, name, content);
    }

    @Override
    public boolean hasAttribute(String barcode, String name) {
        boolean val = false;
        if (this.size() > 0) {
            Set<String> attributes = this.attributes(barcode);
            val = attributes.contains(name);
        }
        return val;
    }

    @Override
    public void combine(Inventory i) {
        while (i.size() > 0) {
            Inventory.InventoryItem current = i.removeAny();
            Map<String, String> attr = current.attributes();
            if (this.contains(attr.value("barcode"))) {
                /*
                 * An item with the barcode is already in this
                 */
                for (int k = 0; k < current.quantity(); k++) {
                    this.increment(attr.value("barcode"));
                }
            } else {
                /*
                 * An item with the barcode is not in this
                 */
                String barcode = attr.remove("barcode").value();
                this.add(barcode);
                for (int k = 0; k < current.quantity(); k++) {
                    this.increment(barcode);
                }
                this.setName(barcode, current.name());
                while (attr.size() > 0) {
                    Map.Pair<String, String> currentAttr = attr.removeAny();
                    this.addAttribute(barcode, currentAttr.key(), currentAttr.value());
                }
            }
        }
    }
}

package components.inventory;

import components.map.Map;
import components.standard.Standard;
/**
 * {@code InventoryKernel} enhanced with secondary methods.
 *
 * @author Jacob Witt
 */
public interface Inventory extends InventoryKernel {

  /**
   * Representation of each item within the inventory.
   */
  interface InventoryItem extends Standard<InventoryItem> {

    /**
    * Returns the name of this.
    *
    * @return the name of this
    * @ensures this.name() = [the name assigned to this], or this.name() = "" if
    * no name has been assigned to this
    */
    String name();

    /**
    * Returns the quantity of this.
    *
    * @return the quantity of this
    * @ensures this.quantity() = the number of this item remaining
    */
    int quantity();

    /**
    * Returns a map containing the names and content of all attributes assigned
    * to this item.
    *
    * @return the complete set of attributes associated with the specified
    * item
    * @ensures this.attributes() = [a map where each pair follows the form
    * <attribute name, attribute content>], or this.attributes() = <> if no
    * attributes have been assigned to this
    */
    Map<String, String> attributes();
  }
    /**
    * Search the inventory for an item by name.
    *
    * @param name the new name of the item
    * @return the barcode of the item with name {@code name}
    * @ensures search(name) = [the barcode of the item with the specified
    * name] if the item with {@code name} exists in the inventory,
    * search(name) = "" otherwise.
    */
    String search(String name);

    /**
    * Add an item with barcode {@code barcode} to the inventory with
    * quantity {@code quantity}.
    *
    * @param barcode the item's barcode
    * @param name the name of the item
    * @requires an item with {@code barcode} is not already in {@code this}
    * @updates this
    * @ensures {@code this = #this * item}, where the item has the specified
    * barcode, quantity(barcode) = 0, |attributes(quantity)| = 0,
    * name(barcode) = name
    */
  void add(String barcode, String name);

  /**
    * Sets the quantity of the item with barcode {@code barcode} to
    * {@code quantity}.
    *
    * @param barcode the item's barcode
    * @param quantity the new quantity of the item
    * @requires an item with {@code barcode} is in {@code this} and
    * {@code quantity} >= 0
    * @updates this
    * @ensures quantity(barcode) = quantity
    */
    void setQuantity(String barcode, int quantity);

    /**
    * Changes the content of attribute with {@code name} on an item with
    * {@code barcode} to {@code content}.
    *
    * @param barcode the item's barcode
    * @param name the name of the attribute
    * @param content the new content of the attribute
    * @requires an item with {@code barcode} is in {@code this} and
    * attributes(barcode) contains {@code name}
    * @updates this
    * @ensures getAttribute(barcode, name) = content
    */
    void setAttribute(String barcode, String name, String content);

    /**
    * Returns whether the item with {@code barcode} has an attribute named
    * {@code attribute}.
    *
    * @param barcode the item's barcode
    * @param name the name of the attribute
    * @return true iff the item with {@code barcode} has an attribute named
    * {@code attribute}
    * @requires an item with {@code barcode} is in {@code this}
    * @ensures hasAttribute(barcode, name) = true, if the item with
    * {@code barcode} has an attribute named {@code attribute}, false
    * otherwise
    */
    boolean hasAttribute(String barcode, String name);

    /**
     * Adds all items within inventory {@code i} to {@code this}.
     *
     * @param i the inventory to combine with this
     * @updates this
     * @clears i
     * @ensures [this contains all items from #this and #i] and [the
     * quantities of items with identical barcodes from each inventory have
     * been added] and [if both inventories have the same barcode, the name and
     * attributes from this are maintained]
     */
    void combine(Inventory i);


}

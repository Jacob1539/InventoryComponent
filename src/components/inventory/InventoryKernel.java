package components.inventory;
import components.standard.Standard;
import components.set.Set;

/**
 * Inventory kernel component with primary references.
 *
 * @author Jacob Witt
 */
public interface InventoryKernel extends Standard<Inventory>,
Iterable<Inventory.InventoryItem> {
  /**
    * Add an item with barcode {@code barcode} to the inventory with a
    * quantity 0.
    *
    * @param barcode the item's barcode
    * @requires an item with {@code barcode} is not already in {@code this}
    * @updates this
    * @ensures {@code this = #this * item}, where the item has the specified
    * barcode, quantity(barcode) = 0, |attributes(quantity)| = 0,
    * name(barcode) = ""
    */
  void add(String barcode);

  /**
    * Removes an item with barcode {@code barcode} from the inventory.
    *
    * @param barcode the item's barcode
    * @requires an item with {@code barcode} is in {@code this}
    * @return the quantity of the item with {@code barcode}
    * @updates this
    * @code this * item = #this}, where the item has the specified barcode
    */
  Inventory.InventoryItem remove(String barcode);

  /**
    * Removes & returns an item from the inventory.
    *
    * @requires this.size > 0
    * @return the InventoryItem removed
    * @updates this
    * @ensures this is unchanged except for the removal of the returned item,
    * and the item is unchanged except for the addition of a barcode attribute
    */
    Inventory.InventoryItem removeAny();

    /**
     * Returns the size of the inventory.
     *
     * @return the size of this
     * @ensures this.size = [the number of unique entries within this]
     */
    int size();

    /**
    * Increases the quantity of the item with barcode {@code barcode} by one.
    *
    * @param barcode the item's barcode
    * @requires an item with {@code barcode} is in {@code this}
    * @updates this
    * @ensures quantity(barcode) = #quantity(barcode) + 1
    */
  void increment(String barcode);

    /**
    * Decrements the quantity of the item with barcode {@code barcode} by one.
    *
    * @param barcode the item's barcode
    * @requires an item with {@code barcode} is in {@code this} and
    * quantity(barcode) > 0
    * @updates this
    * @ensures quantity(barcode) = #quantity(barcode) - 1
    */
  void decrement(String barcode);

  /**
    * Returns the quantity of the item with barcode {@code barcode}.
    *
    * @param barcode the item's barcode
    * @return the quantity of the item with the specified barcode
    * @requires an item with {@code barcode} is in {@code this}
    * @ensures quantity(barcode) = the number of the item with barcode
    * {@code barcode} currently in the inventory
    */
    int quantity(String barcode);

    /**
    * Return the name of the item with barcode {@code barcode}.
    *
    * @param barcode the item's barcode
    * @return the name of the item
    * @requires an item with {@code barcode} exists in {@code this}
    * @ensures name = [the name assigned to the item with the specified
    * barcode, or an empty string if no name has been specified.]
    */
    String name(String barcode);

    /**
    * Change the name of the item with barcode {@code barcode} to
    * {@code name}.
    *
    * @param barcode the item's barcode
    * @param name the new name of the item
    * @requires an item with {@code barcode} exists in {@code this}
    * @updates this
    * @ensures name(barcode) = name
    */
    void setName(String barcode, String name);

    /**
    * Adds an attribute item with barcode {@code barcode}.  The attribute's
    * name is {@code name} and the description is {@code content}
    *
    * @param barcode the item's barcode
    * @param name the name of the new attribute
    * @param content the content of the new attribute
    * @requires an item with {@code barcode} is in {@code this} and
    * attributes(barcode) does not contain {@code name} and {@code name} is not
    * "barcode"
    * @updates this
    * @ensures getAttribute(barcode, name) = content
    */
    void addAttribute(String barcode, String name, String content);

    /**
    * Returns the content of attribute with {@code name} on an item with
    * {@code barcode}.
    *
    * @param barcode the item's barcode
    * @param name the name of the attribute
    * @return the content of the named attribute associated with the item
    * with the specified barcode
    * @requires an item with {@code barcode} is in {@code this} and
    * attributes(barcode) contains {@code name}
    * @ensures getAttribute(barcode, name) = [the content specified when
    * adding or setting the attribute]
    */
    String getAttribute(String barcode, String name);

    /**
    * Removes the  attribute with {@code name} on an item with
    * {@code barcode} and returns it's content.
    *
    * @param barcode the item's barcode
    * @param name the name of the attribute
    * @return the content of the named attribute associated with the item
    * with the specified barcode
    * @updates this
    * @requires an item with {@code barcode} is in {@code this} and
    * attributes(barcode) contains {@code name}
    * @ensures removeAttribute(barcode, name) = [the content specified when
    * adding or setting the attribute] and hasAttribute(barcode, name) = false
    */
    String removeAttribute(String barcode, String name);

    /**
    * Returns a set containing the names of all attributes associated with
    * the item specified by {@code barcode}.
    *
    * @param barcode the item's barcode
    * @return the complete set of attributes associated with the specified
    * item
    * @requires an item with {@code barcode} is in {@code this}
    * @ensures attributes(barcode) = [the complete set of attributes
    * associated with the specified item], or an empty set if the item has
    * no attributes
    */
    Set<String> attributes(String barcode);

    /**
     * Returns whether the inventory contains an item with {@code barcode}.
     *
     * @param barcode the barcode to check for
     * @return true if this contains {@code barcode}, false otherwise
     * @ensures contains(barcode) = true, iff this contains an item with
     * {@code barcode}
     */
    boolean contains(String barcode);

}

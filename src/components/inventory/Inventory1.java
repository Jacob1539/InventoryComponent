package components.inventory;
import java.util.Iterator;
import java.util.NoSuchElementException;

import components.map.Map;
import components.map.Map1L;
import components.set.Set2;
import components.set.Set;

/**
 * Inventory represented as a hashtable of type Map<String barcode, InventoryItem>.
 *
 * @author Jacob Witt
 */
public final class Inventory1 extends InventorySecondary {

  /*
   * Private Members
   */
  /**
     * Default size of hash table.
     */
    private static final int DEFAULT_HASH_TABLE_SIZE = 100;

    /**
     * Buckets for hashing.
     */
    private Map1L<String, InventoryItem1>[] hashTable;

    /**
     * Total size of abstract {@code this}.
     */
    private int size;

    /**
     * Computes {@code a} mod {@code b} as % should have been defined to work.
     *
     * @param a
     *            the number being reduced
     * @param b
     *            the modulus
     * @return the result of a mod b, which satisfies 0 <= {@code mod} < b
     * @requires b > 0
     * @ensures <pre>
     * 0 <= mod  and  mod < b  and
     * there exists k: integer (a = k * b + mod)
     * </pre>
     */
    private static int mod(int a, int b) {
      assert b > 0 : "Violation of: b > 0";

      int result = a % b;
      if (a < 0 && result != 0) {
          result = result + b;
      }
      return result;
  }

  /**
     * Creator of initial representation.
     *
     * @param hashTableSize
     *            the size of the hash table
     * @requires hashTableSize > 0
     * @ensures <pre>
     * |$this.hashTable| = hashTableSize  and
     * for all i: integer
     *     where (0 <= i  and  i < |$this.hashTable|)
     *   ($this.hashTable[i, i+1) = <{}>)  and
     * $this.size = 0
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private void createNewRep(int hashTableSize) {
        /*
         * With "new Map<K, V>[...]" in place of "new Map[...]" it does not
         * compile; as shown, it results in a warning about an unchecked
         * conversion, though it cannot fail.
         */
        this.hashTable = new Map1L[hashTableSize];
        for (int i = 0; i < this.hashTable.length; i++) {
            this.hashTable[i] = new Map1L<String, InventoryItem1>();
        }
        this.size = 0;
    }
    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Inventory1() {
      this.createNewRep(DEFAULT_HASH_TABLE_SIZE);
  }

  /*
  * Standard methods -------------------------------------------------------
  */
    @Override
    public Inventory newInstance() {
      try {
        return this.getClass().getConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
        throw new AssertionError(
          "Cannot construct object of type " + this.getClass());
      }
    }

    @Override
    public void clear() {
      this.createNewRep(DEFAULT_HASH_TABLE_SIZE);
    }

    @Override
    public void transferFrom(Inventory source) {
      assert source != null : "Violation of: source is not null";
      assert source != this : "Violation of: source is not this";
      assert source instanceof Inventory1 : "Violation of: source is of "
      + "dynamic type Inventory1";
      Inventory1 localSource = (Inventory1) source;
      this.hashTable = localSource.hashTable;
      this.size = localSource.size;
      localSource.createNewRep(DEFAULT_HASH_TABLE_SIZE);
    }

  /*
  * Kernel methods -------------------------------------------------------
  */

    /**
     * InventoryItem representation.
     */
    public class InventoryItem1 extends InventoryItemSecondary {
      /*
       * Private Members
       */
      /**
       * Item quantity.
       */
      private int quantity;

      /**
       * Item name.
       */
      private String name;

      /**
       * Item attributes.
       */
      private Map<String, String> attributes;

    /**
     * Creator of initial representation.
     *
     * @ensures this.quantity = 0 and
     * this.name = "" and
     * this.attributes = <>
     */
      private void createNewRep() {
        this.quantity = 0;
        this.name = "";
        this.attributes = new Map1L<String, String>();
      }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public InventoryItem1() {
      this.createNewRep();
    }

    /*
    * Standard methods -------------------------------------------------------
    */
    @Override
    public final InventoryItem1 newInstance() {
      try {
        return this.getClass().getConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
        throw new AssertionError(
          "Cannot construct object of type " + this.getClass());
      }
    }

    @Override
    public final void clear() {
      this.createNewRep();
    }

    @Override
    public final void transferFrom(InventoryItem source) {
      assert source != null : "Violation of: source is not null";
      assert source != this : "Violation of: source is not this";
      assert source instanceof InventoryItem1 : "Violation of: source is of "
      + "dynamic type InventoryItem1";
      InventoryItem1 localSource = (InventoryItem1) source;
      this.attributes = localSource.attributes;
      this.name = localSource.name;
      this.quantity = localSource.quantity;
      localSource.createNewRep();
    }
    /*
    * Kernel methods -------------------------------------------------------
    */
    @Override
    public final String name() {
      return this.name;
    }

    @Override
    public final int quantity() {
      return this.quantity;
    }

    @Override
    public final Map<String, String> attributes() {
      return this.attributes;
    }
  }
/*
* Kernel methods -------------------------------------------------------
*/


  @Override
  public void add(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    this.hashTable[hashBucket].add(barcode, new InventoryItem1());
    this.size++;
  }


  @Override
  public Inventory.InventoryItem remove(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    InventoryItem1 item = this.hashTable[hashBucket].value(barcode);
    this.hashTable[hashBucket].remove(barcode);
    this.size--;
    return item;
  }

    @Override
    public Inventory.InventoryItem removeAny()  {
      assert this.size > 0 : "Violation of: this.size > 0";
      InventoryItem1 value = new InventoryItem1();
      for (int i = 0; i < this.hashTable.length; i++) {
        if (this.hashTable[i].size() > 0) {
          Map.Pair<String, InventoryItem1> removed = this.hashTable[i].removeAny();
            if (removed.value().attributes().hasKey("barcode")) {
              removed.value().attributes().replaceValue("barcode", removed.key());
            } else {
              removed.value().attributes().add("barcode", removed.key());
            }
            value = removed.value();
            this.size--;
            return value;
        }
      }
      this.size--;
      return value;
    };

    @Override
    public int size() {
      return this.size;
    }

  @Override
  public void increment(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    this.hashTable[hashBucket].value(barcode).quantity++;
  }

  @Override
  public void decrement(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    this.hashTable[hashBucket].value(barcode).quantity--;
  }

  @Override
  public int quantity(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    return this.hashTable[hashBucket].value(barcode).quantity();
  }


  @Override
  public String name(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    return this.hashTable[hashBucket].value(barcode).name();
  }

  @Override
  public void setName(String barcode, String name) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    this.hashTable[hashBucket].value(barcode).name = name;
  }

  @Override
  public void addAttribute(String barcode, String name, String content) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    assert !this.hashTable[hashBucket].value(barcode).attributes.hasKey(name)
    : "Violation of: attributes(barcode) does not contain {@code name}";
    this.hashTable[hashBucket].value(barcode).attributes.add(name, content);
  }

  @Override
  public String getAttribute(String barcode, String name) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    return this.hashTable[hashBucket].value(barcode).attributes.value(name);
  }

  @Override
  public String removeAttribute(String barcode, String name) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    String val = this.hashTable[hashBucket].value(barcode).attributes.value(name);
    this.hashTable[hashBucket].value(barcode).attributes.remove(name);
    return val;
  }


  @Override
  public Set<String> attributes(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    Map<String, String> attr = this.hashTable[hashBucket].value(barcode).attributes();
    Map<String, String> temp = attr.newInstance();
    Set<String> result = new Set2<String>();
    while (attr.size() > 0) {
      Map.Pair<String, String> current = attr.removeAny();
      result.add(current.key());
      temp.add(current.key(), current.value());
    }
    this.hashTable[hashBucket].value(barcode).attributes.transferFrom(temp);
    return result;
  }

  @Override
  public boolean contains(String barcode) {
    int hashKey = barcode.hashCode();
    int hashBucket = mod(hashKey, this.hashTable.length);
    boolean result = false;
    if (this.hashTable[hashBucket].hasKey(barcode)) {
      result = true;
    }
    return result;
  }

  @Override
  public Iterator<Inventory.InventoryItem> iterator() {
    return new Inventory1Iterator();
  }

  /**
   * Iterator implementation.
   */
  private final class Inventory1Iterator implements Iterator<Inventory.InventoryItem> {

    /**
     * The index that the iterator is currently at within the hashMap.
     */
    private int hashMapIndex;

    /**
     * The map iterator.
     */
    private Iterator<Map.Pair<String, InventoryItem1>> mapIterator;

    /**
     * No-args constructor.
     */
    private Inventory1Iterator() {
        this.hashMapIndex = 0;
        this.mapIterator = Inventory1.this.hashTable[this.hashMapIndex].iterator();
    }

    @Override
    public boolean hasNext() {
      if (this.mapIterator.hasNext()) {
        return true;
      } else {
        int tempHashMapIndex = this.hashMapIndex;
        while (tempHashMapIndex < Inventory1.this.hashTable.length - 1) {
          tempHashMapIndex++;
          Iterator<Map.Pair<String, InventoryItem1>> tempMapIterator
           = Inventory1.this.hashTable[tempHashMapIndex].iterator();
          if (tempMapIterator.hasNext()) {
            return true;
          }
        }
      }
      return false;
    }

    @Override
    public Inventory.InventoryItem next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      if (this.mapIterator.hasNext()) {
        Map.Pair<String, InventoryItem1> result = this.mapIterator.next();
        if (result.value().attributes.hasKey("barcode")) {
          result.value().attributes.remove("barcode");
        }
        result.value().attributes().add("barcode", result.key());
        return result.value();
      } else {
        while (this.hashMapIndex < Inventory1.this.hashTable.length - 1) {
          this.hashMapIndex++;
          this.mapIterator = Inventory1.this.hashTable[this.hashMapIndex].iterator();
          if (this.mapIterator.hasNext()) {
            Map.Pair<String, InventoryItem1> result = this.mapIterator.next();
            if (result.value().attributes.hasKey("barcode")) {
              result.value().attributes.remove("barcode");
            }
            result.value().attributes().add("barcode", result.key());
            return result.value();
          }
        }
      }
      /*
       * This return statement will never execute.  The only case in which one of
       * the above returns would not next element is when this does not have next,
       * in which case a NoSuchElementException is thrown.
       */
      return null;
    }

  }

}

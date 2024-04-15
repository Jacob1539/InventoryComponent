import static org.junit.Assert.assertEquals;
import org.junit.Test;

import components.inventory.Inventory;
import components.inventory.Inventory1;

/**
 * Comprehensive Junit text fixture for {@code InventorySecondary} and
 * methods (abstract class methods).
 *
 * @author Jacob Witt
 */
public class InventorySecondaryTest {

    /**
     * Create an inventory with the specified arguments.
     *
     * @param args
     *      the barcodes add to the inventory
     * @return
     *      the created inventory
     */
    private Inventory createFromArgsTest(String... args) {
        Inventory result = new Inventory1();
        for (String s: args) {
            result.add(s);
        }
        return result;
    }

    /**
      * Tests search on an empty inventory.
      */
      @Test
      public final void testSearchEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        assertEquals("", test.search("anything"));
        assertEquals(expected, test);
      }

      /**
      * Tests search - found standard case.
      */
      @Test
      public final void testSearchFoundStandard() {
        Inventory test = createFromArgsTest("hey", "hi", "hello");
        Inventory expected = createFromArgsTest("hey", "hi", "hello");
        test.setName("hey", "Greeting 1");
        test.setName("hi", "Greeting 2");
        test.setName("hello", "Greeting 3");
        expected.setName("hey", "Greeting 1");
        expected.setName("hi", "Greeting 2");
        expected.setName("hello", "Greeting 3");
        assertEquals("hello", test.search("Greeting 3"));
        assertEquals(expected, test);
      }

      /**
      * Tests search - found with some barcodes not having a name case.
      */
      @Test
      public final void testSearchFoundSomeUnnamed() {
        Inventory test = createFromArgsTest("hey", "hi", "hello");
        Inventory expected = createFromArgsTest("hey", "hi", "hello");
        test.setName("hello", "Greeting 3");
        expected.setName("hello", "Greeting 3");
        assertEquals("hello", test.search("Greeting 3"));
        assertEquals(expected, test);
      }

      /**
      * Tests search - found one element case.
      */
      @Test
      public final void testSearchFoundOneElement() {
        Inventory test = createFromArgsTest("hello");
        Inventory expected = createFromArgsTest("hello");
        test.setName("hello", "Greeting 1");
        expected.setName("hello", "Greeting 1");
        assertEquals("hello", test.search("Greeting 1"));
        assertEquals(expected, test);
      }

      /**
      * Tests search - not found standard case.
      */
      @Test
      public final void testSearchNotFoundStandard() {
        Inventory test = createFromArgsTest("hey", "hi", "hello");
        Inventory expected = createFromArgsTest("hey", "hi", "hello");
        test.setName("hey", "Greeting 1");
        test.setName("hi", "Greeting 2");
        test.setName("hello", "Greeting 3");
        expected.setName("hey", "Greeting 1");
        expected.setName("hi", "Greeting 2");
        expected.setName("hello", "Greeting 3");
        assertEquals("", test.search("Greeting 4"));
        assertEquals(expected, test);
      }

      /**
      * Tests search - not found one element case.
      */
      @Test
      public final void testSearchNotFoundOneElement() {
        Inventory test = createFromArgsTest("hello");
        Inventory expected = createFromArgsTest("hello");
        test.setName("hello", "Greeting 1");
        expected.setName("hello", "Greeting 1");
        assertEquals("", test.search("Greeting 2"));
        assertEquals(expected, test);
      }

      /**
      * Tests add with a barcode and name - from empty.
      */
      @Test
      public final void testAddWithNameEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest("hey");
        test.add("hey", "Greeting 1");
        expected.setName("hey", "Greeting 1");
        assertEquals(expected, test);
      }

      /**
      * Tests add with a barcode and name - standard case.
      */
      @Test
      public final void testAddWithNameStandard() {
        Inventory test = createFromArgsTest("hey", "hi");
        Inventory expected = createFromArgsTest("hey", "hi", "hello");
        test.add("hello", "Greeting");
        expected.setName("hello", "Greeting");
        assertEquals(expected, test);
      }

      /**
      * Tests setAttribute.
      */
      @Test
      public final void testSetAttribute() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "Attr1", "AttrVal1");
        test.addAttribute("element1", "Attr2", "AttrVal2");
        test.setAttribute("element1", "Attr1", "AttrVal1B");
        expected.addAttribute("element1", "Attr1", "AttrVal1B");
        expected.addAttribute("element1", "Attr2", "AttrVal2");
        assertEquals(expected, test);
      }

      /**
      * Tests has attribute - empty inventory.
      */
      @Test
      public final void testHasAttributeEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        assertEquals(false, test.hasAttribute("abc", "def"));
        assertEquals(expected, test);
      }

      /**
      * Tests has attribute - non-empty inventory, empty attributes list.
      */
      @Test
      public final void testHasAttributeNoAttributes() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        assertEquals(false, test.hasAttribute("element1", "def"));
        assertEquals(expected, test);
      }

      /**
      * Tests has attribute - different item has the attribute (should result in false).
      */
      @Test
      public final void testHasAttributeWrongItem() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "A", "aaa");
        test.addAttribute("element2", "B", "bbb");
        expected.addAttribute("element1", "A", "aaa");
        expected.addAttribute("element2", "B", "bbb");
        assertEquals(false, test.hasAttribute("element2", "A"));
        assertEquals(expected, test);
      }

      /**
      * Tests has attribute - true standard case.
      */
      @Test
      public final void testHasAttributeTrueStandard() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "A", "aaa");
        test.addAttribute("element2", "B", "bbb");
        expected.addAttribute("element1", "A", "aaa");
        expected.addAttribute("element2", "B", "bbb");
        assertEquals(true, test.hasAttribute("element2", "B"));
        assertEquals(expected, test);
      }

      /**
      * Tests has attribute - false standard case.
      */
      @Test
      public final void testHasAttributeFalseStandard() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "A", "aaa");
        test.addAttribute("element2", "B", "bbb");
        expected.addAttribute("element1", "A", "aaa");
        expected.addAttribute("element2", "B", "bbb");
        assertEquals(false, test.hasAttribute("element2", "C"));
        assertEquals(expected, test);
      }

      /**
      * Tests combine - standard case (no info - barcodes only).
      */
      @Test
      public final void testCombineStandard() {
        Inventory test1 = createFromArgsTest("element1", "element2");
        Inventory test2 = createFromArgsTest("element2", "element3");
        Inventory expected = createFromArgsTest("element1", "element2", "element3");
        Inventory blank = createFromArgsTest();
        test1.combine(test2);
        assertEquals(expected, test1);
        assertEquals(blank, test2);
      }

      /**
      * Tests combine where there are identical barcodes and same additional info.
      */
      @Test
      public final void testCombineSameBarcodeSameInfo() {
        Inventory test1 = createFromArgsTest("element1", "element2");
        Inventory test2 = createFromArgsTest("element2", "element3");
        Inventory expected = createFromArgsTest("element1", "element2", "element3");
        Inventory blank = createFromArgsTest();
        test1.addAttribute("element1", "Attr1", "AttrVal1");
        test1.addAttribute("element2", "Attr2", "AttrVal2");
        test2.addAttribute("element3", "Attr3", "AttrVal3");
        test2.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element1", "Attr1", "AttrVal1");
        expected.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element3", "Attr3", "AttrVal3");
        test1.setQuantity("element1", 1);
        test1.setQuantity("element2", 3);
        test2.setQuantity("element2", 5);
        test1.setName("element2", "Name2");
        test2.setName("element2", "Name2");
        expected.setName("element2", "Name2");
        expected.setQuantity("element1", 1);
        expected.setQuantity("element2", 8);
        test1.combine(test2);
        assertEquals(expected, test1);
        assertEquals(blank, test2);
      }

      /**
      * Tests combine where there are identical barcodes and different additional info.
      */
      @Test
      public final void testCombineSameBarcodeDifferentInfo() {
        Inventory test1 = createFromArgsTest("element1", "element2");
        Inventory test2 = createFromArgsTest("element2", "element3");
        Inventory expected = createFromArgsTest("element1", "element2", "element3");
        Inventory blank = createFromArgsTest();
        test1.addAttribute("element1", "Attr1", "AttrVal1");
        test1.addAttribute("element2", "Attr2", "AttrVal2");
        test2.addAttribute("element3", "Attr3", "AttrVal3");
        test2.addAttribute("element2", "GETRIDOFTHIS", "BAD");
        expected.addAttribute("element1", "Attr1", "AttrVal1");
        expected.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element3", "Attr3", "AttrVal3");
        test1.setQuantity("element1", 1);
        test1.setQuantity("element2", 3);
        test2.setQuantity("element2", 5);
        test1.setName("element2", "Name2");
        test2.setName("element2", "WRONGNAME");
        test2.setName("element3", "Keep Name 3!");
        expected.setName("element2", "Name2");
        expected.setName("element3", "Keep Name 3!");
        expected.setQuantity("element1", 1);
        expected.setQuantity("element2", 8);
        test1.combine(test2);
        assertEquals(expected, test1);
        assertEquals(blank, test2);
      }

      /**
      * Tests combine - this is empty, i is non empty.
      */
      @Test
      public final void testCombineThisEmpty() {
        Inventory test1 = createFromArgsTest();
        Inventory test2 = createFromArgsTest("element2", "element3");
        Inventory expected = createFromArgsTest("element2", "element3");
        Inventory blank = createFromArgsTest();
        test2.addAttribute("element3", "Attr3", "AttrVal3");
        test2.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element3", "Attr3", "AttrVal3");
        test2.setQuantity("element2", 5);
        test2.setName("element2", "Name2");
        expected.setName("element2", "Name2");
        expected.setQuantity("element2", 5);
        test1.combine(test2);
        assertEquals(expected, test1);
        assertEquals(blank, test2);
      }

      /**
      * Tests combine - this is non empty, i is empty.
      */
      @Test
      public final void testCombineIEmpty() {
        Inventory test1 = createFromArgsTest();
        Inventory test2 = createFromArgsTest("element2", "element3");
        Inventory expected = createFromArgsTest("element2", "element3");
        Inventory blank = createFromArgsTest();
        test2.addAttribute("element3", "Attr3", "AttrVal3");
        test2.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element2", "Attr2", "AttrVal2");
        expected.addAttribute("element3", "Attr3", "AttrVal3");
        test2.setQuantity("element2", 5);
        test2.setName("element2", "Name2");
        expected.setName("element2", "Name2");
        expected.setQuantity("element2", 5);
        test2.combine(test1);
        assertEquals(expected, test2);
        assertEquals(blank, test1);
      }

      /**
      * Tests combine - this and i are both empty.
      */
      @Test
      public final void testCombineBothEmpty() {
        Inventory test1 = createFromArgsTest();
        Inventory test2 = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        Inventory blank = createFromArgsTest();
        test1.combine(test2);
        assertEquals(expected, test1);
        assertEquals(blank, test2);
      }
}

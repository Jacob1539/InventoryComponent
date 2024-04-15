import static org.junit.Assert.assertEquals;
import org.junit.Test;

import components.inventory.Inventory;
import components.inventory.Inventory1;
import components.map.Map;
import components.set.Set;
/**
 * Comprehensive Junit text fixture for {@code Inventory1}
 * methods (kernel & standard methods).
 *
 * @author Jacob Witt
 */
public class InventoryKernelTest {
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
      * Tests add - standard case.
      */
    @Test
    public final void testAddStandard() {
        Inventory test = createFromArgsTest("BARCODE1");
        Inventory expected = createFromArgsTest("BARCODE2", "BARCODE1", "BARCODE3");
        test.add("BARCODE2");
        test.add("BARCODE3");
        assertEquals(expected, test);
    }

    /**
      * Tests add - from empty.
      */
      @Test
      public final void testAddFromEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest("1234");
        test.add("1234");
        assertEquals(expected, test);
      }

      /**
      * Tests remove - standard case, no data other than barcode.
      */
      @Test
      public final void testRemoveStandardBarcodeOnly() {
        Inventory test = createFromArgsTest("1234", "BarcodeWithWords", "Remove Me");
        Inventory expected = createFromArgsTest("1234", "BarcodeWithWords");
        Inventory.InventoryItem removed = test.remove("Remove Me");
        assertEquals(expected, test);
        assertEquals(removed.name(), "");
        assertEquals(removed.quantity(), 0);
        assertEquals(removed.attributes(), removed.attributes().newInstance());
      }

      /**
      * Tests remove - resulting in empty, no data other than barcode.
      */
      @Test
      public final void testRemoveEmptyResultBarcodeOnly() {
        Inventory test = createFromArgsTest("1234");
        Inventory expected = createFromArgsTest();
        Inventory.InventoryItem removed = test.remove("1234");
        String expectedName = "";
        int expectedQuantity = 0;
        Map<String, String> expectedAttributes = removed.attributes().newInstance();
        assertEquals(expected, test);
        assertEquals(removed.name(), expectedName);
        assertEquals(removed.quantity(), expectedQuantity);
        assertEquals(removed.attributes(), expectedAttributes);
      }

      /**
      * Tests remove - Attributes, name, and nonzero quantity associated with
      * removed item.
      */
      @Test
      public final void testRemoveHasInfo() {
        Inventory test = createFromArgsTest("1234", "BarcodeWithWords", "Remove Me");
        test.addAttribute("Remove Me", "Word Type", "Command");
        test.setName("Remove Me", "My Name!");
        test.setQuantity("Remove Me", 2);
        Inventory expected = createFromArgsTest("1234", "BarcodeWithWords");
        Inventory.InventoryItem removed = test.remove("Remove Me");
        String expectedName = "My Name!";
        int expectedQuantity = 2;
        Map<String, String> expectedAttributes = removed.attributes().newInstance();
        expectedAttributes.add("Word Type", "Command");
        assertEquals(expected, test);
        assertEquals(removed.name(), expectedName);
        assertEquals(removed.quantity(), expectedQuantity);
        assertEquals(removed.attributes(), expectedAttributes);
      }

      /**
      * Tests removeAny - standard case, no data other than barcode.
      */
      @Test
      public final void testRemoveAnyStandardBarcodeOnly() {
        Inventory test = createFromArgsTest("12", "34", "56", "78");
        Inventory expected = createFromArgsTest("12", "34", "56", "78");
        Inventory.InventoryItem removed = test.removeAny();
        assertEquals(true, expected.contains(removed.attributes().value("barcode")));
        expected.remove(removed.attributes().value("barcode"));
        assertEquals(expected, test);
      }

      /**
      * Tests removeAny - resulting in empty, no data other than barcode.
      */
      @Test
      public final void testRemoveAnyEmptyResultBarcodeOnly() {
        Inventory test = createFromArgsTest("12");
        Inventory expected = createFromArgsTest();
        Inventory.InventoryItem removed = test.removeAny();
        assertEquals(expected, test);
        assertEquals("12", removed.attributes().value("barcode"));
        assertEquals(0, removed.quantity());
        assertEquals("", removed.name());
      }

      /**
      * Tests removeAny - Attributes, name, and nonzero quantity associated with
      * removed item.
      */
      @Test
      public final void testRemoveAnyHasInfo() {
        Inventory test = createFromArgsTest("12", "34");
        Inventory expected = createFromArgsTest("12", "34");
        test.setName("12", "FirstItem");
        test.setQuantity("12", 2);
        test.addAttribute("12", "Attr12", "AttrVal12");
        test.setName("34", "SecondItem");
        test.setQuantity("34", 1);
        test.addAttribute("34", "Attr34", "AttrVal34");
        expected.setName("12", "FirstItem");
        expected.setQuantity("12", 2);
        expected.addAttribute("12", "Attr12", "AttrVal12");
        expected.setName("34", "SecondItem");
        expected.setQuantity("34", 1);
        expected.addAttribute("34", "Attr34", "AttrVal34");
        Inventory.InventoryItem removed = test.removeAny();
        assertEquals(true, expected.contains(removed.attributes().value("barcode")));
        Inventory.InventoryItem removedExpected =
          expected.remove(removed.attributes().value("barcode"));
        removed.attributes().remove("barcode");
        assertEquals(expected, test);
        assertEquals(removedExpected, removed);
      }

      /**
       * Tests size - standard case.
       */
      @Test
      public final void testSizeStandard() {
        Inventory test = createFromArgsTest("1234", "5678", "9101112");
        Inventory expected = createFromArgsTest("1234", "5678", "9101112");
        assertEquals(test.size(), 3);
        assertEquals(expected, test);
      }

      /**
       * Tests size - zero elements.
       */
      @Test
      public final void testSizeEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        assertEquals(test.size(), 0);
        assertEquals(expected, test);
      }

      /**
       * Tests size - one element.
       */
      @Test
      public final void testSizeOne() {
        Inventory test = createFromArgsTest("1234");
        Inventory expected = createFromArgsTest("1234");
        assertEquals(test.size(), 1);
        assertEquals(expected, test);
      }

      /**
       * Tests size - More elements than hash buckets.
       */
      @Test
      public final void testSizeLarge() {
        Inventory test = createFromArgsTest("AA", "AB", "AC", "Ad",
        "Ae", "Af", "Ag", "Ah", "Aj", "Ak", "Al", "Am", "An", "Ap", "A[C]",
        "A1", "2B", "3", "4", "5", "Banana", "this pattern is unpredictable",
        "ww", "www", "wwww", "wwwww", "wwwwww", "wwwwwww", "wwwwwwww", "w9",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42",
        "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
         "1AA", "1AB", "1AC", "1Ad", "1Ae", "1Af", "1Ag", "1Ah", "1Aj", "1Ak",
         "1Al", "1Am", "1An", "1Ap", "1A[C]", "1A1", "12B", "13", "14", "15",
         "1Banana", "1this pattern isn't not unpredictable", "1ww", "1www", "1wwww",
         "www1ww", "wwww1ww", "ww1wwwww", "wwwww1www", "1w9", "131", "132",
         "133", "314", "315", "316", "317", "hey", "319", "401", "411", "421",
          "431", "441", "451", "461", "471", "418", "419", "501", "511", "512",
          "barcode variety!", "514", "this is element number 109");
          Inventory expected = createFromArgsTest("AA", "AB", "AC", "Ad",
        "Ae", "Af", "Ag", "Ah", "Aj", "Ak", "Al", "Am", "An", "Ap", "A[C]",
        "A1", "2B", "3", "4", "5", "Banana", "this pattern is unpredictable",
        "ww", "www", "wwww", "wwwww", "wwwwww", "wwwwwww", "wwwwwwww", "w9",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42",
        "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
         "1AA", "1AB", "1AC", "1Ad", "1Ae", "1Af", "1Ag", "1Ah", "1Aj", "1Ak",
         "1Al", "1Am", "1An", "1Ap", "1A[C]", "1A1", "12B", "13", "14", "15",
         "1Banana", "1this pattern isn't not unpredictable", "1ww", "1www", "1wwww",
         "www1ww", "wwww1ww", "ww1wwwww", "wwwww1www", "1w9", "131", "132",
         "133", "314", "315", "316", "317", "hey", "319", "401", "411", "421",
          "431", "441", "451", "461", "471", "418", "419", "501", "511", "512",
          "barcode variety!", "514", "this is element number 109");
        assertEquals(test.size(), 109);
        assertEquals(expected, test);
      }

      /**
       * Tests size - standard case, nonzero quantities.
       */
      @Test
      public final void testSizeStandardNonzeroQuantity() {
        Inventory test = createFromArgsTest("1234", "5678", "9101112");
        test.setQuantity("1234", 5);
        test.setQuantity("5678", 1);
        Inventory expected = createFromArgsTest("1234", "5678", "9101112");
        expected.setQuantity("1234", 5);
        expected.setQuantity("5678", 1);
        assertEquals(test.size(), 3);
        assertEquals(expected, test);
      }

      /**
       * Tests increment - 0 to 1.
       */
      @Test
      public final void testIncrementToOne() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.increment("element1");
        assertEquals(test.quantity("element1"), 1);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests increment - 0 to 3.
       */
      @Test
      public final void testIncrementToThree() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.increment("element1");
        test.increment("element1");
        test.increment("element1");
        assertEquals(3, test.quantity("element1"));
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests increment - 5 to 6.
       */
      @Test
      public final void testIncrementFiveToSix() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setQuantity("element1", 5);
        test.increment("element1");
        assertEquals(test.quantity("element1"), 6);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests increment on multiple elements.
       */
      @Test
      public final void testIncrementMultipleElements() {
        Inventory test = createFromArgsTest("element1", "element2", "element0");
        test.increment("element1");
        test.increment("element2");
        test.increment("element2");
        assertEquals(test.quantity("element1"), 1);
        assertEquals(test.quantity("element2"), 2);
        assertEquals(test.quantity("element0"), 0);
      }

      /**
       * Tests decrement - 1 to 0.
       */
      @Test
      public final void testDecrementOneToZero() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setQuantity("element1", 1);
        test.decrement("element1");
        assertEquals(test.quantity("element1"), 0);
      }

      /**
       * Tests increment - 3 to 0.
       */
      @Test
      public final void testDecrementThreeToZero() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setQuantity("element1", 3);
        test.decrement("element1");
        test.decrement("element1");
        test.decrement("element1");
        assertEquals(test.quantity("element1"), 0);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests decrement - 6 to 5.
       */
      @Test
      public final void testDecrementSixToFive() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setQuantity("element1", 6);
        test.decrement("element1");
        assertEquals(test.quantity("element1"), 5);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests decrement on multiple elements.
       */
      @Test
      public final void testDecrementMultipleElements() {
        Inventory test = createFromArgsTest("element1", "element2", "element0");
        test.setQuantity("element1", 2);
        test.setQuantity("element2", 4);
        test.decrement("element1");
        test.decrement("element2");
        test.decrement("element2");
        assertEquals(test.quantity("element1"), 1);
        assertEquals(test.quantity("element2"), 2);
        assertEquals(test.quantity("element0"), 0);
      }

      /**
       * Tests quantity based on the no args constructor.
       */
      @Test
      public final void testQuantityZeroConstructor() {
        Inventory test = createFromArgsTest("element1", "element2");
        assertEquals(test.quantity("element1"), 0);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests quantity based increment.
       */
      @Test
      public final void testQuantityOneIncrement() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.increment("element1");
        assertEquals(test.quantity("element1"), 1);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests quantity and setQuantity based on each other - standard case.
       */
      @Test
      public final void testQuantityMethodsStandard() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setQuantity("element1", 5);
        assertEquals(test.quantity("element1"), 5);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests quantity and setQuantity based on each other - zero case.
       */
      @Test
      public final void testQuantityMethodsZero() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.increment("element2");
        test.setQuantity("element2", 0);
        assertEquals(test.quantity("element1"), 0);
        assertEquals(test.quantity("element2"), 0);
      }

      /**
       * Tests quantity and setQuantity based on each other and check with increment.
       */
      @Test
      public final void testQuantityMethodsOne() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.increment("element2");
        test.setQuantity("element1", 1);
        assertEquals(test.quantity("element1"), 1);
        assertEquals(test.quantity("element2"), test.quantity("element1"));
      }

      /**
       * Tests name based on the no args constructor.
       */
      @Test
      public final void testNameConstructor() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        assertEquals(test.name("element1"), "");
        assertEquals(expected, test);
      }

      /**
       * Tests name and setName based on each other - standard case.
       */
      @Test
      public final void testNameMethodsStandard() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setName("element1", "Name1");
        test.setName("element2", "Name2");
        assertEquals(test.name("element1"), "Name1");
        assertEquals(test.name("element2"), "Name2");
      }

      /**
       * Tests name and setName based on each other - changing name to
       * something then making it empty again..
       */
      @Test
      public final void testNameMethodsBackToEmpty() {
        Inventory test = createFromArgsTest("element1", "element2");
        test.setName("element1", "Name1");
        test.setName("element1", "");
        assertEquals(test.name("element1"), "");
        assertEquals(test.name("element2"), "");
      }

      /**
       * Tests attributes based on the no args constructor.
       */
      @Test
      public final void testAttributesConstructor() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expectedInv = createFromArgsTest("element1", "element2");
        Set<String> result = test.attributes("element1");
        Set<String> expected = result.newInstance();
        assertEquals(expected, result);
        assertEquals(expectedInv, test);
      }

      /**
       * Tests addAttribute and getAttribute on based on each other - one attribute.
       */
      @Test
      public final void testAddGetAttributesOne() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "Attr1", "AttrVal1");
        expected.addAttribute("element1", "Attr1", "AttrVal1");
        assertEquals("AttrVal1", test.getAttribute("element1", "Attr1"));
        assertEquals(expected, test);
      }

      /**
       * Tests addAttribute and getAttribute on based on each other - multiple attributes.
       */
      @Test
      public final void testAddGetAttributesMultiple() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expected = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "Attr1", "AttrVal1");
        test.addAttribute("element1", "Attr2", "AttrVal2");
        expected.addAttribute("element1", "Attr1", "AttrVal1");
        expected.addAttribute("element1", "Attr2", "AttrVal2");
        assertEquals("AttrVal1", test.getAttribute("element1", "Attr1"));
        assertEquals("AttrVal2", test.getAttribute("element1", "Attr2"));
        assertEquals(expected, test);
      }

      /**
       * Tests addAttribute and attributes on based on each other - multiple attributes.
       */
      @Test
      public final void testAddAttributesSetMultiple() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expectedInv = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "Attr1", "AttrVal1");
        test.addAttribute("element1", "Attr2", "AttrVal2");
        expectedInv.addAttribute("element1", "Attr1", "AttrVal1");
        expectedInv.addAttribute("element1", "Attr2", "AttrVal2");
        Set<String> attributes = test.attributes("element1");
        Set<String> expected = attributes.newInstance();
        expected.add("Attr1");
        expected.add("Attr2");
        assertEquals(expected, attributes);
        assertEquals(expectedInv, test);
      }

      /**
       * Tests addAttribute, removeAttribute and attributes on based on each
       * other when all added attributes are removed.
       */
      @Test
      public final void testAddRemoveAttributesEmptySet() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expectedInv = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "Attr1", "AttrVal1");
        String removed = test.removeAttribute("element1", "Attr1");
        Set<String> attributes = test.attributes("element1");
        Set<String> expected = attributes.newInstance();
        assertEquals(expected, attributes);
        assertEquals("AttrVal1", removed);
        assertEquals(expectedInv, test);
      }

      /**
       * Tests all four attributes kernel methods based on each other - multiple
       *  attributes.
       */
      @Test
      public final void testAllAttributesMethodsMultiple() {
        Inventory test = createFromArgsTest("element1", "element2");
        Inventory expectedInv = createFromArgsTest("element1", "element2");
        test.addAttribute("element1", "Attr1", "AttrVal1");
        test.addAttribute("element1", "Attr2", "AttrVal2");
        test.addAttribute("element1", "Attr3", "AttrVal3");
        expectedInv.addAttribute("element1", "Attr1", "AttrVal1");
        expectedInv.addAttribute("element1", "Attr3", "AttrVal3");
        String removed = test.removeAttribute("element1", "Attr2");
        Set<String> attributes = test.attributes("element1");
        Set<String> expected = attributes.newInstance();
        expected.add("Attr1");
        expected.add("Attr3");
        assertEquals(expected, attributes);
        assertEquals("AttrVal2", removed);
        assertEquals("AttrVal1", test.getAttribute("element1", "Attr1"));
        assertEquals("AttrVal3", test.getAttribute("element1", "Attr3"));
        assertEquals(expectedInv, test);
      }

      /**
       * Tests contains on an empty inventory.
       */
      @Test
      public final void testContainsEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        assertEquals(false, test.contains("Anything"));
        assertEquals(expected, test);
      }

      /**
       * Tests contains - true standard case.
       */
      @Test
      public final void testContainsTrueStandard() {
        Inventory test = createFromArgsTest("Thing1", "Thing2", "Thing3");
        Inventory expected = createFromArgsTest("Thing1", "Thing2", "Thing3");
        assertEquals(true, test.contains("Thing2"));
        assertEquals(expected, test);
      }

      /**
       * Tests contains - true one element case.
       */
      @Test
      public final void testContainsTrueOneElement() {
        Inventory test = createFromArgsTest("Thing1");
        Inventory expected = createFromArgsTest("Thing1");
        assertEquals(true, test.contains("Thing1"));
        assertEquals(expected, test);
      }

      /**
       * Tests contains - false standard case.
       */
      @Test
      public final void testContainsFalseStandard() {
        Inventory test = createFromArgsTest("Thing1", "Thing2", "Thing3");
        Inventory expected = createFromArgsTest("Thing1", "Thing2", "Thing3");
        assertEquals(false, test.contains("Not A Thing"));
        assertEquals(expected, test);
      }

      /**
       * Tests contains - false one element case.
       */
      @Test
      public final void testContainsFalseOneElement() {
        Inventory test = createFromArgsTest("Thing1");
        Inventory expected = createFromArgsTest("Thing1");
        assertEquals(false, test.contains("Not A Thing"));
        assertEquals(expected, test);
      }
      /*
       * Tests of standard methods
       */
      /**
       * Tests clear - standard case.
       */
      @Test
      public final void testClearStandard() {
        Inventory test = createFromArgsTest("Thing1", "Thing2");
        Inventory expected = createFromArgsTest();
        test.clear();
        assertEquals(expected, test);
      }

      /**
       * Tests clear - already empty case.
       */
      @Test
      public final void testClearEmpty() {
        Inventory test = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        test.clear();
        assertEquals(expected, test);
      }

      /**
       * Tests newInstance - standard case.
       */
      @Test
      public final void testNewInstanceStandard() {
        Inventory testOut = createFromArgsTest("Thing1", "Thing2");
        Inventory expected = createFromArgsTest();
        Inventory test = testOut.newInstance();
        assertEquals(expected, test);
      }

      /**
       * Tests newInstance - already empty case.
       */
      @Test
      public final void testNewInstanceEmpty() {
        Inventory testOut = createFromArgsTest();
        Inventory expected = createFromArgsTest();
        Inventory test = testOut.newInstance();
        assertEquals(expected, test);
      }

      /**
       * Tests transferFrom - standard case.
       */
      @Test
      public final void testTransferFromStandard() {
        Inventory testOut = createFromArgsTest("Thing1", "Thing2");
        Inventory testIn = createFromArgsTest("Thing3");
        Inventory expected = createFromArgsTest("Thing1", "Thing2");
        Inventory blank = createFromArgsTest();
        testIn.transferFrom(testOut);
        assertEquals(expected, testIn);
        assertEquals(blank, testOut);
      }

      /**
       * Tests transferFrom - both empty case.
       */
      @Test
      public final void testTransferFromBothEmpty() {
        Inventory testOut = createFromArgsTest();
        Inventory testIn = createFromArgsTest();
        Inventory blank = createFromArgsTest();
        testIn.transferFrom(testOut);
        assertEquals(blank, testIn);
        assertEquals(blank, testOut);
      }

      /**
       * Tests transferFrom - this empty case.
       */
      @Test
      public final void testTransferFromThisEmpty() {
        Inventory testOut = createFromArgsTest("Hi", "Hello", "Hey");
        Inventory testIn = createFromArgsTest();
        Inventory expected = createFromArgsTest("Hi", "Hello", "Hey");
        Inventory blank = createFromArgsTest();
        testIn.transferFrom(testOut);
        assertEquals(expected, testIn);
        assertEquals(blank, testOut);
      }

      /**
       * Tests transferFrom - from empty case (this should be empty after the call).
       */
      @Test
      public final void testTransferFromResultingThisEmpty() {
        Inventory testIn = createFromArgsTest("Hi", "Hello", "Hey");
        Inventory testOut = createFromArgsTest();
        Inventory blank = createFromArgsTest();
        testIn.transferFrom(testOut);
        assertEquals(blank, testIn);
        assertEquals(blank, testOut);
      }

      /**
       * Tests transferFrom - duplicate elements case.
       */
      @Test
      public final void testTransferFromDuplicateElements() {
        Inventory testOut = createFromArgsTest("Thing1", "Thing2");
        Inventory testIn = createFromArgsTest("Thing1");
        Inventory expected = createFromArgsTest("Thing1", "Thing2");
        Inventory blank = createFromArgsTest();
        testIn.transferFrom(testOut);
        assertEquals(expected, testIn);
        assertEquals(blank, testOut);
      }
}

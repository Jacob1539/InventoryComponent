# Inventory Component

This project provides an inventory component that follows the design structure from the software sequence at The Ohio State University.  This project also includes two demo files with examples of how the inventory component could be used.

### Design Structure

The design structure of the component features two interfaces, an abstract class, and a regular class.  In addition, the component's top level interface extends the standard interface from the OSU CSE components.  Documentation for the standard interface is available via this link: https://web.cse.ohio-state.edu/software/common/doc/components/standard/Standard.html

The "InventoryKernel" interface contains contracts for the methods that are essential to the functionality of the component.  The "Inventory" interface contains additional helpful methods, but these methods are not necesary for the functionality of the component.  The "InventorySecondary" class is an abstract class inmplementing methods from the object and "Inventory" interfaces.  All methods within this abstract class are implemented using the kernel methods.  The "Inventory1" class provides the representation of inventory as well as the implementation of its kernel methods.

### Demo Files

There are two demo files available that show different uses of the inventory component.  For the sake of demonstration, both demos have options to add sample data.  The method to add sample data is described within the documentation of each demo.

The first demo file, "DemoInventoryManagementGUI", provides a basic GUI that demonstrates the component.  The GUI has options to add new items, view and modify the quantity of individual items, and view all items within the inventory.

The second demo file, "DemoReorderSystem", is a command line demonstration of the component.  This demonstration gives the user the option to add and remove items, modify item quantities, and view all items within the inventory.  Additionally, this demo provides functionality to quickly reorder any items that are out of stock.

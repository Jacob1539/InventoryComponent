import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import components.inventory.Inventory;
import components.inventory.Inventory1;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


/**
 * Demo program with GUI that utilizes the inventory component.
 *
 * Note: For the sake of demonstration, the user can load sample data
 * into the inventory by typing "Sample" in the add - barcode (top right) field
 * and clicking add item.
 * The program does not test every method within the inventory
 * component.  An additional JUnit test file has been created to test
 * each method individually.
 * For the sake of simplicity, the GUI does not include item attributes.  The
 * second demo utilizes attributes.
 *
 * @author Jacob Witt
 */
public final class DemoInventoryManagementGUI extends JFrame implements ActionListener {
    /*
     * Declaring GUI Elements
     */
    /**
     * Popup frames.
     */
    private JFrame viewInvPopup, itemInfoPopup;
    /**
     * Buttons.
     */
    private JButton addItem, viewItem, viewInv, plus, minus;
    /**
     * Labels.
     */
    private JLabel barcode, name, quantity, view, add, or, barcodeVal,
        nameVal, quantityVal, plusQty, minusQty;
    /**
     * Text Fields.
     */
    private JTextField barcodeFld, nameFld, quantityFld, barcodeFldView;
    /**
     * Panel.
     */
    private JPanel mainPanel, viewItemPanel;
    /**
     * Table, to view the whole inventory at once.
     */
    private JTable viewInvTable;
    /**
     * ScrollPane, to hold the table.
     */
    private JScrollPane scroll;
    /**
     * Inventory object to update.
     */
    private Inventory inv = new Inventory1();
    /**
     * Number of columns in a text field.
     */
    private final int txtFldCol = 15;
    /**
     * Number of rows in the panel.
     */
    private final int numRows = 5;
    /**
     * Window width, in pixels.
     */
    private final int windowWidth = 400;
    /**
     * Window height, in pixels.
     */
    private final int windowHeight = 300;
    /**
     * Base 10.
     */
    private final int radix = 10;

    /**
     * Add sample data to inv.
     */
    private void loadSampleData() {
        this.inv.add("1001", "Honeycrisp Apple");
        this.inv.add("1052", "Blueberry");
        this.inv.add("1198", "Watermellon");
        this.inv.add("20040", "Grape");
        this.inv.add("6546");
        this.inv.add("7888");
        this.inv.add("99999");
        final int randomQuantity1 = 10;
        final int randomQuantity2 = 5;
        this.inv.setQuantity("1001", 2);
        this.inv.setQuantity("1052", randomQuantity1);
        this.inv.setQuantity("20040", 1);
        this.inv.setQuantity("7888", randomQuantity2);
        this.inv.addAttribute("6546", "Order Status", "3 Reordered");
    }


    /**
     * Constructor - builds page.
     */
    public DemoInventoryManagementGUI() {
        this.setTitle("Inventory Component Demo");
        this.setSize(windowWidth, windowHeight);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        /*
        * Initialize GUI Elements
        */
        this.addItem = new JButton("Add Item");
        this.viewItem = new JButton("View Item");
        this.viewInv = new JButton("View Inventory");
        this.plus = new JButton("+");
        this.minus = new JButton("-");
        this.barcode = new JLabel("Barcode");
        this.name = new JLabel("Name");
        this.quantity = new JLabel("Quantity");
        this.view = new JLabel("View (Barcode):", SwingConstants.CENTER);
        this.add = new JLabel("Add (Barcode/Name/Qty):", SwingConstants.CENTER);
        this.or = new JLabel("OR", SwingConstants.CENTER);
        this.barcodeVal = new JLabel("Unknown");
        this.nameVal = new JLabel("Unknown");
        this.quantityVal = new JLabel("Unknown");
        this.plusQty = new JLabel("Add 1");
        this.minusQty = new JLabel("Remove 1");
        this.barcodeFld = new JTextField(this.txtFldCol);
        this.barcodeFldView = new JTextField(this.txtFldCol);
        this.nameFld = new JTextField(this.txtFldCol);
        this.quantityFld = new JTextField(this.txtFldCol);
        this.mainPanel = new JPanel(new GridLayout(numRows, 2));
        this.viewItemPanel = new JPanel(new GridLayout(numRows, 2));
        /*
         * Add actionListener to buttons
         */
        this.addItem.addActionListener(this);
        this.viewItem.addActionListener(this);
        this.viewInv.addActionListener(this);
        this.plus.addActionListener(this);
        this.minus.addActionListener(this);
        /*
         * Add elements to main panel
         */

        this.mainPanel.add(this.view);
        this.mainPanel.add(this.add);
        this.mainPanel.add(this.barcodeFldView);
        this.mainPanel.add(this.barcodeFld);
        this.mainPanel.add(this.viewItem);
        this.mainPanel.add(this.nameFld);
        this.mainPanel.add(this.or);
        this.mainPanel.add(this.quantityFld);
        this.mainPanel.add(this.viewInv);
        this.mainPanel.add(this.addItem);
        /*
         * Add elements to viewItemPanel
         */
        this.viewItemPanel.add(this.name);
        this.viewItemPanel.add(this.nameVal);
        this.viewItemPanel.add(this.barcode);
        this.viewItemPanel.add(this.barcodeVal);
        this.viewItemPanel.add(this.quantity);
        this.viewItemPanel.add(this.quantityVal);
        this.viewItemPanel.add(this.plusQty);
        this.viewItemPanel.add(this.plus);
        this.viewItemPanel.add(this.minusQty);
        this.viewItemPanel.add(this.minus);
        /*
         * Add panel
         */
        this.add(this.mainPanel);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addItem) {
            if (this.barcodeFld.getText().equals("")) {
                /*
                 * Prevent form submission without filling out essential field (barcode)
                 */
                this.clearTextFields();
                JOptionPane.showMessageDialog(new JFrame(), "Please enter a barcode");
                return;
            } else if (this.barcodeFld.getText().equals("Sample")) {
                this.loadSampleData();
                this.clearTextFields();
            } else {
                if (inv.contains(this.barcodeFld.getText())) {
                    /*
                    * Prevent addition of a barcode that already exists in this
                    */
                    this.clearTextFields();
                    JOptionPane.showMessageDialog(new JFrame(),
                    "This item has already been added");
                    return;
                }
                try {
                    Integer.parseInt(this.quantityFld.getText());
                } catch (NumberFormatException n) {
                    /*
                    * Ensure quantity is a valid integer
                    */
                    if (!this.quantityFld.getText().equals("")) {
                        this.clearTextFields();
                        JOptionPane.showMessageDialog(new JFrame(),
                        "Please enter a valid quantity");
                        return;
                    }
                }
                this.inv.add(this.barcodeFld.getText(), this.nameFld.getText());
                this.inv.setQuantity(this.barcodeFld.getText(),
                    Integer.parseInt("0" + this.quantityFld.getText(), radix));
                this.clearTextFields();
            }
        } else if (e.getSource() == this.viewItem) {
            if (!inv.contains(this.barcodeFldView.getText())) {
                /*
                 * Ensure that barcode exists within this
                 */
                this.clearTextFields();
                JOptionPane.showMessageDialog(new JFrame(),
                "Barcode not found");
                return;
            }
            /*
             * Update label values
             */
            String barcode = this.barcodeFldView.getText();
            this.nameVal.setText(this.inv.name(barcode));
            this.barcodeVal.setText(barcode);
            this.quantityVal.setText("" + this.inv.quantity(barcode));
            /*
             * Create a new JFrame for the popup
             */
            this.itemInfoPopup = new JFrame("Item Information (" + barcode + ")");
            this.itemInfoPopup.add(this.viewItemPanel);
            this.itemInfoPopup.setSize(windowWidth, windowHeight);
            this.itemInfoPopup.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.itemInfoPopup.setVisible(true);
            /*
             * Clear fields back in the main window
             */
            this.clearTextFields();
        } else if (e.getSource() == this.viewInv) {
            /*
             * Load data into a 2d array to use the optimal JTable constructor
             */
            String[] colNames = {"Barcode", "Name", "Quantity"};
            String[][] data = new String[this.inv.size()][colNames.length];
            int count = 0;
            Iterator<Inventory.InventoryItem> itr = inv.iterator();
            while (itr.hasNext()) {
                Inventory.InventoryItem i = itr.next();
                data[count][0] = i.attributes().value("barcode");
                data[count][1] = i.name();
                data[count][2] = "" + i.quantity();
                count++;
            }
            this.viewInvTable = new JTable(data, colNames);
            this.scroll = new JScrollPane(this.viewInvTable);
            this.viewInvPopup = new JFrame("View Inventory");
            this.viewInvPopup.add(this.scroll);
            this.viewInvPopup.setSize(windowWidth, windowHeight);
            this.viewInvPopup.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.viewInvPopup.setVisible(true);
            /*
             * Clear fields back in the main window
             */
            this.clearTextFields();
        } else if (e.getSource() == this.plus) {
            this.inv.increment(this.barcodeVal.getText());
            /*
             * Update window by "clicking" view item
             */
            this.itemInfoPopup.dispose();
            this.barcodeFldView.setText(this.barcodeVal.getText());
            this.viewItem.doClick();
        } else if (e.getSource() == this.minus) {
            this.inv.decrement(this.barcodeVal.getText());
            /*
             * Update window by "clicking" view item
             */
            this.itemInfoPopup.dispose();
            this.barcodeFldView.setText(this.barcodeVal.getText());
            this.viewItem.doClick();
        }

    }

    /**
     * Clear all text fields.
     */
    private void clearTextFields() {
        this.barcodeFld.setText("");
        this.nameFld.setText("");
        this.quantityFld.setText("");
        this.barcodeFldView.setText("");
    }

    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DemoInventoryManagementGUI();
    }

}

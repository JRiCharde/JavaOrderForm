/* Name: William Jeremy RiCharde
 Course: CNT 4714 – Summer 2014
 Assignment title: Program 1 – Event-driven Programming
 Date: Sunday May 25, 2014
 */

import java.util.Scanner;
import java.util.*;
import java.io.*;

// import packages for GUIs
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// import packages for creating and formatting dates
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// import packages for writing to a file
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


// class for creating and handling the order form
public class OrderForm extends JFrame
{
    // input fields
    private JTextField jtfNumItems = new JTextField(40);
    private JTextField jtfCDID = new JTextField(40);
    private JTextField jtfQuant = new JTextField(40);
    private JTextField jtfItemInfo = new JTextField (40);
    private JTextField jtfSubTot = new JTextField (40);
    
    // dynamic labels for the input fields
    private JLabel jlblEnterCDID = new JLabel("Enter CD ID for Item #1:" );
    private JLabel jlblEnterQuant = new JLabel("Enter quantity for Item #1:");
    private JLabel jlblItemInfo = new JLabel("     Item #1 info:");
    private JLabel jlblOrderSubtot = new JLabel("Order subtotal for 0 item(s):");
                                          
    // dynamic buttons
    private JButton jbtProcess = new JButton( "Process Item #1" ) ;
    private JButton jbtConfirm = new JButton( "Confirm Item #1" );
    private JButton jbtView = new JButton( "View Order" );
    private JButton jbtFinish = new JButton( "Finish Order" );
    private JButton jbtNew = new JButton( "New Order" );
    private JButton jbtExit = new JButton( "Exit" );
    
    // ArrayList to keep track of available items
    private ArrayList<String> itemList = new ArrayList<String>();
    
    // list of ordered items.
    private ArrayList<OrderedItem> orderList = new ArrayList<OrderedItem>();
    
    // current item that is pending confirmation
    private OrderedItem pendingOrder;
    
    // misc variables with initialization
    private int numItems = 1;
    private int currentItemNum = 1;
    private float subtotal = 0.0f;
    private Scanner file = null;
    
    // constructor for OrderForm class
    public OrderForm()
    {
        super("Not Jeremy's World of Music, because he does not like most of these songs");
        
        
        // create new scanner to access inventory file
        try
        {
            file = new Scanner(new File("inventory.txt"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Test case file not found!");
            return;
        }
        
        // go through file and add each item to an arraylist
        while( file.hasNextLine() ){
            //process each line
            String line = file.nextLine();
            itemList.addAll( Arrays.asList(line.split(",[ ]*")) );
        }
        
        file.close();
        
        
        // print out the contents of the itemList to make sure it reads them in
        // correctly - for debugging only - uncomment if needed
        /*
        for (String items: itemList)
        {
            System.out.println(items);
        }
         */
        
        
        // create panel to capture and present the order details
        JPanel orderFields = new JPanel();
        //orderFields.setLayout( new GridLayout (5, 0, 5, 5) );
        orderFields.setLayout( new FlowLayout( FlowLayout.RIGHT, 3 , 5 ) );
        
        // add label for number of items in order
        orderFields.add ( new JLabel( "Enter number of items in this order:" ) );
        orderFields.add (jtfNumItems);
        
        // add label to enter CD ID for the order
        orderFields.add( jlblEnterCDID );
        orderFields.add (jtfCDID);
        
        // add label to enter requested quantity of this item
        orderFields.add( jlblEnterQuant );
        orderFields.add (jtfQuant);
        
        // add label for item #
        orderFields.add( jlblItemInfo );
        orderFields.add (jtfItemInfo);
        jtfItemInfo.setEditable(false);
        
        // add label for order subtotal
        orderFields.add( jlblOrderSubtot );
        orderFields.add( jtfSubTot );
        jtfSubTot.setEditable(false);
        
        
        // create panel to add the buttons at the bottom
        JPanel buttons = new JPanel();
        buttons.setLayout( new FlowLayout( FlowLayout.LEFT, 3 , 10 ) );
        
        buttons.add( jbtProcess );
        buttons.add( jbtConfirm );
        jbtConfirm.setEnabled(false);
        buttons.add( jbtView );
        jbtView.setEnabled(false);
        buttons.add( jbtFinish );
        jbtFinish.setEnabled(false);
        buttons.add( jbtNew );
        buttons.add( jbtExit );
        
        
        // add panels into the frame
        add( orderFields, BorderLayout.CENTER );
        add( buttons, BorderLayout.SOUTH );
        
        orderFields.setBackground(Color.GREEN);
        buttons.setBackground(Color.GREEN);
        
        
        // Create a listener and register it with the buttons
        ButtonListener listener = new ButtonListener();
        
        jbtProcess.addActionListener(listener);
        jbtConfirm.addActionListener(listener);
        jbtView.addActionListener(listener);
        jbtFinish.addActionListener(listener);
        jbtNew.addActionListener(listener);
        jbtExit.addActionListener(listener);
     
    }// end OrderForm constructor


    // create a class to listen for button presses for each button
    class ButtonListener implements ActionListener {
        public void actionPerformed( ActionEvent e) {
            
            // call the respective function for each button
            if ( e.getSource() == jbtProcess )
                processItem();
            else if ( e.getSource() == jbtConfirm )
                confirmItem();
            else if ( e.getSource() == jbtView )
                viewOrder();
            else if ( e.getSource() == jbtFinish )
                finishOrder();
            else if ( e.getSource() == jbtNew )
                newOrder();
            else if ( e.getSource() == jbtExit )
                exitOrderForm();
        
        }
    }// end inner class ButtonListener
    
    
    
    // order class for each item as it is ordered.
    class OrderedItem {
        
        // private variables to identify the ordered item and keep track of the
        // cost.  And strings to identify the item
        private int cdIdent;
        private int quantity;
        private String orderInfo;
        private String commaOrder;
        private float discount = 0;
        private float cost;
        
        
        // constructor for OrderedItem.  Needs a valid CDID and the index in the
        // itemList to work properly (handled in other parts of the class.)
        OrderedItem( int cdID, int quant, int index)
        {
            cdIdent = cdID;
            quantity = quant;
            
            // calculate the discount based on the quantity
            if ( quant >= 15 )
                discount = 20;
            else if ( quant >= 10 )
                discount = 15;
            else if ( quant >= 5 )
                discount = 10;
            else
                discount = 0;
            
            // retrieve the cost, and adjusts it based on the number of items
            // ordered and the corresponding discount.
            cost = Float.parseFloat( itemList.get(index + 2) );
            cost = quant * cost * ( 1 - discount/100 );
            
            // creates a string to identify the order
            orderInfo = itemList.get(index) + " " + itemList.get(index + 1 ) +
                        " $" + itemList.get(index + 2) + " " + quantity + " " +
                        discount + "% $" + String.format("%.02f", cost);
            
            // creates a comma-separated string used to write to the
            // transactions file
            commaOrder = itemList.get(index) + ", " + itemList.get(index + 1 ) +
                ", " + itemList.get(index + 2) + ", " + quantity + ", " +
                discount + ", " + String.format("%.02f", cost);
        }// end OrderedItem constructor
        
        // returns string detail of the order
        public String toString()
        {
            return ( orderInfo );
        }
        
        // returns the cost of the order
        public float getCost()
        {
            return cost;
        }
        
        // returns details of the order separated by commas
        public String getCommaOrder()
        {
            return commaOrder;
        }
        
    }// end inner class OrderedItem


    // processes the item after the process item button is clicked
    void processItem()
    {
        // make sure the boxes are not empty before proceeding
        if ( jtfNumItems.getText().equals("") || jtfCDID.getText().equals("")
                || jtfQuant.getText().equals("") )
            JOptionPane.showMessageDialog(null,
                    "Please enter a value into all allowed fields.");
        
        // if all necessary fields have been filled out, proceed
        else
        {
            // get the CDID entered
            String cdNumText = jtfCDID.getText();
            
            // convert the fields to integers for handling
            numItems = Integer.parseInt( jtfNumItems.getText() );
            int cdNum = Integer.parseInt( jtfCDID.getText() );
            int quantity = Integer.parseInt( jtfQuant.getText() );
            
            // find the index of the item in the inventory List
            int index = itemList.indexOf( cdNumText );
            
            // next line is for debugging purposes only
            //System.out.println(index);
            
            // if the item is in the inventory list
            if ( index != -1 )
            {
                // change button enabling
                jbtConfirm.setEnabled(true);
                jbtProcess.setEnabled(false);
            
                // create a new pending Order, and display to the user
                pendingOrder = new OrderedItem(cdNum, quantity, index);
                jtfItemInfo.setText( pendingOrder.toString() );
                
            }
            // if the item is not in the inventory list
            else
                JOptionPane.showMessageDialog(null,
                            "Item not available.  Choose item from the file");
            
        }// end action for processing Item
        
    }// end method processItem
    
    
    // confirms the item after the confirm item button is pressed.
    void confirmItem()
    {
        // shows message confirming the item
        JOptionPane.showMessageDialog(null, "Item #" + currentItemNum + " accepted");
        
        // change all necessary buttons and labels for the next item
        jlblOrderSubtot.setText( "Order subtotal for "+ currentItemNum+ " item(s):");
        currentItemNum++;
        jlblEnterCDID.setText ( "Enter CD ID for Item #"+ currentItemNum +":" );
        jlblEnterQuant.setText ( "Enter quantity for Item #"+ currentItemNum +":" );
        jlblItemInfo.setText ( "     Item #"+ currentItemNum +" info:" );
        
        
        jbtProcess.setText( "Process Item #" + currentItemNum );
        jbtConfirm.setText( "Confirm Item #" + currentItemNum );
        
        jbtConfirm.setEnabled(false);
        jbtProcess.setEnabled(true);
        jbtFinish.setEnabled(true);
        jbtView.setEnabled(true);
        
        // erase the old item, so a new one can be entered
        jtfCDID.setText("");
        jtfQuant.setText("");
        
        // if we have reached the last item to be entered, adjust labels and
        // buttons accordingly
        if ( currentItemNum > numItems )
        {
            jbtProcess.setEnabled(false);
            jtfCDID.setEditable(false);
            jtfQuant.setEditable(false);
            jlblEnterCDID.setText("");
            jlblEnterQuant.setText("");
            jlblItemInfo.setText ( "Item #" + numItems + " info:" );
            
        }
        
        // make sure the number of items can no longer be changed
        jtfNumItems.setEditable(false);
        
        // add the pending Order to the confirmed order list, and update and
        // display the subtotal
        orderList.add(pendingOrder);
        subtotal += pendingOrder.getCost();
        jtfSubTot.setText( "$" + String.format("%.02f", subtotal ) );
        
    }// end method confirmItem

    
    // view the current order to date
    void viewOrder()
    {
        // create string for displaying output
        String display = new String();
        Integer n = 1;
        
        // go through each item in the confirmed orders, and add each string in
        // the order to the display
        for ( OrderedItem item : orderList )
        {
            display += n.toString() + ". " + item.toString() + "\n";
            n++;
        }
        
        // display the message for the entire order to date
        JOptionPane.showMessageDialog(null, display );
                                      
    }// end method viewOrder
    
    
    // finish the Order
    void finishOrder()
    {
        // variables to calculate the tax
        float taxRate = 6.0f;
        float taxAmnt = subtotal * taxRate/100f;
        
        // create 2 date formats for displaying in various areas
        DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/YY hh:mm:ss a z");
        DateFormat dateFormat2 = new SimpleDateFormat("YYMMddHHmmss");
        
        //get current date time with Date()
        Date date = new Date();
        
        // create a string to display the finish order message
        String message = new String();
        
        message = "Date: " + dateFormat1.format( date ) + "\n\n";
        message += "Number of line items: " + ( (Integer)orderList.size() ).toString();
        message += "\n\nItem# / ID / Title / Price / Qty / Disc% / Subtotal: \n\n";
        
        Integer n = 1;
        
        // add each string in the order to the display
        for ( OrderedItem item : orderList )
        {
            message += n.toString() + ". " + item.toString() + "\n";
            n++;
        }
        
        message += "\n\nOrder subtotal: $" + String.format("%.02f", subtotal ) ;
        message += "\n\nTax Rate: \t "+ String.format( "%s", taxRate) +"% \n\n";
        
        message += "Tax amount: \t$" + String.format("%.02f", taxAmnt) + "\n\n";
        message += "Order total: \t$" + String.format("%.02f",
                            (taxAmnt + subtotal)) + "\n\n";
        
        message += "Thanks for shopping at Jeremy's World of Music!";
        
        
        // add the total order to the transaction file before displaying the
        // message
        try{
            // create new file to write the order
            File orderFile = new File("transactions.txt");
        
            // if the file does not exist, create it first
            if ( !orderFile.exists() )
            {
                orderFile.createNewFile();
            }
            
            // create filewriter to append to the current file
            FileWriter fw = new FileWriter( orderFile, true );
            BufferedWriter bw = new BufferedWriter( fw );
            
            
            
            // add each string in the confirmed order to the file
            for ( OrderedItem item : orderList )
            {
                String transaction = new String();
                transaction += dateFormat2.format( date ) + ", ";
                transaction += item.getCommaOrder() + ", ";
                transaction += dateFormat1.format( date );
                bw.append(transaction);
                bw.newLine();
            }
            // close the file after updating
            bw.close();
            
        }
        catch (IOException e)
        {
                
        }
        
        
        // display the entire confirmed order message
        JOptionPane.showMessageDialog(null, message );
        
    }// end method finishOrder
    
    
    
    // clears order and starts over
    void newOrder()
    {
        // clear items currently in order list and reset all variables
        orderList.clear();
        numItems = 1;
        currentItemNum = 0;
        subtotal = 0.0f;
        
        // reset text and buttons to beginning
        jtfNumItems.setText("");
        jtfNumItems.setEditable(true);
        jtfCDID.setText("");
        jtfCDID.setEditable(true);
        jtfQuant.setText("");
        jtfQuant.setEditable(true);
        jtfItemInfo.setText("");
        jtfSubTot.setText("");
        
        jlblOrderSubtot.setText( "Order subtotal for "+ currentItemNum+ " item(s):");
        currentItemNum++;
        jlblEnterCDID.setText ( "Enter CD ID for Item #"+ currentItemNum +":" );
        jlblEnterQuant.setText ( "Enter quantity for Item #"+ currentItemNum +":" );
        jlblItemInfo.setText ( "     Item #"+ currentItemNum +" info:" );
        
        
        jbtProcess.setText( "Process Item #" + currentItemNum );
        jbtProcess.setEnabled(true);
        jbtConfirm.setText( "Confirm Item #" + currentItemNum );
        jbtConfirm.setEnabled(false);
        jbtView.setEnabled(false);
        jbtFinish.setEnabled(false);
        
    }// end method newOrder
    
    
    // quit and exit
    void exitOrderForm()
    {
        this.dispose();
        
    }// end method exitOrderForm
    
}// end class OrderForm






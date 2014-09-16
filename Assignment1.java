/* Name: William Jeremy RiCharde
 Course: CNT 4714 – Summer 2014
 Assignment title: Program 1 – Event-driven Programming
 Date: Sunday May 25, 2014
 */

import java.io.*;
import java.util.Scanner;
import javax.swing.JFrame;


// This is the driver class for the assignment - It uses the OrderForm class
// to perform the GUI and associated operations.
public class Assignment1
{
    // main method
    public static void main ( String [] args )
    {
        
        // Create a new OrderForm class
        OrderForm thisOrder = new OrderForm();
        thisOrder.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        thisOrder.setLocationRelativeTo(null);
        thisOrder.setSize( 750, 300 ); // set frame size
        //thisOrder.pack();
        thisOrder.setVisible( true ); // display frame

    }// end main method
    
    
}// end class Assignment1
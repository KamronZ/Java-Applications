package InventoryApp;


// this is a test
/**
 * @author Kamron Zell,  001355235
 */

/** FUTURE ENHANCEMENT
 * SECTION G
 * To Improve on this program, I would consider the possibility of adding an auto order function.  This would serve to
 * replenish parts that are low and create a list of items that need to be replenished.  I feel as though this would be
 * extremely beneficial to an inventory system as it allows the user to detect a possible shortage and correct it quickly
 * before it runs out of stock and impacting sales.
*/
/**
 * RUNTIME ERROR
 *
 *   SECTION G
 *   Logical error I had here was auto having the modified product actually replace the the original product.
 *   Instead of replacing the original object I was creating a new object which left my new object and old object existing
 *   at the same time.  I think I could have tried to dereference like we learned in c++ but I couldn't figure it out in java.
 *   After regrouping I came to the realizing what i needed was a method to search for a matching part/product by something unique such as the id
 *   and then use set() method instead of a trying to do something like Inventory.addPart(newPart).  Then  I realized that I had already
 *   written this method awhile ago in the inventory class called updateProduct()/updatePart.  This method searches for a matching ID,
 *   and if it finds it it uses the set method to replace the item.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 945,500 ));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



}





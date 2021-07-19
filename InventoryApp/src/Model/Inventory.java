package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Klzel,  001355235
 */



/**
 * Inventory class contains total inventory of parts and products and all the methods to add, delete, and search for these items.
 */
public class Inventory {


    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * getter for ID
     * @param index
     * @return returns the index of the object
     */
    private static int idChecker(int index){
        return allParts.get(index).getId();
    }

    /**
     * generates a random ID  from 1-1000. Checks to make sure ID has not been created already;
     * @return
     */
    public static int generateRandomID() { // would have been better to use a collection.  this will work fine until
                                            // until the bound has been exceeded but think it might be pretty inefficient



        Random rand = new Random();
        int randomInt = rand.nextInt(1000) + 1;

        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
                if (randomInt == Inventory.idChecker(i)) {
                    randomInt = generateRandomID();
                }
        }
            return randomInt;

    }



    /**Method adds a part into the inventory.
     * @param newPart what part is to be added to the list
     */
    public static void addPart(Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    /**
     * Method adds a product into the inventory
     * @param newProduct what product to add
     */
    public static void addProduct(Product newProduct) {
        if (newProduct != null) {
            allProducts.add(newProduct);
        }
    }

    /**
     * Method searches for a part via ID
     * @param partID ID to search for
     * @return part having matching ID
     */

    public static Part lookupPart(int partID) {
        for (int i = 0; i < allParts.size(); i++) {
            Part foundPart = allParts.get(i);
            if (foundPart.getId() == partID) {
                return foundPart;
            }
        }
        return null;
    }

    /**
     * Method searches for a product via ID
     * @param productID ID to search for
     * @return product having matching ID
     */
    public static Product lookupProduct(int productID) {
        for (int i = 0; i < allProducts.size(); i++) {
            Product foundProduct = allProducts.get(i);
            if (foundProduct.getId() == productID) {
                return foundProduct;
            }
        }
        return null;
    }


    /**
     * This is the part search method.  It will create an Observable list and Add items that match argument
     * @param partName search parameter
     * @return the parts that match
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for(int i = 0; i < allParts.size(); i ++){
            Part foundPart = allParts.get(i);
            if(foundPart.getName().toLowerCase().contains(partName.toLowerCase())){ // replaced ".equalsIgnoreCase(partName)" contains returns
                foundParts.add(foundPart);                                                                     //  partial so it doesnt have to be exact

            }
        }
        return foundParts;
        }

    /**
     * This is the product search method.  It will create an Observable list and add items that match argument
     * @param productName search parameter
     * @return the products  that match
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for(int i = 0; i < allProducts.size(); i ++){
            Product found = allProducts.get(i);
            if(found.getName().toLowerCase().contains(productName.toLowerCase())){
                foundProducts.add(found);
            }

        }
        return foundProducts;
    }

    /**Overloaded method searches for a Part object that has a matching ID
     * If ID matches then replace it with the argument
     * @param selectedPart item to be updated
     */
    public static void updatePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                Part currentPart = allParts.get(i);
                int index = allParts.indexOf(currentPart);
                updatePart(index, selectedPart);
            }
        }
    }

    /**
     * Overloaded method, updates part
     * @param index index of the part found having a matching id
     * @param selectedPart part that will replace the oldPart
     */
    private static void updatePart(int index,Part selectedPart){
        allParts.set(index,selectedPart);
    }


    /** Overloaded method Searches for a Product object that has a matching ID
     * If ID matches then replace it with the argument
     * @param selectProduct product to be updated
     */
    public static void updateProduct(Product selectProduct){
        for(int i = 0; i < allProducts.size(); i ++){
            if(allProducts.get(i).getId() == selectProduct.getId()){
                Product currentProduct = allProducts.get(i);
                int index = allProducts.indexOf(currentProduct);
                updateProduct(index,selectProduct);
            }
        }
    }

    /**
     * Overloaded Method that will update the product
     * @param index index of product to be updated
     * @param selectedProduct product that will replace old product
     */
    private static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index,selectedProduct);
    }

    // this is cleaner and simpler but project does not allow variance from UML
    /*public static void updateProduct(Product selectProduct){

        for(int i = 0; i < allProducts.size(); i ++){
            if(allProducts.get(i).getId() == selectProduct.getId()){
                allProducts.set(i,selectProduct);
            }
        }
    }*/

    /**
     * Method removes part from the list
     * @param selectedPart Part object to remove
     * @return true if part removed
     */
    public static boolean deletePart(Part selectedPart){
        allParts.remove(selectedPart);
        return true;
    }

    /**
     *
     * @param selectedProduct Product object to remove
     * @return true if product removed
     */
    public static boolean deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * GETTER
     * @return all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * GETTER
     * @return all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}

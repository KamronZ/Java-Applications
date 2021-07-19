package Model;

/**
 @author Klzel,  001355235
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class that has lsit of parts
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    int id;
    String name;
    double price;
    int stock;
    int min;
    int max;

    /**
     * Constructor used to create a Product
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product( int id, String name, double price, int stock, int min, int max) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**
     * Method adds a part to associatedParts list
     * @param part part to be added
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);

    }

    /**
     *
     * @param part part to me removed
     * @return true if item was found, false if not
     */
    public boolean deleteAssociatedPart(Part part){
        int foundPosition = findAssociatedPart(part); // used overloaded method that returns index
        if(foundPosition < 0){
            return false;
        }
        this.associatedParts.remove(foundPosition);
        return true;
    }

    /**
     * Takes a Part object and returns the index of the object from the to list to be used with deleteAssociatedPart
     * if it returns anything > 0, we know the object exist.
     * @param part
     * @return index number
     */
    private int findAssociatedPart(Part part) {// private because the rest of program does not need to see this method
        return this.associatedParts.indexOf(part);// returns index of object or -1 if not found
    }


// GETTERS AND SETTERS


    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

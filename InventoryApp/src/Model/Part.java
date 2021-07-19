package Model;

/**
 * @author Klzel,  001355235
 */

/** abstract class defining part
 * abstraction is good here because both in-house and outsources will use all these fields
 */
public abstract class Part {


    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     *Constructor to create a Part
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock
     * @param min part min
     * @param max part max
     */

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


   // SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    // GETTERS


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}

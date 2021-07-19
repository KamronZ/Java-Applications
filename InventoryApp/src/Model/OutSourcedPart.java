package Model;

/**
 * @author klzel ,001355235
 */

/**
 * Class for outsourcedParts
 */
public class OutSourcedPart extends Part {
    // attributes
    private String companyName;

    /**
     * Constructor
     * @param id Outsourced part ID
     * @param name Outsourced part name
     * @param price Outsourced part price
     * @param stock Outsourced stock price
     * @param min Outsourced part min
     * @param max Outsourced part max
     * @param companyName Outsourced companyName having the part
     */
    public OutSourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    /**
     * GETTER
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * SETTER
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

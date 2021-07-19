package Model;

/**
 * @author Klzel,  001355235
 *
 */

/**
 * InHousePart class for parts that are in house
 */
public class InHousePart extends Part{
    //Attributes
    private int machineID;

    /**
     * Constructor
     * @param  machineID InHousePart machine id
     * @param id inHouse part id
     * @param name inHouse name
     * @param price inHouse price
     * @param stock inHouse stock
     * @param min inHouse min
     * @param max inHouse max
     */

    //Constructor
    public InHousePart(int id,String name, double price,int stock,int min,int max, int machineID){
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    //Getter/Setter

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}

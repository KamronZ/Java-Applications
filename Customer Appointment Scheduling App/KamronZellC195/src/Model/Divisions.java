package Model;

/**
 * Divisions class
 */
public class Divisions {
    private int divisionID;
    private String divisionName;
    private int countryID;

    /**
     * Divisions constructor
     * @param divisionID
     * @param divisionName
     * @param countryID
     */
    public Divisions(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }
    public Divisions(String divisionName){
        this.divisionName = divisionName;
    }

    /**
     * used to display name in comboboxes in instead of all the object infomration
     * @return String divisionName
     */
    @Override
    public String toString() {
        return divisionName;
    }



    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}

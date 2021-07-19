package Model;

/**
 * class to make countries object
 */
public class Countries {
    private int countryID;
    private String country;

    /**
     * Country class
     * @param countryID
     * @param country
     */
    public Countries(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    @Override
    public String toString() {
        return country;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

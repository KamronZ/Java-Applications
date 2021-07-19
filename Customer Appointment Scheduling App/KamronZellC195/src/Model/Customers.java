package Model;

/**
 * Customers object
 */
public class Customers {
    private  int customerID;
    public String customerName;
    public String country;
    public int countryID;
    public String street;
    public String state;
    public int stateID;
    public String postalCode;
    public String phoneNumber;


    public int getCustomerID() {
        return customerID;
    }

    /**
     * Customers Constructor
     * @param customerName
     * @param postalCode
     * @param phoneNumber
     * @param street
     * @param country
     * @param countryID
     * @param state
     * @param stateID
     * @param customerID
     */
    public Customers(String customerName, String postalCode, String phoneNumber, String street, String country, int countryID, String state, int stateID, int customerID) {
        this.customerName = customerName;
        this.street = street;
        this.state = state;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.customerID = customerID;
        this.stateID = stateID;
        this.countryID = countryID;
    }


    public String getCountry() {
        return country;
    }

    public String getCustomerName() {
        return customerName;
    }
    public String getFirstName(String fullName) {
        return fullName.split("(?!.*)")[0];

    }




    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

   /* @Override
    public String toString() {

       return "Customer name:" + customerName +" \nCountry: " + country+ "\nStreet: " + street +  "\nPost code: " + postalCode + "\nPhone number: "+  phoneNumber;
    }*/

    /**
     * used only for debugging
     * @return CustomerName & CustomerID
     */
   @Override
   public String toString() {

       return "Name: " + customerName + " ID: " + customerID;
   }

}

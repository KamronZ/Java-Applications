package Model;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * class for dealing with DB commands with Customers
 */
public class CustomersDB {
    /**
     * This is a table created with a list of all Customers in the DB joined with their First_level_division information
     */
    public static ObservableList<Customers> customersJoinedDivisionsTable = FXCollections.observableArrayList();

    /**
     * Gets customerJoinedDivisionTable if no updates have been done and is less expensive
     * @return customersJoinedDivisionTable
     */
    public static ObservableList<Customers> getAllCustomers(){
        return customersJoinedDivisionsTable;
    }

    /**
     * Used to return the customer Object that has the matching ID
     * @param customerID
     * @return Customers obj or null if not found
     */
    public static Customers findCustomer(int customerID){
        for(Customers c : customersJoinedDivisionsTable){
            if( c.getCustomerID() == customerID){
                return c;
            }
        }
        return null;
    }

    /**
     * Finds the name of the customer with the input ID
     * @param customerID
     * @return customer name or null if not found
     */
    public static String findCustomerName(int customerID) {
        for (Customers c : getAllCustomers()) {
            if (c.getCustomerID() == customerID) {
                return c.customerName;
            }
        }
        return null;
    }

    /**
     * DB command that will create the customerJoinedDivisionTable.  Used when ever customer records or first_level_division is changed
     * @return customersJoinedDivisionTable or null if SQLException occurs
     * @throws SQLException
     */
    public static ObservableList<Customers> loadCustomerJoinedDivisionsTable() {
        ObservableList<Customers> records = FXCollections.observableArrayList();
        Customers customerRecord;

        try {
            String selectStatement = "select Customer_Name,Postal_Code,Phone,Address,Country, first_level_divisions.COUNTRY_ID,Division,first_level_divisions.Division_ID,Customer_ID FROM customers, first_level_divisions, countries \n" +
                    "WHERE first_level_divisions.COUNTRY_ID = countries.Country_ID AND first_level_divisions.Division_ID = customers.Division_ID;";

            Statement statement = DBConnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            while (resultSet.next()) {
                customerRecord = new Customers(resultSet.getString("Customer_Name"), resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),resultSet.getString("Address"), resultSet.getString("Country"),resultSet.getInt("COUNTRY_ID"), resultSet.getString("Division"),resultSet.getInt("Division_ID"),resultSet.getInt("Customer_ID"));
                records.add(customerRecord);
                System.out.println("name: " + resultSet.getString("Customer_Name"));
                System.out.println("postal code: " + resultSet.getString("Postal_Code"));
                System.out.println("Phone: " + resultSet.getString("Phone"));
                System.out.println("Country: " + resultSet.getString("country"));
                System.out.println("State: " + resultSet.getString("Division"));
            }
            statement.close();
            customersJoinedDivisionsTable.setAll(records);
            return records;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }

    }
}

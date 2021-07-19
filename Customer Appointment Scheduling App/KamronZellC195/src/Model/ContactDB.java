package Model;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Model.Contact.allContact;

/**
 * used for DB commands and other methods around Contact class
 */
public class ContactDB {

    /**
     * appointment construct needs to have a Contact_Name(however DB does not have this available in Appointment table.
     * This will use the ContactID(provided in Appointment table to search for matching contactID(in the list off all contacts
     *  and will return the name of the contact associated with it
     * @param contactID
     * @return null if not found
     */
    public static String findContact(int contactID) {
        for (Contact c : allContact) {
            if (c.getContactID() == contactID) {
                return c.getContactName();
            }
        }
        return null;
    }

    /**
     * Used to get ContactID from a contact when name match is found
     * @param contactName
     * @return -1 if contact not found
     */
    // This method should be overhauled since someone could have the same name and it would return the first match.
    public static int findContact(String contactName){
        for(Contact c : allContact) {
            if (c.getContactName().equalsIgnoreCase(contactName)) {
                return c.getContactID();
            }
        }
        return -1;
    }

    /**
     * Takes a contact object and returns it's ID
     * @param contactID
     * @return null if not found
     */
    public static Contact findContactObj(int contactID) {
        for (Contact c : allContact) {
            if (c.getContactID() == contactID) {
                return c;
            }
        }
        return null;
    }

    /**
     * DB query to get all contacts
     * @return null if SQL exception is thrown
     * @throws SQLException
     */
    public static ObservableList<Contact> setAllContact() {
        ObservableList<Contact> gettingContacts = FXCollections.observableArrayList();
        Contact contactToAdd;

        try {

            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM contacts";

            ResultSet rs = statement.executeQuery(selectStatement);
            while (rs.next()) {
                contactToAdd = new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_name") , rs.getString("Email"));
                gettingContacts.add(contactToAdd);

            }
            statement.close();
            allContact.setAll(gettingContacts);



            return gettingContacts;

        } catch (SQLException e) {
            System.out.println("MYSQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * gets all the contacts if no need for update and is less expensive
     * @return allContact
     */
    public static ObservableList<Contact> getAllContact() {
        return allContact;
    }


}

package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class to make contact object
 */
public class Contact {
    private int contactID;
    private String contactName;
    private String contactEmail;
    /**
     * list of all contacts pulled from DB
     */
    public static ObservableList<Contact> allContact = FXCollections.observableArrayList();

    /**
     * Contact constructor
     * @param contactID
     * @param contactName
     * @param contactEmail
     */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return contactName;

    }
}

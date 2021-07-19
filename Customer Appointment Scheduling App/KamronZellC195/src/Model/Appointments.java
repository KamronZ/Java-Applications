package Model;


import java.sql.Timestamp;

import static Model.Contact.allContact;

/**
 * Class to create Appointments object
 */
public class Appointments  {


    private int aptID;
    private String title;
    private String description;
    private String location;
    private int contactID;
    private String contactName;
    private String type;
    private Timestamp startTime;
    private Timestamp endTime;
    private int customerID;
    private int userID;


    /**
     * Appointment constructor
     * @param aptID
     * @param title
     * @param description
     * @param location
     * @param contactID
     * @param contactName
     * @param type
     * @param startTime
     * @param endTime
     * @param customerID
     * @param userID
     */
    public Appointments(int aptID, String title, String description, String location, int contactID,String contactName, String type,
                        Timestamp startTime, Timestamp endTime, int customerID,int userID) {
        this.aptID = aptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.contactName =contactName;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getAptID() {
        return aptID;
    }

    public void setAptID(int aptID) {
        this.aptID = aptID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getContact() {
        return contactID;
    }

    public void setContactID (int contact) {
        contactID = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    @Override
    public String toString() {

        return contactName;
    }

}


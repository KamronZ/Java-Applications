package Model;

import Utils.DBConnection;
import Utils.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Timer;

import static java.lang.Math.abs;

/**
 * Class for DataBase commands for appointments
 */
public class AppointmentsDB {

    /**
     * list of all appointments that were pulled from DB
     */
    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

    /**
     * This method is used to perform a DataBase query everytime the scheduledApt Tableview in mainmenu needs o be reset
     * or allAppointments needs to be rest;
     * @return allAppointments & null if a SQL exception occurs
     */
    public static Appointments resetScheduledAptTableView(){
        setAllAppointments();

        return null;
    }

    /**
     * Sets all contacts and appointments associated with them from the DB
     * @return allAppointments
     */
    private static ObservableList<Appointments> setAllAppointments() {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        Appointments appointment;

        try {
            // this will populate the  allcontacts list and is a SQLQuery
            ContactDB.setAllContact();
            Statement statement = DBConnection.getConnection().createStatement();
            String selectStatement = "SELECT * FROM appointments";
            ResultSet resultSet = statement.executeQuery(selectStatement);
            while (resultSet.next()) {
                appointment = new Appointments(resultSet.getInt("Appointment_ID"), resultSet.getString("Title"),
                         resultSet.getString("Description"), resultSet.getString("Location"),
                        resultSet.getInt("Contact_ID"),ContactDB.findContact(resultSet.getInt("Contact_ID")),resultSet.getString("Type"),
                       resultSet.getTimestamp("Start"), resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),resultSet.getInt("User_ID"));
                appointments.add(appointment);
            }
            statement.close();
            allAppointments.setAll(appointments);
            return appointments;

        } catch (SQLException exception) {
            System.out.println("SQLException: " + exception.getMessage());
            return null;
        }
    }

    /**
     * This is used after successful log in to give an alert on whether there is an appointment in the system within 15 minutes
     * of user logging in.
     * @return true if there is an upcoming appointment in 15min or less.
     * @throws ParseException
     */
    public static boolean isUpcomingApt() throws ParseException {

        for (Appointments appointment : allAppointments) {
                if ((appointment.getStartTime().after(Time.convertLDTToTimestamp(Time.getCurrentTime()))
                        && appointment.getStartTime().before(Time.convertLDTToTimestamp(Time.getCurrentTime().plusMinutes(15))))
                        || appointment.getStartTime().equals(Time.convertLDTToTimestamp(Time.getCurrentTime()))) {
                    LocalDateTime ldt1 = appointment.getStartTime().toLocalDateTime();
                    int aptStartTime = appointment.getStartTime().toLocalDateTime().toLocalTime().getMinute();
                    int currentTime = Time.getCurrentTime().toLocalTime().getMinute();
                    int difference = aptStartTime - currentTime;


                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("UPCOMING APPOINTMENT");
                    alert.setContentText(appointment.getContactName() + " has an appointment at "
                        + appointment.getStartTime().toLocalDateTime().toLocalTime() +" today on "
                            + appointment.getStartTime().toLocalDateTime().toLocalDate() +  " with "
                        + appointment.getContactName() + " in " + difference + " minutes."
                            + " Appointment ID: " + appointment.getAptID());

                    alert.setHeaderText("UPCOMING APPOINTMENT in " + difference + " minutes");
                    alert.showAndWait();
                    return true;
                }
               /* if(appointment.getStartTime().compareTo(Time.convertLDTToTimestamp(Time.getCurrentTime()))){
                    LocalDateTime ldt1 = appointment.getStartTime().toLocalDateTime();
                    int aptStartTime = appointment.getStartTime().toLocalDateTime().toLocalTime().getMinute();
                    int currentTime = Time.getCurrentTime().toLocalTime().getMinute();
                    int difference = aptStartTime - currentTime;

                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment starting now");
                    alert.setContentText(appointment.getContactName() + " has an appointment at "
                            + appointment.getStartTime().toLocalDateTime().toLocalTime() +" today on "
                            + appointment.getStartTime().toLocalDateTime().toLocalDate() +  " with "
                            + appointment.getContactName() +
                            " Appointment ID: " + appointment.getAptID());

                    alert.setHeaderText("UPCOMING APPOINTMENT in " + difference + " minutes");
                    alert.showAndWait();
                    return true;

                }*/
            }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("NO APPOINTMENTS");
        alert.setContentText("There are no appointments coming up in the next 15 minutes: ");
        alert.showAndWait();
        return false;
        }


    /**
     * Used to retrieve all appointments when nothing has changed and is less expensive
     * @return allAppointments
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }


}



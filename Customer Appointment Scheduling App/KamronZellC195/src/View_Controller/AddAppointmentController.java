 // FIXME REMEMBER THAT JAVA AUTOMATICALLY PUTS TIMESTAMP INTO UTC DO NOT CONVERT!!!!

package View_Controller;
import Model.*;
import Utils.DBConnection;
import Utils.DBQuery;
import Utils.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static Model.Contact.allContact;
import static Model.CustomersDB.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

 /**
  *  setBusinessHours() contains the  lambda used to offset the business hours from EST to local
  */
 public class AddAppointmentController implements Initializable {
    /**
     * BusinessHours that are set in eastern time
     */
    @FXML ObservableList<Integer> businessHoursInEST = FXCollections.observableArrayList(Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21,22));
    /**
     * list will be populated with updated business hours based on usersLocalTIme
     */
    @FXML ObservableList<Integer> offsetBusinessHoursToLocal = FXCollections.observableArrayList();
    /**
     * all available hours in a day
     */
    @FXML ObservableList<Integer> hours =  FXCollections.observableArrayList(00,01,02,03,04,05,06,07,8,9,10,11,12,13,14,15,16,17,18,19,
            20,21,22,23,24);
    /**
     * allowed minutes for appointment incrementation
     */
    @FXML ObservableList<Integer> minutes = FXCollections.observableArrayList(00,15,30,45);


    @FXML public ComboBox<Customers> customerComboBox;
    @FXML public ComboBox<Contact> contactCombo;

    @FXML private DatePicker datePicker;

    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;

    @FXML private ComboBox<Integer> startHourCombo;
    @FXML private ComboBox<Integer> startMinCombo;

   // @FXML private ComboBox<String> startPeriodCombo;

    @FXML private ComboBox<Integer> endHourCombo;
    @FXML private ComboBox<Integer>  endMinuteCombo;

    //@FXML private ComboBox<String> endPeriodCombo;

    @FXML private Button cancelButt;
    @FXML public Text businessHoursText;

    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;

    private static int count = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        loadCustomerJoinedDivisionsTable();
        setBusinessHours();

        Collections.sort(offsetBusinessHoursToLocal);

        customerComboBox.setItems(CustomersDB.getAllCustomers());

        businessHoursText.setText("Current Business hours are from: "+ offsetBusinessHoursToLocal.get(0) + ":00 hours to "+ offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size()-1)+ ":00 hours");

        startHourCombo.setPromptText("Hours");

        startHourCombo.setItems(offsetBusinessHoursToLocal);
        //startHourCombo.setItems(hours);

        startMinCombo.setPromptText("Minutes");
        startMinCombo.setItems(minutes);

        /*startPeriodCombo.setPromptText("AM/PM");
        startPeriodCombo.setItems(period);*/

        endHourCombo.setPromptText("Hours");
        endHourCombo.setItems(offsetBusinessHoursToLocal);
        //endHourCombo.setItems(hours);

        endMinuteCombo.setPromptText("Minutes");
        endMinuteCombo.setItems(minutes);

        /*endPeriodCombo.setPromptText("AM/PM");
        endPeriodCombo.setItems(period);*/

        contactCombo.setItems(allContact);
        contactCombo.setPromptText("Choose a contact");
    }


     /**
      * This method uses a lambda and it takes the established business hours in eastern time and converts to local time.   Uses Lambda on abstract method .forEach().  Due to my long naming conventions the lambda looks much cleaner and shorter.
      */
     private void setBusinessHours(){
        businessHoursInEST.forEach( (hour) ->{
            if(Time.getTimeZoneOffsetEastToLocal(hour) <= 24){
                offsetBusinessHoursToLocal.add(Time.getTimeZoneOffsetEastToLocal(hour));

            }else{
                offsetBusinessHoursToLocal.add(count);
                count++;
            } });
        /*for(int i = 0; i < businessHoursInEST.size(); i++){
           if(Time.getTimeZoneOffsetEastToLocal(businessHoursInEST.get(i)) <= 24) {
                offsetBusinessHoursToLocal.add(Time.getTimeZoneOffsetEastToLocal(businessHoursInEST.get(i)));
                System.out.println("Old hour was: " + businessHoursInEST.get(i));
                System.out.println("New hour is: " + offsetBusinessHoursToLocal.get(i));
           }else{
               offsetBusinessHoursToLocal.add(count);
               count ++;

           }
        }*/
    }


    /**
     * this grabs information from start time comboBoxes and end time comboBoxes to create a local time.  The local time
     * will then be used with getLocalTime() to create a LDT
     */
    private void setSelectedTime() {
        Integer startHour;
        Integer startMin;
        Integer endHour;
        Integer endMin;

        if (startMinCombo.getValue() == null || startHourCombo == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please ensure a start hour and minute is selected");
            alert.showAndWait();
        }
        if (endHourCombo.getValue() == null || endMinuteCombo == null) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("ERROR");
            alert1.setContentText("Please ensure an end hour and minute is selected");
            alert1.showAndWait();
        } else {

            startHour = startHourCombo.getValue();
            startMin = startMinCombo.getValue();
            endHour = endHourCombo.getValue();
            endMin = endMinuteCombo.getValue();

            selectedStartTime = LocalTime.of(startHour, startMin);
            selectedEndTime = LocalTime.of(endHour, endMin);
        }
    }

    /**
     * checks to make sure information input is valid
     * @return true if valid or false if invalid
     */
    private boolean isValid() {

    setSelectedTime();
    getLocalDateTime(selectedStartTime);
    Time.convertToUTC(getLocalDateTime(selectedStartTime));

    getLocalDateTime(selectedEndTime);
    Timestamp startTsUTC = Time.convertToUTC(getLocalDateTime(selectedStartTime));
    Timestamp endTsUTC = Time.convertToUTC(getLocalDateTime(selectedEndTime));
    System.out.println("Start time in UTC is: " + startTsUTC);
    System.out.println("End time in UTC is " + endTsUTC );

    Timestamp  startTsLocal = Time.convertLDTToTimestamp(getLocalDateTime((selectedStartTime)));
    Timestamp endTsLocal = Time.convertLDTToTimestamp(getLocalDateTime(selectedEndTime));


    for(Appointments appointment : AppointmentsDB.getAllAppointments()){
        if(appointment.getStartTime().equals(startTsLocal)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("An appointment is already set up to start at that time!");
            alert.showAndWait();
            return false;
        }
        if(startTsLocal.before(appointment.getEndTime()) && startTsLocal.after(appointment.getStartTime())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("An appointment already exist in that time frame!");
            alert.showAndWait();
            return false;
        }
    }
    if(startTsLocal.before(Time.convertLDTToTimestamp(Time.getCurrentTime()))){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("That time frame has already elapsed.");
        alert.showAndWait();
        return false;
    };
    if (startTsLocal.after(endTsLocal)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Start time cannot be after end time.");
        alert.showAndWait();
        return false;
    } else if (endTsLocal.before(startTsLocal)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("End time cannot be earlier than start time.");
        alert.showAndWait();
        return false;
    } else if (endTsLocal.equals(startTsLocal)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Appointment must be at least 15 minutes long.");
        alert.showAndWait();
        return false;
    } else if (datePicker.getValue() == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Please select a date");
        alert.showAndWait();
        return false;
    } else if (datePicker.getValue().isBefore(Time.currentLocalDate())) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Date selected must not have already passed.");
        alert.showAndWait();
        return false;
    } else if (contactCombo.getValue() == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Please make a contact selection.");
        alert.showAndWait();
        return false;
    } else if (customerComboBox == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Please make a customer selection.");
        alert.showAndWait();
        return false;
    }
    else{
        return true;
    }
}

    /**
     * This adds appointment
     * @param actionEvent
     * @throws SQLException
     */
    public void addButt(ActionEvent actionEvent) {
        // FIXME JAVA WILL CONVERT TO UTC !!! DO NOT CONVERT
        try {
            if(isValid()){
                    String insertStatement = "INSERT INTO appointments (Title,Description,Location,Type,User_ID,Contact_ID,Start,End,Customer_ID)\n" +
                            "values(?,?,?,?,?,?,?,?,?);";

                    DBQuery.setPreparedStatement(DBConnection.getConnection(), insertStatement);
                    PreparedStatement ps = DBQuery.getPreparedStatement();

                    ps.setString(1, titleTextField.getText());
                    ps.setString(2, descriptionTextField.getText());
                    ps.setString(3, locationTextField.getText());
                    ps.setString(4, typeTextField.getText());
                    ps.setInt(5, LogInController.getUserID());
                    ps.setInt(6, contactCombo.getValue().getContactID());
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.of(selectedDate,selectedStartTime)));
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.of(selectedDate,selectedEndTime)));
                    ps.setInt(9, customerComboBox.getValue().getCustomerID());

                    ps.execute();
                    ps.close();

                    AppointmentsDB.resetScheduledAptTableView();

                    Stage stage = (Stage) cancelButt.getScene().getWindow();
                    stage.close();

                }

        } catch (SQLException exception) {
                exception.printStackTrace();
            }
    }

    /**
     * closes current window
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButt(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) cancelButt.getScene().getWindow();
        stage.close();
    }

    /**
     * when user makes a date selection it creates a LD object
     * @param actionEvent
     */
    public void datePicked(ActionEvent actionEvent) {
        if(datePicker.getValue() != null){
            selectedDate = datePicker.getValue();
            System.out.println(selectedDate);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a date");
            alert.showAndWait();
        }
    }

    /**
     * THis method used the LD with getSelectedTime() method to create a LDT
     * @param timeSelected
     * @return
     */
    public LocalDateTime getLocalDateTime(LocalTime timeSelected){
        return LocalDateTime.of(selectedDate,timeSelected);
    }

    /**
     *
     * @param actionEvent
     * creates a start min and checks to make sure after hours are not selected
     */
    public void startMinSelect(ActionEvent actionEvent) {
        if (startHourCombo.getValue() == 24 && startMinCombo.getValue() > 0) {
            startHourCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
            alert.showAndWait();
        }
        if (startHourCombo.getValue().equals(offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1)) && startMinCombo.getValue() > 0) {
            startMinCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment cannot be after hours.");
            alert.showAndWait();
        }
    }

   /* public void startPeriodSelect(ActionEvent actionEvent) {
    }*/

    /**
     * creates a start hour and makes sure after horus are not selected
     * @param actionEvent
     */
    public void startHourSelect(ActionEvent actionEvent) {
        if (startHourCombo.getValue() == 24 && startMinCombo.getValue() > 0) {
            startHourCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
            alert.showAndWait();
            }
        if (startHourCombo.getValue().equals(offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1)) && startMinCombo.getValue() > 0) {
            startMinCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment cannot be after hours.");
            alert.showAndWait();
        }
    }

    /**
     * creates end hour and makes sure after hours are not selected
     * @param actionEvent
     */
    public void endHourSelect(ActionEvent actionEvent) {
        if(endHourCombo.getValue() == 24 && endMinuteCombo.getValue() > 0) {
            endHourCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
            alert.showAndWait();
        }
        if (endHourCombo.getValue().equals(offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1)) && endMinuteCombo.getValue() > 0) {
            endMinuteCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment cannot be after hours.");
            alert.showAndWait();
        }

    }

    /**
     * creates end min and makes sure after hours are not selected
     * @param actionEvent
     */
    public void endMinSelect(ActionEvent actionEvent) {
        if(endHourCombo.getValue() == 24 && endMinuteCombo.getValue() > 0){
            endHourCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
            alert.showAndWait();
        }
        if (endHourCombo.getValue().equals(offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1)) && endMinuteCombo.getValue() > 0) {
            endMinuteCombo.setValue(0);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setContentText("Appointment cannot be after hours.");
            alert.showAndWait();
        }
    }


   /* public void endPeriodSelect(ActionEvent actionEvent) {
    }*/
}

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

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Contact.allContact;
import static Model.CustomersDB.loadCustomerJoinedDivisionsTable;


/**
 * deleteButton() contains the lambda
 * Used to modify Appointments
 */
public class ModifyAppointmentController implements Initializable {


  @FXML ObservableList<Customers> customerList = FXCollections.observableArrayList();
  /**
   * List of the business hours unmodified in eastern time
   */
  @FXML ObservableList<Integer> businessHoursInEST = FXCollections.observableArrayList(Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));
  /**
   * list that will be created from businesshoursInEST after they are converted to locale time
   */
  @FXML ObservableList<Integer> offsetBusinessHoursToLocal = FXCollections.observableArrayList();
  /**
   * list to get all 24 hours in the day
   */
  @FXML ObservableList<Integer> hours = FXCollections.observableArrayList(00, 01, 02, 03, 04, 05, 06, 07, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
          20, 21, 22, 23, 24);
  /**
   * list to get 15 minute appointment increments
   */
  @FXML ObservableList<Integer> minutes = FXCollections.observableArrayList(00, 15, 30, 45);

  @FXML private DatePicker datePicker;

  @FXML private ComboBox<Integer> startHourCombo;
  @FXML private ComboBox<Integer> startMinCombo;

  @FXML private ComboBox<Integer> endHourCombo;
  @FXML private ComboBox<Integer> endMinuteCombo;

  @FXML private ComboBox<Contact> contactCombo;
  @FXML private ComboBox<Customers> customerComboBox;

  @FXML private TextField titleTextField;
  @FXML private TextField descriptionTextField;
  @FXML private TextField locationTextField;

  @FXML private TextField typeTextField;
  @FXML private Text businessHoursText;

  @FXML private Button cancelButt;


  private Appointments selectedApt = null;
  private LocalDate selectedDate;
  private LocalTime selectedStartTime;
  private LocalTime selectedEndTime;

  /**
   * Used to populate table, set business hours, and set the labels
   * @param Location
   * @param resources
   */
  public void initialize(URL Location, ResourceBundle resources) {
    loadCustomerJoinedDivisionsTable();
    setBusinessHours();
    customerComboBox.setItems(CustomersDB.getAllCustomers());

    businessHoursText.setText("Current Business hours are from: " + offsetBusinessHoursToLocal.get(0) + ":00 hours to " + offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1) + ":00 hours");

    startHourCombo.setPromptText("Hours");
    startHourCombo.setItems(offsetBusinessHoursToLocal);

    startMinCombo.setPromptText("Minutes");
    startMinCombo.setItems(minutes);

    endHourCombo.setPromptText("Hours");
    endHourCombo.setItems(offsetBusinessHoursToLocal);


    endMinuteCombo.setPromptText("Minutes");
    endMinuteCombo.setItems(minutes);

    contactCombo.setItems(allContact);
    contactCombo.setPromptText("Choose a contact");

  }

  /**
   * This sets all the labels contained in the view to display they appropriate information passed in from the
   * Appointments object
   * @param modifyApt
   */
  private void setLabels(Appointments modifyApt) {
    titleTextField.setText(selectedApt.getTitle());
    descriptionTextField.setText(selectedApt.getDescription());
    locationTextField.setText(selectedApt.getLocation());
    typeTextField.setText(selectedApt.getLocation());

    //FIXME ask about depreciated methods
    startHourCombo.setValue(selectedApt.getStartTime().getHours());
    startMinCombo.setValue(selectedApt.getStartTime().getMinutes());
    endHourCombo.setValue(selectedApt.getEndTime().getHours());
    endMinuteCombo.setValue(selectedApt.getEndTime().getMinutes());

    contactCombo.setValue(ContactDB.findContactObj(selectedApt.getContactID()));
    customerComboBox.setValue(CustomersDB.findCustomer(selectedApt.getCustomerID()));
    datePicker.setValue(selectedApt.getStartTime().toLocalDateTime().toLocalDate());

  }
  /**
   * This passes an Appointments object to the from MainMenuController to ModifyAppointmentController
   *
   * @param modifyApt
   */
  public void getApt(Appointments modifyApt) {
    this.selectedApt = modifyApt;
    setLabels(selectedApt);
  }

  /**
   * this method converts EST business hours to locale time
   */
  public void setBusinessHours() {

    for (int i = 0; i < businessHoursInEST.size(); i++) {
      offsetBusinessHoursToLocal.add(Time.getTimeZoneOffsetEastToLocal(businessHoursInEST.get(i)));
      System.out.println("Old hour was: " + businessHoursInEST.get(i) + " in EST time");
      System.out.println("New hour is: " + offsetBusinessHoursToLocal.get(i) + "in " + Time.getTimeZoneID());
    }
  }

  /**
   * Used to get LDT of selectedDate with timeSelected
   * @param timeSelected this is passed in from the various time comboboxes
   * @return LDT from datepicker and combooxes
   */
  public LocalDateTime getLocalDateTime(LocalTime timeSelected) {
    return LocalDateTime.of(selectedDate, timeSelected);
  }

  /**
   * This method grabs the selected times from combo boxes and turns them into LocalTime objects
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


  @FXML private void modButt(ActionEvent actionEvent) {
    //FIXME DO NOT CONVERT TO UTC JAVA ALREADY DOES THIS!!
    try {


      if(isValid()){
        String updateStatement = "UPDATE appointments SET Title = ?,Description = ?,Location = ?,Type = ?,User_ID = ?,Contact_ID = ?,Start = ?,End = ?,Customer_ID = ? " +
                "WHERE Appointment_ID = ?;";

        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        DBQuery.setPreparedStatement(DBConnection.getConnection(), updateStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        setSelectedTime();
       getLocalDateTime(selectedStartTime);
        getLocalDateTime(selectedEndTime);


        ps.setString(1, titleTextField.getText());
        ps.setString(2, descriptionTextField.getText());
        ps.setString(3, locationTextField.getText());
        ps.setString(4, typeTextField.getText());
        ps.setInt(5, LogInController.getUserID());
        ps.setInt(6, contactCombo.getValue().getContactID());
        ps.setTimestamp(7, Time.convertLDTToTimestamp(getLocalDateTime(selectedStartTime)));
        ps.setTimestamp(8, Time.convertLDTToTimestamp(getLocalDateTime(selectedEndTime)));
        ps.setInt(9, customerComboBox.getValue().getCustomerID());
        ps.setInt(10,selectedApt.getAptID());

        ps.execute();
        ps.close();

        Stage stage = (Stage) cancelButt.getScene().getWindow();
        stage.close();
        AppointmentsDB.resetScheduledAptTableView();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void startMinSelect(ActionEvent actionEvent) {
    if (startHourCombo.getValue() == 24 && startMinCombo.getValue() > 0) {
      startHourCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
      alert.showAndWait();
    }
    if (startHourCombo.getValue() == offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1) && startMinCombo.getValue() > 0) {
      startMinCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("Appointment cannot be after hours.");
      alert.showAndWait();
    }
  }


  public void startHourSelect(ActionEvent actionEvent) {
    if (startHourCombo.getValue() == 24 && startMinCombo.getValue() > 0) {
      startHourCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
      alert.showAndWait();
    }
    if (startHourCombo.getValue() == offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1) && startMinCombo.getValue() > 0) {
      startMinCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("Appointment cannot be after hours.");
      alert.showAndWait();
    }
  }

  public void endHourSelect(ActionEvent actionEvent) {
    if (endHourCombo.getValue() == 24 && endMinuteCombo.getValue() > 0) {
      endHourCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
      alert.showAndWait();
    }
    if (endHourCombo.getValue() == offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1) && endMinuteCombo.getValue() > 0) {
      endMinuteCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("Appointment cannot be after hours.");
      alert.showAndWait();
    }

  }

  public void endMinSelect(ActionEvent actionEvent) {
    if (endHourCombo.getValue() == 24 && endMinuteCombo.getValue() > 0) {
      endHourCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("cannot add minutes to 24:00 hours, selecting 0 hours instead");
      alert.showAndWait();
    }
    if (endHourCombo.getValue() == offsetBusinessHoursToLocal.get(offsetBusinessHoursToLocal.size() - 1) && endMinuteCombo.getValue() > 0) {
      endMinuteCombo.setValue(0);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("ERROR");
      alert.setContentText("Appointment cannot be after hours.");
      alert.showAndWait();
    }
  }

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
   * closes current stage
   * @param actionEvent
   * @throws IOException
   */
  public void cancelButt(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) cancelButt.getScene().getWindow();
    stage.close();

  }

  /**
   * ensures valid inputs
   * @return true if valid and false if not
   */
  private boolean isValid() {
    setSelectedTime();
    getLocalDateTime(selectedStartTime);
    Time.convertToUTC(getLocalDateTime(selectedStartTime));

    getLocalDateTime(selectedEndTime);
    Timestamp startTS = Time.convertToUTC(getLocalDateTime(selectedStartTime));
    Timestamp endTS = Time.convertToUTC(getLocalDateTime(selectedEndTime));


    for(Appointments appointment : AppointmentsDB.getAllAppointments()){
      if(appointment.getStartTime().equals(startTS)){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("An appointment is already set up to start at that time!");
        alert.showAndWait();
        return false;
      }
      if(startTS.before(appointment.getEndTime()) && startTS.after(appointment.getStartTime())){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("An appointment already exist in that time frame!");
        alert.showAndWait();
        return false;
      }
    }
    if(startTS.before(Time.convertLDTToTimestamp(Time.getCurrentTime()))){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("ERROR");
      alert.setContentText("That time frame has already elapsed.");
      alert.showAndWait();
      return false;
    };
    if (startTS.after(endTS)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("ERROR");
      alert.setContentText("Start time cannot be after end time.");
      alert.showAndWait();
      return false;
    } else if (endTS.before(startTS)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("ERROR");
      alert.setContentText("End time cannot be earlier than start time.");
      alert.showAndWait();
      return false;
    } else if (endTS.equals(startTS)) {
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
   * <p><b>
   *lambda beneficial in that it looks cleaner.  The code "listens" for the button "OK" to be clicked.  If not
   * clicked, it simply will not doing anything further.</b></p>
   * @param actionEvent
   */
  public void deleteButton(ActionEvent actionEvent) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("CONFIRMATION");
    alert.setContentText("Are you sure you would like to cancel appointment with ID of:  "
            + selectedApt.getAptID()+", appointment type of: "+selectedApt.getType() + ", with customer "
            + CustomersDB.findCustomerName(selectedApt.getCustomerID()) + " with contact "
            + selectedApt.getContactName() + " at " + selectedApt.getStartTime() + "?");
    alert.showAndWait().ifPresent(choice -> {
      if (choice == ButtonType.OK) {

    /*Optional<ButtonType> result = alert.showAndWait();
    if (result.isEmpty() || result.get() != ButtonType.OK) {
    } else {*/
        try {
          String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
          DBQuery.setPreparedStatement(DBConnection.getConnection(), deleteStatement);
          PreparedStatement ps = DBQuery.getPreparedStatement();
          ps.setInt(1, selectedApt.getAptID());
          ps.execute();
          AppointmentsDB.resetScheduledAptTableView();
          ps.close();

          Stage stage = (Stage) cancelButt.getScene().getWindow();
          stage.close();
        } catch (SQLException e) {
          e.getMessage();

        }
      }
    });
  }
}

package View_Controller;

import Model.Appointments;
import Model.AppointmentsDB;
import Utils.DBConnection;
import Utils.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    /**
     * scheduledAptTable the tableView used to show our appointsments in the mainmenu and needs to be reset everytime
     * a DB change has been made
     */
    @FXML private TableView<Appointments> scheduledAptTable;
    /**
     * not really used but looking to implement this to replace scheduledAptTable when no DB query has been made
     */
    @FXML private  ObservableList<Appointments> viewAllAppt = FXCollections.observableArrayList();
    /**
     * this list is for when we have filtered appointments by month
     */
    @FXML private ObservableList<Appointments> monthFilteredAppointment = FXCollections.observableArrayList();
    /**
     * this list is used for when we have filtered appointments by week
     */
    @FXML private ObservableList<Appointments> weekFilteredAppointment = FXCollections.observableArrayList();
    /**
     * this is used for when we have filtered appointments by a range
     */
    @FXML private ObservableList<Appointments> betweenFilteredAppointment = FXCollections.observableArrayList();

    @FXML private  TableColumn<Appointments,Integer> aptIDCol;
    @FXML private  TableColumn<Appointments,String> titleCol;
    @FXML private  TableColumn<Appointments,String> descriptionCol;
    @FXML private  TableColumn<Appointments,String> locationCol;
    @FXML private  TableColumn<Appointments,String> typeCol;
    @FXML private  TableColumn<Appointments, Timestamp> startCol;
    @FXML private  TableColumn<Appointments,Timestamp> endCol;
    @FXML private  TableColumn<Appointments,Integer> customerIDCol;


    @FXML private Label timeZoneLabel;
    @FXML private ToggleGroup view;

    @FXML private DatePicker datePickerStart;
    @FXML private DatePicker datePickerEnd;

    private Date selectedStartDate;
    private Date selectedEndDate;

    @FXML private Button exitProgramButton;

    private boolean viewAll = true;
    private boolean weeklyView = false;
    private boolean monthlyView = false;
    private boolean betweenView = false;

    //private static Appointments selectedApt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeZoneLabel.setText(Time.getTimeZone());
        datePickerEnd.setDisable(true);
        datePickerStart.setDisable(true);

        aptIDCol.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        titleCol.setCellValueFactory((new PropertyValueFactory<>("title")));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory((new PropertyValueFactory<>("type")));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        viewAllAppt.setAll(AppointmentsDB.resetScheduledAptTableView());
        scheduledAptTable.setItems(AppointmentsDB.getAllAppointments());


}

    /**
     * this will populate schedulatedAptTable to show Apt coming up based on selected date and a month out from there
     * @param actionEvent
     */
    public void ViewMonthRadioButton(ActionEvent actionEvent) {
        datePickerStart.setDisable(false);
        datePickerEnd.setDisable(true);
        viewAll = false;
        weeklyView = false;
        monthlyView = true;
        betweenView = false;
        if(datePickerStart.getValue() != null) {
            monthFilter();
        }

    }

    /**
     * this will populate scheduledAptTable to show Apt coming up based on selected date and a week out from  there
     * @param actionEvent
     */
    public void viewWeekRadioButton(ActionEvent actionEvent) {
        datePickerStart.setDisable(false);
        datePickerEnd.setDisable(true);
        viewAll = false;
        weeklyView = true;
        monthlyView = false;
        betweenView = false;

        if(datePickerStart.getValue() != null) {
            weekFilter();

        }
    }

    /**
     * this will populate scheduledAptTable with all available appointments
     * @param actionEvent
     */
    public void viewAllRadioButton(ActionEvent actionEvent) {
        datePickerStart.setDisable(true);
        datePickerEnd.setDisable(true);
        viewAll = true;
        weeklyView = false;
        monthlyView = false;
        betweenView = false;
        scheduledAptTable.setItems(AppointmentsDB.getAllAppointments());
    }

    /**
     * this will populate scheduledAptTable with all available appointments form selected range
     * @param actionEvent
     */
    public void betweenRadioButt(ActionEvent actionEvent) {
        datePickerStart.setDisable(false);
        datePickerEnd.setDisable(false);
        viewAll = false;
        weeklyView = false;
        monthlyView = false;
        betweenView = true;

        if(datePickerStart.getValue() != null && datePickerEnd != null){
            rangeFilter();
        }


    }

    /**
     * loads AddCustomer.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void addCustButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/AddCustomer.fxml")));
        Stage addPartStage = new Stage();
        addPartStage.setTitle("Add Customer");
        addPartStage.setScene(new Scene(root, 700, 475));
        addPartStage.show();
    }

    /**
     * loads AddAppointment.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void addAptButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/AddAppointment.fxml")));
        Stage addAptStage = new Stage();
        addAptStage.setTitle("Add Customer");
        addAptStage.setScene(new Scene(root, 415, 520));
        addAptStage.show();



}

    /**
     * checks to see if start date and end date selected have any appointments
     */
    private void rangeFilter(){
        ObservableList<Appointments> allAppointments = AppointmentsDB.getAllAppointments();
        ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();

        for (Appointments filteredAppt : allAppointments){
            // adding 1 day so I can use the before() method
            LocalDate ldEndDate = datePickerEnd.getValue().plusDays(1);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date endDate= Date.from(ldEndDate.atStartOfDay(defaultZoneId).toInstant());
            if ((filteredAppt.getStartTime().after(selectedStartDate) && filteredAppt.getStartTime().before(endDate))) {
                filteredAppointments.add(filteredAppt);
            }
        }
        betweenFilteredAppointment.setAll(filteredAppointments);
        scheduledAptTable.setItems(filteredAppointments);
    }

    /**
     * checks for appoints within 1 week of selected start date
     */
    private void weekFilter() {

        ObservableList<Appointments> allAppointments = AppointmentsDB.getAllAppointments();
        ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();

        for (Appointments filteredAppt : allAppointments){
            // adding 1 day so I can use the before() method
            LocalDate ldEndDate = datePickerStart.getValue().plusWeeks(1).plusDays(1);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date endDate= Date.from(ldEndDate.atStartOfDay(defaultZoneId).toInstant());
            if ((filteredAppt.getStartTime().after(selectedStartDate) && filteredAppt.getStartTime().before(endDate))) {
                filteredAppointments.add(filteredAppt);
            }
        }
        weekFilteredAppointment.setAll(filteredAppointments);
        scheduledAptTable.setItems(filteredAppointments);
}

    /**
     * checks for appointments withing 1 month of selected start date
     */
    private void monthFilter() {

        ObservableList<Appointments> allAppointments = AppointmentsDB.getAllAppointments();
        ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();

        for (Appointments filteredAppt : allAppointments){
            LocalDate ldEndDate = datePickerStart.getValue().plusMonths(1);
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date endDate= Date.from(ldEndDate.atStartOfDay(defaultZoneId).toInstant());
            if ((filteredAppt.getStartTime().after(selectedStartDate) && filteredAppt.getStartTime().before(endDate))) {
                filteredAppointments.add(filteredAppt);
            }
        }
        monthFilteredAppointment.setAll(filteredAppointments);
        scheduledAptTable.setItems(filteredAppointments);
    }

    /**
     * This method will load ModifyAppoint.fxml and also send an Appointments object to they ModifyAppointmentController
     * @param actionEvent
     * @throws IOException
     */
    public void modAptButton(ActionEvent actionEvent) throws IOException {
        //Stage stage = (Stage) exitProgramButton.getScene().getWindow();
        Appointments modifyAppointment = scheduledAptTable.getSelectionModel().getSelectedItem();

        if(modifyAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setContentText("No appointment has been selected");
            alert.showAndWait();
        }else {
            try {

                FXMLLoader loader = new FXMLLoader((getClass().getResource("/View_Controller/ModifyAppointment.fxml")));

                Parent mainMenuController = loader.load();

                Stage modifyAppointmentStage = new Stage();
                modifyAppointmentStage.setTitle("Modify Appointment");

                modifyAppointmentStage.setScene(new Scene(mainMenuController, 415, 525));
                modifyAppointmentStage.show();
                ModifyAppointmentController controller = loader.getController();
                controller.getApt(modifyAppointment);


            } catch (Exception e) {
                System.out.println( e.getMessage());
            }
        }

    }


    /**
     * loads Reports.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void reportButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/Reports.fxml")));
        Stage addPartStage = new Stage();
        addPartStage.setTitle("Reports");
        addPartStage.setScene(new Scene(root, 779, 610));
        addPartStage.show();
    }

    /**
     * Closes program and conneciton
     * @param actionEvent
     */
    public void exitButton(ActionEvent actionEvent) {
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * loads ModifyCustomer.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void modCustButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/ModifyCustomer.fxml")));
        Stage addPartStage = new Stage();
        addPartStage.setTitle("Modify Customer");
        addPartStage.setScene(new Scene(root, 760, 599));
        addPartStage.show();
    }

    /**
     * when user selects a date checks if any of the filter radio buttons are selected.  IF they are it will display
     * appropriate appointments
     * @param actionEvent
     */
    public void startDatePicked(ActionEvent actionEvent) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate ldt = datePickerStart.getValue();
        //substracting a day so I can use the .after() method
        selectedStartDate = Date.from(ldt.atStartOfDay(defaultZoneId).minusDays(1).toInstant());

        if(weeklyView ){
            weekFilter();
        }
        if(monthlyView ){
            monthFilter();
        }
        if(betweenView && datePickerEnd.getValue() != null){
            rangeFilter();
        }
    }

    /**
     * used for rangeFilter() to get dates between a range
     * @param actionEvent
     */
    public void endDatePicked(ActionEvent actionEvent) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate ldt = datePickerEnd.getValue();
        //adding a day to work with .before(0) method
        selectedEndDate = Date.from(ldt.atStartOfDay(defaultZoneId).toInstant());

        if(betweenView && datePickerStart.getValue() != null){
            rangeFilter();
        }

    }


}

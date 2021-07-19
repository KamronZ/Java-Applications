package View_Controller;

import Model.Appointments;
import Model.AppointmentsDB;
import Model.Contact;
import Model.ContactDB;
import Utils.DBConnection;
import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    /**
     * shows total appointments for a specific person and type
     */
    public Text totalAptLabel;
    /**
     * show total number of appointments in the system
     */
    public Text totalAptInSystemlabel;
    @FXML private ObservableList<Appointments> viewAllAppt = FXCollections.observableArrayList();
    /**
     *populated with a list of distinct types from DB
     */
    @FXML private ObservableList<String> types = FXCollections.observableArrayList();
    /**
     * list of months needed to make a DB query
     */
    @FXML private ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October", "November", "December");


    @FXML
    private TableView<Appointments> scheduledAptTable;
    @FXML
    private TableColumn<Appointments, Integer> aptIDCol;
    @FXML
    private TableColumn<Appointments, String> titleCol;
    @FXML
    private TableColumn<Appointments, String> descriptionCol;
    @FXML
    private TableColumn<Appointments, String> locationCol;
    @FXML
    private TableColumn<Appointments, String> typeCol;
    @FXML
    private TableColumn<Appointments, Timestamp> startCol;
    @FXML
    private TableColumn<Appointments, Timestamp> endCol;
    @FXML
    private TableColumn<Appointments, Integer> customerIDCol;

    public ComboBox<String> typeComboBox;
    public ComboBox<String> monthComboBox;
    public Button totalReportsBut;
    public ComboBox<Contact> contactComboBox;
    public Button contactScheduleBut;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aptIDCol.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        titleCol.setCellValueFactory((new PropertyValueFactory<>("title")));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory((new PropertyValueFactory<>("type")));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        getTotalAptCount();


        contactComboBox.setItems(ContactDB.getAllContact());
        contactComboBox.setPromptText("Contact");

        monthComboBox.setItems(months);
        monthComboBox.setPromptText("Month");

        try {
            getTypes();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        typeComboBox.setItems(types);
        typeComboBox.setPromptText("Type");

    }

    /**
     * populates Type comboBox
     * @throws SQLException
     */
    private void getTypes() throws SQLException {
        try {
            String selectStatement = "SELECT distinct Type FROM appointments";
            Statement statement = DBQuery.getPreparedStatement();
            ResultSet rs = statement.executeQuery(selectStatement);
            while (rs.next()) {
                types.add(rs.getString("Type"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * generates report for total appt
     */
    private void getTotalAptCount(){
        try {
            String selectStatement = "Select COUNT(*)  from appointments";
            Statement statement = DBQuery.getPreparedStatement();
            ResultSet rs = statement.executeQuery(selectStatement);
            while(rs.next()){
                int totaAptInSystem = rs.getInt("COUNT(*)");
                totalAptInSystemlabel.setText(Integer.toString(totaAptInSystem));
                System.out.println(totaAptInSystem);
            }
        }catch(SQLException e){

        }

    }

    /**
     * generates reports for total apt for selected month and Type
     * @param actionEvent
     * @throws SQLException
     */
    public void totalAptBut(ActionEvent actionEvent) {
        try {
            if (monthComboBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please Select a month");
                alert.showAndWait();
            } else if (typeComboBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please select appointment type");
                alert.showAndWait();
            } else {
                String selectStatement = "Select COUNT(*)  from appointments WHERE monthname(Start)= ? AND Type = ?";

                DBQuery.setPreparedStatement(DBConnection.getConnection(), selectStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();
                ps.setString(1, monthComboBox.getValue());
                ps.setString(2, typeComboBox.getValue());

                ps.execute();

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int totalApt = rs.getInt("COUNT(*)");
                    totalAptLabel.setText(Integer.toString(totalApt));
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generates report for selected contact and all their appointments from the DB
     * @param actionEvent
     * @throws SQLException
     */
    public void showScheduleBut(ActionEvent actionEvent) {
        try {
            if (contactComboBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select a contact");
            } else {
                ObservableList<Appointments> appointments= FXCollections.observableArrayList();
                Appointments appointment;

                String selectStatement = "SELECT * FROM appointments where Contact_ID= ?";

                DBQuery.setPreparedStatement(DBConnection.getConnection(), selectStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();
                System.out.println(ContactDB.findContact(contactComboBox.getValue().toString()));
                ps.setInt(1, ContactDB.findContact(contactComboBox.getValue().toString()));
                ps.execute();

                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    appointment = new Appointments(resultSet.getInt("Appointment_ID"), resultSet.getString("Title"),
                            resultSet.getString("Description"), resultSet.getString("Location"),
                            resultSet.getInt("Contact_ID"), ContactDB.findContact(resultSet.getInt("Contact_ID")), resultSet.getString("Type"),
                            resultSet.getTimestamp("Start"), resultSet.getTimestamp("End"),
                            resultSet.getInt("Customer_ID"), resultSet.getInt("User_ID"));
                    appointments.add(appointment);
                }
                scheduledAptTable.setItems(appointments);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}




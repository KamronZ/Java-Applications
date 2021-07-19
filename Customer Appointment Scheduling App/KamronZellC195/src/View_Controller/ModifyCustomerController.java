package View_Controller;

import Model.*;
import Utils.DBConnection;
import Utils.DBQuery;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * used to modifyCustomers
 */
public class ModifyCustomerController implements Initializable {
    @FXML public TableView<Customers> customerTableView;
    @FXML public TableColumn<Customers,String> customerNameCol;
    @FXML public TableColumn<Customers,String> postCodeCol;
    @FXML public TableColumn<Customers, String> phoneNumberCol;
    @FXML public TableColumn<Customers, String> streetCol;
    @FXML public TableColumn<Customers,String> countryCol;
    @FXML public TableColumn<Customers,String> stateCol;

    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField streetField;


    @FXML public TextField phoneNumberField;
    @FXML public TextField postCodeField;

    @FXML public ComboBox<Countries> countryComboBox;
    @FXML public ComboBox<Divisions> stateComboBox;

    @FXML public Button cancelButton;

    //public int correspondingCustomerID;
    public int customerID;
    public int divisionID;
    Customers selectedCustomer;


    public void initialize(URL location, ResourceBundle resources) {

        countryComboBox.setItems(CountryDB.setAllCountries());
        stateComboBox.setItems(DivisionsDB.getAllDivisions());
        stateComboBox.setPromptText("Select country first");



       customerTableView.setItems(CustomersDB.loadCustomerJoinedDivisionsTable());
       customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
       postCodeCol.setCellValueFactory((new PropertyValueFactory<>("postalCode")));
       phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
       streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
       countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
       stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
       customerTableView.setItems(CustomersDB.loadCustomerJoinedDivisionsTable());
    }

// fixme add to DivisionsDB after program passes..leaving alone for now to not introduce bugs

    /**
     * gets countryID from the selected country and matches it to a countryID which is also stored in the division class
     * (CountryID is foreign key in Divisions table)
     * @return
     */
    public ObservableList<Divisions> setCorrespondingDivisions() {
        int correspondingCustomerID;

        ObservableList<Divisions> correspondingDivision = FXCollections.observableArrayList();
        Countries countrySelected = (Countries) countryComboBox.getSelectionModel().getSelectedItem();
        correspondingCustomerID = countrySelected.getCountryID();

        for (int i = 0; i < DivisionsDB.getAllDivisions().size(); i++) {
            Divisions matchedDivision = DivisionsDB.getAllDivisions().get(i);
            if (matchedDivision.getCountryID() == correspondingCustomerID) {
                correspondingDivision.add(matchedDivision);
            }
        }
        return correspondingDivision;
    }

    /**
     * Clears and relabels everything when ever the table is cliked
     */
    public void setLabels(){

        // clear everything when something new is selected
        postCodeField.setText("");
        phoneNumberField.setText("");
        streetField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");

        stateComboBox.getSelectionModel().clearSelection();
        countryComboBox.getSelectionModel().clearSelection();

        // get customer object

        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        customerID = selectedCustomer.getCustomerID();


// get list in combobox
        for(Countries c : countryComboBox.getItems()){
            if(c.getCountryID() == selectedCustomer.countryID){
                countryComboBox.setValue(c);
                break;
            }
        }
        stateComboBox.setItems(DivisionsDB.setAllDivisions());
        for(Divisions d : stateComboBox.getItems()){
            if(d.getDivisionID() == selectedCustomer.stateID){
                stateComboBox.setValue(d);
                break;
            }
        }
        /*stateComboBox.getSelectionModel().select(selectedCustomer.getState());
        countryComboBox.getSelectionModel().select( selectedCustomer.getCountry());*/

        System.out.println("Customer ID is: " + customerID);
        System.out.println(selectedCustomer.toString());
        System.out.println("Country is: " + countryComboBox.getSelectionModel().getSelectedItem());
        System.out.println("State/prov is: " + stateComboBox.getSelectionModel().getSelectedItem());

        // set text fields
        postCodeField.setText(selectedCustomer.getPostalCode());
        phoneNumberField.setText(selectedCustomer.getPhoneNumber());
        streetField.setText(selectedCustomer.getStreet());
        countryComboBox.setPromptText(selectedCustomer.getCountry());

        countryComboBox.getSelectionModel().getSelectedItem();
        stateComboBox.setPromptText(selectedCustomer.getState());

        // split full name into first and last name
        String[] fullNameSplit = selectedCustomer.customerName.split(" ");

        try {
            if(fullNameSplit.length < 2){
                firstNameField.setText(fullNameSplit[0]);
                lastNameField.setText("");
            }else{
                firstNameField.setText(fullNameSplit[0]);
                String data = fullNameSplit[1];
                for(int i = 2; i < fullNameSplit.length; i ++){
                    data += " " + fullNameSplit[i];
                }
                lastNameField.setText(data);
            }
            /*if(fullNameSplit.length == 2) {
                firstNameField.setText(fullNameSplit[0]);
                lastNameField.setText(fullNameSplit[1]);
            }if(fullNameSplit.length == 3){
                firstNameField.setText(fullNameSplit[0]);
                lastNameField.setText(fullNameSplit[2]);
            }*/
        }catch(ArrayIndexOutOfBoundsException e){
        }
    }

    /**
     * attempts to modify the customer in db
     * @param actionEvent
     * @return true if the update completed otherwise returns false
     * @throws SQLException
     */
    public boolean modifyCustomerButton(ActionEvent actionEvent) {
        if(selectedCustomer != null) {
            String fistName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String fullName = fistName + " " + lastName;
            String street = streetField.getText();
            String postCode = postCodeField.getText();
            String phoneNumber = phoneNumberField.getText();

            Divisions stateComboBoxValue = (Divisions) stateComboBox.getSelectionModel().getSelectedItem();
            int divisionID = stateComboBoxValue.getDivisionID();

            try {
                String updateStatement1 = "UPDATE customers SET Customer_name = ?,Address = ?,Postal_Code = ?,Phone = ?,Division_ID = ? WHERE Customer_ID = ? ";

                DBQuery.setPreparedStatement(DBConnection.getConnection(), updateStatement1); //create prepared statement
                PreparedStatement firstPS = DBQuery.getPreparedStatement();

                firstPS.setString(1, fullName);
                firstPS.setString(2, street);
                firstPS.setString(3, postCode);
                firstPS.setString(4, phoneNumber);
                firstPS.setInt(5, divisionID);
                firstPS.setInt(6, customerID);

                firstPS.execute();
                int update = firstPS.executeUpdate();

                System.out.println("ROWS AFFECTED *** " + update);
                System.out.println("DIVISION ID **** " + divisionID);

                customerTableView.setItems(CustomersDB.loadCustomerJoinedDivisionsTable());
                firstPS.close();

            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Please make a selection");
        alert.setTitle("ERROR");
        alert.showAndWait();
        return false;
    }

    /**
     * closes current window
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * when the country comboBox has a selection, it will show the appropriate divisions
     * @param actionEvent
     */
    public void countryCombo(ActionEvent actionEvent) {
        if(countryComboBox.getValue() != null) {
            stateComboBox.setItems(DivisionsDB.setAllDivisions());
            stateComboBox.setItems(setCorrespondingDivisions());

            stateComboBox.getSelectionModel().selectFirst();
            stateComboBox.setPromptText("State/Prov");

        }
    }

    /**
     * attempts to delete customer from DB and all their appointments. <p><b>
     *      * @lambda  this lamba is beneficial in that it looks cleaner.  The code "listens" for the button "OK" to be clicked.  If not
     *      * clicked, it simply will not doing anything further.
     *      * </b></p>
     * @lambda  this lamba is beneficial in that it looks cleaner.  The code "listens" for the button "OK" to be clicked.  If not
     * clicked, it simply will not doing anything further.
     * </b></p>
     * @param actionEvent
     */
    public void deleteCustomerButton(ActionEvent actionEvent) {

        if(selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you would like to delete " + selectedCustomer.customerName + " ?");
            alert.showAndWait().ifPresent(choice ->{
            if(choice == ButtonType.OK){
                /* Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
            } else {*/
            }

                try {

                    String deleteStatement1 = "DELETE FROM customers WHERE Customer_ID = ? ";
                    String deleteStatement2 = "DELETE FROM appointments WHERE Customer_ID = ?";

                    DBQuery.setPreparedStatement(DBConnection.getConnection(), deleteStatement2); //create prepared statement
                    PreparedStatement ds1 = DBQuery.getPreparedStatement();

                    ds1.setInt(1, customerID);
                    ds1.execute();
                    ds1.close();
                    DBQuery.setPreparedStatement(DBConnection.getConnection(), deleteStatement1);
                    PreparedStatement ds2 = DBQuery.getPreparedStatement();

                    ds2.setInt(1, customerID);
                    ds2.execute();
                    ds1.close();


                    firstNameField.setText("");
                    lastNameField.setText("");
                    streetField.setText("");
                    phoneNumberField.setText("");
                    postCodeField.setText("");
                    countryComboBox.setValue(null);
                    stateComboBox.setValue(null);
                    selectedCustomer = null;

                    stateComboBox.setPromptText("");
                    countryComboBox.setPromptText("");


                    customerTableView.setItems(CustomersDB.loadCustomerJoinedDivisionsTable());
                    AppointmentsDB.resetScheduledAptTableView();


                } catch (SQLException e) {
                    System.out.println("SQLException: " + e.getMessage());
                }
            });
        }

    }

    /**
     * when the table is clicked, get everything needed to modify a customer and reset textfields
     * @param mouseEvent
     */
    public void onTableClick(MouseEvent mouseEvent) {
        setLabels();
    }
}

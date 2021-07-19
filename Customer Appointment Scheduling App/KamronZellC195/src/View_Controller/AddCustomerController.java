package View_Controller;

import Model.*;
import Utils.DBConnection;
import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {



    public TextField lastNameField;
    public TextField streetField;
    public TextField firstNameField;
    //public TextField cityField;
    public TextField stateField;
    //public TextField emailField;
    public TextField postCodeField;
    public TextField phoneNumberField;
    public Button cancelButton;
    public ComboBox<Divisions> stateComboBox;
    public ComboBox<Countries> countryComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        countryComboBox.setPromptText("Choose a Country");
        countryComboBox.setVisibleRowCount(5);
        countryComboBox.setItems(CountryDB.setAllCountries());
        countryComboBox.getSelectionModel().selectFirst();
        stateComboBox.setPromptText("State/Prov");

        stateComboBox.setItems(DivisionsDB.setAllDivisions());
        stateComboBox.setItems(setCorrespondingDivisions());

    }

    /**
     * gets the list of Divisions that match the Division_ID from DB in the countries table.
     *
     * @return
     */


    public ObservableList<Divisions> setCorrespondingDivisions() {
         int correspondingID;

         ObservableList<Divisions> correspondingDivision = FXCollections.observableArrayList();
        Countries countrySelected = (Countries) countryComboBox.getSelectionModel().getSelectedItem();
        correspondingID = countrySelected.getCountryID();

        for (int i = 0; i < DivisionsDB.getAllDivisions().size(); i++) {
            Divisions matchedDivision = DivisionsDB.getAllDivisions().get(i);
            if (matchedDivision.getCountryID() == correspondingID) {
                correspondingDivision.add(matchedDivision);
            }
        }
        return correspondingDivision;
    }


    /**
     * add customer
     * @param actionEvent
     * @throws SQLException
     */
    public void addButton(ActionEvent actionEvent) throws SQLException {

        try {
            Countries countrySelected = (Countries) countryComboBox.getSelectionModel().getSelectedItem();
            Divisions divisionSelected = (Divisions) stateComboBox.getSelectionModel().getSelectedItem();

            if (divisionSelected == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Please select a division");
                alert.showAndWait();
            } else {

                int divisionID = DivisionsDB.getDivisionMatchingID(divisionSelected);

                String lastName = lastNameField.getText();
                String firstName = firstNameField.getText();
                String street = streetField.getText();
                String country = countrySelected.toString();
                String state = divisionSelected.toString();
                String postCode = postCodeField.getText();
                String phoneNumber = phoneNumberField.getText();

                String fullName = firstName + " " + lastName;
                String fullAddress = country + " Address: " + street + ", " + state;

                // System.out.println(fullAddress);
                if (lastName.isBlank() || lastNameField.getText().isBlank() || lastNameField.getText().equalsIgnoreCase("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Customer must have at least a last name attached");
                    alert.showAndWait();
                } else if (countryComboBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Please select a country");
                    alert.showAndWait();
                } else {

                    String insertStatement = "INSERT INTO customers(Customer_name,Address,Postal_Code,Phone,Division_ID) VALUES(?,?,?,?,?)";

                    DBQuery.setPreparedStatement(DBConnection.getConnection(), insertStatement); //create prepared statement
                    PreparedStatement ps = DBQuery.getPreparedStatement();

                    ps.setString(1, fullName);
                    ps.setString(2, street);
                    ps.setString(3, postCode);
                    ps.setString(4, phoneNumber);
                    ps.setInt(5, divisionID);

                    ps.execute();
                    ps.close();

                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }
            }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }


    /**
     * closes window
     * @param actionEvent
     */
    public void cancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * when country comboBox is selected it will then populate the division comboBox with matching divisions(through division_ID)
     * @param actionEvent
     */
    public void countryCombo(ActionEvent actionEvent) {
        if(countryComboBox.getSelectionModel().getSelectedItem() != null){

            stateComboBox.setItems(setCorrespondingDivisions());

        }
    }
}

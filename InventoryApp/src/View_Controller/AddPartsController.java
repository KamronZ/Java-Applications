package View_Controller;

import Model.InHousePart;
import Model.Inventory;
import Model.OutSourcedPart;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class AddPartsController extends Inventory {

// RadioButtons
    public boolean isInHouse = true;

    @FXML public RadioButton addPartInHouseRadioButton;
    @FXML public RadioButton addPartOutsourcedRadioButton;

    @FXML public TextField addPartsIDField;
    @FXML public TextField addPartNameField;
    @FXML public TextField addPartInventoryField;
    @FXML public TextField addPartCostField;
    @FXML public TextField addPartMaxField;
    @FXML public TextField addPartMinField;
    @FXML public Label sourceLabelField;
    @FXML public TextField addPartSourceField;
    @FXML public ToggleGroup sourceGroup;
    public Button addPartCancelButton;


    /**
     * Changes source Label
      * @param actionEvent
     */
    public void addPartInHouseRadio(ActionEvent actionEvent) {
        isInHouse = true;
        sourceLabelField.setText("Machine ID");
        addPartSourceField.setText("Machine ID");
    }

    /**
     * changes sourceLabel
     * @param actionEvent
     */
    public void addPartOutsourcedRadio(ActionEvent actionEvent) {
        isInHouse = false;
        sourceLabelField.setText("Company name");
        addPartSourceField.setText("Company name");
    }


    /**
     *
     * @param part
     * @return returns true if object passes the checks
     * @throws IOException
     */
    public  boolean isValid(Part part) throws IOException {

        String name = addPartNameField.getText();
        int inventory = Integer.parseInt(addPartInventoryField.getText());
        double cost = Double.parseDouble(addPartCostField.getText());
        int max = Integer.parseInt(addPartMaxField.getText());
        int min = Integer.parseInt(addPartMinField.getText());
        String companyName = addPartSourceField.getText();


        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FORMAT ERROR");
            alert.setContentText("Name field cannot be empty");
            alert.showAndWait();
            throw new IOException("NAME FIELD MIGHT BE EMPTY");
        }

        if (inventory > max || inventory < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setContentText("Inventory must be greater than or equal to MIN and less than or equal to MAX");
            alert.showAndWait();
            throw new IOException("Inventory number is not within min/max parameters");
        }
        if(cost < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setContentText("Cost cannot be negative");
            alert.showAndWait();
            throw new IOException("Cost cannot be negative");
        }
        return true;
    }


    /**
     * Adds a part and allows either inHouse or Outsource instances
     * @param actionEvent
     * @throws IOException
     */
    public void addPartSaveButton(ActionEvent actionEvent) throws IOException {


            int id = generateRandomID();
            String name = addPartNameField.getText();
            double cost = 0.00;
            int inventory = 0;
            int max = 0;
            int min = 0;
            int machineID = 0;
            String companyName = "";

            if(isInHouse){
                try{
                    inventory = Integer.parseInt(addPartInventoryField.getText());
             }catch(NumberFormatException ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("inventory cannot be left blank and must be numeric");
            alert.showAndWait();
        }
                try {
                    cost = Double.parseDouble(addPartCostField.getText());
                }catch(NumberFormatException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setContentText("cost cannot be left blank and must be numeric");
                    alert.showAndWait();
                }
                try {
                    max = Integer.parseInt(addPartMaxField.getText());
                }catch(NumberFormatException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setContentText("max cannot be left blank and must be numeric");
                    alert.showAndWait();
                }
                try {
                    min = Integer.parseInt(addPartMinField.getText());
                }catch(NumberFormatException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setContentText("min cannot be left blank and must be numeric");
                    alert.showAndWait();
                }
                try {
                    machineID = Integer.parseInt(addPartSourceField.getText());
                }catch(NumberFormatException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("");
                    alert.setContentText("inventory cannot be left blank and must be numeric");
                    alert.showAndWait();
                }
                // create a new InHousePart object
                InHousePart newInHousePart = new InHousePart(id,name,cost, inventory,max,min,machineID);
             // check if it's valid
                if(isValid(newInHousePart)) {
                 Inventory.addPart(newInHousePart);
                 Stage stage = (Stage) addPartCancelButton.getScene().getWindow();
                 stage.close();
             }

        }else{
             try {
                 inventory = Integer.parseInt(addPartInventoryField.getText());
             }catch(NumberFormatException ex) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("");
                 alert.setContentText("inventory cannot be left blank and must be numeric");
                 alert.showAndWait();
             }
             try {
                 cost = Double.parseDouble(addPartCostField.getText());
             }catch(NumberFormatException ex) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("");
                 alert.setContentText("cost cannot be left blank and must be numeric");
                 alert.showAndWait();
             }
             try {
                 max = Integer.parseInt(addPartMaxField.getText());
             }catch(NumberFormatException ex) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("");
                 alert.setContentText("max cannot be left blank and must be numeric");
                 alert.showAndWait();
             }
             try {
                 min = Integer.parseInt(addPartMinField.getText());
             }catch(NumberFormatException ex) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("");
                 alert.setContentText("min cannot be left blank and must be numeric");
                 alert.showAndWait();
             }
             try {
                 companyName = addPartSourceField.getText();
             }catch(NumberFormatException ex) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("");
                 alert.setContentText("company name cannot be blank");
                 alert.showAndWait();
             }
            OutSourcedPart newOutSourcedPart = new OutSourcedPart(id,name,cost, inventory,max,min,companyName);
            if(isValid(newOutSourcedPart)) {
                Inventory.addPart(newOutSourcedPart);
                Stage stage = (Stage) addPartCancelButton.getScene().getWindow();
                stage.close();
            }
        }

    }

    /**
     * Closes window and returns to mainscreen
     * @param actionEvent
     */
    public void addPartCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Part Cancel");
        alert.setContentText("are you sure you  want to cancel?");
        alert.showAndWait().ifPresent(choice -> {
            if (choice == ButtonType.OK) {
                Stage stage = (Stage) addPartCancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    /**
     * sets toggle group
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sourceGroup = new ToggleGroup();
        this.addPartInHouseRadioButton.setToggleGroup(sourceGroup);
        this.addPartOutsourcedRadioButton.setToggleGroup(sourceGroup);




    }


}

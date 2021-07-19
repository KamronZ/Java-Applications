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

public class ModifyPartsController extends Inventory {


    @FXML private  TextField modifyPartIDField;
    @FXML private TextField modifyPartNameField;
    @FXML private  TextField modifyPartInventoryField;
    @FXML private  TextField modifyPartPriceField;
    @FXML private  TextField modifyPartMaxField;
    @FXML  private  TextField modifyPartMinField;
    @FXML private  TextField modifyPartSourceField;
    @FXML private Label modifyPartSourceLabel;

    @FXML  private Button modifyPartCancelButton;
    @FXML  private Button modifyPartSaveButton;
    // I have inHouse automatically selected via scene builder
    @FXML  private RadioButton modifyPartInHouseRadioButton;
    @FXML  private RadioButton modifyPartOutSourcedRadioButton;
    @FXML  private boolean isInHouse = true;

    private Part thePart = null;


    /**
     * takes the part object sent from mainscreen Controller and uses it to set textFields and allows us
     * to actually modify the same object
     * @param modifyPart
     */
    public  void getPart(Part modifyPart){
        this.thePart = modifyPart;
        setLabels(thePart);

    }

    /**
     * Takes in a part and checks for good data,
     * @param part
     * @param radioButton used to determine whether we are working with inhouse or outsourced part
     * @return returns true if data is entered properly
     * @throws IOException
     */
    public  boolean isValid(Part part, RadioButton radioButton ) throws IOException {
        String name = modifyPartNameField.getText();
        int inventory = Integer.parseInt(modifyPartInventoryField.getText());
        double cost = Double.parseDouble(modifyPartPriceField.getText());
        int max = Integer.parseInt(modifyPartMaxField.getText());
        int min = Integer.parseInt(modifyPartMinField.getText());
        String companyName = "";
        int machineID = 0;

        if(radioButton.isSelected()) {
            companyName = modifyPartSourceField.getText();
}else{
    machineID =Integer.parseInt(modifyPartSourceField.getText());
}


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
     * setLabels to what user selected from Mainscreen
     * @param modifyPart
     */
    private void setLabels(Part modifyPart){
        if(modifyPart instanceof InHousePart){
            modifyPartInHouseRadioButton.setSelected(true);
            isInHouse = true;
            modifyPartIDField.setText(Integer.toString(modifyPart.getId()));
            modifyPartNameField.setText(modifyPart.getName());
            modifyPartInventoryField.setText(Integer.toString(modifyPart.getStock()));
            modifyPartPriceField.setText(Double.toString(modifyPart.getPrice()));
            modifyPartMaxField.setText((Integer.toString(modifyPart.getMax())));
            modifyPartMinField.setText(Integer.toString(modifyPart.getMin()));
            modifyPartSourceLabel.setText("Machine ID");
            modifyPartSourceField.setText(Integer.toString(((InHousePart) modifyPart).getMachineID()));

        }
        if(modifyPart instanceof OutSourcedPart){
            modifyPartOutSourcedRadioButton.setSelected(true);
            isInHouse = false;
            modifyPartIDField.setText(Integer.toString(modifyPart.getId()));
            modifyPartNameField.setText(modifyPart.getName());
            modifyPartInventoryField.setText(Integer.toString(modifyPart.getStock()));
            modifyPartPriceField.setText(Double.toString(modifyPart.getPrice()));
            modifyPartMaxField.setText((Integer.toString(modifyPart.getMax())));
            modifyPartMinField.setText(Integer.toString(modifyPart.getMin()));
            modifyPartSourceLabel.setText("Company name");
            modifyPartSourceField.setText(((OutSourcedPart) modifyPart).getCompanyName());
        }


    }

    /**
     * sets source to be InHouse
     * @param actionEvent
     */
    public void ModifyPartInHouseRadioButton(ActionEvent actionEvent) {
        isInHouse = true;
        modifyPartSourceField.setText("Machine ID");
        modifyPartSourceLabel.setText("Machine ID");
    }

    /**
     * sets source to be Outsourced
     * @param actionEvent
     */
    public void ModifyPartOutsourcedRadioButton(ActionEvent actionEvent) {
        isInHouse = false;
        modifyPartSourceField.setText("Company name");
        modifyPartSourceLabel.setText("Company name");
    }


    /**
     * Saves part with new fields
     * @param actionEvent
     */
    public void modifyPartSaveButton(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(modifyPartIDField.getText());
        String name = modifyPartNameField.getText();
        int inventory = 0;
        double cost = 0.00;
        int max = 0;
        int min = 0;
        String companyName = "";
        int machineID = 0;



        if(isInHouse){
            try {
                name = modifyPartNameField.getText();
            }catch(NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Name cannot be left blank");
                alert.showAndWait();
            }
            try{
            inventory = Integer.parseInt(modifyPartInventoryField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(" Inv field cannot be left blank and must be an integer");
                alert.showAndWait();
            }try{
                cost = Double.parseDouble( modifyPartPriceField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Cost field cannot be left blank and must be a number");
                alert.showAndWait();
            }
            try{
            max = Integer.parseInt(modifyPartMaxField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Max field cannot be left blank and must be an integer");
                alert.showAndWait();
            }try{
            min = Integer.parseInt(modifyPartMinField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Min field cannot be left blankd and must be an integer");
                alert.showAndWait();
            }try{
            machineID = Integer.parseInt(modifyPartSourceField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("MachineId Field cannot be left blank and must be an integer");
                alert.showAndWait();
            }
            InHousePart newInHousePart = new InHousePart(id,name,cost, inventory,max,min,machineID);
            if(isValid(newInHousePart,modifyPartInHouseRadioButton)) {
                Inventory.updatePart(newInHousePart);
                Stage stage = (Stage) modifyPartCancelButton.getScene().getWindow();
                stage.close();
            }
            }if(!isInHouse){


            try {
                name = modifyPartNameField.getText();
            }catch(NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Name cannot be left blank");
                alert.showAndWait();
            }
            try{
                inventory = Integer.parseInt(modifyPartInventoryField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(" Inv field cannot be left blank and must be an integer");
                alert.showAndWait();
            }try{
                cost = Double.parseDouble( modifyPartPriceField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Cost field cannot be left blank and must be a number");
                alert.showAndWait();
            }
            try{
                max = Integer.parseInt(modifyPartMaxField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Max field cannot be left blank and must be an integer");
                alert.showAndWait();
            }try{
                min = Integer.parseInt(modifyPartMinField.getText());
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Min field cannot be left blankd and must be an integer");
                alert.showAndWait();
            }try{
                companyName = modifyPartSourceField.getText();
            }catch(NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Company name field cannot be left blank");
                alert.showAndWait();
            }
            Part newOutsourcedPart = new OutSourcedPart(id,name,cost, inventory,max,min,companyName);

            if(isValid(newOutsourcedPart, modifyPartOutSourcedRadioButton)) {
                Inventory.updatePart(newOutsourcedPart);
                Stage stage = (Stage) modifyPartCancelButton.getScene().getWindow();
                stage.close();
           }

            //OutSourcedPart newOutSourcedPart = new OutSourcedPart(id,name,cost, inventory,max,min,companyName);
            //Inventory.addPart(newOutSourcedPart);

            }

        }

    /**
     * return to mainscreen
     * @param actionEvent
     */
    public void ModifyPartCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait().ifPresent(choice -> {
            if(choice == ButtonType.OK){
                Stage stage = (Stage)modifyPartCancelButton.getScene().getWindow();
                stage.close();

            }
        });
    }
}

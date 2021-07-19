/**
 * JavaDoc location is KamronZellC484\javaDoc
 */


package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/** RUNTIME ERROR

 * SECTION G
 * Logical error I had here was auto having the modified product actually replace the the original product.
 * Instead of replacing the original object I was creating a new object which left my new object and old object existing
 * at the same time.  I think I could have tried to dereference like we learned in c++ but I couldn't figure it out in java.
 * After regrouping I came to the realizing what i needed was a method to search for a matching part/product by something unique such as the id
 * and then use set() method instead of a trying to do something like Inventory.addPart(newPart).  Then  I realized that I had already
 * written this method awhile ago in the inventory class called updateProduct()/updatePart.  This method searches for a matching ID,
 * and if it finds it it uses the set method to replace the item.
 */
public class ModifyProductController extends Inventory implements Initializable  {

    @FXML public Button modifyProductsCancelButton;

    @FXML private  TextField modifyProductSearchField;

    @FXML private TableView<Part> associatedPartsTable;
    @FXML private  TableColumn<Part, Integer> associatedPartIDColumn;
    @FXML private  TableColumn<Part, String> associatedPartNameColumn;
    @FXML private  TableColumn<Part, Integer> associatedPartInventoryLvlColumn;
    @FXML private  TableColumn<Part, Integer> associatedPartCostColumn;

    @FXML private  TextField modifyProductMaxField;
    @FXML private  TextField modifyProductMinField;
    @FXML private  TextField modifyProductIDField;
    @FXML private  TextField modifyProductNameField;
    @FXML private  TextField modifyProductInventoryField;
    @FXML private  TextField modifyProductCostField;

    @FXML private  TableView<Part> modifyProductCurrentTable;
    @FXML private  TableColumn<Part, Integer> modifyProductCurrentProductsPartIDColumn;
    @FXML private  TableColumn <Part, String>modifyProductCurrentProductsPartNameColumn;
    @FXML private  TableColumn<Part, Integer> modifyProductCurrentInventoryLvlColumn;
    @FXML private  TableColumn <Part, Integer>modifyProductCurrentCostColumn;

    private Product theProduct = null;

    private ObservableList<Part> partsToAssociate =  FXCollections.observableArrayList();

    /**
     * needed to populate tables
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory((new PropertyValueFactory<>("name")));
        associatedPartInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));




        modifyProductCurrentProductsPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductCurrentProductsPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductCurrentInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductCurrentCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        modifyProductCurrentTable.setItems(Inventory.getAllParts());
    }

    /**
     * checks that parts do not exceed product cost
     * @param productToValidate
     * @return
     */
    private boolean validCost(Product productToValidate){
        double partCost = 0.00;
        for(int i = 0; i < partsToAssociate.size(); i++){
           partCost += partsToAssociate.get(i).getPrice();
        }
        return productToValidate.getPrice() > partCost;
    }

    /**
     * validates input
     * @param product
     * @return
     * @throws IOException
     */
    public  boolean isValid(Product product) throws IOException {

        String name = modifyProductNameField.getText();
        int inventory = Integer.parseInt(modifyProductInventoryField.getText());
        double cost = Double.parseDouble(modifyProductCostField.getText());
        int max = Integer.parseInt(modifyProductMaxField.getText());
        int min = Integer.parseInt(modifyProductMinField.getText());

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
        if(!validCost(product)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cost error");
            alert.setContentText("Parts cannot cost more than Product");
            alert.showAndWait();
            throw new IOException("Parts cannot cost more than Product");
        }
        return true;
    }

    /**
     * get selected object from mainscreen
     * @param modifyProduct
     */
    public void getProduct(Product modifyProduct){
        this.theProduct = modifyProduct;
        associatedPartsTable.setItems(theProduct.getAllAssociatedParts());
        partsToAssociate.setAll(theProduct.getAllAssociatedParts());
        setLabels(theProduct);

    }
    // private, rest of program doesnt need to see this

    /**
     * sets labels from selected Product from main screen
     * @param modifyProduct
     */
    private void setLabels(Product modifyProduct){
        modifyProductIDField.setText(Integer.toString(modifyProduct.getId()));
        modifyProductNameField.setText(modifyProduct.getName());
        modifyProductInventoryField.setText(Integer.toString(modifyProduct.getStock()));
        modifyProductCostField.setText(Double.toString(modifyProduct.getPrice()));
        modifyProductMaxField.setText((Integer.toString(modifyProduct.getMax())));
        modifyProductMinField.setText(Integer.toString(modifyProduct.getMin()));
    }

    /**
     * returns to main screen
     * @param actionEvent
     */
    public void ModifyProductCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("Are you sure you want to cancel");
        alert.showAndWait().ifPresent(choice -> {
            if(choice == ButtonType.OK){
                Stage stage = (Stage) modifyProductsCancelButton.getScene().getWindow();
                stage.close();
            }
        });

    }

    /**
     * saves product with parts associated to it
     * @param actionEvent
     * @throws IOException
     */
    public void ModifyProductSaveButton(ActionEvent actionEvent) throws IOException {


        int id = theProduct.getId();
        String name = "";
        int inventory = 0;
        double cost = 0.00;
        int max = 0;
        int min = 0;

        try{
            name = modifyProductNameField.getText();
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Name cannot be left blank");
            alert.showAndWait();
        }
        try{
            inventory = Integer.parseInt(modifyProductInventoryField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Inventory field cannot be left blank and must be an integer");
            alert.showAndWait();
        }
        try{
            cost = Double.parseDouble(modifyProductCostField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Cost field cannot be left blank and must be an Integer");
            alert.showAndWait();
        }
        try{
            max = Integer.parseInt(modifyProductMaxField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Max field cannot be left blank and must be an integer");
            alert.showAndWait();
        }
        try{
            min = Integer.parseInt(modifyProductMinField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Min field cannot be left blank and must be an integer");
            alert.showAndWait();
        }
        theProduct = new Product(id,name,cost,inventory,min,max);
        if(isValid(theProduct)) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setContentText("Are you sure you want associate: " + partsToAssociate.size() + " Parts to: " + theProduct.getName());
            alert.showAndWait().ifPresent(choice -> {
                if (choice == ButtonType.OK) {
                    for (Part part : partsToAssociate) {
                        theProduct.addAssociatedPart(part);
                    }


                    Inventory.updateProduct(theProduct);
                    Stage stage = (Stage) modifyProductsCancelButton.getScene().getWindow();
                    stage.close();

                }
            });
        }
    }

    /**
     * searches for parts to add to product
     * @param actionEvent
     */
    public void ModifyProductSearchButton(ActionEvent actionEvent) {
        String search = modifyProductSearchField.getText();
        ObservableList<Part> foundParts = Inventory.lookupPart(search);
        modifyProductSearchField.setText("");
        modifyProductCurrentTable.setItems(foundParts);
        if (foundParts.isEmpty()) {

            try {
                int id = Integer.parseInt(search);
                Part foundPart = Inventory.lookupPart(id);
                if(foundPart != null){
                    foundParts.add(foundPart);
                }
                modifyProductCurrentTable.setItems(foundParts);
            } catch (NumberFormatException error) {
            }
            modifyProductSearchField.setText("");
            modifyProductCurrentTable.setItems(foundParts);

            if(foundParts.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setContentText("No item was found");
                alert.showAndWait();
                modifyProductCurrentTable.setItems(Inventory.getAllParts());
            }
        }

    }

    /**
     * adds parts to be associated with product
     * @param actionEvent
     */
    public void ModifyProductAddButton(ActionEvent actionEvent) {
        Part partToAssociate = modifyProductCurrentTable.getSelectionModel().getSelectedItem();


        if (partToAssociate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select an item");
            alert.showAndWait();
            } else{


            partsToAssociate.add(partToAssociate);
            associatedPartsTable.setItems(partsToAssociate);

        }
    }

    /**
     * deletes a part that was going to be associated with a product
     * @param actionEvent
     */
    public void ModifyProductDeleteButton(ActionEvent actionEvent) {
        Part selectedProduct = associatedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("IMPROPER SELECTION MADE");
            alert.setContentText("Please select an item from the current list to remove.  If you would like to" +
                    "remove a product, please return to the main screen");
            alert.showAndWait();
        } else {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("CONFIRMATION");
            alert2.setContentText("are you sure you want to delete item?");
            alert2.showAndWait().ifPresent(choice -> {
                if (choice == ButtonType.OK) {
                    partsToAssociate.remove(selectedProduct);
                }
            });
        }
    }
}

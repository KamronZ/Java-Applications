package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
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

public class AddProductsController extends Inventory implements Initializable {

    @FXML public TextField addProductSearchField;
    @FXML public TextField addProductIDField;
    @FXML public TextField addProductNameField;
    @FXML public TextField addProductInventoryField;
    @FXML public TextField addProductPriceField;
    @FXML public TextField addProductMaxField;
    @FXML public TextField addProductMinField;

    @FXML public TableView<Part> associatedPartsTable;
    @FXML  public TableColumn<Part,Integer> addProductProductIDColumn;
    @FXML  public TableColumn<Part,String> addProductProductNameColumn;
    @FXML  public TableColumn<Part, Integer> addProductInventoryLvlColumn;
    @FXML public TableColumn<Part, Double> addProductCostColumn;

    @FXML public TableView<Part> addProductCurrentTable;
    @FXML  public TableColumn<Part,Integer> addProductCurrentProductPartIDColumn;
    @FXML public TableColumn<Part,String> addProductCurrentPartNameColumn;
    @FXML public TableColumn<Part,Integer> addProductCurrentInventoryLvlColumn;
    @FXML public TableColumn<Part,Double> addProductCurrentCostColumn;
    public Button addProductCancelButton;

    private ObservableList<Part> partsToAssociate =  FXCollections.observableArrayList();

    /**
     * Used to populate tables
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        addProductProductIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductProductNameColumn.setCellValueFactory((new PropertyValueFactory<>("name")));
        addProductInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartsTable.setItems(partsToAssociate);



        addProductCurrentProductPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductCurrentPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductCurrentInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductCurrentCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductCurrentTable.setItems(Inventory.getAllParts());
        addProductCurrentTable.setItems(Inventory.getAllParts());
    }

    /**
     * checks that parts do not cost more than Product is used with isValid method
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
     * does a check to make sure data is useable
     * @param product
     * @return
     * @throws IOException
     */
    public  boolean isValid(Product product) throws IOException {

        String name = addProductNameField.getText();
        int inventory = Integer.parseInt(addProductInventoryField.getText());
        double cost = Double.parseDouble(addProductPriceField.getText());
        int max = Integer.parseInt(addProductMaxField.getText());
        int min = Integer.parseInt(addProductMinField.getText());

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
            alert.setContentText("Parts cannot cost more than product");
            alert.showAndWait();
            throw new IOException("Parts cannot cost more than product");
        }
        return true;
    }

    /**
     * returns to mainscreen
     * @param actionEvent
     */
    public void addProductCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRM");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait().ifPresent(choice ->{
            if(choice == ButtonType.OK){
                Stage stage = (Stage) addProductCancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    /**
     * adds product
     * @param actionEvent
     * @throws IOException
     */
    public void addProductSaveButton(ActionEvent actionEvent) throws IOException {
        int id = generateRandomID();
        String name = "";
        int inventory = 0;
        double cost = 0.00;
        int max = 0;
        int min = 0;
        try {
             name = addProductNameField.getText();
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Name cannot be left blank");
            alert.showAndWait();
        }
        try {
             inventory = Integer.parseInt(addProductInventoryField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Inv cannot be left blank and must be a whole number");
            alert.showAndWait();
        }
        try{
         cost = Double.parseDouble(addProductPriceField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Cost cannot be left blank and must be a number");
            alert.showAndWait();
        }
        try{
         max = Integer.parseInt(addProductMaxField.getText());
        }catch(NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Max cannot be left blank and must be number");
            alert.showAndWait();
        }
        try{
         min = Integer.parseInt(addProductMinField.getText());
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Min cannot be left blank and must be number");
            alert.showAndWait();
        }

        Product modifiedProduct = new Product(id, name, cost, inventory, max, min);

        for (Part part : partsToAssociate) {
            modifiedProduct.addAssociatedPart(part);
        }
        if (partsToAssociate.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not enough parts added");
            alert.setContentText("At least one part must be added to the product first.");
            alert.showAndWait();
        } else {
            if(isValid(modifiedProduct)) {

                Inventory.addProduct(modifiedProduct);
                Stage stage = (Stage) addProductCancelButton.getScene().getWindow();
                stage.close();
            }
        }
   }

    /**
     * searches for part to be added to product
     * @param actionEvent
     */
    public void addProductSearchButton(ActionEvent actionEvent) {
        String search = addProductSearchField.getText();
        ObservableList<Part> matchingParts = Inventory.lookupPart(search);
        addProductCurrentTable.setItems(matchingParts);
        addProductSearchField.setText("");

        if(matchingParts.isEmpty()){
            try{
                 int id = Integer.parseInt(search);
                 Part matchingPart = Inventory.lookupPart(id);
                 if(matchingPart != null){
                     matchingParts.add(matchingPart);

                 }
            }catch(NumberFormatException error){
            }
            addProductSearchField.setText("");
            addProductCurrentTable.setItems(matchingParts);

            if(matchingParts.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search Failed");
                alert.setContentText("No products found");
                alert.showAndWait();
                addProductCurrentTable.setItems(Inventory.getAllParts());
            }


        }

    }

    /**
     * adds parts to be associated with a product
     * @param actionEvent
     */
    public void addProductAddButton(ActionEvent actionEvent) {
        Part partToAdd = addProductCurrentTable.getSelectionModel().getSelectedItem();
        if (partToAdd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No items selected");
            alert.setContentText("Please select a part to add");
            alert.showAndWait();
        } else {
            partsToAssociate.add(partToAdd);
        }
    }

    /**
     * deletes part to be assocaited to product
     * @param actionEvent
     */
    public void addProductDeleteButton(ActionEvent actionEvent) {
        Part deleteItem = associatedPartsTable.getSelectionModel().getSelectedItem();
        boolean isSelectionEmpty = associatedPartsTable.getSelectionModel().isEmpty();

        if (isSelectionEmpty) {
            System.out.println("Error");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED");
            alert.setContentText("Please select item from ADD TABLE to remove.  Return to main screen to remove from current Inventory");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETING ITEM");
            alert.setContentText("Are you sure you want to delete selected item? ");
            alert.showAndWait().ifPresent(choice -> {
                if (choice == ButtonType.OK) {
                    partsToAssociate.remove(deleteItem);
                }
            });
        }
    }



}

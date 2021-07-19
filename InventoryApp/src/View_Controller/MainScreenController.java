package View_Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.*;
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


public class MainScreenController  implements Initializable  {



    @FXML private TextField mainPartsSearchField;
    @FXML private TextField mainProductsSearchField;

    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, Integer> mainPartIDColumn;
    @FXML private TableColumn<Part, Integer> mainPartNameColumn;
    @FXML private TableColumn<Part, Integer> mainPartInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> mainPartPriceColumn;


    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Part, Integer> mainProductsPartIDColumn;
    @FXML private TableColumn<Part, Integer> mainProductsPartNameColumn;
    @FXML  private TableColumn<Part, Integer> mainProductInventoryLvlColumn;
    @FXML private TableColumn<Part, Double> mainProductCostColumn;


    /**
     * Used to put in test data and populate tables
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        // some test data
        Inventory.addPart(new OutSourcedPart(3,"Ryzen 5800x", 450, 10,1,1000,"AMD"));
        Inventory.addPart(new OutSourcedPart(2,"EVGA 3080 RTX", 850, 2,1,1000,"EVGA"));
        Inventory.addPart(new OutSourcedPart(1,"Meshify C", 80.50, 100,1,1000,"Fractal Design"));
        Inventory.addPart(new InHousePart(9,"Generic case", 45.00, 120,1,1000,12));
        Inventory.addPart(new InHousePart(7,"Generic 600w PSU", 50.00, 120,1,1000,10));


        Inventory.addProduct(new Product(2, "High-end", 2700, 15, 1, 1000));

        Inventory.addProduct(new Product(1, "Low-end",800 , 100, 1, 1000));
        Inventory.addProduct(new Product(0, "Med-end", 1600.00, 15, 1, 1000));


        mainPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameColumn.setCellValueFactory((new PropertyValueFactory<>("name")));
        mainPartInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(Inventory.getAllParts());


        mainProductsPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductsPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductInventoryLvlColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(Inventory.getAllProducts());




    }

    /**
     * searches for parts
     * @param actionEvent
     */
    public void mainPartsSearchButton(ActionEvent actionEvent) {
        String search = mainPartsSearchField.getText();

        ObservableList<Part> matchingParts = Inventory.lookupPart(search);
       /* ObservableList<Part> reset; // copy of partTable in case I want to reset list
        reset = partsTable.getItems();*/

        partsTable.setItems(matchingParts);

        if(matchingParts.isEmpty()){

            try{
                int id = Integer.parseInt(search); //Integer.getInteger(mainPartsSearchField.getText()); did not work
                Part matchedParts = Inventory.lookupPart(id);
                if(matchedParts != null){
                    matchingParts.add(matchedParts);

                }
            }catch(NumberFormatException error){


            }
            mainPartsSearchField.setText(""); // resets search field
            partsTable.setItems(matchingParts);

            if(matchingParts.isEmpty()) {// if a search found nothing, reset list and give an alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("failed");
                alert.setContentText("No item was found");
                alert.showAndWait(); // waits for user input on error screen

                partsTable.setItems(Inventory.getAllParts()); //list is reset to current inventory
            }
        }
    }

    /**
     * searches for products
     * @param actionEvent
     */
    public void mainProductsSearchButton(ActionEvent actionEvent) {
        String search = mainProductsSearchField.getText();
        ObservableList<Product> matchingProducts = Inventory.lookupProduct(search);
        productsTable.setItems(matchingProducts);

        if(matchingProducts.isEmpty()){
            try {
                int prodID = Integer.parseInt(search);
                Product matchedProduct = Inventory.lookupProduct(prodID);
                if (matchedProduct != null) {
                    matchingProducts.add(matchedProduct);
                }
            }catch(NumberFormatException error){

            }
            mainProductsSearchField.setText("");
            productsTable.setItems(matchingProducts);
            if(matchingProducts.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setContentText("No item was found");
                alert.showAndWait();
                productsTable.setItems(Inventory.getAllProducts());
            }
        }



    }

    /**
     * Oens add part form is nothing is selected, otherwise opens modifyParts form
     * @param mouseEvent
     */
    public  void mainPartModifyButton(javafx.scene.input.MouseEvent mouseEvent) {
        Part modifyPart = partsTable.getSelectionModel().getSelectedItem();


        if(modifyPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NO PRODUCT SELECTED");
            alert.setContentText("Opening add part form instead");
            alert.showAndWait();
            try {
                // nothing selected therefore AddParts.fxml is loaded instead of ModifyParts
                Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/AddParts.fxml")));
                Stage modifyPartsStage = new Stage();
                modifyPartsStage.setTitle("Modify Part");
                modifyPartsStage.setScene(new Scene(root, 535, 435));
                modifyPartsStage.show();
            } catch (Exception error) {
                error.printStackTrace();
            }
        }else{


            try{
                // something is selected there ModifyParts.fmxl is selected
                FXMLLoader loader = new FXMLLoader((getClass().getResource("/View_Controller/ModifyParts.fxml")));
                Parent mainScreenController = loader.load();
                Stage modifyPartsStage = new Stage();
                modifyPartsStage.setTitle("Modify Part");
                modifyPartsStage.setScene(new Scene(mainScreenController, 535, 435));
                modifyPartsStage.show();

                ModifyPartsController controller = loader.getController();
                controller.getPart(modifyPart);



            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    /**
     * deletes a part
     * @param actionEvent
     */
    public void mainPartDeleteButton(ActionEvent actionEvent) {


        Part partSelected = partsTable.getSelectionModel().getSelectedItem();
        boolean isSelectionEmpty = partsTable.getSelectionModel().isEmpty();

        if (isSelectionEmpty) {
            System.out.println("Error");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED");
            alert.setContentText("Please select item from table to be removed");
            alert.showAndWait();
        } else {
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("CONFIRMATION");
                alert2.setContentText("Are you sure you want to remove item?");
                alert2.showAndWait().ifPresent(choice -> {
                    if (choice == ButtonType.OK) {
                        Inventory.deletePart(partSelected);
                    }
                });

            }

    }

    /**
     * adds a part
     * @param mouseEvent
     */
    public void mainProductsAddButton(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/AddProduct.fxml")));
            Stage addPartStage = new Stage();
            addPartStage.setTitle("Add Product");
            addPartStage.setScene(new Scene(root, 1020, 525));
            addPartStage.show();
        }
        catch (Exception error){
            error.printStackTrace();
        }

    }

    /**
     * opens addProduct if nothing selected, otherwise it grabs a selected product and sends it over to the modify
     * Product controller
     * @param mouseEvent
     */
    public void mainProductsModifyButton(javafx.scene.input.MouseEvent mouseEvent) {

        Product modifyProduct = productsTable.getSelectionModel().getSelectedItem();
        if (modifyProduct == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NO PRODUCT SELECTED");
            alert.setContentText("Opening add product form instead");
            alert.showAndWait();

            try {
                //loading addProduct.fxml since no product is selected
                Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/AddProduct.fxml")));
                Stage modifyProductStage = new Stage();
                modifyProductStage.setTitle("Add Product");
                modifyProductStage.setScene(new Scene(root, 1020, 525));
                modifyProductStage.show();
            } catch (Exception error) {
                error.printStackTrace();
            }
        }else {
            try {
                FXMLLoader loader = new FXMLLoader((getClass().getResource("/View_Controller/ModifyProduct.fxml")));
                Parent mainScreenController = loader.load();
                Stage modifyProductStage = new Stage();
                modifyProductStage.setTitle("Modify Product");
                modifyProductStage.setScene(new Scene(mainScreenController, 1020, 525));
                modifyProductStage.show();

                ModifyProductController controller = loader.getController();
                controller.getProduct(modifyProduct);

            } catch (Exception error) {
                error.printStackTrace();

            }
        }
    }

    /**
     * deletes a product as long as no parts are assocaited with it
     * @param actionEvent
     */
    public void mainProductsDelete(ActionEvent actionEvent) {
        Product productSelected = productsTable.getSelectionModel().getSelectedItem();
        boolean isSelectionEmpty = productsTable.getSelectionModel().isEmpty();

        if(isSelectionEmpty){
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
            emptyAlert.setTitle("NOT SELECTION MADE");
            emptyAlert.setContentText("Please select a product from the product table");
            emptyAlert.showAndWait();
        }

        if(productSelected.getAllAssociatedParts().size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Product cannot be deleted");
            alert.setContentText("Product has parts associated with it.  Please modify product first.");
            alert.showAndWait();
        } else {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("CONFIRMATION");
            alert2.setContentText("Are you sure you want to remove item?");
            alert2.showAndWait().ifPresent(choice -> {
                if (choice == ButtonType.OK) {
                    Inventory.deleteProduct(productSelected);
                }
            });
        }
    }


    /**
     * exits the program
     * @param actionEvent
     */
    public void mainExitButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setContentText("Are you sure you want to exit?");

       // Optional<ButtonType> choice = alert.showAndWait(); replaced with
        // lambda expression

        alert.showAndWait().ifPresent(choice -> {
            if (choice == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    /**
     * opens addPart form
     * @param mouseEvent
     */
    public void mainPartAddButton(javafx.scene.input.MouseEvent mouseEvent) {

        try {
            Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/AddParts.fxml")));
            Stage addPartStage = new Stage();
            addPartStage.setTitle("Add Part");
            addPartStage.setScene(new Scene(root, 550, 450));
            addPartStage.show();
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }




}


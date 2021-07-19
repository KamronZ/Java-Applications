
package KamronZellC195;

import Utils.DBConnection;
import Utils.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Scanner;


public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View_Controller/LogInScreen.fxml"));
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 600, 500));

        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
       // Locale.setDefault(new Locale("fr"));

        Connection conn = DBConnection.startConnection();







        launch(args);

       // anything typed after launch will only happen once app is closed.
        DBConnection.closeConnection();





    }
    public static void performUpdate(Connection conn) throws SQLException {
        String countryName;
        String createdBy;
        String newCountry;

        String updateStatement = "UPDATE countries SET country = ?, created_By = ? WHERE country = ?";
        DBQuery.setPreparedStatement(conn,updateStatement); //create prepared statement
        PreparedStatement ps = DBQuery.getPreparedStatement();

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter a country to update: ");
        countryName = keyboard.nextLine();

        System.out.println("Enter a new country");
        newCountry = keyboard.nextLine();

        System.out.println("Enter user: ");
        createdBy = keyboard.nextLine();

        //key-value mapping
        ps.setString(1, newCountry);
        ps.setString(2,createdBy);
        ps.setString(3, countryName);


        ps.execute(); // execute PreparedStatement

        // checks rows affected
        if(ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " rows affected");
        else
            System.out.println("No change");
        keyboard.close();
    }

}

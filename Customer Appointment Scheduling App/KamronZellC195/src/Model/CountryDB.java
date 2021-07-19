package Model;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * class to work with DB commands on countries objects
 */
public class CountryDB {
    private static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    /**
     * Used to get a list of all the countries and is less expensive than setAllCountries
     * @return allCountries
     */
    public static ObservableList<Countries> getAllCountries(){
        return allCountries;
    }

    /**
     * DB Query that gets all the countries available in the database
     * @return allCountries, null if SQL exception
     * @Throws MYSQLException
     */
    public static ObservableList<Countries> setAllCountries() {
        ObservableList<Countries> countries = FXCollections.observableArrayList();
        Countries country;

        try {
            String selectStatement = "SELECT * FROM countries";
            Statement statement = DBConnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            while(resultSet.next()){
                country = new Countries(resultSet.getInt("Country_ID"),resultSet.getString("Country"));
                countries.add(country);

            }
            statement.close();
            allCountries.setAll(countries);
            return countries;
        }catch(SQLException exception){
            System.out.println("MYSQL exception: " + exception.getMessage());
            return null;
        }
    }

}

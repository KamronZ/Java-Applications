package Model;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * used for DB commands on Divisions
 */
public class DivisionsDB {
    /***
     * List of all available Divisions
     */
    private static ObservableList<Divisions> allDivisions = FXCollections.observableArrayList();

    /**
     * used to get all divisions list
     *
     * @return allDivisions
     */
    public static ObservableList<Divisions> getAllDivisions() {
        return allDivisions;
    }

    /**
     * Takes the name of the divison and looks for a match in the allDivision table.  Returns the ID of the division found
     *
     * @param division
     * @return divisionID or -1 if not found(all divisions are > 0)
     */
    public static int getDivisionMatchingID(Divisions division) {
        for (Divisions foundDivision : allDivisions) {
            if (foundDivision.getDivisionName().equalsIgnoreCase(division.getDivisionName())) {
                return foundDivision.getDivisionID();
            }
        }
        return -1;
    }




    /**
     * redundant remove and test program
     * @param divisionName
     * @return
     */
    public static int getDivisionMatchingID(String divisionName) {
        for (Divisions foundDivision : allDivisions) {
            if (foundDivision.getDivisionName().equalsIgnoreCase(divisionName)) {
                return foundDivision.getDivisionID();
            }
        }
        return -1;
    }

    /**
     * db command to be used when allDivisions needs to be rest
     * @return allDivision or null
     * @throws SQLException
     */
    public static ObservableList<Divisions> setAllDivisions(){
        ObservableList<Divisions> divisions = FXCollections.observableArrayList();
        Divisions division;

        try{
            String selectStatement = "SELECT * FROM first_level_divisions";
            Statement statement = DBConnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            while(resultSet.next()){
                 division = new Divisions(resultSet.getInt("Division_ID"),resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID"));
                 divisions.add(division);

            }
            allDivisions.setAll(divisions);
            statement.close();
            return divisions;


        }catch(SQLException exception){
            System.out.println("SQL ERROR: " + exception.getMessage());
        }
        return null;
    }



}

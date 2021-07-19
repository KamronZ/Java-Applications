package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * creates DB connection
 */
public class DBConnection {
    /**
     * used to Create a connect to DB
     */

    /**
     * URL Pieces
     */
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ07PPM?connectionTimeZone=SERVER";

    private static final String jdbcURL = protocol + vendorName + ipAddress;
    /**
     * Driver Interface reference
     */
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U07PPM";
    private static String password = "53689092642";

    /**
     * starts a connection with provided URL and Driver references
     * @return conn
     * @throws SQLException
     */
    public static Connection startConnection() {

        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage()); // get what error occurred
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }

    /**
     * This closes the DB connection and is used at program termination
     * @throws SQLException
     */
    public static void closeConnection(){ //throws SQLException; this works but I want to get a message so try/catch instead

        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * used when the current connections is needed
     * @return conn
     */
    public static Connection getConnection(){
        return conn;
    }
}

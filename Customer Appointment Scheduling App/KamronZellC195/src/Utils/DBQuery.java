package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class purpose is to create a Statement reference
 * It is established by using the Connection class first and calling the createStatement method.
 */
public class DBQuery {

    private static PreparedStatement statement; // statement ref

    /**
     * Creates a statement object and helps create a statement refernce
     * @param conn
     * @param sqlStatement
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    /**
     * return statement object
     * @return statement
     */
    public static PreparedStatement getPreparedStatement(){
        return statement;
    }

}

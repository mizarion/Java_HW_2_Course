package phonebook.io.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static phonebook.io.db.DBDefinitions.*;

final class InitUtils {

    public static Connection connectToDB() throws SQLException {

        // define the Derby connection URL to use
        String connectionURL = "jdbc:derby:" + host + dbName + ";create=true";

        // Create (if needed) and connect to the database.
        // The driver is loaded automatically.
        Connection conn = DriverManager.getConnection(connectionURL);
        System.out.println("Connected to database " + host + dbName);

        return conn;
    }

    public static void createTable(Connection conn, Statement s) throws SQLException {

        //   ## INITIAL SQL SECTION ##

        // Call utility method to check if table exists.
        //      Create the table if needed
        if (!wwdChk4Table(conn)) {
            System.out.println(" . . . . creating table " + tableName);
            s.execute(createString);
        }
    }

    private static boolean wwdChk4Table(Connection conTst) throws SQLException {
        boolean chk = true;
        boolean doCreate = false;
        try {
            Statement s = conTst.createStatement();
            //s.execute("update WISH_LIST set ENTRY_DATE = CURRENT_TIMESTAMP, WISH_ITEM = 'TEST ENTRY' where 1=3");
            s.execute("update " + tableName + " set " + tableField_id + " = 99999, " + tableField_surname + "= 'Dark', "
                    + tableField_name + " = 'Van' where 1=3");
        } catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            //   System.out.println("  Utils GOT:  " + theError);
            /** If table exists will get -  WARNING 02000: No row was found **/
            if (theError.equals("42X05"))   // Table does not exist
            {
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                System.out.println("WwdChk4Table: Incorrect table definition. Drop table WISH_LIST and rerun this program");
                throw sqle;
            } else {
                System.out.println("WwdChk4Table: Unhandled SQLException");
                throw sqle;
            }
        }
        //  System.out.println("Just got the warning - table exists OK ");
        return true;
    }
}

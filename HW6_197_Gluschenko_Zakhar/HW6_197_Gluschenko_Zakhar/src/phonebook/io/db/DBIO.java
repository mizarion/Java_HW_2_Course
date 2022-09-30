package phonebook.io.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import phonebook.Person;

import java.sql.*;

import static phonebook.io.db.DBDefinitions.*;

public class DBIO {

    /**
     * 1 - tableField_id <br>
     * 2 - tableField_surname <br>
     * 3 - tableField_name <br>
     * 4 - tableField_patronymic <br>
     * 5 - tableField_phoneNumber <br>
     * 6 - tableField_homeNumber <br>
     * 7 - tableField_address <br>
     * 8 - tableField_birthday <br>
     * 9 - tableField_notes
     */
    private static PreparedStatement insertStatement;
    private static Connection conn;
    private static Statement statement;

    public static void start() throws SQLException {

        conn = InitUtils.connectToDB();

        statement = conn.createStatement();

        InitUtils.createTable(conn, statement);

        insertStatement = conn.prepareStatement("INSERT INTO " + tableName + ""
                + " (" + tableField_id + ", " + tableField_surname + ", " + tableField_name + ", "
                + tableField_patronymic + ", " + tableField_phoneNumber + ", " + tableField_homeNumber + ", "
                + tableField_address + ", " + tableField_birthday + ", " + tableField_notes
                + ") Values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }

    public static void shutdown() throws SQLException {
        // Release the resources (clean up )
        insertStatement.close();
        statement.close();
        conn.close();
        System.out.println("Closed connection");

        //   ## DATABASE SHUTDOWN SECTION ##
        /*** In embedded mode, an application should shut down Derby.
         Shutdown throws the XJ015 exception to confirm success. ***/
        if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
            boolean gotSQLExc = false;
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (se.getSQLState().equals("XJ015")) {
                    gotSQLExc = true;
                }
            }
            if (!gotSQLExc) {
                //System.out.println("Database did not shut down normally");
                throw new SQLException("Database did not shut down normally");
            } else {
                System.out.println("Database shut down normally");
            }
        }
    }

//    private static void addInTable() throws SQLException {
//        insertStatement.setInt(1, new Random().nextInt(100000));
//        insertStatement.setString(2, "Simple_Ivan");
//        insertStatement.setString(3, "Darkovich");
//        insertStatement.executeUpdate();
//    }

    private static void addPersonDB(Person person) throws SQLException {
        insertStatement.setInt(1, person.getId());
        insertStatement.setString(2, person.getSurname());
        insertStatement.setString(3, person.getName());
        insertStatement.setString(4, person.getPatronymic());
        insertStatement.setString(5, person.getPhoneNumber());
        insertStatement.setString(6, person.getPhoneNumber2());
        insertStatement.setString(7, person.getAddress());
        insertStatement.setString(8, person.getBirthday());
        insertStatement.setString(9, person.getNotes());

        insertStatement.executeUpdate();
    }

    public static void writeDB(ObservableList<Person> persons) throws SQLException {

        // очищаем таблицу перед записью
        statement.executeUpdate("TRUNCATE TABLE  " + tableName);

        // записываем persons
        for (Person person : persons) {
            addPersonDB(person);
        }
    }

    public static ObservableList<Person> readDB() throws SQLException {
        //   Select all records in table

        //final String printLine = "  __________________________________________________";

        //final String executeQueryRequest = "select ENTRY_DATE, WISH_ITEM, WISH_ITEM_PRICE from WISH_LIST order by ENTRY_DATE";
        final String executeQueryRequest = "select * from " + tableName;

        ObservableList<Person> newPersons = FXCollections.observableArrayList();

        ResultSet resultSet = statement.executeQuery(executeQueryRequest);

        //  Loop through the ResultSet and print the data
        //System.out.println(printLine);
        while (resultSet.next()) {
            int id = resultSet.getInt(tableField_id);
            String surname = resultSet.getString(tableField_surname);
            String name = resultSet.getString(tableField_name);
            String patronymic = resultSet.getString(tableField_patronymic);
            String phoneNumber = resultSet.getString(tableField_phoneNumber);
            String homeNumber = resultSet.getString(tableField_homeNumber);
            String address = resultSet.getString(tableField_address);
            String birthday = resultSet.getString(tableField_birthday);
            String notes = resultSet.getString(tableField_notes);

            newPersons.add(new Person(id, surname, name, patronymic, phoneNumber, homeNumber, address, birthday, notes));
            //System.out.println(Objects.toString(newPersons.get(newPersons.size() - 1)));
            //System.out.println("hello № " + id + " " + surname + " " + name);
        }
        //System.out.println(printLine);
        //  Close the resultSet
        resultSet.close();

        return newPersons;
    }
}

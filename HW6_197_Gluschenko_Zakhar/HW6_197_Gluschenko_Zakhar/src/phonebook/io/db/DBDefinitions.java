package phonebook.io.db;

//   ## DEFINE VARIABLES SECTION ##

//    public static final String createString = "CREATE TABLE WISH_LIST  "
//            + "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY "
//            + "   CONSTRAINT WISH_PK PRIMARY KEY, "
//            + " ENTRY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
//            + " WISH_ITEM VARCHAR(32) NOT NULL,"
//            + " WISH_ITEM_PRICE INT NOT NULL)";


final class DBDefinitions {
    public static final String tableName = "Persons";
    public static final String tableField_id = "id";
    public static final String tableField_surname = "surname";
    public static final String tableField_name = "name";
    public static final String tableField_patronymic = "patronymic";
    public static final String tableField_phoneNumber = "phoneNumber";
    public static final String tableField_homeNumber = "homeNumber";
    public static final String tableField_address = "address";
    public static final String tableField_birthday = "birthday";
    public static final String tableField_notes = "notes";
    public static final String createString = "CREATE TABLE " + tableName + " ("
            + tableField_id + " INT NOT NULL PRIMARY KEY,"
            + tableField_surname + " VARCHAR(255) NOT NULL,"
            + tableField_name + " VARCHAR(255) NOT NULL,"
            + tableField_patronymic + " VARCHAR(255),"
            + tableField_phoneNumber + " VARCHAR(255),"
            + tableField_homeNumber + " VARCHAR(255),"
            + tableField_address + " VARCHAR(255),"
            + tableField_birthday + " VARCHAR(255),"
            + tableField_notes + " VARCHAR(255)"
            + ")";


    // define the driver to use
    public static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    public static final String host = ""; //    "//localhost:1527/";

    // the database name
    public static final String dbName = "PhoneBookDB";

}

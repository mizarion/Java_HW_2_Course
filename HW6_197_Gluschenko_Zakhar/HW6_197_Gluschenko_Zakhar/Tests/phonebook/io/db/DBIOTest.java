package phonebook.io.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import phonebook.Person;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DBIOTest {
    private DBIOTest() throws SQLException {
        DBIO.start();
    }

    private static final String surname = "Ivanov";
    private static final String name = "Ivan";
    private static final String patronymic = "Ivanovich";
    private static final String phoneNumber = "8(800)555-35-35";
    private static final String address = "address";
    private static final String birthday = "20.12.1990";
    private static final String notes = "notes";
    private static final String phoneNumber2 = "8(800)555-35-35";


    @Test
    void writeAndReadDB() throws SQLException {
        ObservableList<Person> persons = FXCollections.observableArrayList();

        final int count = 10;

        for (int i = 1; i <= count; i++) {
            persons.add(new Person(i, surname + i, name + i, patronymic + i, phoneNumber, phoneNumber2, address, birthday, notes));
        }

        assertEquals(persons.size(), count);
        DBIO.writeDB(persons);
        assertEquals(persons.size(), count);

        ObservableList<Person> savedPersons = DBIO.readDB();
        assertEquals(savedPersons.size(), count);
        assertEquals(persons, savedPersons);
    }

    @Test
    void rewrite() throws SQLException {

        ObservableList<Person> persons = FXCollections.observableArrayList();

        final int count = 10;
        final int rewrites = 5;

        for (int i = 1; i <= count; i++) {
            persons.add(new Person(i, surname + i, name + i, patronymic + i, phoneNumber, phoneNumber2, address, birthday, notes));
        }

        for (int i = 0; i < rewrites; i++) {
            assertEquals(persons.size(), count);
            DBIO.writeDB(persons);
            assertEquals(persons.size(), count);
            ObservableList<Person> savedPersons = DBIO.readDB();
            assertEquals(savedPersons.size(), count);
            assertEquals(persons, savedPersons);
        }
    }

    @Test
    void unmodifiedRead() throws SQLException {

        ObservableList<Person> persons = FXCollections.observableArrayList();

        final int count = 10;
        final int rereads = 10;

        for (int i = 1; i <= count; i++) {
            persons.add(new Person(i, surname + i, name + i, patronymic + i, phoneNumber, phoneNumber2, address, birthday, notes));
        }

        DBIO.writeDB(persons);

        for (int i = 0; i < rereads; i++) {
            assertEquals(persons.size(), count);

            ObservableList<Person> savedPersons = DBIO.readDB();
            assertEquals(savedPersons.size(), count);
            assertEquals(persons, savedPersons);
        }
    }
}
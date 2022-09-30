package phonebook.io.file;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import phonebook.Person;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonsIOTest {
    private final ObservableList<Person> persons = FXCollections.observableArrayList();

    private static final String surname = "Ivanov";
    private static final String name = "Ivan";
    private static final String phoneNumber = "8(800)555-35-35";

    private static final String newSurname = "Petrovich";
    private static final String newName = "Petr";
    private static final String newPhoneNumber = "123456";

    private final Person person1, person2;

    private final File file = new File(String.valueOf(Paths.get(System.getProperty("user.dir") + "\\PersonsIOTestTemp.ser")));

    PersonsIOTest() {
        person1 = new Person(surname, name, phoneNumber);
        person2 = new Person(newSurname, newName, newPhoneNumber);
        persons.add(person1);
        persons.add(person2);
    }

    @Test
    void testWriteRead() throws IOException, ClassNotFoundException {

        PersonsIO.write(file.getPath(), persons);
        // Загружаем выбранный файл
        ObservableList<Person> newPersons = PersonsIO.read(file.getPath());

        assertEquals(persons.size(), newPersons.size());
        assertEquals(persons, newPersons);

        for (int i = 0; i < persons.size(); i++) {

            assertTrue(persons.get(i).equals(newPersons.get(i)));
            assertEquals(persons.get(i), newPersons.get(i));
        }
        file.delete();
    }
}
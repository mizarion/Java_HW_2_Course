package phonebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private final String surname = "Ivanov";
    private final String name = "Ivan";
    private final String patronymic = "Ivanovich";
    private final String phoneNumber = "8(800)555-35-35";
    private final String address = "address";
    private final String birthday = "20.12.1990";
    private final String notes = "notes";

    private final String newSurname = "Petrov";
    private final String newName = "Petr";
    private final String newPatronymic = "Petrovich";
    private final String newPhoneNumber = "123456";
    private final String phoneNumber2 = "8(800)555-35-35";

    private final Person contact = new Person(surname, name, phoneNumber);

    @Test
    void checkNotEmpty() {

        Person contact = new Person(surname, name, phoneNumber);
        assertEquals(surname, contact.getSurname());
        assertEquals(name, contact.getName());
        assertEquals(phoneNumber, contact.getPhoneNumber());
    }

    @Test
    void checkEmptyCtor() {

        // surname is blank
        Throwable surnameException = assertThrows(IllegalArgumentException.class, () -> new Person("", name, phoneNumber));
        assertEquals("surname is blank", surnameException.getMessage());

        // name is blank
        Throwable nameException = assertThrows(IllegalArgumentException.class, () -> new Person(surname, "", phoneNumber));
        assertEquals("name is blank", nameException.getMessage());

        // phone is blank
        Throwable phoneException = assertThrows(IllegalArgumentException.class, () -> new Person(surname, name, ""));
        assertEquals("phoneNumber is blank", phoneException.getMessage());


        Throwable surnameException2 = assertThrows(IllegalArgumentException.class,
                () -> new Person("", name, phoneNumber, "", "", "", "", ""));
        assertEquals("surname is blank", surnameException2.getMessage());

        // name is blank
        Throwable nameException2 = assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, "", phoneNumber, "", "", "", "", ""));
        assertEquals("name is blank", nameException2.getMessage());

        // phone is blank
        Throwable phoneException2 = assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, name, "", "", "", "", "", ""));
        assertEquals("phoneNumber is blank", phoneException2.getMessage());

        // phones is blank
        Throwable phoneException3 = assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, name, patronymic, "", "", address, birthday, notes));
        assertEquals("phoneNumber is blank", phoneException3.getMessage());

        // phone1 isn't blank
        assertDoesNotThrow(() -> new Person(surname, name, patronymic, phoneNumber, "", address, birthday, notes));

        // phone1 isn't blank
        assertDoesNotThrow(() -> new Person(surname, name, patronymic, "", phoneNumber2, address, birthday, notes));

        // день рождение не соответсвует формату / не является датой
        Throwable birthday = assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, "2021", notes));
        assertEquals("Введена не корректная дата для дня рождения", birthday.getMessage());
        assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, "text", notes));
        assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, "26-03-2021", notes));
        assertThrows(IllegalArgumentException.class,
                () -> new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, "32-13-2021", notes));
        assertDoesNotThrow(() -> new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, "20.12.1990", notes));

    }

    @Test
    void checkSetters() {
        Person contact = new Person(surname, name, phoneNumber);

        // Проверяем значение по умолчанию
        assertEquals(surname, contact.getSurname());
        assertEquals(name, contact.getName());
        assertEquals(phoneNumber, contact.getPhoneNumber());

        // Присваеваем новые значения
        contact.setSurname(newSurname);
        contact.setName(newName);
        contact.setPhoneNumber(newPhoneNumber);

        // Проверяем присвоенные значения
        assertEquals(newSurname, contact.getSurname());
        assertEquals(newName, contact.getName());
        assertEquals(newPhoneNumber, contact.getPhoneNumber());
    }

    @Test
    void checkEmptySetters() {
        Person contact = new Person(surname, name, phoneNumber);

        // name is blank
        Throwable nameException = assertThrows(IllegalArgumentException.class, () -> contact.setName(""));
        assertEquals("new name is blank", nameException.getMessage());

        // surname is blank
        Throwable surnameException = assertThrows(IllegalArgumentException.class, () -> contact.setSurname(""));
        assertEquals("new surname is blank", surnameException.getMessage());

        // phoneNumber is blank
        Throwable phoneNumberException = assertThrows(IllegalArgumentException.class, () -> contact.setPhoneNumber(""));
        assertEquals("new phone number is blank", phoneNumberException.getMessage());
    }

    @Test
    void checkCopy() {
        Person contact = new Person(surname, name, phoneNumber);

        // Проверяем значение по умолчанию
        assertEquals(surname, contact.getSurname());
        assertEquals(name, contact.getName());
        assertEquals(phoneNumber, contact.getPhoneNumber());

        // Создаем новый контакт и передаем его данные другому
        Person temp = new Person(newSurname, newName, newPhoneNumber);
        contact.copy(temp);

        // Проверяем присвоенные значения
        assertEquals(newSurname, contact.getSurname());
        assertEquals(newName, contact.getName());
        assertEquals(newPhoneNumber, contact.getPhoneNumber());


        Person temp2 = new Person(newSurname, newName, newPhoneNumber);
        Person mainPerson = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
        temp2.copy(mainPerson);

        assertEquals(mainPerson, temp2);
        assertEquals(mainPerson.getSurname(), temp2.getSurname());
        assertEquals(mainPerson.getName(), temp2.getName());
        assertEquals(mainPerson.getPatronymic(), temp2.getPatronymic());
        assertEquals(mainPerson.getPhoneNumber(), temp2.getPhoneNumber());
        assertEquals(mainPerson.getPhoneNumber2(), temp2.getPhoneNumber2());
        assertEquals(mainPerson.getAddress(), temp2.getAddress());
        assertEquals(mainPerson.getBirthday(), temp2.getBirthday());
        assertEquals(mainPerson.getNotes(), temp2.getNotes());
    }

    @Test
    void checkEquals() {

        // Проверяем два контакта с одинаковыми данными
        Person contact = new Person(surname, name, phoneNumber);
        Person contact1 = new Person(surname, name, phoneNumber);
        assertEquals(contact, contact);
        assertEquals(contact1, contact);
        assertEquals(contact, contact1);
        assertSame(contact1, contact1);

        // Проверяем два контакта с разными данными не являющиеся уникальными идентификаторами
        Person contact2 = new Person(surname, name, newPhoneNumber);
        assertEquals(contact, contact2);

        // Проверяем два контакта с разными данными не являющиеся уникальными идентификаторами
        Person mainPerson = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
        Person Person1 = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
        Person Person2 = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, "newNotes");
        Person Person3 = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, "", notes);
        Person Person4 = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, "new address", birthday, notes);
        Person Person5 = new Person(surname, name, patronymic, phoneNumber, "888888", address, birthday, notes);
        Person Person6 = new Person(surname, name, patronymic, "999999", phoneNumber2, address, birthday, notes);

        assertEquals(mainPerson, Person1);
        assertEquals(mainPerson, Person2);
        assertEquals(mainPerson, Person3);
        assertEquals(mainPerson, Person4);
        assertEquals(mainPerson, Person5);
        assertEquals(mainPerson, Person6);
    }

    @Test
    void testNotEquals() {
        // Проверяем явное различие с разными типами & null
        assertNotEquals(contact, null);
        assertNotEquals(contact, "");
        assertNotEquals(contact, 10);

        // Проверяем два контакта с разными уникальными идентификаторами
        Person contact4 = new Person(surname, newName, phoneNumber);
        Person contact5 = new Person(newSurname, name, phoneNumber);
        Person contact6 = new Person(newSurname, newName, newPhoneNumber);

        assertNotEquals(contact, contact4);
        assertNotEquals(contact, contact5);
        assertNotEquals(contact, contact6);

        // Проверяем два контакта с разными уникальными идентификаторами ctor2
        Person mainPerson = new Person(surname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
        Person person1 = new Person(newSurname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
        Person person2 = new Person(surname, newName, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
        Person person3 = new Person(surname, name, newPatronymic, phoneNumber, phoneNumber2, address, birthday, notes);

        assertNotEquals(mainPerson, person1);
        assertNotEquals(mainPerson, person2);
        assertNotEquals(mainPerson, person3);
    }
}
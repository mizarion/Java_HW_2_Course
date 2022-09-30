package phonebook;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Person implements Serializable {
    private String surname;
    private String name;
    private String patronymic;
    private String phoneNumber;
    private String phoneNumber2;
    private String address;
    private String birthday;
    private String notes;

    private static int nextId = 1;
    private final int id;

//    {
//        id = nextId++;

    //    }
    public Person(int newId, String surname, String name, String patronymic,
                  String phoneNumber, String phoneNumber2, String address, String birthday, String notes) {

        if (surname.isBlank()) {
            throw new IllegalArgumentException("surname is blank");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("name is blank");
        }
        if (phoneNumber.isBlank() && phoneNumber2.isBlank()) {
            throw new IllegalArgumentException("phoneNumber is blank");
        }
        // todo: переделать тип
        // Если день рождение введен - он обязан быть соответсовать формату, иначе иключение
        if (!birthday.isBlank()) {
            try {
                LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (Exception e) { // обертка для "понятного" сообщения об ошибке
                throw new IllegalArgumentException("Введена не корректная дата для дня рождения");
            }
        }
        //nextId++;
        this.id = newId;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.phoneNumber2 = phoneNumber2;
        this.address = address;
        this.birthday = birthday;
        this.notes = notes;
    }

    public Person(String surname, String name, String phoneNumber) {

        this(nextId++, surname, name, "", phoneNumber, "", "", "", "");

//        if (surname.isBlank()) {
//            throw new IllegalArgumentException("surname is blank");
//        }
//        if (name.isBlank()) {
//            throw new IllegalArgumentException("name is blank");
//        }
//        if (phoneNumber.isBlank()) {
//            throw new IllegalArgumentException("phoneNumber is blank");
//        }
//
//        this.id = nextId++;
//        this.surname = surname;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.patronymic = "";
//        this.phoneNumber2 = "";
//        this.address = "";
//        this.birthday = "";
//        this.notes = "";
    }

    public Person(String surname, String name, String patronymic,
                  String phoneNumber, String phoneNumber2, String address, String birthday, String notes) {
        this(nextId++, surname, name, patronymic, phoneNumber, phoneNumber2, address, birthday, notes);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getNotes() {
        return notes;
    }

    public void setSurname(String newSurname) {
        if (newSurname.isBlank()) {
            throw new IllegalArgumentException("new surname is blank");
        }
        this.surname = newSurname;
    }

    public void setName(String newName) {
        if (newName.isBlank()) {
            throw new IllegalArgumentException("new name is blank");
        }
        this.name = newName;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        if (newPhoneNumber.isBlank()) {
            throw new IllegalArgumentException("new phone number is blank");
        }
        this.phoneNumber = newPhoneNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Person contact = (Person) obj;

        // Уникальным идентификатором Контакта является комбинация полей Имя, Фамилия и Отчество
        return this.getName().equals(contact.getName())
                && this.getSurname().equals(contact.getSurname())
                && this.getPatronymic().equals(contact.getPatronymic());
    }

    /**
     * Копирует данные из переданного контакта
     *
     * @param newPerson Ссылка на другой контакт
     */
    public void copy(Person newPerson) {
        // т.к. newPerson уже существует => он прошел все проверки
        this.surname = newPerson.surname;
        this.name = newPerson.name;
        this.patronymic = newPerson.patronymic;
        this.phoneNumber = newPerson.phoneNumber;
        this.phoneNumber2 = newPerson.phoneNumber2;
        this.address = newPerson.address;
        this.birthday = newPerson.birthday;
        this.notes = newPerson.notes;
    }
}

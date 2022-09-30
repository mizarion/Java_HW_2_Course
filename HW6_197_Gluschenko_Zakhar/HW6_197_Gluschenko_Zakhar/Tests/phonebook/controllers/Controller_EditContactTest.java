package phonebook.controllers;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import phonebook.Person;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Controller_EditContactTest {
    private static final int testTime = 5000;
    private final String surname = "Ivanov";
    private final String name = "Ivan";
    private final String patronymic = "Ivanovich";
    private final String phoneNumber = "8(800)555-35-35";
    private final String address = "adress";
    private final String birthday = "20.12.1990";
    private final String note = "notes";

    private void setFields(Controller_EditContact controller) {
        controller.getTextField_surname().setText(surname);
        controller.getTextField_name().setText(name);
        controller.getTextField_patronymic().setText(patronymic);
        controller.getTextField_phoneNumber().setText(phoneNumber);
        controller.getTextField_address().setText(address);
        controller.getTextField_birthday().setText(birthday);
        controller.getTextField_note().setText(note);
    }

    private final String newSurname = "Petrovich";
    private final String newName = "Petr";
    private final String newPhoneNumber = "123456";

    private final Person person = new Person(surname, name, phoneNumber);

    Person mainPerson = new Person(surname, name, patronymic, phoneNumber, newPhoneNumber, address, birthday, "");

    @Test
    void handleApply() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        // Controller
                        Stage stage = new Stage();
                        Group group = new Group();
                        Scene scene = new Scene(group);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
                        loader.setController(controller);
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        Person van = new Person("Darknode", "Van", "300");
                        Person bill = new Person("Hariton", "Bill", "300");

                        TableView<Person> personTableView = controller.getTableView_table();
                        controller.addPerson(person);
                        controller.addPerson(van);
                        controller.addPerson(bill);

                        assertEquals(3, personTableView.getItems().size());
                        assertEquals(8, personTableView.getColumns().size());

                        personTableView.getSelectionModel().select(0);

                        // Открываем окно с редактированием контакта
                        controller.handleEditButton(new ActionEvent());
                        Controller_EditContact controller_editContact = controller.getController_editContact();
                        controller_editContact.setNotification(false);
                        ((Stage) controller_editContact.getTextField_name().getScene().getWindow()).hide();

                        // Проверяем что нет изменений
                        assertEquals(surname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals("", person.getPatronymic());
                        assertEquals(phoneNumber, person.getPhoneNumber());
                        assertEquals("", person.getPhoneNumber2());
                        assertEquals("", person.getBirthday());
                        assertEquals("", person.getNotes());
                        assertEquals("", person.getAddress());

                        // Задаем значения полям
                        setFields(controller_editContact);
                        controller_editContact.handleApply(new ActionEvent());

                        // Проверяем новые значения
                        assertEquals(surname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals(patronymic, person.getPatronymic());
                        assertEquals(phoneNumber, person.getPhoneNumber());
                        assertEquals("", person.getPhoneNumber2());
                        assertEquals(address, person.getAddress());
                        assertEquals(birthday, person.getBirthday());
                        assertEquals(note, person.getNotes());

                        controller_editContact.getTextField_surname().setText(newSurname);
                        controller_editContact.handleApply(new ActionEvent());

                        // Проверяем изменения - а именно, что изменилась фамилия
                        assertEquals(newSurname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals(patronymic, person.getPatronymic());
                        assertEquals(phoneNumber, person.getPhoneNumber());
                        assertEquals("", person.getPhoneNumber2());
                        assertEquals(address, person.getAddress());
                        assertEquals(birthday, person.getBirthday());
                        assertEquals(note, person.getNotes());

                        // Меняем имя
                        controller_editContact.getTextField_name().setText(newName);
                        controller_editContact.handleApply(new ActionEvent());

                        // Проверяем изменения - изменилось имя
                        assertEquals(newSurname, person.getSurname());
                        assertEquals(newName, person.getName());
                        assertEquals(patronymic, person.getPatronymic());
                        assertEquals(phoneNumber, person.getPhoneNumber());
                        assertEquals("", person.getPhoneNumber2());
                        assertEquals(address, person.getAddress());
                        assertEquals(birthday, person.getBirthday());
                        assertEquals(note, person.getNotes());

                        // Меняем телефон
                        controller_editContact.getTextField_phoneNumber().setText(newPhoneNumber);
                        controller_editContact.handleApply(new ActionEvent());

                        // Проверяем изменения - изменился телефон
                        assertEquals(newSurname, person.getSurname());
                        assertEquals(newName, person.getName());
                        assertEquals(newPhoneNumber, person.getPhoneNumber());
                        assertEquals(patronymic, person.getPatronymic());
                        assertEquals("", person.getPhoneNumber2());
                        assertEquals(address, person.getAddress());
                        assertEquals(birthday, person.getBirthday());
                        assertEquals(note, person.getNotes());

                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void handleApplyEmpty() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        // Controller
                        Stage stage = new Stage();
                        Group group = new Group();
                        Scene scene = new Scene(group);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
                        loader.setController(controller);
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        Person van = new Person("Darknode", "Van", "300");
                        Person bill = new Person("Hariton", "Bill", "300");

                        TableView<Person> personTableView = controller.getTableView_table();
                        controller.addPerson(person);
                        controller.addPerson(van);
                        controller.addPerson(bill);

                        assertEquals(3, personTableView.getItems().size());
                        assertEquals(8, personTableView.getColumns().size());

                        personTableView.getSelectionModel().select(0);

                        // Открываем окно с редактированием контакта
                        controller.handleEditButton(new ActionEvent());
                        Controller_EditContact collector = controller.getController_editContact();
                        collector.setNotification(false);
                        ((Stage) collector.getTextField_name().getScene().getWindow()).hide();

                        // Пытаемся сменить фамилию
                        collector.getTextField_surname().setText("");
                        collector.handleApply(new ActionEvent());

                        // Проверяем что ничего не изменилось
                        assertEquals(surname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals(phoneNumber, person.getPhoneNumber());

                        // Пытаемся сменить имя
                        collector.getTextField_name().setText("");
                        collector.handleApply(new ActionEvent());

                        // Проверяем что ничего не изменилось
                        assertEquals(surname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals(phoneNumber, person.getPhoneNumber());

                        // Пытаемся сменить телефон
                        collector.getTextField_phoneNumber().setText("");
                        collector.handleApply(new ActionEvent());

                        // Проверяем что ничего не изменилось
                        assertEquals(surname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals(phoneNumber, person.getPhoneNumber());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void testCopyValuesAndGetters() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        // Controller
                        Stage stage = new Stage();
                        Group group = new Group();
                        Scene scene = new Scene(group);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../forms/MainForm.fxml"));
                        loader.setController(controller);
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        Person van = new Person("Darknode", "Van", "300");
                        Person bill = new Person("Hariton", "Bill", "300");

                        TableView<Person> personTableView = controller.getTableView_table();
                        controller.addPerson(mainPerson);
                        controller.addPerson(van);
                        controller.addPerson(bill);

                        assertEquals(3, personTableView.getItems().size());
                        assertEquals(8, personTableView.getColumns().size());

                        personTableView.getSelectionModel().select(0);

                        // Открываем окно с редактированием контакта
                        controller.handleEditButton(new ActionEvent());
                        Controller_EditContact controller_editContact = controller.getController_editContact();
                        controller_editContact.setNotification(false);
                        ((Stage) controller_editContact.getTextField_name().getScene().getWindow()).hide();

                        // Проверяем что все значения по умолчанию
                        assertEquals(mainPerson.getSurname(), controller_editContact.getTextField_surname().getText());
                        assertEquals(mainPerson.getName(), controller_editContact.getTextField_name().getText());
                        assertEquals(mainPerson.getPatronymic(), controller_editContact.getTextField_patronymic().getText());
                        assertEquals(mainPerson.getPhoneNumber(), controller_editContact.getTextField_phoneNumber().getText());
                        assertEquals(mainPerson.getPhoneNumber2(), controller_editContact.getTextField_phoneNumber2().getText());
                        assertEquals(mainPerson.getAddress(), controller_editContact.getTextField_address().getText());
                        assertEquals(mainPerson.getBirthday(), controller_editContact.getTextField_birthday().getText());
                        assertEquals(mainPerson.getNotes(), controller_editContact.getTextField_note().getText());

                        String noise = "some text";
                        // Меняем текст в полях ввода и пытаемся изменить данные в контакте
                        controller_editContact.getTextField_surname().setText(mainPerson.getSurname() + noise);
                        controller_editContact.getTextField_name().setText(mainPerson.getName() + noise);
                        controller_editContact.getTextField_patronymic().setText(mainPerson.getPatronymic() + noise);
                        controller_editContact.getTextField_phoneNumber().setText(mainPerson.getPhoneNumber() + noise);
                        controller_editContact.getTextField_phoneNumber2().setText(mainPerson.getPhoneNumber2() + noise);
                        controller_editContact.getTextField_address().setText(mainPerson.getAddress() + noise);
                        controller_editContact.getTextField_birthday().setText(mainPerson.getBirthday() + noise);
                        controller_editContact.getTextField_note().setText(mainPerson.getNotes() + noise);

                        // Проверяем что текст поменялся в полях и он не равен исходному
                        assertNotEquals(mainPerson.getSurname(), controller_editContact.getTextField_surname().getText());
                        assertNotEquals(mainPerson.getName(), controller_editContact.getTextField_name().getText());
                        assertNotEquals(mainPerson.getPatronymic(), controller_editContact.getTextField_patronymic().getText());
                        assertNotEquals(mainPerson.getPhoneNumber(), controller_editContact.getTextField_phoneNumber().getText());
                        assertNotEquals(mainPerson.getPhoneNumber2(), controller_editContact.getTextField_phoneNumber2().getText());
                        assertNotEquals(mainPerson.getAddress(), controller_editContact.getTextField_address().getText());
                        assertNotEquals(mainPerson.getBirthday(), controller_editContact.getTextField_birthday().getText());
                        assertNotEquals(mainPerson.getNotes(), controller_editContact.getTextField_note().getText());

                        controller_editContact.copyValues();

                        // Проверяем что все значения скопировались (вернулись обратно)
                        assertEquals(mainPerson.getSurname(), controller_editContact.getTextField_surname().getText());
                        assertEquals(mainPerson.getName(), controller_editContact.getTextField_name().getText());
                        assertEquals(mainPerson.getPatronymic(), controller_editContact.getTextField_patronymic().getText());
                        assertEquals(mainPerson.getPhoneNumber(), controller_editContact.getTextField_phoneNumber().getText());
                        assertEquals(mainPerson.getPhoneNumber2(), controller_editContact.getTextField_phoneNumber2().getText());
                        assertEquals(mainPerson.getAddress(), controller_editContact.getTextField_address().getText());
                        assertEquals(mainPerson.getBirthday(), controller_editContact.getTextField_birthday().getText());
                        assertEquals(mainPerson.getNotes(), controller_editContact.getTextField_note().getText());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }
}
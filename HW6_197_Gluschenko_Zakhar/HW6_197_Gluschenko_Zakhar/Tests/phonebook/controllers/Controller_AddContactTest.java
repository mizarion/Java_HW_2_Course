package phonebook.controllers;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import phonebook.Person;
import phonebook.io.db.DBIO;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Controller_AddContactTest {

    private static final String surname = "Ivanov2";
    private static final String name = "Ivan";
    private static final String patronymic = "Ivanovich";
    private static final String phoneNumber = "8(800)555-35-35";
    private static final String address = "adress";
    private static final String birthday = "20.12.1990";
    private static final String note = "notes";

    private static final int testTime = 5000;

    private static void setFields(Controller_AddContact controllerAddContact) {
        controllerAddContact.getTextField_surname().setText(surname);
        controllerAddContact.getTextField_name().setText(name);
        controllerAddContact.getTextField_patronymic().setText(patronymic);
        controllerAddContact.getTextField_phoneNumber().setText(phoneNumber);
        controllerAddContact.getTextField_address().setText(address);
        controllerAddContact.getTextField_birthday().setText(birthday);
        controllerAddContact.getTextField_note().setText(note);
    }

    @Test
    void handleAdd() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        //Controller.showAlert("Ожидайте", "Выполняется тестирование (3 секунды / тест )");

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

                        // Открываем окно с добавлением контакта
                        controller.handleAddButton(new ActionEvent());
                        Controller_AddContact controllerAddContact = controller.getControllerAddContact();
                        controllerAddContact.setShowNotification(false);
                        controllerAddContact.getTextField_name().getScene().getWindow().hide();

                        // Проверяем что изначально никого нету
                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        // Задаем значения для полей
                        setFields(controllerAddContact);

                        // Симулируем нажатие кнопки добавить
                        controllerAddContact.handleAdd(new ActionEvent());

                        // Проверяем что контакт успешно добавлен
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(1, controller.getPersons().size());

                        // Проверяем данные добавленного контакта
                        Person person = controller.getPersons().get(0);
                        assertEquals(surname, person.getSurname());
                        assertEquals(name, person.getName());
                        assertEquals(patronymic, person.getPatronymic());
                        assertEquals(phoneNumber, person.getPhoneNumber());
                        assertEquals("", person.getPhoneNumber2());
                        assertEquals(address, person.getAddress());
                        assertEquals(birthday, person.getBirthday());
                        assertEquals(note, person.getNotes());

                        // Задаем теже значения для полей
                        setFields(controllerAddContact);

                        // Симулируем нажатие кнопки добавить
                        controllerAddContact.handleAdd(new ActionEvent());

                        // Проверяем что контакт не был добавлен
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(1, controller.getPersons().size());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void clearFields() throws InterruptedException {
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

                        // Открываем окно с добавлением контакта
                        controller.handleAddButton(new ActionEvent());
                        Controller_AddContact controllerAddContact = controller.getControllerAddContact();
                        controllerAddContact.setShowNotification(true);
                        controllerAddContact.getTextField_name().getScene().getWindow().hide();

                        // Задаем значения поля
                        setFields(controllerAddContact);

                        // Проверяем что полям присвоилось значение
                        assertEquals(surname, controllerAddContact.getTextField_surname().getText());
                        assertEquals(name, controllerAddContact.getTextField_name().getText());
                        assertEquals(patronymic, controllerAddContact.getTextField_patronymic().getText());
                        assertEquals(phoneNumber, controllerAddContact.getTextField_phoneNumber().getText());
                        assertEquals("", controllerAddContact.getTextField_phoneNumber2().getText());
                        assertEquals(address, controllerAddContact.getTextField_address().getText());
                        assertEquals(birthday, controllerAddContact.getTextField_birthday().getText());
                        assertEquals(note, controllerAddContact.getTextField_note().getText());

                        // Симулируем нажатие кнопки добавить
                        controllerAddContact.handleAdd(new ActionEvent());

                        // Проверяем очистку полей
                        assertEquals("", controllerAddContact.getTextField_surname().getText());
                        assertEquals("", controllerAddContact.getTextField_name().getText());
                        assertEquals("", controllerAddContact.getTextField_patronymic().getText());
                        assertEquals("", controllerAddContact.getTextField_phoneNumber2().getText());
                        assertEquals("", controllerAddContact.getTextField_address().getText());
                        assertEquals("", controllerAddContact.getTextField_birthday().getText());
                        assertEquals("", controllerAddContact.getTextField_note().getText());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }
}
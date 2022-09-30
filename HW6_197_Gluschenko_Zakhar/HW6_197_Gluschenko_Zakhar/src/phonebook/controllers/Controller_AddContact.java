package phonebook.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import phonebook.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_AddContact implements Initializable {

    private final Controller controller;
    private boolean showNotification = true;

    /**
     * Устанавливает значение, контролирующее отображение уведомлений
     *
     * @param showNotification Нужно ли отображать уведомления?
     */
    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public Controller_AddContact(Controller controller) {
        this.controller = controller;
    }

    @FXML
    private TextField textField_surname;

    @FXML
    private TextField textField_name;

    @FXML
    private TextField textField_address;

    @FXML
    private Button button_add;

    @FXML
    private TextField textField_phoneNumber2;

    @FXML
    private TextField textField_patronymic;

    @FXML
    private TextField textField_phoneNumber;

    @FXML
    private TextField textField_note;

    @FXML
    private TextField textField_birthday;

    public TextField getTextField_surname() {
        return textField_surname;
    }

    public TextField getTextField_name() {
        return textField_name;
    }

    public TextField getTextField_address() {
        return textField_address;
    }

    public TextField getTextField_phoneNumber2() {
        return textField_phoneNumber2;
    }

    public TextField getTextField_patronymic() {
        return textField_patronymic;
    }

    public TextField getTextField_phoneNumber() {
        return textField_phoneNumber;
    }

    public TextField getTextField_note() {
        return textField_note;
    }

    public TextField getTextField_birthday() {
        return textField_birthday;
    }

    /**
     * Обработчик нажатия кнопки "Добавить"
     * Создает контакт из данных в полях ввода, если эти данные корректны.
     * Если нет, то выводит сообщение с ошибкой
     */
    public void handleAdd(ActionEvent actionEvent) {

        // Создаем из контакт из данных в полях ввода
        try {
            // Пытаемся создать контакт из переданных данных
            Person person = new Person(textField_surname.getText(), textField_name.getText(), textField_patronymic.getText(),
                    textField_phoneNumber.getText(), textField_phoneNumber2.getText(), textField_address.getText(),
                    textField_birthday.getText(), textField_note.getText());

            // Если контакт создался то пытаемся добавить его в наш список
            controller.addPerson(person);

            // Зачищаем поля ввода
            clearFields();
        } catch (IllegalArgumentException ex) {
            if (showNotification) {
                Controller.showAlert("Ошибка при добавлении", ex.getMessage());
            }
        }
    }

    /**
     * Зачищает поля ввода при успешном создании нового контакта
     */
    public void clearFields() {
        textField_surname.clear();
        textField_name.clear();
        textField_patronymic.clear();
        textField_phoneNumber.clear();
        textField_phoneNumber2.clear();
        textField_address.clear();
        textField_birthday.clear();
        textField_note.clear();
        textField_phoneNumber.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        button_add.setOnAction(this::handleAdd);
    }
}

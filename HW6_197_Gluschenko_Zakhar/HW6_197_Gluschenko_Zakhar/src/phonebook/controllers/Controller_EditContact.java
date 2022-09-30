package phonebook.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import phonebook.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_EditContact implements Initializable {

    private final Person selectedPerson;

    private final TableView<Person> tableView;

    private boolean showNotification = true;

    public Controller_EditContact(Person selectedPerson, TableView<Person> tableView) {
        this.selectedPerson = selectedPerson;
        this.tableView = tableView;
    }

    /**
     * Устанавливает значение, контролирующее отображение уведомлений
     *
     * @param showNotification Нужно ли отображать уведомления?
     */
    public void setNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    @FXML
    private TextField textField_surname;

    @FXML
    private TextField textField_name;

    @FXML
    private TextField textField_address;

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

    @FXML
    private Button button_apply;
    @FXML
    private Button button_cancel;

    public TextField getTextField_surname() {
        return textField_surname;
    }

    public TextField getTextField_name() {
        return textField_name;
    }

    public TextField getTextField_phoneNumber() {
        return textField_phoneNumber;
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

    public TextField getTextField_note() {
        return textField_note;
    }

    public TextField getTextField_birthday() {
        return textField_birthday;
    }

    /**
     * Обработчик нажатия кнопки "Принять".
     * Изменяет данные в переданном контакте
     */
    public void handleApply(ActionEvent actionEvent) {

        // Присваеваем возможно измененные данные контакту
        try {
            // Временный контакт, который проверяет валидность данных
            Person temp = new Person(textField_surname.getText(), textField_name.getText(), textField_patronymic.getText(),
                    textField_phoneNumber.getText(), textField_phoneNumber2.getText(), textField_address.getText(),
                    textField_birthday.getText(), textField_note.getText());

            // Если данные валидные то просто копируем их
            selectedPerson.copy(temp);
        } catch (IllegalArgumentException e) {
            if (showNotification) {
                Controller.showAlert("Ошибка при редактировании", e.getMessage());
            }
        }

        // Обновляем таблицу (иначе изменения не отобразятся)
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
    }

    /**
     * Копирует значения из переданного контакта в поля ввода
     */
    public void copyValues() {
        textField_surname.setText(selectedPerson.getSurname());
        textField_name.setText(selectedPerson.getName());
        textField_patronymic.setText(selectedPerson.getPatronymic());
        textField_phoneNumber.setText(selectedPerson.getPhoneNumber());
        textField_phoneNumber2.setText(selectedPerson.getPhoneNumber2());
        textField_address.setText(selectedPerson.getAddress());
        textField_birthday.setText(selectedPerson.getBirthday());
        textField_note.setText(selectedPerson.getNotes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Подписываемся на события события
        button_apply.setOnAction(this::handleApply);
        button_cancel.setOnAction(actionEvent -> copyValues());

        // Копируем данные контакта в ячейки
        copyValues();
    }
}

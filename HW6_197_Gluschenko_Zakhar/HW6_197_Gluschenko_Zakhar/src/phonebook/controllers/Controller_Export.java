package phonebook.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import phonebook.Person;
import phonebook.io.file.PersonsIO;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_Export implements Initializable {

    private final ObservableList<Person> persons;
    private boolean showNotification = true;

    /**
     * Устанавливает значение, контролирующее отображение уведомлений
     *
     * @param showNotification Нужно ли отображать уведомления?
     */
    public void setNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public Controller_Export(ObservableList<Person> persons) {
        this.persons = persons;
    }

    @FXML
    private TextField textField_path;

    public TextField getTextField_path() {
        return textField_path;
    }

    @FXML
    private Button button_ok;

    @FXML
    private Button button_cancel;

    /**
     * Обработчик нажатия кнопки Ок для окна экспорта.
     * Записывает в файл список контактов
     */
    public void handleOk(ActionEvent actionEvent) {
        String path = textField_path.getText();

        // Проверяем переданный путь
        if (path.isBlank()) {
            if (showNotification) {
                Controller.showAlert("[Export::handleOk] path is blank", "path is blank");
            }
            return;
        }

        // Пишем в файл
        try {
            PersonsIO.write(path, persons);
        } catch (IOException e) {
            if (showNotification) {
                Controller.showAlert("[Export::handleOk] write error", e.getMessage());
                //e.printStackTrace();
            }
            return;
        }
        if (showNotification) {
            Controller.showAlert("Успех", "Успешное сохранение");
        }
        ((Stage) button_cancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_ok.setOnAction(this::handleOk);
        button_cancel.setOnAction(actionEvent -> ((Stage) button_cancel.getScene().getWindow()).close());
    }
}

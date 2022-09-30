package phonebook.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phonebook.Person;
import phonebook.io.db.DBIO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Stage primaryStage;

    public Controller(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private MenuItem menu_addContact;

    @FXML
    private MenuItem menu_editContact;

    @FXML
    private MenuItem menu_deleteContact;

    @FXML
    private MenuItem menu_exit;

    @FXML
    private MenuItem menuItem_import;

    @FXML
    private MenuItem menuItem_export;

    @FXML
    private MenuItem menuItem_info;

    @FXML
    private Button button_add;

    @FXML
    private Button button_edit;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_find;

    private ObservableList<Person> persons = FXCollections.observableArrayList();

    public ObservableList<Person> getPersons() {
        return persons;
    }

    @FXML
    private TextField textField_search;

    public TextField getTextField_search() {
        return textField_search;
    }

    @FXML
    private TableView<Person> tableView_table;

    public TableView<Person> getTableView_table() {
        return tableView_table;
    }

    /**
     * Добавляет персонажа в список
     *
     * @param newPerson Новый контакт
     */
    public void addPerson(Person newPerson) throws IllegalArgumentException {

        if (persons.contains(newPerson)) {
            throw new IllegalArgumentException(newPerson.getSurname() + " "
                    + newPerson.getName() + " " + newPerson.getPatronymic() + " already exist");
        }

        persons.add(newPerson);
        tableView_table.getItems().add(newPerson);
    }

    /**
     * Удаляет контакт из списка
     *
     * @param person Удаляемый контакт
     */
    public void removePerson(Person person) {

        persons.remove(person);
        tableView_table.getItems().remove(person);
    }

    /**
     * Обрабатывает полное перезаписывание всех контактов
     *
     * @param newPersons Список новых контактов
     */
    public void updatePersons(ObservableList<Person> newPersons) {

        persons = FXCollections.observableArrayList();
        tableView_table.setItems(FXCollections.observableArrayList());
        for (Person person : newPersons) {
            try {
                addPerson(person);
            } catch (IllegalArgumentException e) {
                showAlert("Возможный дубль", e.getMessage());
            }
        }

//        persons = newPersons;
//        tableView_table.setItems(persons);
//
//        // Обновляем таблицу (иначе изменения не отобразятся)
//        for (int i = 0; i < tableView_table.getColumns().size(); i++) {
//            tableView_table.getColumns().get(i).setVisible(false);
//            tableView_table.getColumns().get(i).setVisible(true);
//        }
    }

    /**
     * Обработчик сообщений об ошибках
     *
     * @param title   Заголовок сообщеия об ошибке
     * @param message Само сообщение об ошибке
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(message);
        //alert.setContentText("some text");
        alert.showAndWait();
    }

    /**
     * Обработчик кнопки "Удалить".
     * Удаляет выбранный контакт, если контакт не выбран или их вообще нет,
     * выводит сообщение об ошибке
     */
    private void handleDeleteButton(ActionEvent event) {

        // Получаем выбранный контакт в TableView
        Person p = tableView_table.getSelectionModel().getSelectedItem();
        // Если с ним все хорошо удаляем его
        if (p != null) {
            removePerson(p);
        } else {
            showAlert("Ошибка удаления", "Вы не выбрали кого хотите удалить");
        }
    }

    private Controller_AddContact controllerAddContact;

    public Controller_AddContact getControllerAddContact() {
        return controllerAddContact;
    }


    /**
     * Обработчик кнопки "Добавить"
     * Позволяет добавить новый контакт с уникальным ФИО.
     * Если введенные данные не корректы или такой контакт с уникальными данными уже существует,
     * выводит сообщение об ошибке
     */
    public void handleAddButton(ActionEvent actionEvent) {

        // Вызываем новое окно / форму для заполнения
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/phonebook/forms/AddContactForm.fxml"));
            controllerAddContact = new Controller_AddContact(this);
            loader.setController(controllerAddContact);
            Parent root = null;
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Add Form");
            stage.setScene(new Scene(root));

            // Делаем вызываемую окно/форму модальной
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка при добавлении", e.getMessage());
        }
    }

    private Controller_EditContact controller_editContact;

    public Controller_EditContact getController_editContact() {
        return controller_editContact;
    }

    /**
     * Обработчик нажатия кнопки "Редактировать"
     * Позволяет для выбранного элемента (строки/линии) изменить значения.
     * Если значения не корректы, выводит сообщение об ошибке
     */
    public void handleEditButton(ActionEvent actionEvent) {
        // Вызываем новое окно / форму для редактирования
        try {
            // Получаем выбранный контакт в TableView
            Person p = tableView_table.getSelectionModel().getSelectedItem();
            //
            if (p == null) {
                showAlert("Ошибка при редактировании", "Выберите контакт для редактирования");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/phonebook/forms/EditContactForm.fxml"));
            controller_editContact = new Controller_EditContact(p, tableView_table);
            loader.setController(controller_editContact);
            Parent root = null;
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Edit Form");
            stage.setScene(new Scene(root));

            // Делаем вызываемую окно/форму модальной
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e) {
            showAlert("Ошибка при редактировании", e.getMessage());
        }
    }

    /**
     * Обработчик нажатия на кнопку "Поиск" или клавиши Enter.
     * Выполняет поиск и отображение (фильтрацию) всех контактов,
     * ФИО которых совпадает с переданной строкой
     */
    public void handleSearch(ActionEvent actionEvent) {

        // Обнуляем таблицу
        tableView_table.setItems(FXCollections.observableArrayList());
        String text = textField_search.getText();

        // Если ничего не введенно то возвращаем в tableView все контакты (persons)
        if (text.isBlank()) {
            tableView_table.setItems(persons);
            return;
        }

        // Проходимся по коллекции всех пользователей (persons) и добавляем в tableView тех у кого совпало ***
        for (var person : persons) {
            if (person.getName().toLowerCase().contains(text.toLowerCase())
                    || person.getSurname().toLowerCase().contains(text.toLowerCase())
                    || person.getPatronymic().toLowerCase().contains(text.toLowerCase())) {
                tableView_table.getItems().add(person);
            }
        }
    }

    /**
     * Настраивает tableView
     *
     * @param tableView Ссылка на настраиваемую таблицу
     */
    public static void initTableView(TableView<Person> tableView) {

        List<String> columnNames = List.of("surname", "name", "patronymic", "phoneNumber", "phoneNumber2", "address", "birthday", "notes");
        for (int i = 0; i < columnNames.size(); i++) {
            if (i < tableView.getColumns().size()) {
                //if (tableView.getColumns().get(i).equals(columnNames.get(i))) {
                tableView.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(columnNames.get(i)));
//                }
//                else {
//                    TableColumn<Person, String> newColumn = new TableColumn<>(columnNames.get(i));
//                    tableView.getColumns().set(i, newColumn);
//                    tableView.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(columnNames.get(i)));
//                }
            } else {
                TableColumn<Person, String> newColumn = new TableColumn<>(columnNames.get(i));
                newColumn.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(i)));
                tableView.getColumns().add(newColumn);
            }
        }
    }

    /**
     * Обработчик нажатия на элемент меню "Импорт"
     * Загружает список всех контактов (т.е. без фильтрации)
     */
    private void handleImportDB(ActionEvent actionEvent) {

        ObservableList<Person> newPersons = null;
        try {
            newPersons = DBIO.readDB();
        } catch (SQLException e) {
            showAlert("Ошибка импорта", "Ошибка при считавании контактов из БД");
            return;
        }

        // Обновляем данные
        updatePersons(newPersons);
    }

//    /**
//     * Обработчик нажатия на элемент меню "Импорт"
//     * Загружает список всех контактов (т.е. без фильтрации)
//     */
//    @Deprecated
//    private void handleImportFile(ActionEvent actionEvent) {
//
//        FileChooser fileChooser = new FileChooser();
//        // Устанавливаем директорию по умолчанию
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
//        // Открываем меню с выбором файла
//        fileChooser.setTitle("Open File");
//        File file = fileChooser.showOpenDialog(primaryStage);
//
//        if (file == null) {
//            return;
//        }
//
//        // Загружаем выбранный файл
//        ObservableList<Person> newPersons = null;
//        try {
//            newPersons = PersonsIO.read(file.getAbsolutePath());
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            showAlert("Ошибка считывания", e.getMessage());
//            return;
//        }
//
//        // Обновляем данные
//        updatePersons(newPersons);
//    }

    private Controller_Export controller_export;

    public Controller_Export getController_Export() {
        return controller_export;
    }

    /**
     * Обработчик нажатия на элемент меню "Экспорт"
     * Сохраняет список всех контактов (т.е. без фильтрации) в базе данных
     */
    public void handleExportDB(ActionEvent actionEvent) {

        try {
            DBIO.writeDB(persons);
        } catch (SQLException throwables) {
            showAlert("Ошибка экпорта", "При экспортировании произошла ошибка");
        }
    }

//    /**
//     * Обработчик нажатия на элемент меню "Экспорт"
//     * Сохраняет список всех контактов (т.е. без фильтрации) в файле
//     */
//    @Deprecated
//    public void handleExportFile(ActionEvent actionEvent) {
//
//        // Открываем форму для ввода пути и сохранения по нему
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("../phonebook/forms/ExportForm.fxml"));
//            Stage stage = new Stage();
//            stage.setResizable(false);
//            controller_export = new Controller_Export(persons);
//            loader.setController(controller_export);
//            Parent root = null;
//            root = loader.load();
//            stage.setTitle("Edit Form");
//            stage.setScene(new Scene(root));
//
//            // Делаем вызываемую окно/форму модальной
//            stage.initModality(Modality.APPLICATION_MODAL);
//
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * Обработчик нажатия на элемент меню "Об авторе"
     * Открывает окно с важной информацией
     */
    private void handleInfo(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/phonebook/forms/infoForm.fxml"));
            Stage stage = new Stage();
            stage.setResizable(false);
            //loader.setController(new Controller_Info());
            Parent root = null;
            root = loader.load();
            stage.setTitle("Info");
            stage.setScene(new Scene(root));

            // Делаем вызываемую окно/форму модальной
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            DBIO.start();
        } catch (SQLException throwables) {
            showAlert("Ошибка Базы данных", "При подключении базы данных что-то пошло не так");
            System.out.println(throwables.getMessage());
        }

        // Настраиваем TableView
        initTableView(tableView_table);

        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                DBIO.shutdown();
            } catch (SQLException throwables) {
                showAlert("Ошибка БД", "При закрытии БД произошла ошибка");
            }
        });

        // Подписываем обработчки кнопок
        button_add.setOnAction(this::handleAddButton);
        button_delete.setOnAction(this::handleDeleteButton);
        button_edit.setOnAction(this::handleEditButton);
        button_find.setOnAction(this::handleSearch);

        // Настраиваем обработчики меню "Файл"
        menu_addContact.setOnAction(this::handleAddButton);
        menu_deleteContact.setOnAction(this::handleDeleteButton);
        menu_editContact.setOnAction(this::handleEditButton);
        menu_exit.setOnAction(actionEvent -> {
            primaryStage.close();
            try {
                DBIO.shutdown();
            } catch (SQLException throwables) {
                showAlert("Ошибка БД", "При закрытии БД произошла ошибка");
            }
        });

        // Настраиваем обработчики меню "Настройки"
        menuItem_import.setOnAction(this::handleImportDB);
        menuItem_export.setOnAction(this::handleExportDB);

        // Настраиваем обработчики меню "Справка"
        menuItem_info.setOnAction(this::handleInfo);

        // Настраиваем обработчку нажатия клавиши enter в поисковой строке
        textField_search.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch(new ActionEvent());
            }
        });
    }
}

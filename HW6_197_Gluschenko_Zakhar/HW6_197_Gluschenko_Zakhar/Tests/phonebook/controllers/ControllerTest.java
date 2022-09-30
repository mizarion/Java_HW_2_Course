package phonebook.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import phonebook.Main;
import phonebook.Person;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ControllerTest {
    static final int testTime = 5000;

    Person person = new Person("Ivanov", "Ivan", "123456");

    @Test
    public void testStartMain() throws Exception {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                Stage stage = new Stage();
                try {
                    new Main().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stage.hide();
            });
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    public void testAddRemoveContact() throws Exception {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        Stage stage = new Stage();

                        Group group = new Group();
                        Scene scene = new Scene(group, 600, 400);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../forms/MainForm.fxml"));
                        loader.setController(controller);
                        Pane root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        Person van = new Person("Darknode", "Van", "300");
                        Person bill = new Person("Hariton", "Bill", "300");

                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        controller.addPerson(van);
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(1, controller.getPersons().size());

                        // Пытаемся повторно добавить тот же контакт
                        Throwable vanExist = assertThrows(IllegalArgumentException.class,
                                () -> controller.addPerson(van));
                        assertEquals("Darknode Van  already exist", vanExist.getMessage());

                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(1, controller.getPersons().size());

                        controller.addPerson(bill);
                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Пытаемся повторно добавить те же контакты
                        Throwable billExist = assertThrows(IllegalArgumentException.class,
                                () -> controller.addPerson(bill));
                        assertEquals("Hariton Bill  already exist", billExist.getMessage());
                        Throwable vanReturns = assertThrows(IllegalArgumentException.class,
                                () -> controller.addPerson(van));
                        assertEquals("Darknode Van  already exist", vanReturns.getMessage());

                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void testUpdatePersons() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        Stage stage = new Stage();

                        Group group = new Group();
                        Scene scene = new Scene(group);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../forms/MainForm.fxml"));
                        loader.setController(controller);
                        Pane root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        ObservableList<Person> newPersons = FXCollections.observableArrayList();
                        newPersons.add(new Person("Darknode", "Van", "300"));
                        newPersons.add(person);
                        controller.updatePersons(newPersons);

                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Пытаемся еще раз добавить тех же людей
                        controller.updatePersons(newPersons);
                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void testRemovePerson() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        Stage stage = new Stage();

                        Group group = new Group();
                        Scene scene = new Scene(group, 600, 400);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../forms/MainForm.fxml"));
                        loader.setController(controller);
                        Pane root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        //ObservableList<Person> newPersons = FXCollections.observableArrayList();
                        Person van = new Person("Darknode", "Van", "300");

                        controller.addPerson(van);
                        controller.addPerson(person);
                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        controller.removePerson(person);
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(1, controller.getPersons().size());

                        // Попытка повторного удаления - ничего не происходит
                        controller.removePerson(person);
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(1, controller.getPersons().size());

                        controller.removePerson(van);
                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        controller.removePerson(van);
                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        // Контакт, который никогда и добавлялся туда
                        controller.removePerson(person);
                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void initTableView() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {

                TableView<Person> personTableView = new TableView<>();
                Controller.initTableView(personTableView);

                assertEquals(0, personTableView.getItems().size());
                personTableView.getItems().add(new Person("Darknode", "Van", "300"));
                assertEquals(1, personTableView.getItems().size());
                personTableView.getItems().add(new Person("Hariton", "Bill", "300"));
                assertEquals(2, personTableView.getItems().size());
            });
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void handleSearch() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        Stage stage = new Stage();
                        Group group = new Group();
                        Scene scene = new Scene(group, 600, 400);
                        Controller controller = new Controller(stage);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../forms/MainForm.fxml"));
                        loader.setController(controller);
                        Pane root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        group.getChildren().add(root);
                        stage.setScene(scene);

                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(0, controller.getPersons().size());

                        Person van = new Person("Darknode", "Van", "300");

                        // Добавляем контакты и проверяем их добавление
                        controller.addPerson(person);
                        controller.addPerson(van);
                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Запускаем поиск с пустым полем - ничего не "удалилось"
                        controller.getTextField_search().setText("");
                        controller.handleSearch(new ActionEvent());
                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Делаем поиск по имени для person
                        controller.getTextField_search().setText(person.getName());
                        controller.handleSearch(new ActionEvent());

                        // Нашелся ровно один контакт - person
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Делаем поиск по фамилии для person
                        controller.getTextField_search().setText(person.getSurname());
                        controller.handleSearch(new ActionEvent());

                        // Нашелся ровно один контакт - person
                        assertEquals(1, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Задаем значение полю для поиска
                        controller.getTextField_search().setText(person.getName() + van.getName());
                        controller.handleSearch(new ActionEvent());

                        // Никто не нашелся
                        assertEquals(0, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());

                        // Запускаем поиск с пустым полем - отмена фильтра
                        controller.getTextField_search().setText("");
                        controller.handleSearch(new ActionEvent());
                        assertEquals(2, controller.getTableView_table().getItems().size());
                        assertEquals(2, controller.getPersons().size());
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }
}
package phonebook.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import phonebook.Person;
import phonebook.io.file.PersonsIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Controller_ExportTest {
    static final int testTime = 5000;

    private final String surname = "Ivanov";
    private final String name = "Ivan";
    private final String phoneNumber = "8(800)555-35-35";
    private final Person person = new Person(surname, name, phoneNumber);

    private final String path = new File(System.getProperty("user.dir")).getAbsolutePath() + "\\testExport.ser";
    private final String blankPath = "";

    @Test
    void handleOk() throws InterruptedException {

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

                        ObservableList<Person> persons = controller.getTableView_table().getItems();
                        controller.addPerson(person);
                        controller.addPerson(van);
                        controller.addPerson(bill);

                        assertEquals(3, controller.getTableView_table().getItems().size());
                        assertEquals(8, controller.getTableView_table().getColumns().size());

                        // Открываем(скрытое) окно экспорта контактов
                        controller.handleExportDB(new ActionEvent());
                        Controller_Export controllerExport = controller.getController_Export();
                        controllerExport.setNotification(false);
                        controllerExport.getTextField_path().getScene().getWindow().hide();

                        // Задаем путь для файла сохранения и запускаем обработку сохранения
                        controllerExport.getTextField_path().setText(path);
                        controllerExport.handleOk(new ActionEvent());

                        // Пытаемся загрузить сохраненный файл
                        ObservableList<Person> newPersons = null;
                        try {
                            newPersons = PersonsIO.read(path);
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        //
                        assertEquals(persons, newPersons);
                        for (int i = 0; i < persons.size(); i++) {
                            assertEquals(persons.get(i), newPersons.get(i));
                        }
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }

    @Test
    void handleOkBlank() throws InterruptedException {

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

                        //ObservableList<Person> persons = controllers.getTableView_table().getItems();
                        controller.addPerson(person);
                        controller.addPerson(van);
                        controller.addPerson(bill);

                        assertEquals(3, controller.getTableView_table().getItems().size());
                        assertEquals(8, controller.getTableView_table().getColumns().size());

                        // Открываем(скрытое) окно экспорта контактов
                        controller.handleExportDB(new ActionEvent());
                        Controller_Export controllerExport = controller.getController_Export();
                        controllerExport.setNotification(false);
                        controllerExport.getTextField_path().getScene().getWindow().hide();

                        // Задаем пустой путь для файла сохранения и запускаем обработку сохранения
                        controllerExport.getTextField_path().setText(blankPath);
                        controllerExport.handleOk(new ActionEvent());
                        assertThrows(AccessDeniedException.class, () -> PersonsIO.read(blankPath));

                        // Задаем не пустой, но неверный путь для файла сохранения и запускаем обработку сохранения
                        controllerExport.getTextField_path().setText(blankPath);
                        controllerExport.handleOk(new ActionEvent());
                        assertThrows(NoSuchFileException.class, () -> PersonsIO.read(blankPath + "a"));

                    }
                });
            }
        });
        thread.start();
        Thread.sleep(testTime);
    }
}
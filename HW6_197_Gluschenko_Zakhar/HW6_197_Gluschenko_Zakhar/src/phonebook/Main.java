package phonebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phonebook.controllers.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Group group = new Group();
        Scene scene = new Scene(group);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/phonebook/forms/MainForm.fxml"));
        loader.setController(new Controller(primaryStage));
        Pane root = loader.load();
        group.getChildren().add(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

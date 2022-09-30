package phonebook.io.file;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import phonebook.Person;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PersonsIO {

    public static ObservableList<Person> read(String path) throws IOException, ClassNotFoundException {
        InputStream in = Files.newInputStream(Path.of(path));
        ObjectInputStream ois = new ObjectInputStream(in);
        List<Person> list = (List<Person>) ois.readObject();
        return FXCollections.observableList(list);
    }

    public static void write(String path, ObservableList<Person> persons) throws IOException {
        OutputStream fos = Files.newOutputStream(Path.of(path));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(new ArrayList<>(persons));
        oos.close();
    }
}

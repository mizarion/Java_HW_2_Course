package downloader.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryManagerTest {


    private static final String dir = System.getProperty("user.dir");

    @Test
    void invalidPath() {

        assertThrows(IllegalArgumentException.class, () -> new DirectoryManager("AAA"));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryManager("A\\A"));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryManager("A\\A\\A"));

        File root = new File(dir);
        if (root.exists()) {
            for (File dirItem : Objects.requireNonNull(root.listFiles())) {
                if (!dirItem.isDirectory()) {
                    assertThrows(IllegalArgumentException.class, () -> new DirectoryManager(dirItem.getAbsolutePath()));
                }
            }
        }
    }


    @Test
    void validPath() {
        File root = new File(dir);
        if (root.exists()) {
            assertDoesNotThrow(() -> new DirectoryManager(dir));

            for (File dirItem : Objects.requireNonNull(root.listFiles())) {
                if (dirItem.isDirectory()) {
                    assertDoesNotThrow(() -> new DirectoryManager(dir));
                }
            }
        }
    }

    @Test
    void getFiles() {
        File root = new File(dir);
        if (root.exists()) {
            DirectoryManager dm = new DirectoryManager(dir);
            ArrayList<File> dmFiles = new ArrayList<>();

            for (int i = 0; i < dm.getCount(); i++) {
                dmFiles.add(dm.getFile(i));
            }

            for (File dirItem : Objects.requireNonNull(root.listFiles())) {

                // Проверяем отсутствие директории
                if (dirItem.isDirectory()) {
                    assertFalse(dmFiles.contains(dirItem));
                }
                // Проверяем наличие файла
                else {
                    assertTrue(dmFiles.contains(dirItem));
                }
            }
        }
    }

    @Test
    void getFileInfosTest() {
        File root = new File(dir);
        if (root.exists()) {
            DirectoryManager dm = new DirectoryManager(dir);
            ArrayList<File> dmFiles = new ArrayList<>();

            for (int i = 0; i < dm.getCount(); i++) {
                dmFiles.add(dm.getFile(i));
            }

            assertEquals(dmFiles.size(), dm.getFileInfos().size());

            ArrayList<FileInfo> fileInfos = new ArrayList<>();
            for (final File dirItem : dmFiles) {
                fileInfos.add(new FileInfo(dirItem.getName(), dirItem.length(), "byte"));
            }

            assertEquals(fileInfos.size(), dm.getFileInfos().size());

            for (FileInfo fi : dm.getFileInfos()) {
                assertTrue(fileInfos.contains(fi));
            }
        }
    }

    @Test
    void getSavePathTest() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream((dir + "\n").getBytes());
        String result = DirectoryManager.getDirPath(in, out, "");
        assertEquals(dir, result);
    }

    @Test
    void getInvalidSavePathTest() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("AAAA\n A\\B\\C\n" + dir + "\n").getBytes());
        String result = DirectoryManager.getDirPath(in, out, "");
        assertEquals(dir, result);
    }

}
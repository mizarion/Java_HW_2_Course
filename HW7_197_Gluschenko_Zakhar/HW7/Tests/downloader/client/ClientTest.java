package downloader.client;

import downloader.utils.FileInfo;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private static final String dir = System.getProperty("user.dir");

    private ArrayList<FileInfo> fileInfos = new ArrayList<>();
    private ArrayList<FileInfo> downloaded = new ArrayList<>();

    private ClientTest() {
        final String name = "password.jpeg";
        final long size = 12345;
        final String type = "byte";
        final int count = 10;
        for (int i = 1; i < count; i++) {
            fileInfos.add(new FileInfo(name + i, size + i, type));
        }
    }

    @Test
    void chooseExit() {
        final int sourceAction = -1;
        for (int i = 0; i < 5; i++) {
            PrintStream out = new PrintStream(new ByteArrayOutputStream());
            ByteArrayInputStream in = new ByteArrayInputStream((sourceAction + "\n ").getBytes());
            int result = Client.chooseAction(fileInfos, downloaded, in, out);
            assertEquals(result, sourceAction);
        }
    }

    @Test
    void chooseFile() {
        final int sourceAction = 1;
        assertTrue(fileInfos.size() > 0);
        assertTrue(fileInfos.size() >= sourceAction);
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream((sourceAction + "\n ").getBytes());
        int result = Client.chooseAction(fileInfos, downloaded, in, out);
        assertEquals(result, sourceAction);
    }

    @Test
    void chooseLastFile() {
        final int sourceAction = fileInfos.size() - 1;
        assertTrue(fileInfos.size() > 0);
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream((sourceAction + "\n ").getBytes());
        int result = Client.chooseAction(fileInfos, downloaded, in, out);
        assertEquals(result, sourceAction);
    }

    @Test
    void chooseNegative() {
        final int sourceAction = -1;
        assertTrue(fileInfos.size() > 0);
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("-2\n -5\n " + sourceAction + "\n ").getBytes());
        int result = Client.chooseAction(fileInfos, downloaded, in, out);
        assertEquals(result, sourceAction);
    }

    @Test
    void chooseNotNumber() {
        final int sourceAction = -1;
        assertTrue(fileInfos.size() > 0);
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("a\n bb\n ccc\n one\n" + sourceAction + "\n ").getBytes());
        int result = Client.chooseAction(fileInfos, downloaded, in, out);
        assertEquals(result, sourceAction);
    }


    @Test
    void agreeTestTrue() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("Y\n").getBytes());
        boolean result = Client.agree(in, out);
        assertTrue(result);
    }

    @Test
    void agreeTestFalse() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("N\n").getBytes());
        boolean result = Client.agree(in, out);
        assertFalse(result);
    }

    @Test
    void agreeTestLoopTrue() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("loop\nit\nY\n").getBytes());
        boolean result = Client.agree(in, out);
        assertTrue(result);
    }

    @Test
    void agreeTestLoopFalse() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ByteArrayInputStream in = new ByteArrayInputStream(("loop\nit\nN\n").getBytes());
        boolean result = Client.agree(in, out);
        assertFalse(result);
    }
}

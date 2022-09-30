package downloader.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static downloader.utils.FileDownloader.transferData;
import static org.junit.jupiter.api.Assertions.*;

class FileDownloaderTest {

    @Test
    void transferDataTest() throws IOException {

        final String sourceString = "abc".repeat(1000);
        ByteArrayInputStream in = new ByteArrayInputStream(sourceString.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        transferData(in, out, sourceString.length(), "");

        String newStr = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals(sourceString, newStr);
        assertNotSame(sourceString, newStr);
    }
}
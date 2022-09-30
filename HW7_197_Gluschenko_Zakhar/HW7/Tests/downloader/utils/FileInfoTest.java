package downloader.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FileInfoTest {

    final String name = "password.jpeg";
    final long size = 12345;
    final String type = "byte";

    @Test
    void constructorTest() {

        FileInfo fi = new FileInfo(name, size, type);
        FileInfo fi2 = new FileInfo(fi, System.getProperty("user.dir") + "\\" + name);
        assertNotEquals(fi, fi2);
        assertNotEquals(fi.getName(), fi2.getName());
        assertEquals(fi.getSize(), fi2.getSize());
        assertEquals(fi.getSizeType(), fi2.getSizeType());
    }

    @Test
    void getFileInfoTest() {

        FileInfo fi = new FileInfo(name, size, type);

        assertEquals(name, fi.getName());
        assertEquals(size, fi.getSize());
        assertEquals(type, fi.getSizeType());
    }

    @Test
    void testEquals() {
        FileInfo fi1 = new FileInfo(name, size, type);
        FileInfo fi2 = new FileInfo(name, size, type);
        assertEquals(fi1, fi1);
        assertEquals(fi1, fi2);
        assertEquals(fi1.getName(), fi2.getName());
        assertEquals(fi1.getSize(), fi2.getSize());
        assertEquals(fi1.getSizeType(), fi2.getSizeType());
    }

    @Test
    void testNotEqualsByName() {
        FileInfo fi1 = new FileInfo(name, size, type);
        FileInfo fi2 = new FileInfo(name + "a", size, type);
        assertNotEquals(fi1, fi2);
        assertNotEquals(fi1.getName(), fi2.getName());
        assertEquals(fi1.getSize(), fi2.getSize());
        assertEquals(fi1.getSizeType(), fi2.getSizeType());
    }

    @Test
    void testNotEqualsBySize() {
        FileInfo fi1 = new FileInfo(name, size, type);
        FileInfo fi2 = new FileInfo(name, size + 1, type);
        assertNotEquals(fi1, fi2);
        assertEquals(fi1.getName(), fi2.getName());
        assertNotEquals(fi1.getSize(), fi2.getSize());
        assertEquals(fi1.getSizeType(), fi2.getSizeType());
    }

    @Test
    void testNotEqualsByNull() {
        FileInfo fi1 = new FileInfo(name, size, type);
        assertNotEquals(fi1, null);
    }

    @Test
    void testHashCode() {
        FileInfo fi1 = new FileInfo(name, size, type);
        FileInfo fi2 = new FileInfo(name, size, type);
        FileInfo fi3 = new FileInfo(name + "a", size, type);
        FileInfo fi4 = new FileInfo(name, size + 1, type);
        assertEquals(fi1.hashCode(), fi1.hashCode());
        assertEquals(fi1.hashCode(), fi2.hashCode());
        assertNotEquals(fi1.hashCode(), fi3.hashCode());
        assertNotEquals(fi1.hashCode(), fi4.hashCode());

    }
}
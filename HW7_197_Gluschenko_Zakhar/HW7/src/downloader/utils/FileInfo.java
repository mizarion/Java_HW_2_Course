package downloader.utils;

import java.io.Serializable;
import java.util.Objects;

// Опасно передовать информацию о абсолютных путях посторонним.

/**
 * Класс, скрывающий информацию о содержащихся файлах.
 */
public class FileInfo implements Serializable {
    private final String name;
    private final long size;
    //private final Type sizeType;
    private final String sizeType; // bytes

    /**
     * Конструктор
     *
     * @param name     Имя файла или путь до него
     * @param size     Размер файла
     * @param sizeType Строковое представление единицы информации
     */
    public FileInfo(String name, long size, String sizeType) {
        this.name = name;
        this.size = size;
        this.sizeType = sizeType;
    }

    /**
     * Конструктор
     *
     * @param fi   Информация о файле
     * @param path Путь до папки с файлом
     */
    public FileInfo(FileInfo fi, String path) {
        this(path + fi.name, fi.size, fi.sizeType);
    }

    /**
     * Возвращает сохраненное имя файла,
     * которое может быть, как названием файла,
     * так и путем до него
     *
     * @return Имя файла
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает сохраненный размер файла
     *
     * @return Размер файла
     */
    public long getSize() {
        return size;
    }


    /**
     * Возвращает строку, в которой записана строка означающая единицу хранения информации
     *
     * @return Строка означающая единицу хранения информации
     */
    public String getSizeType() {
        return sizeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileInfo fileInfo = (FileInfo) o;
        return size == fileInfo.size
                && name.equals(fileInfo.name)
                && sizeType.equals(fileInfo.sizeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, sizeType);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", sizeType='" + sizeType + '\'' +
                '}';
    }
}

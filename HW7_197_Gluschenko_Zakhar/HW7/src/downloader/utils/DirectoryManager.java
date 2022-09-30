package downloader.utils;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс, для взаимодействия с директорией и файлами в ней
 */
public class DirectoryManager {

    private final ArrayList<File> files = new ArrayList<>();
    private final ArrayList<FileInfo> fileInfos = new ArrayList<>();

    public DirectoryManager(String path) {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Такого пути не существует или он не является директорией.");
        }

        for (final File dirItem : Objects.requireNonNull(directory.listFiles())) {
            if (!dirItem.isDirectory()) {
                files.add(dirItem);
            }
        }
        for (final File dirItem : files) {
            fileInfos.add(new FileInfo(dirItem.getName(), dirItem.length(), "byte"));
        }
    }

    /**
     * Возвращает файл по переданному индексу
     *
     * @param index Индекс файла
     * @return Файл
     */
    public File getFile(int index) {
        return files.get(index);
    }


    int getCount() {
        return files.size();
    }

    /**
     * Возвращает список файлов обернутых в FileInfo.
     */
    public ArrayList<FileInfo> getFileInfos() {
        return fileInfos;
    }

    /**
     * Возвращает путь до существующей директории (не является файлом)
     *
     * @return Путь до директории
     */
    public static String getDirPath(InputStream in, PrintStream out, String message) {
        Scanner scanner = new Scanner(in);
        out.print(message);
        File path = new File(scanner.nextLine());
        while (!path.exists() || !path.isDirectory()) {
            out.print("Такого пути не существует или он не является директорией. Повторите ввод: ");
            path = new File(scanner.nextLine());
        }
        return path.getAbsolutePath();
    }
}

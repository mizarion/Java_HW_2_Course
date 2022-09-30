package downloader.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileDownloader {
    /**
     * Читает и перемещает данные из входного потока (in) в выходной (out)
     *
     * @param in       Входной (читаемый) поток
     * @param out      Выходной поток
     * @param fileSize Размер файла
     * @param initInfo Начальная строка с информацией
     */
    public static void transferData(InputStream in, OutputStream out, long fileSize, String initInfo) throws IOException {
        System.out.print(initInfo);
        byte[] bytes = new byte[16 * 1024];
        long sum = 0;
        int count;
        while (sum < fileSize && (count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
            sum += count;

            // вычисляем текущий прогресс
            String progress = Math.min(100, sum * 100 / fileSize) + "%";

            // удаляем старый прогресс и печатаем новый
            System.out.print("\b".repeat(progress.length()) + progress);

        }
        out.flush();
        System.out.println();
    }
}

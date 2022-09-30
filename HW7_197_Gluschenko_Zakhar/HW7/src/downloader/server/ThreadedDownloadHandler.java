package downloader.server;

import downloader.utils.DirectoryManager;
import downloader.utils.FileDownloader;

import java.io.*;
import java.net.Socket;

class ThreadedDownloadHandler implements Runnable {
    private final Socket incoming;
    private final DirectoryManager dm;

    public ThreadedDownloadHandler(final Socket incoming, final DirectoryManager dm) {
        this.incoming = incoming;
        this.dm = dm;
    }

    @Override
    public void run() {
        try {
            try (OutputStream out = incoming.getOutputStream();
                 InputStream in = incoming.getInputStream()) {

                // Получаем потоки для взаимодействия с клиентом
                ObjectOutputStream socketOutput = new ObjectOutputStream(out);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                // Отправляем клиенту данные о файлах в указанной директории
                System.out.println("[Сервер]: Отправляю список доступных для скачивания файлов");
                socketOutput.writeObject(dm.getFileInfos());
                System.out.print("[Сервер]: Отправил.");

                while (true) {
                    System.out.println("[Сервер]: Ожидание ответа от клиента");
                    int index = Integer.parseInt(br.readLine());
                    if (index == -2) {
                        System.out.println("[Сервер]: Пока!");
                        break;
                    }
                    if (index == -1) {
                        continue;
                    }
                    File file = dm.getFile(index);
                    InputStream fin = new FileInputStream(file);
                    System.out.println("[Сервер]: Начинаю отправку " + file.getName());
                    FileDownloader.transferData(fin, out, file.length(), "[Сервер]: Прогресс отправки  00%");
                    System.out.println("[Сервер]: Файл " + file.getName() + " отправил");
                }
            } finally {
                incoming.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

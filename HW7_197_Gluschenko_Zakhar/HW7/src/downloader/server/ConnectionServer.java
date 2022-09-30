package downloader.server;

import downloader.utils.DirectoryManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionServer {

    private static final int serverPort = 3456;

    public static void main(String[] args) {
        try {
            int i = 1;
            ServerSocket s = new ServerSocket(serverPort);
            System.out.println("[Сервер]: Доступен для подключения по " + serverPort + " порту");
            String saveDir = DirectoryManager.getDirPath(System.in, System.out, "Введите путь до папки с раздаваемыми файлами: ");
            final DirectoryManager dm = new DirectoryManager(saveDir);
            System.out.println("Ожидаю подключение клиентов");
            while (true) {
                Socket incoming = s.accept();
                System.out.println("Подключился " + i + " клиент");
                Runnable r = new ThreadedDownloadHandler(incoming, dm);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

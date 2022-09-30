package downloader.client;

import downloader.utils.DirectoryManager;
import downloader.utils.FileDownloader;
import downloader.utils.FileInfo;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private static Socket clientSocket;

    /**
     * Выводит сообщение и список файлов
     *
     * @param fileInfos Список файлов
     * @param out       Выходной поток
     * @param message   Начальное сообщение
     */
    static void printFiles(ArrayList<FileInfo> fileInfos, PrintStream out, String message) {
        out.println(message);
        int i = 0;
        for (FileInfo fi : fileInfos) {
            out.println(++i + ") " + fi.getName() + " " + fi.getSize() + " " + fi.getSizeType());
        }
    }

    /**
     * Реализует выбор файла из предложенного списка или действие
     *
     * @param fileInfos  Список файлов
     * @param downloaded Список загруженных файлов
     * @param in         Входной поток
     * @param out        Выходной поток
     * @return число соответсвующее индексу файла + 1 или прекращению программы.
     */
    static int chooseAction(ArrayList<FileInfo> fileInfos, ArrayList<FileInfo> downloaded, InputStream in, PrintStream out) {
        Scanner scanner = new Scanner(in);
        int value = -2;
        do {
            out.println("Введите номер интересующего вас файла или соответсвуеющее действие");
            out.println("(-1 для завершения работы или 0 для показа уже скачанных файлов)");
            printFiles(fileInfos, out, "Список доступных файлов для скачивания:");

            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value == 0) {
                    if (downloaded.size() == 0) {
                        out.println("Вы еще ничего не скачали");
                    } else {
                        printFiles(downloaded, out, "Список скачанных файлов:");
                    }

                } else if (value < -1 || value > fileInfos.size()) {
                    out.print("Вы ввели недопустимое число. ");
                    scanner.nextLine();
                }
            } else {
                out.print("Вы ввели не число. ");
                scanner.nextLine();
            }

        } while (value == 0 || value < -1 || value > fileInfos.size());
        return value;
    }

    /**
     * Подключение к серверу по переданным параметрам
     *
     * @param serverHostName Хост
     * @param serverPort     Порт
     */
    private static void connect(String serverHostName, int serverPort) throws IOException {
        InetAddress serverHost = InetAddress.getByName(serverHostName);
        clientSocket = new Socket(serverHost, serverPort);
    }

    /**
     * Получает хост из консоли
     *
     * @return хост
     */
    private static String getHostName() {
        System.out.print("Введите хост: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Получает порт из консоли.
     *
     * @return номер порта
     */
    private static int getPort() {
        System.out.print("Введите порт: ");
        Scanner scanner = new Scanner(System.in);

        while (!scanner.hasNextInt()) {
            System.out.print("Ошибка ввода. Вы ввели не число. Введите порт:");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

    /**
     * Уточняет потребность в загрузке файла.
     *
     * @return сохранилась ли потребность
     */
    static boolean agree(InputStream in, PrintStream out) {
        Scanner scanner = new Scanner(in);
        do {
            out.println("Вы хотите скачать файл? [Y/N]?");
            String answer = scanner.nextLine();
            if (answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes")) {
                return true;
            } else if (answer.toLowerCase().equals("n") || answer.toLowerCase().equals("no")) {
                return false;
            }
        } while (true);
    }

    public static void main(String[] args) {
        // Подключаемся к серверу
        while (true) {
            try {
                connect(getHostName(), getPort());
                //connect("localhost", 3456);
                break;
            } catch (IOException e) {
                System.out.println("Не удалось подключиться к серверу с такими параметрами. "
                        + "Повторите ввод или закройте приложение");
            }
        }
        System.out.println("[Клиент]: подключился к серверу");
        try {
            try (InputStream in = clientSocket.getInputStream();
                 ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                 PrintStream ps = new PrintStream(clientSocket.getOutputStream())) {

                // Запрашиваем у сервера список файлов
                System.out.println("[Клиент]: Скачиваю список файлов");
                ArrayList<FileInfo> fileInfos = (ArrayList<FileInfo>) ois.readObject();
                // Список, в котором будут храниться скачанные файлы
                ArrayList<FileInfo> downloaded = new ArrayList<>();

                while (true) {

                    // Выбираем интересующий нас действие или файл
                    int action = chooseAction(fileInfos, downloaded, System.in, System.out);

                    // Если выбрали -1 - нас ничего не интересует - выходим
                    if (action == -1) {
                        ps.println(-2);
                        break;
                    }

                    // Спрашиваем действительно ли хочет скачать
                    if (!agree(System.in, System.out)) {
                        ps.println(-1);
                        continue;
                    }

                    // Переводим идексацию (1->0) и сообщаем серверу наш выбор.
                    --action;
                    ps.println(action);

                    // Создаем File, куда будем записывать и его FileInfo
                    String saveDir = DirectoryManager.getDirPath(System.in, System.out, "Введите путь для загружаемого файла: ");
                    FileInfo currentFI = new FileInfo(fileInfos.get(action), saveDir + "\\");
                    File newFile = new File(currentFI.getName());
                    OutputStream fos = new FileOutputStream(newFile);

                    // Получаем ответ от сервара - загружаемый файл
                    System.out.println("[Клиент]: Начинаю загрузку");
                    try {
                        FileDownloader.transferData(in, fos, currentFI.getSize(), "[Клиент]: Прогресс загрузки  00%");
                    } catch (Exception e) {
                        newFile.delete();
                        throw new Exception("Произошла ошибка при загрузке файла");
                    }

                    System.out.println("[Клиент]: Файл загрузил");

                    if (!downloaded.contains(currentFI)) {
                        downloaded.add(currentFI);
                    }
                }
            } finally {
                clientSocket.close();
                System.out.println("\n[Клиент]: Отключился от сервера");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
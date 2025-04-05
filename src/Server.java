import java.io.*; // импорт пакета, содержащего классы для ввода/вывода
import java.net.*; // импорт пакета, содержащего классы для работы в сети
import java.util.Arrays;

public class Server {
    public static void main(String[] arg) {
        String boofer="";

        ServerSocket serverSocket = null;
        Socket clientAccepted = null; // объявление объекта класса Socket
        ObjectInputStream sois = null; // объявление байтового потока ввода
        ObjectOutputStream soos = null; // объявление байтового потока вывода

        try {
            System.out.println("Server starting...");
            serverSocket = new ServerSocket(2525); // создание сокета сервера для заданного порта
            clientAccepted = serverSocket.accept(); // ожидание подключения клиента
            System.out.println("Connection established...");

            sois = new ObjectInputStream(clientAccepted.getInputStream());
            soos = new ObjectOutputStream(clientAccepted.getOutputStream()); // создание потока вывода

            String clientMessageReceived = (String) sois.readObject(); // чтение сообщения от клиента
            char[] characters = clientMessageReceived.toCharArray();
            System.out.println(characters[0]);
            String modifiedString = clientMessageReceived.substring(2);
            while (!clientMessageReceived.equals("quite")) { // цикл до получения "quite"

                System.out.println(clientMessageReceived);

                soos.writeObject("data: \n"+boofer);
                clientMessageReceived = (String) sois.readObject(); // ожидание нового сообщения от клиента
            }
        } catch (Exception e) {
            e.printStackTrace(); // печать стека исключений для отладки
        } finally {
            // Закрытие потоков и сокетов
            try {
                if (sois != null) sois.close(); // закрытие потока ввода
                if (soos != null) soos.close(); // закрытие потока вывода
                if (clientAccepted != null) clientAccepted.close(); // закрытие сокета клиента
                if (serverSocket != null) serverSocket.close(); // закрытие сокета сервера
            } catch (IOException e) {
                e.printStackTrace(); // печать стека исключений для отладки
            }
        }
    }
}
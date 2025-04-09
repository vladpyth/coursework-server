import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            System.out.println("Server starting...");
            serverSocket = new ServerSocket(2525); // создание сокета сервера для заданного порта

            while (true) {
                Socket clientAccepted = serverSocket.accept(); // ожидание подключения клиента
                System.out.println("Connection established...");

                // Запускаем новый поток для обработки клиента
                new ClientHandler(clientAccepted).start();
            }
        } catch (IOException e) {
            e.printStackTrace(); // печать стека исключений для отладки
        } finally {
            // Закрытие сокета сервера
            try {
                if (serverSocket != null) serverSocket.close(); // закрытие сокета сервера
            } catch (IOException e) {
                e.printStackTrace(); // печать стека исключений для отладки
            }
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private ObjectInputStream sois;
    private ObjectOutputStream soos;
    private Commands commands = new Commands();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            sois = new ObjectInputStream(clientSocket.getInputStream());
            soos = new ObjectOutputStream(clientSocket.getOutputStream()); // создание потока вывода

            String clientMessageReceived = (String) sois.readObject(); // чтение сообщения от клиента
            String answer;
            while (!clientMessageReceived.equals("quit")) { // цикл до получения "quit"
                System.out.println(clientMessageReceived);
                answer = commands.initCommand(clientMessageReceived);
                if (answer != null) {
                    soos.writeObject(answer);
                } else {
                    soos.writeObject("0");
                }

                clientMessageReceived = (String) sois.readObject(); // ожидание нового сообщения от клиента
            }
        } catch (Exception e) {
            e.printStackTrace(); // печать стека исключений для отладки
        } finally {
            // Закрытие потоков и сокета
            try {
                if (sois != null) sois.close(); // закрытие потока ввода
                if (soos != null) soos.close(); // закрытие потока вывода
                if (clientSocket != null) clientSocket.close(); // закрытие сокета клиента
            } catch (IOException e) {
                e.printStackTrace(); // печать стека исключений для отладки
            }
        }
    }
}
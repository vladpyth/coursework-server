import java.io.*;
import java.net.*;

public class Server {
    private static Commands command = new Commands();
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {

            System.out.println("Server starting...");
            serverSocket = new ServerSocket(2525); // создание сокета сервера для заданного порта
            command.closeAll();
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
    private String nameUser=null;

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
                if(nameUser==null){
                    nameUser=Username(answer);
                }
                soos.writeObject(answer);

                clientMessageReceived = (String) sois.readObject(); // ожидание нового сообщения от клиента
            }

        } catch (Exception e) {
            e.printStackTrace(); // печать стека исключений для отладки
        } finally {
            // Закрытие потоков и сокета
            try {
                if(nameUser!=null){
                    if("0".equals(commands.initCommand("close "+nameUser))){
                        System.out.println("User quit is correct!");
                    }else{
                        System.out.println("User quit is not correct!");
                    }
                }

                if (sois != null) sois.close(); // закрытие потока ввода
                if (soos != null) soos.close(); // закрытие потока вывода
                if (clientSocket != null) clientSocket.close(); // закрытие сокета клиента
            } catch (IOException e) {
                e.printStackTrace(); // печать стека исключений для отладки
            }
        }
    }
    private String Username(String input) {
        // Разбиваем строку на слова по пробелам
        String[] words = input.split("\\s+");
        String name=null;
        if(words.length==4){
            name=words[3];
        }
        return name;
    }

}
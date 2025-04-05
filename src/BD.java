import java.sql.*;
import java.util.ArrayList;

public class BD {
    private final String HOST = "localhost";
    private final String PORT = "3306"; // Убедитесь, что порт правильный
    private final String DB_NAME = "java_task7";
    private final String LOGIN = "root"; // Имя пользователя
    private final String PASSWORD = "root"; // Пароль

    // Метод для получения соединения с базой данных
    private Connection getDbConnection() throws SQLException {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        return DriverManager.getConnection(url, LOGIN, PASSWORD);
    }




    // Метод для вставки пользователя в базу данных
    public void insertUser(String login, String password) {
        String sql = "INSERT INTO `task7_1` (login, password) VALUES (?, ?)"; // Используйте 'login' и 'password'
        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, password);
            prSt.executeUpdate();
            System.out.println("Пользователь успешно добавлен: " + login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения всех пользователей из базы данных
    public ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        String sql = "SELECT login, password FROM `task7_1`"; // Извлекаем login и password

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);
             ResultSet resultSet = prSt.executeQuery()) {

            while (resultSet.next()) {
                String user = "Login: " + resultSet.getString("login") + ", Password: " + resultSet.getString("password");
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Метод для вывода всех пользователей
    public void displayUsers() {
        ArrayList<String> users = getUsers();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей для отображения.");
        } else {
            System.out.println("Список пользователей:");
            for (String user : users) {
                System.out.println("- " + user);
            }
        }
    }





    public void insertStatus(String login, int status) {
        String sql = "INSERT INTO `black_list` (login, status) VALUES (?, ?)"; // Используйте 'login' и 'password'
        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, String.valueOf(status));
            prSt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getStatus1() {
        ArrayList<String> users = new ArrayList<>();
        String sql = "SELECT login, status FROM `black_list`"; // Извлекаем login и

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);
             ResultSet resultSet = prSt.executeQuery()) {

            while (resultSet.next()) {
                String userStatus = resultSet.getString("status");
                if("1".equals(userStatus)){
                    String user = resultSet.getString("login");

                    users.add(user);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    public ArrayList<String> getStatus0() {
        ArrayList<String> users = new ArrayList<>();
        String sql = "SELECT login, status FROM `black_list`"; // Извлекаем login и

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);
             ResultSet resultSet = prSt.executeQuery()) {

            while (resultSet.next()) {
                String userStatus = resultSet.getString("status");
                if("0".equals(userStatus)){
                    String user = resultSet.getString("login");

                    users.add(user);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void Update(String login, int newStatus){
        String updateSQL = "UPDATE black_list SET status = ? WHERE login = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setString(1, String.valueOf(newStatus)); // Устанавливаем новое значение
            pstmt.setString(2, login); // Устанавливаем ID пользователя

            pstmt.executeUpdate(); // Выполняем запрос

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public  void del(String login){
        String deleteSQL = "DELETE FROM black_list WHERE login = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {


            pstmt.setString(1, login); // Устанавливаем ID пользователя

            pstmt.executeUpdate(); // Выполняем запрос

        } catch (SQLException e) {
            e.printStackTrace();
        }
        deleteSQL = "DELETE FROM task7_1 WHERE login = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement pstm = connection.prepareStatement(deleteSQL)) {


            pstm.setString(1, login);

            pstm.executeUpdate(); // Выполняем запрос


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void find(String login){
        String findSQL = "SELECT login FROM `black_list` WHERE login = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement pstmt = connection.prepareStatement(findSQL)) {


            pstmt.setString(1, login);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                System.out.println("Пользователь с именем: "+resultSet.getString("login")+" найден");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
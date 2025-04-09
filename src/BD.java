import java.sql.*;
import java.util.ArrayList;

public class BD {
    private final String HOST = "localhost";
    private final String PORT = "3306"; // Убедитесь, что порт правильный
    private final String DB_NAME = "kyrs_work";
    private final String LOGIN = "root"; // Имя пользователя
    private final String PASSWORD = "root"; // Пароль

    // Метод для получения соединения с базой данных
    private Connection getDbConnection() throws SQLException {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        return DriverManager.getConnection(url, LOGIN, PASSWORD);
    }


    public int getUser(String login, String password) {
        String sql = "SELECT login FROM `users_log` WHERE login = ? AND password = ?"; // Extracting login
        int result = 0;

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, password);
            ResultSet resultSet = prSt.executeQuery();

            if (resultSet.next()) {

                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int insertUser(String gmail, String login, String password) {
        String sqlCheck = "SELECT login FROM `users_log` WHERE gmail = ? OR login = ?";
        String sqlInsert = "INSERT INTO `users_log` (gmail, login, password) VALUES (?, ?, ?)";
        int result = 0;

        try (Connection connection = getDbConnection();
             PreparedStatement prStCheck = connection.prepareStatement(sqlCheck)) {

            prStCheck.setString(1, gmail);
            prStCheck.setString(2, login);
            ResultSet resultSet = prStCheck.executeQuery();

            // Проверяем, есть ли уже пользователь с таким gmail и login
            if (!resultSet.next()) { // Если нет результатов, добавляем пользователя
                try (PreparedStatement prStInsert = connection.prepareStatement(sqlInsert)) {
                    prStInsert.setString(1, gmail);
                    prStInsert.setString(2, login);
                    prStInsert.setString(3, password);
                    prStInsert.executeUpdate();
                    result=1;
                    System.out.println("Пользователь успешно добавлен: " + login);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Пользователь с таким gmail и login уже существует.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }















    // Метод для вставки пользователя в базу данных


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

            if(resultSet!=null){
                while (resultSet.next()) {

                    String user = resultSet.getString("login");



                }
            }else{

            }


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
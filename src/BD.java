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
        String sql = "SELECT role FROM `users_log` WHERE login = ? AND password = ?";
        String updateSQL = "UPDATE users_log SET status = ? WHERE login = ?";// Extracting login
        int result = 0;

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, password);
            ResultSet resultSet = prSt.executeQuery();

            if (resultSet.next()) {
                try (PreparedStatement prStUpdate = connection.prepareStatement(updateSQL)) {
                    prStUpdate.setString(1, "1");
                    prStUpdate.setString(2, login);

                    prStUpdate.executeUpdate();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                result = resultSet.getInt("role");
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

    public String closeUser(String login){
        String updateSQL = "UPDATE users_log SET status = ? WHERE login = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setString(1, "0"); // Устанавливаем новое значение
            pstmt.setString(2, login); // Устанавливаем ID пользователя

            pstmt.executeUpdate(); // Выполняем запрос

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";

    }

    public String closeAll(){
        String mess="bad connect";
        String updateSQL = "UPDATE users_log SET status = 0";
        try (Connection connection = getDbConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.executeUpdate(); // Выполняем запрос
            mess="good connect!";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mess;
    }

    public String AllUsers(){
        String result="";
        String sql = "SELECT gmail,login, password, role, status FROM `users_log`"; // Извлекаем login и password

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);
             ResultSet resultSet = prSt.executeQuery()) {

            while (resultSet.next()) {
                result +=resultSet.getString("gmail")+" "+resultSet.getString("login") + " " + resultSet.getString("password")+ " " + resultSet.getString("role")+ " " + resultSet.getString("status")+" ";

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String UpdateUser(String[] data){
        String status="error bd";
        String updateSQL = "UPDATE users_log SET login = ?, password =?, role=?, status=? WHERE gmail = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setString(1, data[2]);
            pstmt.setString(2, data[3]);
            pstmt.setInt(3, Integer.parseInt(data[4]));
            pstmt.setInt(4, Integer.parseInt(data[5]));
            pstmt.setString(5, data[1]); // Устанавливаем ID пользователя

            pstmt.executeUpdate(); // Выполняем запрос
            status="1";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;

    }

    public String FindUser(String login){
       String sql = "SELECT role FROM `users_log` WHERE login = ? ";

       String result = "error: user not found";

       try (Connection connection = getDbConnection();
            PreparedStatement prSt = connection.prepareStatement(sql)) {

           prSt.setString(1, login);

           ResultSet resultSet = prSt.executeQuery();

           if (resultSet.next()) {

               result="1";


           }
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return result;
    }

    public String insertGroup(String login_master, String name_group,String num_users, String description) {
        String sqlCheck = "SELECT name_group FROM `groups` WHERE name_group = ? ";
        String sqlInsert = "INSERT INTO `groups` (login_master, name_group, num_users,description) VALUES (?, ?, ?, ?)";
        String result = "0";

        try (Connection connection = getDbConnection();
             PreparedStatement prStCheck = connection.prepareStatement(sqlCheck)) {

            prStCheck.setString(1, name_group);

            ResultSet resultSet = prStCheck.executeQuery();

            // Проверяем, есть ли уже пользователь с таким gmail и login
            if (!resultSet.next()) { // Если нет результатов, добавляем пользователя
                try (PreparedStatement prStInsert = connection.prepareStatement(sqlInsert)) {
                    prStInsert.setString(1, login_master);
                    prStInsert.setString(2, name_group);
                    prStInsert.setString(3, num_users);
                    prStInsert.setString(4, description);
                    prStInsert.executeUpdate();
                    result="1";
                    //System.out.println("Пользователь успешно добавлен: " + login);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                result = "a group with this name already exists.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String addUserinGroup(String name_group,String name_user, String role) {
        String sqlCheck = "SELECT name_group FROM `user_groups` WHERE login = ? ";
        String sqlInsert = "INSERT INTO `user_groups` (login, name_group, role) VALUES (?, ?, ?)";
        String result = "0";

        try (Connection connection = getDbConnection();
             PreparedStatement prStCheck = connection.prepareStatement(sqlCheck)) {

            prStCheck.setString(1, name_group);

            ResultSet resultSet = prStCheck.executeQuery();

            // Проверяем, есть ли уже пользователь с таким gmail и login
            if (!resultSet.next()) { // Если нет результатов, добавляем пользователя
                try (PreparedStatement prStInsert = connection.prepareStatement(sqlInsert)) {
                    prStInsert.setString(1, name_user);
                    prStInsert.setString(2, name_group);
                    prStInsert.setString(3, role);

                    prStInsert.executeUpdate();
                    result="1";
                    //System.out.println("Пользователь успешно добавлен: " + login);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                result = "a user with this name already exists.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String AllGroup(){
        String result="";
        String sql = "SELECT login_master, name_group, num_users FROM `groups`"; // Извлекаем login и password

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);
             ResultSet resultSet = prSt.executeQuery()) {

            while (resultSet.next()) {
                result +=resultSet.getString("login_master")+" "+resultSet.getString("name_group") + " " + resultSet.getString("num_users")+" ";

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String youGroup(String login){
        String result="";
        String sql = "SELECT name_group FROM `user_groups` WHERE login = ? ";



        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);

            ResultSet resultSet = prSt.executeQuery();
            int i=0;
            while (resultSet.next()) {

                result += resultSet.getString("name_group");
                result+=" ";
                i++;
                System.out.println(result+i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String delUserFromGroup(String name_group, String login) {
        String result = "";
        String sql = "DELETE FROM `user_groups` WHERE login = ? AND name_group = ? ";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, name_group);

            int affectedRows = prSt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Пользователь успешно удалён из группы.");
                result = "1";
            } else {
                System.out.println("Пользователь не найден в указанной группе.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Ошибка при удалении пользователя.";
        }
        return result;
    }

    public String alluserGroup(String group){
        String result="";
        String sql = "SELECT login, role, workload, completed_tasks FROM `user_groups` WHERE name_group=?"; // Извлекаем login и password

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);){
            prSt.setString(1, group);
            ResultSet resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                result +=resultSet.getString("login")+" "+resultSet.getString("role") + " " + resultSet.getString("workload")+ " " + resultSet.getString("completed_tasks")+" ";

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String addTask(String login, String task, String description, String priority, String group) {
        String result = "";

        // SQL statement for inserting a new task
        String sqlInsert = "INSERT INTO `task` (login, `group`, name_task, description, status, priority) VALUES (?, ?, ?, ?, ?, ?)";;

        try (Connection connection = getDbConnection();
             PreparedStatement prStInsert = connection.prepareStatement(sqlInsert)) {

            // Set parameters for the prepared statement
            prStInsert.setString(1, login);
            prStInsert.setString(2, group);
            prStInsert.setString(3, task);
            prStInsert.setString(4, description);
            prStInsert.setString(5, "In_progress");
            prStInsert.setInt(6, Integer.parseInt(priority));

            // Execute the update
            prStInsert.executeUpdate();
            result = "1"; // Success
            // Use logging instead of printing to stdout
            System.out.println("Task successfully added.");

        } catch (SQLException e) {
            // Consider returning a failure message
            e.printStackTrace();
            result = "Error: " + e.getMessage();
        }

        return result;
    }

    public String allTask(String group){
        String result="";
        String sql = "SELECT login,  name_task, description, status, priority FROM `task` WHERE `group`=?"; // Извлекаем login и password

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql);) {
            prSt.setString(1, group);
            try (ResultSet resultSet = prSt.executeQuery()) {
                while (resultSet.next()) {
                    result += resultSet.getString("login") + " " + resultSet.getString("name_task") + " " + resultSet.getString("status") + " " + resultSet.getString("priority") + " ";

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String delTask(String group, String login, String name_task) {
        String result = "";
        String sql = "DELETE FROM `task` WHERE `group` = ? AND name_task = ? AND login = ?"; // Удаляем задачу

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            // Установка параметров для подготовленного выражения
            prSt.setString(1, group);
            prSt.setString(2, name_task);
            prSt.setString(3, login);

            // Выполнение удаления
            int rowsAffected = prSt.executeUpdate();

            if (rowsAffected > 0) {
                result = "1";
            } else {
                result = "Задача не найдена.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Ошибка: " + e.getMessage();
        }

        return result;
    }

    public String checkMaster(String group, String login){


        String sql = "SELECT id FROM `groups` WHERE login_master = ? AND name_group = ?";

        String result = "0";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, group);

            ResultSet resultSet = prSt.executeQuery();

            if (resultSet.next()) {

                result="1";


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String youTask(String group, String login){


        String sql = "SELECT name_task, description, status, priority FROM `task` WHERE login = ? AND `group` = ?";

        String result = "";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, login);
            prSt.setString(2, group);

            ResultSet resultSet = prSt.executeQuery();

            while (resultSet.next()) {
                result +=resultSet.getString("name_task")+" "+resultSet.getString("description") + " " + resultSet.getString("status")+ " " + resultSet.getString("priority")+" ";

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String executeTask(String group, String name_task, String  login) {
        String sql = "UPDATE `task` SET status = ? WHERE login = ? AND `group` = ? AND name_task = ?";
        String result = "";

        try (Connection connection = getDbConnection();
             PreparedStatement prSt = connection.prepareStatement(sql)) {

            prSt.setString(1, "completed");
            prSt.setString(2, login);
            prSt.setString(3, group);
            prSt.setString(4, name_task);

            int rowsAffected = prSt.executeUpdate(); // Выполняем запрос

            if (rowsAffected > 0) {
                result = "1";
            } else {
                result = "Задача не найдена.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Ошибка: " + e.getMessage();
        }

        return result;
    }


}
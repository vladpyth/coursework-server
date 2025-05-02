import java.lang.reflect.Method;

public class Commands {

    static final String logining="logining";
    static final String autorisation="autorisation";
    static final String all_data_user="alldatauser";
    static final String updateUserTable="updateUserTable";
    static final String findUser="findUser";
    static final String addGroup="addGroup";
    static final String addUserinGroup="addUserinGroup";
    static final String allGroup="allGroup";
    static final String youGroup="youGroup";
    static final String delUserinGroup="delUserinGroup";
    static final String alluserGroup="alluserGroup";
    static final String addtask="addtask";
    static final String allTask="allTask";
    static final String dellTask="dellTask";
    static final String checkMaster="checkMaster";
    static final String youTask="youTask";
    static final String executeTask="executeTask";

    BD bd = new BD();




    public  String close(String[] com){
        return bd.closeUser(com[1]);
    }

    public String logining(String[] com){


        String result="0 "+logining;
        int role = bd.getUser(com[1],com[2]);
        if(role>=1){
            result="1 "+logining+" "+role+" "+com[1];

        }

        return result;
    }

    private String autorisation(String[] com){


        String result="0 "+autorisation;
        if(bd.insertUser(com[1],com[2],com[3])==1){
            result="1 "+autorisation;

        }

        return result;
    }

    private String alldatauser(String[] com){

        String result=bd.AllUsers();
        System.out.println(result);
        return result;
    }

    private String updateUserTable(String[] com){

        return bd.UpdateUser(com);
    }

    private String findUser(String[] com){

        return bd.FindUser(com[1]);
    }

    private String addGroup(String[] com){

        return bd.insertGroup(com[1],com[2],com[3],com[4]);
    }

    private String addUserinGroup(String[] com){

        return bd.addUserinGroup(com[1],com[2],com[3]);
    }

    private String allGroup(String[] com){

        String result=bd.AllGroup();
        System.out.println(result);
        return result;
    }


    private String youGroup(String[] com){

        String result=bd.youGroup(com[1]);
        System.out.println(result);
        return result;
    }
    private String delUserinGroup(String[] com){

        String result=bd.delUserFromGroup(com[1],com[2]);
        System.out.println(result);
        return result;
    }
    private String alluserGroup(String[] com){

        String result=bd.alluserGroup(com[1]);
        System.out.println(result);
        return result;
    }
    private String addtask(String[] com){

        String result=bd.addTask(com[1], com[2],com[3],com[4],com[5]);
        System.out.println(result);
        return result;
    }

    private String allTask(String[] com){

        String result=bd.allTask(com[1]);
        System.out.println(result);
        return result;
    }
    private String dellTask(String[] com){

        String result=bd.delTask(com[1],com[2],com[3]);
        System.out.println(result);
        return result;
    }
    private String checkMaster(String[] com){

        String result=bd.checkMaster(com[1],com[2]);
        System.out.println(result);
        return result;
    }
    private String youTask(String[] com){

        String result=bd.youTask(com[1],com[2]);
        System.out.println(result);
        return result;
    }
    private String executeTask(String[] com){

        String result=bd.executeTask(com[1],com[2],com[3]);
        System.out.println(result);
        return result;
    }




    private String[] splitStringIntoArray(String input) {
        // Разбиваем строку на слова по пробелам
        String[] words = input.split("\\s+");
        return words;
    }

    public String initCommand(String mess) {
        String[] com = splitStringIntoArray(mess);
        try {
            // Получаем метод по имени из первого слова
            Method method = this.getClass().getDeclaredMethod(com[0], String[].class);
            // Вызываем метод
            return (String) method.invoke(this, (Object) com);
        } catch (NoSuchMethodException e) {
            System.out.println("Метод не найден: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0 server";
    }
    public  void closeAll(){
        System.out.println(bd.closeAll());
    }

}


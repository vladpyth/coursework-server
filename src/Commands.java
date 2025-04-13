import java.lang.reflect.Method;

public class Commands {

    static final String logining="logining";
    static final String autorisation="autorisation";
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








    private String[] splitStringIntoArray(String input) {
        // Разбиваем строку на слова по пробелам
        String[] words = input.split("\\s+");
        return words;
    }

    public String initCommand(String mess) {
        String[] com = splitStringIntoArray(mess);
        try {
            // Получаем метод по имени из первого слова
            Method method = this.getClass().getMethod(com[0], String[].class);
            // Вызываем метод

            return (String) method.invoke(this, (Object) com);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0 server";
    }
    public  void closeAll(){
        System.out.println(bd.closeAll());
    }

}


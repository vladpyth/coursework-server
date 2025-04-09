import java.lang.reflect.Method;

public class Commands {

    static final String logining="logining";
    static final String autorisation="autorisation";
    BD bd = new BD();





    public String logining(String[] com){
        // return bd.getUser(com[1],com[2]);
        //System.out.println("logining");

        String result="0 "+logining;
        if(bd.getUser(com[1],com[2])==1){
            result="1 "+logining;
            return result;
        }

        return result;
    }

    public String autorisation(String[] com){
       // return bd.insertUser(com[1],com[2],com[3]);
        //System.out.println("autorisation");

        String result="0 "+autorisation;
        if(bd.insertUser(com[1],com[2],com[3])==1){
            result="1 "+autorisation;
            return result;
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
        return null;
    }
}

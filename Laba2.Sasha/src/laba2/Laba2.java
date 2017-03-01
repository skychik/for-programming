/*
Дописать PrintWriter - записаь данных в файл;
 */
package pkgnew;

import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class New {

    public static void main(String[] args) throws IOException { 
        try {
            String fileWay = System.getenv("EmployeeFile");
            BufferedReader reader = new BufferedReader(new FileReader(fileWay));
            
            reader = new BufferedReader(new InputStreamReader(System.in));
            String command = reader.readLine().trim();
            String save = "save";
            String load = "load";
            String remove = "remove";
            String removeEl = "remove {element}";
            String removeAllTheSame = "remove_all {element}";
            String removeLower = "remove_lower {element}";
            if (command.equals(save));
        }catch(NullPointerException e){
            System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, указав путь к файлу.");
        } 
       /* catch (FileNotFoundException e) {
            File file = new File(fileWay); 
        }
        try {
            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(fileWay);

            try {
                //Записываем текст в файл
                out.print(""); //Дописать
            } finally {
                
                //После чего мы должны закрыть файл
                //Иначе файл не запишется 
                
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }*/
        }
}

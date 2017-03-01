/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package laba2;

import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.IOException;

public class New {

    public static void main(String[] args) { 
        BufferedReader fileReader = null;
        String fileWay = System.getenv("EmployeeFile");
        try {
            fileReader = new BufferedReader(new FileReader(fileWay));
        } 
        catch (FileNotFoundException e) {
            File file = new File(fileWay); 
        }
 
    try {
        //PrintWriter обеспечит возможности записи в файл
        PrintWriter out = new PrintWriter(fileWay);
 
        try {
            //Записываем текст в файл
            out.print(""); //Дописать
        } finally {
            /*
            *После чего мы должны закрыть файл
            *Иначе файл не запишется 
            */
            out.close();
        }
    } catch(IOException e) {
        throw new RuntimeException(e);
    }
        }
}

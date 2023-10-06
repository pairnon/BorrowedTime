package io.papermc.funnygame;

import java.util.*;
import java.io.*;

public class FileRW {

    public static void writeFile(String path, ArrayList<FunnyPlayer> arrList) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(arrList);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static ArrayList<FunnyPlayer> readFile(String path) {
        ArrayList<FunnyPlayer> arrList = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            arrList = (ArrayList<FunnyPlayer>) in.readObject();
            in.close();
            fileIn.close();
            return arrList;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("FunnyPlayer class not found");
            c.printStackTrace();
            return null;
        }

    }

}
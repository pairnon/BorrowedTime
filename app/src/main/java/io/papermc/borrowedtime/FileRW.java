package io.papermc.borrowedtime;

import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;

public class FileRW {

    public static void writeFile(String path, ArrayList<BTPlayer> arrList) {
        checkDir(BorrowedTime.pluginDirPath);
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

    public static ArrayList<BTPlayer> readFile(String path) {
        checkDir(BorrowedTime.pluginDirPath);
        ArrayList<BTPlayer> arrList = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            arrList = (ArrayList<BTPlayer>) in.readObject();
            in.close();
            fileIn.close();
            return arrList;
        } catch (IOException i) {
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("BTPlayer class not found");
            c.printStackTrace();
            return null;
        }

    }

    private static boolean checkDir(String dirPath) {
        Path path = Paths.get(dirPath);
        try {
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException f) {
            return true;
        } catch (IOException i) {
            return false;
        }
        return false;
    }

}
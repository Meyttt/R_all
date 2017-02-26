package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by svkreml on 26.02.2017.
 */
public class LastOpened {
    public static void save(File directory){
        Path last = Paths.get("last.prop");
        try {
            Files.write(last, directory.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File load(){
        File file = new File("last.prop");
        File projectDirectory = null;
        try {
            projectDirectory = new File(new Scanner(file).nextLine());

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        return projectDirectory;
    }
}

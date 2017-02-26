package xml;

import project.ProjFile;

import java.io.File;

/**
 * Created by svkreml on 26.02.2017.
 */
public class test {
    public static void main(String[] args) {
        ProjFile p = new ProjFile("rtran","1.1.1","name");
        Xml.save(new File("C:\\Users\\svkreml\\Desktop\\project.rtran"),p);
        ProjFile projFile = (ProjFile) Xml.load(new File("C:\\Users\\svkreml\\Desktop\\project.rtran"), ProjFile.class);
        System.out.println("projFile = " + projFile);

    }
}

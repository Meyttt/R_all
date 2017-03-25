package project;

import xml.Xml;

import java.io.File;
import java.io.IOException;

/**
 * Created by svkreml on 26.02.2017.
 */
public class ProjectR {
    ProjFile projFile;
    File root;
    public ProjectR(File projectDirectory) {
        if (projectDirectory.getName().equals("project.rpro")) {
            projFile = (ProjFile) Xml.load(projectDirectory, ProjFile.class);
            projFile.setPath(projectDirectory.getParentFile());
        } else {
            try {
                File input = new File(projectDirectory, "Входные данные");
                input.mkdir();
                File output = new File(projectDirectory, "Выходные данные");
                output.mkdir();
                File test = new File(projectDirectory, "Тестовые данные");
                test.mkdir();
                File program = new File(projectDirectory, "Программа.rtran");
                program.createNewFile();
                projFile = new ProjFile(projectDirectory);
                Xml.save(new File(projectDirectory, "project.rpro"), projFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ProjFile getProjFile() {
        return projFile;
    }

    public void setProjFile(ProjFile projFile) {
        this.projFile = projFile;
    }

    public void loadProjFile() {

    }

    public void createNewProject(File directory) {

    }
}

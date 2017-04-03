package project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import xml.Xml;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by svkreml on 20.11.2016.
 */
@JacksonXmlRootElement(localName = "project")
public class ProjFile {
    @JacksonXmlProperty(isAttribute = true)
    String type;
    @JacksonXmlProperty(isAttribute = true)
    String version;
    @JacksonXmlProperty()
    boolean lenta;
    @JacksonXmlProperty()
    String path;
    @JacksonXmlProperty()
    String lentaPath;

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    @JacksonXmlProperty()
    String runType;
    @JacksonXmlProperty(isAttribute = true)
    String project_name;
    public ProjFile(File projectDirectory) {
        this.type = "R-MACHINE";
        this.version = "1.0.0";
        this.project_name = projectDirectory.getName();
        this.setLenta(false);
        this.setLentaPath("");
        setPath(projectDirectory.getParentFile());
    }
    public ProjFile() {
    }

    public ProjFile(String type, String version, String project_name) {
        this.type = type;
        this.version = version;
        this.project_name = project_name;
        this.setLenta(true);
        this.setLentaPath("");
    }

    public File getPath() {
        return new File(path);
    }

    public void setPath(File projectDirectory) {
        path = projectDirectory.toString();
    }

    public String getProject_name() {
        return project_name;
    }

    public boolean isLenta() {
        return lenta;
    }

    public void setLenta(boolean lenta) {
        this.lenta = lenta;
    }

    public String getLentaPath() {
        return lentaPath;
    }

    public void setLentaPath(String lentaPath) {
        this.lentaPath = lentaPath;
    }

    public void save() {
        Xml.save(new File(getPath().toString(),"project.rpro"),this);
    }


  /*  public void save() {
        Xml.save(new File(projectDirectory,))
    }*/
}

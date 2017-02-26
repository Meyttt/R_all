package project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import xml.Xml;

import java.io.File;

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
    @JacksonXmlProperty(isAttribute = true)
    String project_name;
    public ProjFile(File projectDirectory) {
        this.type = "R-MACHINE";
        this.version = "1.0.0";
        this.project_name = projectDirectory.getName();
        this.setLenta(false);
        this.setLentaPath("");
        setPath(projectDirectory);
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
        Xml.save(getPath(),this);
    }


  /*  public void save() {
        Xml.save(new File(projectDirectory,))
    }*/
}

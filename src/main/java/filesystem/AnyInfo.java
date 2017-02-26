package filesystem;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javafx.scene.control.Tab;

import java.nio.file.Path;

/**
 * Created by svkreml on 26.02.2017.
 */
public class AnyInfo {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    Tab tab;
    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
    @JacksonXmlProperty(isAttribute = true)
    private Path path;
    public AnyInfo(String name,Path path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }
}

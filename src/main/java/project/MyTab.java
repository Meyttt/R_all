package project;

import filesystem.FileInfo;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

/**
 * Created by svkreml on 26.02.2017.
 */
public class MyTab extends Tab {
    public TreeItem treeItem;
    String type = "null";
    boolean changed = false;
    FileInfo fileInfo;

    public MyTab() {
        super();

    }

    public MyTab(TreeItem treeItem) {
        super();
        this.treeItem = treeItem;
    }

    public void setChanged() {
        this.changed = true;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }
}

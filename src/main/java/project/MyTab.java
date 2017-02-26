package project;

import filesystem.FileInfo;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

/**
 * Created by svkreml on 26.02.2017.
 */
public class MyTab extends Tab {
    public TreeItem treeItem;
    boolean changed;

    public MyTab() {
        super();
        
    }
FileInfo fileInfo;

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public MyTab(TreeItem treeItem) {
        super();
        this.treeItem = treeItem;
    }
    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }
}

package project;

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

    public MyTab(TreeItem treeItem) {
        super();
        this.treeItem = treeItem;
    }
    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }
}

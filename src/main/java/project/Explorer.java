package project;

import filesystem.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by svkreml on 26.02.2017.
 */
public class Explorer {
    private static final Pattern XML_TAG = Pattern.compile("(?<ELEMENT>(</?\\h*)(\\w+)([^<>]*)(\\h*/?>))"
            + "|(?<COMMENT><!--[^<>]+-->)");
    private static final Pattern ATTRIBUTES = Pattern.compile("(\\w+\\h*)(=)(\\h*\"[^\"]+\")");
    private static final int GROUP_OPEN_BRACKET = 2;
    private static final int GROUP_ELEMENT_NAME = 3;
    private static final int GROUP_ATTRIBUTES_SECTION = 4;
    private static final int GROUP_CLOSE_BRACKET = 5;
    private static final int GROUP_ATTRIBUTE_NAME = 1;
    private static final int GROUP_EQUAL_SYMBOL = 2;
    private static final int GROUP_ATTRIBUTE_VALUE = 3;
    static private final Image folderImage = new Image("/icons/ic_folder_black_48dp_1x.png");
    static private final Image fileImage = new Image("/icons/ic_insert_drive_file_black_48dp_1x.png");
    static private final Image settingsImage = new Image("/icons/ic_settings_applications_black_48dp_1x.png");
    static private final Image programImage = new Image("/icons/ic_play_circle_filled_black_48dp_1x.png");
    TreeView treeView;
    TabPane redactorTabs;
    TreeItem oldTreeItem = null;
    FileInfo lastSelectedFile = null;
    boolean loopBlocker = false;
    TreeItem<AnyInfo> rootItem = null;
    private TreeItem<AnyInfo> dragging = null;

    public Explorer() {
    }

    public Explorer(TreeView treeView, TabPane redactorTabs) {
        this.treeView = treeView;
        this.redactorTabs = redactorTabs;
        treeView.setEditable(true);
        treeView.setShowRoot(true);
        //-----------------------------
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showMenu(treeView, event);
            }
        });
        treeView.setEditable(true);
        treeView.setShowRoot(true);
        treeView.setCellFactory(param -> {
            return setCellFactory(treeView);
        });


        //-----------------------------
    }

    private static boolean containsParent(TreeItem<?> parent, TreeItem<?> child) {
        TreeItem<?> current = child;
        while (current != null) {
            if (current.equals(parent))
                return true;
            current = current.getParent();
        }
        return false;
    }

    static String readFile(Path path, Charset encoding) throws IOException {

        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {

        Matcher matcher = XML_TAG.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            if (matcher.group("COMMENT") != null) {
                spansBuilder.add(Collections.singleton("comment"), matcher.end() - matcher.start());
            } else {
                if (matcher.group("ELEMENT") != null) {
                    String attributesText = matcher.group(GROUP_ATTRIBUTES_SECTION);

                    spansBuilder.add(Collections.singleton("tagmark"), matcher.end(GROUP_OPEN_BRACKET) - matcher.start(GROUP_OPEN_BRACKET));
                    spansBuilder.add(Collections.singleton("anytag"), matcher.end(GROUP_ELEMENT_NAME) - matcher.end(GROUP_OPEN_BRACKET));

                    if (!attributesText.isEmpty()) {

                        lastKwEnd = 0;

                        Matcher amatcher = ATTRIBUTES.matcher(attributesText);
                        while (amatcher.find()) {
                            spansBuilder.add(Collections.emptyList(), amatcher.start() - lastKwEnd);
                            spansBuilder.add(Collections.singleton("attribute"), amatcher.end(GROUP_ATTRIBUTE_NAME) - amatcher.start(GROUP_ATTRIBUTE_NAME));
                            spansBuilder.add(Collections.singleton("tagmark"), amatcher.end(GROUP_EQUAL_SYMBOL) - amatcher.end(GROUP_ATTRIBUTE_NAME));
                            spansBuilder.add(Collections.singleton("avalue"), amatcher.end(GROUP_ATTRIBUTE_VALUE) - amatcher.end(GROUP_EQUAL_SYMBOL));
                            lastKwEnd = amatcher.end();
                        }
                        if (attributesText.length() > lastKwEnd)
                            spansBuilder.add(Collections.emptyList(), attributesText.length() - lastKwEnd);
                    }

                    lastKwEnd = matcher.end(GROUP_ATTRIBUTES_SECTION);

                    spansBuilder.add(Collections.singleton("tagmark"), matcher.end(GROUP_CLOSE_BRACKET) - lastKwEnd);
                }
            }
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    Path newName(Path oldName, String newNameString) throws IOException {
        return Files.move(oldName, oldName.resolveSibling(newNameString));
    }
    private Object setCellFactory(TreeView treeView) {

        TextFieldTreeCell<AnyInfo> cell = new TextFieldTreeCell<AnyInfo>() {
            String oldName;
            @Override
            public void startEdit() {
                oldName=getText();
                if (getTreeItem().getParent() == null || getTreeItem().getParent().getParent() == null)
                    return;
                super.startEdit();
            }

            @Override
            public void commitEdit(AnyInfo anyInfo) {
                if(anyInfo instanceof FileInfo) {
                    try {
                        newName(anyInfo.getPath(),getText());
                        anyInfo.getTab().setText(getText());
                        super.commitEdit(anyInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                        //fixme сделать красиво
            }
        };
        StringConverter<AnyInfo> converter = new StringConverter<AnyInfo>() {
            @Override
            public String toString(AnyInfo object) {
                return object.getName();
            }

            @Override
            public AnyInfo fromString(String string) {
                TreeItem<AnyInfo> item = cell.getTreeItem();
                AnyInfo info = item.getValue();
                info.setName(string);
                sortChildren(item.getParent());
                cell.getTreeView().getSelectionModel().select(item);
                item.getValue().setName(string);
                Path newName = Paths.get(item.getValue().getPath().getParent().toString(), string);
                try {
                    Files.move(item.getValue().getPath(), newName);
                    item.getValue().setPath(newName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return info;
            }
        };
        cell.setOnDragDetected(event -> {
            setOnDrag(cell, event);
        });
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSelectedItem(newValue));
        cell.setOnDragOver(event -> event.acceptTransferModes(TransferMode.COPY_OR_MOVE));
        cell.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            MenuItem copyItem = new MenuItem("Копировать");
            MenuItem moveItem = new MenuItem("Перенести");
            TreeItem<AnyInfo> dropped = dragging; // куда перетаскиваем
            TreeItem<AnyInfo> dragged = cell.getTreeItem();
            // то, что перетаскиваем
            if (dropped.getParent() == rootItem || dragged.getParent() == null || dragged == rootItem) {
                System.out.println("root");
                return;
            }
            if (containsParent(dropped, dragged)) {
                // todo: показать ошибку
                return;
            }
            if (dragged.getValue() instanceof FolderInfo) {
                moveItem.setOnAction(e -> {
                    //todo проверка на перемещение корня в внутрь себя
                    try {
                        Files.move(dropped.getValue().getPath(), Paths.get(dragged.getValue().getPath().toString(),
                                dropped.getValue().getPath().getFileName().toString()));
                        dropped.getValue().setPath(Paths.get(dragged.getValue().getPath().toString(),
                                dropped.getValue().getPath().getFileName().toString()));
                        //todo как найти файл, который тащат?
                        dropped.getParent().getChildren().remove(dropped);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return;
                    }
                    insert(cell.getTreeItem(), dropped);
                    treeView.getSelectionModel().select(dropped);
                });
                copyItem.setOnAction(e -> {
                    TreeItem<AnyInfo> copyNode = cloneTree(dropped);
                    insert(cell.getTreeItem(), copyNode);
                    try {
                        Files.copy(dropped.getValue().getPath(), Paths.get(cell.getTreeItem().getValue().getPath().toString(),
                                dropped.getValue().getPath().getFileName().toString()));
                        dropped.getValue().setPath(Paths.get(cell.getTreeItem().getValue().getPath().toString(),
                                dropped.getValue().getPath().getFileName().toString()));
                        dropped.getParent().getChildren().remove(dropped);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return;
                    }
                    insert(cell.getTreeItem(), dropped);
                    treeView.getSelectionModel().select(dropped);
                    treeView.getSelectionModel().select(copyNode);
                });
                dragging = null;
                ContextMenu menu = new ContextMenu(copyItem, moveItem);
                menu.show(treeView.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            } else System.out.println("это не папка");
        });
        cell.setConverter(converter);
        return cell;
    }

    private void setOnDrag(TextFieldTreeCell<AnyInfo> cell, MouseEvent event) {
        Dragboard dragboard = cell.startDragAndDrop(TransferMode.COPY_OR_MOVE);
        TreeItem<AnyInfo> treeItem = cell.getTreeItem();
        ClipboardContent content = new ClipboardContent();
        content.putString(treeItem.getValue().getName());
        dragboard.setContent(content);
        dragging = treeItem;
        event.consume();
    }

    public void saveTextArea() {
        //todo сохранение текущей вкладки
        MyTab tab = (MyTab) redactorTabs.getSelectionModel().getSelectedItem();
        Path path = ((AnyInfo) tab.getTreeItem().getValue()).getPath();
        if (tab.getType().equals("text")) {
            CodeArea textArea = (CodeArea) ((VirtualizedScrollPane) ((StackPane) tab.getContent()).getChildren().get(0)).getContent();
            try {
                Files.write(path, textArea.getText().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveTextArea(MyTab tab) {
        //todo сохранение текущей вкладки
        Path path = ((AnyInfo) tab.getTreeItem().getValue()).getPath();
        if (tab.getType().equals("text")) {
            CodeArea textArea = (CodeArea) ((VirtualizedScrollPane) ((StackPane) tab.getContent()).getChildren().get(0)).getContent();
            try {
                Files.write(path, textArea.getText().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showMenu(TreeView<AnyInfo> tree, MouseEvent event) {
        ContextMenu menu;

        TreeItem<AnyInfo> current = tree.getSelectionModel().getSelectedItem();


        MenuItem itemEdit = new MenuItem("Изменить");

        itemEdit.setOnAction(e -> {
            tree.edit(current);
            //todo изменить название
            System.out.println("e = " + e);
            System.out.println("current = " + current.getValue().getPath());
            System.out.println();

        });
        itemEdit.setDisable(current.getValue().getPath().equals(rootItem.getValue().getPath().getParent()));
        //itemEdit.setDisable(current.getParent().getValue().getPath().equals(rootItem.getValue().getPath().getParent()));
        MenuItem itemDelete = new MenuItem("Удалить");
        itemDelete.setOnAction(e -> {
            deleteBranch(current);
        });
        itemDelete.setDisable(current.getParent() == null);
        itemDelete.setDisable(current.getValue().getPath().equals(rootItem.getValue().getPath().getParent()));
        itemDelete.setDisable(current.getParent().getValue().getPath().equals(rootItem.getValue().getPath()));
        if (current.getValue() instanceof FolderInfo) {

/*            MenuItem itemAddFolder = new MenuItem("Добавить папку");
            itemAddFolder.setOnAction(e -> {
                createFolder(tree, current);
            });*/

            MenuItem itemAddFile = new MenuItem("Добавить файл");
            itemAddFile.setOnAction(e -> {
                createFile(tree, current);
            });
            menu = new ContextMenu(/*itemAddFolder,*/ itemAddFile, itemEdit, itemDelete);
        } else {
            menu = new ContextMenu(itemEdit, itemDelete);
        }
        menu.show(tree.getScene().getWindow(), event.getScreenX(), event.getScreenY());

    }

    private void deleteBranch(TreeItem<AnyInfo> current) {
        try {
            redactorTabs.getTabs().remove(current.getValue().getTab());
            Files.delete(current.getValue().getPath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        current.getParent().getChildren().remove(current);
    }

    private void createFile(TreeView<AnyInfo> tree, TreeItem<AnyInfo> current) {
        try {
            Files.createFile(Paths.get(current.getValue().getPath().toString(), "Новый файл"));
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        TreeItem<AnyInfo> fileItem = new TreeItem<>(new FileInfo("Новый файл", Paths.get(current.getValue().getPath().toString(), "Новый файл")), new ImageView(fileImage));
        insert(current, fileItem);
        tree.getSelectionModel().select(fileItem);
    }

    private void createFolder(TreeView<AnyInfo> tree, TreeItem<AnyInfo> current) {
        TreeItem<AnyInfo> folderItem = new TreeItem<>(new FolderInfo("Новая папка", current.getValue().getPath()), new ImageView(folderImage));
        Path path = folderItem.getValue().getPath();
        try {
            Files.createDirectory(Paths.get(path.toString(), "Новая папка"));
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        insert(current, folderItem);
        tree.getSelectionModel().select(folderItem);
    }

    private void insert(TreeItem<AnyInfo> parent, TreeItem<AnyInfo> child) {
        ObservableList<TreeItem<AnyInfo>> children = parent.getChildren();
        children.add(child);
        sortChildren(parent);
    }

    private TreeItem<AnyInfo> cloneTree(TreeItem<AnyInfo> item) {
        TreeItem<AnyInfo> copy;
        if (item.getValue() instanceof FolderInfo)
            copy = new TreeItem<>(item.getValue(), new ImageView(folderImage));
        else
            copy = new TreeItem<>(item.getValue(), new ImageView(fileImage));
        for (TreeItem<AnyInfo> child : item.getChildren()) {
            copy.getChildren().add(cloneTree(child));
        }
        return copy;
    }

    public void openProject(Path directory) throws IOException {
        if (Files.isDirectory(directory))
            rootItem = loadFolders(directory);
        else
            rootItem = loadFolders(directory.getParent());
        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSelectedItem(newValue));
    }

    private void updateSelectedItem(Object newValue) {
        SingleSelectionModel<Tab> selectionModelTabs = redactorTabs.getSelectionModel();
        MultipleSelectionModel<TreeItem> selectionModelTree = treeView.getSelectionModel();
        TreeItem newTreeItem = (TreeItem) newValue;
        if (newTreeItem == null) return;

        if (!newTreeItem.equals(oldTreeItem) && newTreeItem.getValue() instanceof FileInfo) {
            FileInfo file = (FileInfo) newTreeItem.getValue();
            oldTreeItem = newTreeItem;
            lastSelectedFile = (FileInfo) newTreeItem.getValue();

            if (file.opened) {
                if (loopBlocker) return;
                loopBlocker = true;
                selectionModelTabs.select(file.getTab());
                loopBlocker = false;
            } else {
                file.opened = true;
                MyTab myTab = new MyTab();
                myTab.setTreeItem(selectionModelTree.getSelectedItem());
                myTab.setFileInfo(file);
                file.setTab(myTab);
                myTab.setOnCloseRequest(arg0 -> {
                    MyTab cTab = (MyTab) arg0.getTarget();
                    selectionModelTabs.select(cTab);
                    if (cTab.changed)
                        if (saveDialog())
                            saveTextArea();
                    cTab.getFileInfo().setOpened(false);
                    cTab.getFileInfo().setTab(null);
                });
                myTab.setOnSelectionChanged((arg0) -> {
                    //int row = treeView.getRow();
                    //System.out.println("row = " + row);
                    // if(arg0.getTarget())
                    MyTab peekTab = (MyTab) arg0.getTarget();
                    if (selectionModelTree.getSelectedItem() != peekTab.getTreeItem()) ;
                    {
                        if (loopBlocker) return;
                        loopBlocker = true;
                        selectionModelTree.select(peekTab.getTreeItem());
                        loopBlocker = false;
                    }
                });
                try {
                    if (newTreeItem.getValue() instanceof ProgramInfo)
                        setRtranRedactor(myTab, readFile(((FileInfo) newTreeItem.getValue()).getPath(), Charset.defaultCharset()), file.getName());
                    else
                        setText(myTab, readFile(((FileInfo) newTreeItem.getValue()).getPath(), Charset.defaultCharset()), file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((AnyInfo) newTreeItem.getValue()).setTab(myTab);
                myTab.setClosable(true);
                redactorTabs.getTabs().add(myTab);
                selectionModelTabs.select(myTab);
            }

        }
    }

    private void setText(MyTab tab, String text, String name) {
        CheckMenuItem wrapItem = new CheckMenuItem("Перенос строк");
        wrapItem.setSelected(true);
        ContextMenu contextMenu = new ContextMenu(wrapItem);
        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        tab.setType("text");
        codeArea.setWrapText(true);
        codeArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .subscribe(change -> {
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
                });
        wrapItem.setOnAction(e -> {
            codeArea.setWrapText(wrapItem.isSelected());
        });
        codeArea.replaceText(0, 0, text);
        tab.setContextMenu(contextMenu);
        codeArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                tab.setChanged();
            }
        });
        tab.setText(name);
        tab.setContent(new StackPane(new VirtualizedScrollPane<CodeArea>(codeArea)));
    }

    private void setRtranRedactor(Tab tab, String text, String name) {
        Label label = new Label("Тут должен быть редактор кода");
        tab.setText(name);
        tab.setContent(new StackPane((label)));
    }

    private TreeItem<AnyInfo> loadFolders(Path file) throws IOException {
        if (Files.isDirectory(file)) {
            TreeItem<AnyInfo> folder = new TreeItem<>(new FolderInfo(file.getFileName().toString(), file.toAbsolutePath()), new ImageView(folderImage));
            Files.list(file).forEach(path -> {
                try {
                    folder.getChildren().add(loadFolders(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            sortChildren(folder);
            return folder;
        } else if (file.getFileName().toString().endsWith(".rpro")) {
            return new TreeItem<>(new SettingsInfo(file.getFileName().toString(), file.toAbsolutePath()), new ImageView(settingsImage));
        } else if (file.getFileName().toString().endsWith(".rtran")) {
            return new TreeItem<>(new ProgramInfo(file.getFileName().toString(), file.toAbsolutePath()), new ImageView(programImage));
        } else {
            return new TreeItem<>(new FileInfo(file.getFileName().toString(), file.toAbsolutePath()), new ImageView(fileImage));
        }
    }

    private void sortChildren(TreeItem<AnyInfo> parent) {
        // if(parent.getChildren().size()==1) return;
        (parent.getChildren()).sort((o1, o2) -> {
            AnyInfo v1 = o1.getValue();
            AnyInfo v2 = o2.getValue();
            if (v1 instanceof FolderInfo && v2 instanceof FileInfo)
                return 1;
            if (v1 instanceof FileInfo && v2 instanceof FolderInfo)
                return -1;
            return v1.getName().compareToIgnoreCase(v2.getName());
        });
    }

    public boolean saveDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Закрытие файла");
        alert.setHeaderText("Файл будет закрыт");
        alert.setContentText("Сохранить изменения?");
        ButtonType yes = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Нет", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        System.out.println("result.get() = " + result.get());

        return result.get().getButtonData().equals(ButtonBar.ButtonData.YES);
    }

    public TreeView getTreeView() {
        return treeView;
    }

    public void setTreeView(TreeView treeView) {
        this.treeView = treeView;
    }
}
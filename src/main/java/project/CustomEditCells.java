package project;

/**
 * Created by svkreml on 06.03.2017.
 */


import filesystem.AnyInfo;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CustomEditCells extends TreeCell<AnyInfo> {
    private TextField textField;

    @Override
    public void cancelEdit() {
        super.cancelEdit();
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null)
            createTextField();
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.ENTER) {
                    //commitEdit(an);
                } else if (e.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    @Override
    protected void updateItem(AnyInfo anyInfo, boolean empty) {
        super.updateItem(anyInfo, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
            }
        }
    }
}
package ui;

import controller.filemanipulation.FileManipulation;
import interfaces.FileManipulationDialogCallback;
import utils.PathUtils;

import javax.swing.*;
import java.awt.*;

public class MoveFileDialog extends JDialog {

    private final String oldParentPath;
    private final String fileName;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldNewPath;
    private JTextField textFieldOldPath;

    private final FileManipulationDialogCallback callbacks;

    public MoveFileDialog(String oldPath, FileManipulationDialogCallback callbacks) {
        this.callbacks = callbacks;
        this.oldParentPath = PathUtils.getParentFolder(oldPath);
        this.fileName = PathUtils.getFileName(oldPath);
        textFieldOldPath.setText(oldParentPath);
        textFieldNewPath.setText(oldParentPath);

        setContentPane(contentPane);
        if (PathUtils.isDirectory(oldPath)) {
            setTitle("Move directory - " + fileName);
        } else {
            setTitle("Move file - " + fileName);
        }
        setPreferredSize(new Dimension(500, 200));
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onOK() {
        if (textFieldNewPath.getText() != null && !textFieldNewPath.getText().isEmpty()) {
            String newPath = textFieldNewPath.getText();
            FileManipulation.moveFile(oldParentPath + fileName, newPath + "\\" + fileName);
            callbacks.onOk();
            dispose();
        } else {
            textFieldNewPath.requestFocus();
        }
    }

    private void onCancel() {
        dispose();
    }
}

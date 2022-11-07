package ui;

import controller.filemanipulation.FileManipulation;
import interfaces.FileManipulationDialogCallback;
import utils.PathUtils;

import javax.swing.*;

public class RenameFileDialog extends JDialog {
    private final String oldPath;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldNewName;

    private final FileManipulationDialogCallback callbacks;

    public RenameFileDialog(String path, FileManipulationDialogCallback callbacks) {
        this.callbacks = callbacks;
        this.oldPath = path;
        String fileName = PathUtils.getFileName(path);
        textFieldNewName.setText(fileName);

        setTitle("Rename " + fileName);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onOK() {
        if (textFieldNewName.getText() != null && !textFieldNewName.getText().equals("")) {
            String newFileName = textFieldNewName.getText();
            FileManipulation.renameFile(oldPath, PathUtils.getParentFolder(oldPath) + newFileName);
            callbacks.onOk();
            dispose();
        } else {
            textFieldNewName.requestFocus();
        }
    }

    private void onCancel() {
        dispose();
    }

}

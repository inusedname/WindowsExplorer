package ui;

import controller.filemanipulation.FileManipulation;

import javax.swing.*;
import java.awt.event.*;

public class RenameFileDialog extends JDialog {
    private final String path;
    private final String fileName;
    private String newFileName;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane textPaneNewName;
    private JTextField textFieldNewName;

    public RenameFileDialog(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        textPaneNewName.setText("New Name");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if(textFieldNewName.getText()!=null){
            newFileName = textFieldNewName.getText();
            FileManipulation fileManipulation = new FileManipulation();
            fileManipulation.renameFile(path,path.substring(0,path.length()-fileName.length()-1)+newFileName);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}

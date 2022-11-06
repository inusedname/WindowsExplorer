package ui;

import controller.filemanipulation.FileManipulation;

import javax.swing.*;
import java.awt.event.*;

public class MoveFileDialog extends JDialog {

    private final String oldPath;
    private final String fileName;
    private String newPath;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldNewPath;
    private JTextField textFieldOldPath;
    private JTextPane textOldPath;
    private JTextPane textNewPath;

    public MoveFileDialog(String oldPath, String fileName) {
        this.oldPath = oldPath;
        this.fileName = fileName;
        textFieldOldPath.setText(oldPath);
        textOldPath.setText("Old Path");
        textNewPath.setText("New Path");
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
        if(textFieldNewPath.getText()!=null){
            newPath = textFieldNewPath.getText();
            FileManipulation fileManipulation = new FileManipulation();
            fileManipulation.moveFile(oldPath,(newPath+fileName));
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}

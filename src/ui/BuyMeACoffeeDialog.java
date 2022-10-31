package ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BuyMeACoffeeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea windowsExplorerWrittenInTextArea;
    private JButton btBrowseSource;

    public BuyMeACoffeeDialog() {
        windowsExplorerWrittenInTextArea.setText("""
                Windows Explorer written in Java.
                                
                Authors:
                   - Nguyễn Viết Quang (@inusedname)
                   - Đặng Việt Quân (@quan3a1dvt)
                   - Nguyễn Văn Nhất (@NbutS)
                   - Lê Quang Dũng (@S4ltF1sh)
                   - Cao Duy Dũng (@DungTheRipper)""");
        setTitle("About");
        setPreferredSize(new java.awt.Dimension(400, 300));
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        btBrowseSource.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(java.net.URI.create("https://github.com/inusedname/WindowsExplorer"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonOK.addActionListener(e -> onOK());

    }

    private void onOK() {
        // add your code here
        dispose();
    }

}

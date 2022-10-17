package ui;

import ui.popupmenu.FolderPopClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home {
    private JPanel homePanel;
    private JTree treeBrowse;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private JTextField tfAddress;
    private JButton btPicker;

    public Home() {
        setUpUI();
    }

    private void setUpUI() {
        homePanel.setPreferredSize(new Dimension(1280, 720));
        setUpIcons();
        setUpOnClick();
    }

    private void setUpOnClick() {
        treeBrowse.addMouseListener(new FolderPopClickListener());
        tableCurrentFolder.addMouseListener(new FolderPopClickListener());
    }

    private void setUpIcons() {
        btBack.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_back.png"))));
        btForward.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_forward.png"))));
        btUp.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_up.png"))));
        btPicker.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_open_folder.png"))));

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Home");
        frame.addMouseListener(new FolderPopClickListener());
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

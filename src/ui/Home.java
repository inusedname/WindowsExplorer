package ui;

import controller.treebrowse.TreeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home implements Runnable, TreeHelper.CallBack {
    private JPanel homePanel;
    private JTree tree;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private JTextField tfAddress;
    private JButton btPicker;
    private JScrollPane leftScrollPane;

    public Home() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Home());
    }

    @Override
    public void run() {

        setUpUI();

        JFrame frame = new JFrame("Home");
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void setUpUI() {
        homePanel.setPreferredSize(new Dimension(1280, 720));
        setUpIcons();
    }

    private void setUpIcons() {
        btBack.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_back.png"))));
        btForward.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_forward.png"))));
        btUp.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_up.png"))));
        btPicker.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_open_folder.png"))));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tree = new JTree();
        TreeHelper treeHelper = new TreeHelper(tree, this);
        treeHelper.initTree();
    }

    @Override
    public void onTreeClicked(String newDir){
        System.out.println(newDir);
    }
}

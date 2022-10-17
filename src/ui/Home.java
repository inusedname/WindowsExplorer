package ui;

import ui.popupmenu.FolderPopClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


import javax.swing.tree.*;
import java.io.File;
public class Home implements Runnable {
    private JPanel homePanel;
    private JScrollPane scrollPane;
    private JTree treeBrowse;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private JTextField tfAddress;
    private JButton btPicker;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    public Home() {


//        File[] rootDrive = File.listRoots();
//        for(File sysDrive : rootDrive){
//            System.out.println("Drive : " + sysDrive);
//        }
        setUpUI();
    }

    @Override
    public void run(){
        JFrame frame = new JFrame("Home");



        frame.addMouseListener(new FolderPopClickListener());
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


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

        SwingUtilities.invokeLater(new Home());

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        File fileRoot = new File("C:/");
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);



        treeBrowse = new JTree(treeModel);
        treeBrowse.setShowsRootHandles(true);

        scrollPane = new JScrollPane(treeBrowse);

        CreateChildNodes ccn =
                new CreateChildNodes(fileRoot, root, treeModel);

        new Thread(ccn).start();
    }

    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        private DefaultTreeModel model;
        public CreateChildNodes(File fileRoot,
                                DefaultMutableTreeNode root, DefaultTreeModel model) {
            this.fileRoot = fileRoot;
            this.root = root;
            this.model = model;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root, model);

        }

        private void createChildren(File fileRoot,
                                    DefaultMutableTreeNode node, DefaultTreeModel model) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {

//                model.reload(node);
                if (file.isDirectory()) {
                    DefaultMutableTreeNode childNode =
                            new DefaultMutableTreeNode(new FileNode(file));
                    node.add(childNode);
                    createChildren(file, childNode, model);
                }
            }
        }

    }

    public class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }
}

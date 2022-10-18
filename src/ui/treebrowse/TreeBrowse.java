package ui.treebrowse;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class TreeBrowse {
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    public TreeBrowse(JTree tree) {
        this.tree = tree;
        File[] rootDrive = File.listRoots();
        for (File sysDrive : rootDrive) {
            System.out.println("Drive : " + sysDrive);
        }

        try {
            File fileRoot = new File(rootDrive[0].toURI());
            root = new DefaultMutableTreeNode(new FileNode(fileRoot));
            treeModel = new DefaultTreeModel(root);
            tree.setModel(treeModel);
            tree.setShowsRootHandles(true);
            CreateChildNodes ccn = new CreateChildNodes(fileRoot, root);
            new Thread(ccn).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;
        private File fileRoot;

        public CreateChildNodes(File fileRoot, DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }

        private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
                if (file.isDirectory()) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
                    node.add(childNode);
                    createChildren(file, childNode);
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



package controller.treebrowse;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class TreeHelper {
    private final JTree tree;
    private DefaultMutableTreeNode root;

    public TreeHelper(JTree tree) {
        this.tree = tree;
    }

    public void initTree() {
        File[] rootDrive = File.listRoots();
        for (File sysDrive : rootDrive) {
            System.out.println("Drive : " + sysDrive);
        }

        // TODO: Tree can show multiple Drives
        try {
            File fileRoot = new File(rootDrive[0].toURI());
            root = new DefaultMutableTreeNode(new FileNode(fileRoot));
            tree.setModel(new DefaultTreeModel(root));
            tree.setShowsRootHandles(true);

            // New node will be inserted into root
            CreateChildNodes ccn = new CreateChildNodes(fileRoot, root);
            new Thread(ccn).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class CreateChildNodes implements Runnable {

        private final DefaultMutableTreeNode root;
        private final File rootFile;

        public CreateChildNodes(File rootFile, DefaultMutableTreeNode root) {
            this.rootFile = rootFile;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(rootFile, root);
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

    private final class FileNode {
        private final File file;

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



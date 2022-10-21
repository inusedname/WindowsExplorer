package controller.treebrowse;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

public class TreeHelper {
    static String value = "";
    private final JTree tree;
    final private TreeCallbacks callbacks;
    private FileSystemView fileSystemView;
    private DefaultMutableTreeNode root;

    public TreeHelper(JTree tree, TreeCallbacks callbacks) {
        this.tree = tree;
        this.callbacks = callbacks;
    }

    public void initTree() {
        File[] rootDrive = File.listRoots();
        for (File sysDrive : rootDrive) {
            System.out.println("Drive : " + sysDrive);
        }
        try {
            fileSystemView = FileSystemView.getFileSystemView();
            root = new DefaultMutableTreeNode();

            for (File fileSystemRoot : rootDrive) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add(node);
                addChildren(node);
            }
            tree.setModel(new DefaultTreeModel(root));
            tree.setShowsRootHandles(true);
            tree.setRootVisible(false);

            // tree GUI
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.addTreeSelectionListener(e -> {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                addChildren(node);
                callbacks.onTreeClicked(node.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addChildren(final DefaultMutableTreeNode node) {
        SwingWorker<String, Object> worker = new SwingWorker<>() {
            @Override
            public String doInBackground() {
                tree.setEnabled(false);
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true);
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if (child.isDirectory()) {
                                node.add(new DefaultMutableTreeNode(child));
                            }
                        }
                    }
                }
                tree.setEnabled(true);
                return "done";
            }
        };
        worker.execute();
    }

    public interface TreeCallbacks {
        void onTreeClicked(String newDir);
    }

    class FileTreeCellRenderer extends DefaultTreeCellRenderer {

        private final FileSystemView fileSystemView;

        private final JLabel label;

        FileTreeCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
            fileSystemView = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean selected,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            File file = (File) node.getUserObject();
            if (file == null) return label;
            label.setIcon(fileSystemView.getSystemIcon(file));
            label.setText(fileSystemView.getSystemDisplayName(file));
            label.setToolTipText(file.getPath());

            if (selected) {
                label.setBackground(backgroundSelectionColor);
            } else {
                label.setBackground(backgroundNonSelectionColor);
            }

            return label;
        }
    }
}



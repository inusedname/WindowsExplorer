package controller.treebrowse;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class TreeHelper {
    private final JTree tree;
    final private TreeCallbacks callbacks;
    TreePath submitTreePath;
    private FileSystemView fileSystemView;
    private DefaultMutableTreeNode root;


    public TreeHelper(JTree tree, TreeCallbacks callbacks) {
        this.tree = tree;
        this.callbacks = callbacks;
    }

    public void initTree() {
        ((BasicTreeUI) tree.getUI()).setRightChildIndent(5);
        ((BasicTreeUI) tree.getUI()).setLeftChildIndent(3);
        tree.setToggleClickCount(1);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        File[] rootDrive = File.listRoots();
        try {
            fileSystemView = FileSystemView.getFileSystemView();
            root = new DefaultMutableTreeNode();

            for (File fileSystemRoot : rootDrive) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add(node);
            }
            tree.setModel(new DefaultTreeModel(root));
            tree.setShowsRootHandles(true);
            tree.setRootVisible(false);
            // tree GUI
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath == null)
                        return;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    if (node.isLeaf()) {
                        addChildrenThenExpand(node);
                    }
                    callbacks.onTreeItemClicked(node.toString() + "\\");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);
        File file = (File) node.getUserObject();
        if (file.isDirectory()) {
            File[] files = fileSystemView.getFiles(file, false);
            if (node.isLeaf()) {
                for (File child : files) {
                    if (child.isDirectory() && !child.isHidden()) {
                        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(child);
                        node.add(childnode);
                    }
                }
            }
        }
        tree.setEnabled(true);
    }

    private void addChildrenThenExpand(final DefaultMutableTreeNode node) {
        SwingWorker<String, Object> worker = new SwingWorker<>() {

            @Override
            public String doInBackground() {
                addChildren(node);
                return "done";
            }

            @Override
            protected void done() {
                tree.setEnabled(false);
                tree.expandPath(new TreePath(node.getPath()));
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }

    private void addChildrenThenFindNextNode(final DefaultMutableTreeNode node, String[] pathList, int idx) {
        SwingWorker<String, Object> worker = new SwingWorker<>() {
            @Override
            public String doInBackground() {
                addChildren(node);
                return "done";
            }

            @Override
            protected void done() {
                setTreePath(node, pathList, idx);
            }
        };
        worker.execute();

    }

    //from root node find scan all childNode and find one equal to path recursively
    public boolean goToPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        //Only show the closest folder (tree not show file)
        while (!file.isDirectory()) {
            file = file.getParentFile();
        }
        File tmp = file;
        int len = 1;
        while (tmp.getParentFile() != null) {
            len += 1;
            tmp = tmp.getParentFile();
        }
        String[] pathList = new String[len];
        pathList[len - 1] = file.toString();
        len -= 1;
        while (file.getParentFile() != null) {
            len -= 1;
            file = file.getParentFile();
            pathList[len] = file.toString();
        }
        setTreePath(root, pathList, 0);
        return true;
    }

    public void setTreePath(DefaultMutableTreeNode node, String[] pathList, int idx) {
        if (idx >= pathList.length) {
            submitTreePath = new TreePath(node.getPath());
            tree.expandPath(submitTreePath);
            tree.clearSelection();
            tree.addSelectionPath(submitTreePath);
            tree.scrollPathToVisible(submitTreePath);
            return;
        }
        boolean check = false;
        int nextIdx = idx;
        DefaultMutableTreeNode nextNode;
        for (int i = 0; i < node.getChildCount(); i++) {
            nextNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (nextNode.toString().equals(pathList[idx])) {
                nextIdx += 1;
                check = true;
                if (nextNode.isLeaf()) {
                    tree.setEnabled(false);
                    addChildrenThenFindNextNode(nextNode, pathList, nextIdx);
                    tree.setEnabled(true);
                } else {
                    setTreePath(nextNode, pathList, nextIdx);
                }
                break;
            }
        }
        try {
            if (!check) {
                nextNode = (DefaultMutableTreeNode) node.getFirstChild();
                setTreePath(nextNode, pathList, nextIdx);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public interface TreeCallbacks {
        void onTreeItemClicked(String newDir);
    }

    private static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
        private final FileSystemView fileSystemView;
        private final JLabel label;

        FileTreeCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
            fileSystemView = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            File file = (File) node.getUserObject();
            if (file == null) return label;
            label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
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



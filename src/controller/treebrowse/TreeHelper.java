package controller.treebrowse;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.util.regex.Matcher;

public class TreeHelper {
    private final JTree tree;
    private FileSystemView fileSystemView;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    final private CallBack callBack;
    static String value = "";

    public TreeHelper(JTree tree, CallBack callBack) {
        this.tree = tree;
        this.callBack = callBack;
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
                root.add( node );
                addChildren(node);
            }
            model = new DefaultTreeModel(root);
            tree.setModel(model);
            tree.setShowsRootHandles(true);
            tree.setRootVisible(false);
            // tree GUI
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
                    addChildren(node);
                    TreePath treepath = e.getPath();
                    Object path = treepath.getLastPathComponent();
                    callBack.onTreeClicked(path.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public interface CallBack{
        public void onTreeClicked(String newDir);
    }
    private void addChildren(final DefaultMutableTreeNode node) {
        SwingWorker worker = new SwingWorker() {
            @Override
            public String doInBackground() {
                tree.setEnabled(false);
                File file = (File)node.getUserObject();
                if ( file.isDirectory() ) {
                    File[] files = fileSystemView.getFiles(file, true);
                    if (node.isLeaf()) {
                        for (File child : files) {
                            if ( child.isDirectory() ) {
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


    class FileTreeCellRenderer extends DefaultTreeCellRenderer {

        private FileSystemView fileSystemView;

        private JLabel label;

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



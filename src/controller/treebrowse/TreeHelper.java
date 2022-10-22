package controller.treebrowse;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
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
        ((BasicTreeUI) tree.getUI()).setRightChildIndent(5);
        ((BasicTreeUI) tree.getUI()).setLeftChildIndent(3);

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
            }
            tree.setModel(new DefaultTreeModel(root));
            tree.setShowsRootHandles(true);
            tree.setRootVisible(false);
            // tree GUI
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.addTreeSelectionListener(e -> {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                addChildrenThenExpand(node);
                callbacks.onTreeClicked(node.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addChildrenThenExpand(final DefaultMutableTreeNode node) {
        SwingWorker<String, Object> worker = new SwingWorker<>() {
            boolean isLeaf = false;

            @Override
            public String doInBackground() {
                tree.setEnabled(false);
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true);
                    if (node.isLeaf()) {
                        isLeaf = true;
                        for (File child : files) {
                            if (child.isDirectory()) {
                                DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(child);
                                node.add(childnode);

                            }
                        }
                    }
                }
                tree.setEnabled(true);
                return "done";
            }

            @Override
            protected void done() {
                tree.setEnabled(false);
                if (isLeaf == true) {

                    tree.expandPath(new TreePath(node.getPath()));

                }
                tree.setEnabled(true);
            }
        };
        worker.execute();

    }

    public boolean goToPath(String path) {
        File file = new File(path);
        File tmp = file;

        int dem = 1;
        while (tmp.getParentFile() != null) {
            dem += 1;
            tmp = tmp.getParentFile();
        }
        String[] pathList = new String[dem];

        pathList[dem - 1] = file.toString();
        dem -= 1;
        while (file.getParentFile() != null) {
            dem -= 1;
            file = file.getParentFile();
            pathList[dem] = file.toString();

        }
        System.out.println(findTreePath(root, pathList, 0));
        //   tree.expandPath(findTreePath(root, pathList, 0));

//        while (true) {
//            if (curIdx >= pathList.length) break;
//            boolean check = false;
//            for (int i = 0; i < curNode.getChildCount(); i++) {
//                DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) curNode.getChildAt(i);
////                System.out.println(tmpNode.toString());
////                System.out.println(pathList[curIdx]);
////                System.out.println("lll");
//                if (tmpNode.toString().equals(pathList[curIdx])) {
//                    curNode = tmpNode;
//                    curIdx += 1;
//                    check = true;
//                    if (tmpNode.isLeaf()) {
//                        addChildrenThenExpand(tmpNode);
//                    }
//                    break;
//                }
//            }
//            if (check == false) {
//                curNode = (DefaultMutableTreeNode) curNode.getChildAt(0);
//            }
//        }
//        tree.expandPath(new TreePath(curNode.getPath()));
        return true;
    }

    private DefaultMutableTreeNode findTreePath(DefaultMutableTreeNode node, String[] pathList, int idx) {
        final DefaultMutableTreeNode[] treePath = new DefaultMutableTreeNode[1];
//        System.out.println(node);
        if (idx >= pathList.length) {
            System.out.println("bruh");
            System.out.println(node);
            return node;
        }
        SwingWorker<String, Object> worker1 = new SwingWorker<>() {

            @Override
            public String doInBackground() {
                SwingWorker<String, Object> worker = new SwingWorker<>() {
                    boolean isLeaf = false;
                    DefaultMutableTreeNode nextNode;

                    int nextIdx;

                    @Override
                    public String doInBackground() {
                        boolean check = false;
                        for (int i = 0; i < node.getChildCount(); i++) {
                            nextNode = (DefaultMutableTreeNode) node.getChildAt(i);
                            if (nextNode.toString().equals(pathList[idx])) {
                                nextIdx = idx + 1;
                                check = true;
                                if (nextNode.isLeaf()) {
                                    tree.setEnabled(false);
                                    addChildrenThenExpand(nextNode);
                                    tree.setEnabled(true);
                                }
                                break;
                            }
                        }
                        if (check == false) {
                            nextNode = (DefaultMutableTreeNode) node.getFirstChild();
                            nextIdx = idx;
                        }
                        return "done";
                    }

                    @Override
                    protected void done() {
                        treePath[0] = findTreePath(nextNode, pathList, nextIdx);
                    }
                };
                worker.execute();
                return "done";
            }
        };
        worker1.execute();
        return treePath[0];
    }

    public interface TreeCallbacks {
        void onTreeClicked(String newDir);
    }

    private class FileTreeCellRenderer extends DefaultTreeCellRenderer {

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



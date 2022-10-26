package ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import controller.table.TableHelper;
import controller.treebrowse.TreeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home implements Runnable, TreeHelper.TreeCallbacks {
    private JPanel homePanel;
    private JTree tree;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private JTextField tfAddress;
    private JButton btPicker;
    private JScrollPane leftScrollPane;

    private TreeHelper treeHelper;

    private TableHelper tableHelper;
    public Home() {
        setUpUI();
        setUpActionListeners();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Home());
    }

    private void setUpActionListeners() {
        setUpTextFieldActionListener();
    }

    private void setUpTextFieldActionListener() {
        tfAddress.addActionListener(e -> {
            String path = tfAddress.getText();
            System.out.println("Path: " + path);

            /** TODO: Check if path is valid
             * [CLEAR AFTER DONE]
             * public boolean findText(String nodes) {
             *         String[] parts = nodes.split(":");
             *         TreePath path = null;
             *         for (String part : parts) {
             *             int row = (path==null ? 0 : tree.getRowForPath(path));
             *             path = tree.getNextMatch(part, row, Position.Bias.Forward);
             *             if (path==null) {
             *                 return false;
             *             }
             *         }
             *         tree.scrollPathToVisible(path);
             *         tree.setSelectionPath(path);
             *         return path!=null;
             *     }
             *  Hàm goToPath sẽ là boolean, đầu tiên cần kiểm tra xem đường dẫn có hợp lệ không,
             *  sau đó mới thực hiện update JTree
             *  <p>
             *  Đường dẫn mới có thể trỏ tới 1 file hoặc 1 folder, nếu là folder thì sẽ cập nhật JTree
             *  in đậm folder đó thể hiện là đang chọn
             *  <p>
             *  Nếu là file thì trỏ tới folder chứa file đó, như trên
             */

            if (!treeHelper.goToPath(path)) {
                JOptionPane.showMessageDialog(homePanel, "Invalid path");
            }
        });
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Home");
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setUpUI() {

        FlatDarculaLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        homePanel.setPreferredSize(new Dimension(1280, 720));
        setUpIcons();
    }

    private void setUpIcons() {

        ImageIcon iconBack = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_back.png")));
        ImageIcon iconForward = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_forward.png")));
        ImageIcon iconUp = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_up.png")));
        ImageIcon iconPicker = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_open_folder.png")));

        btBack.setIcon(iconBack);
        btForward.setIcon(iconForward);
        btUp.setIcon(iconUp);
        btPicker.setIcon(iconPicker);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        setUpJTree();
        setUpJTable();
    }

    private void setUpJTree() {
        tree = new JTree();
        tree.setToggleClickCount(1);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        treeHelper = new TreeHelper(tree, this);
        treeHelper.initTree();
    }
    private void setUpJTable() {
        String[] columnNames = {"Name", "Age", "City"};
        Object[][] data = {{"Raja", "35", "Hyderabad"}, {"Adithya", "25", "Chennai"},
                {"Vineet", "23", "Mumbai"},
                {"Archana", "32", "Pune"},
                {"Krishna", "30", "Kolkata"}};
        tableCurrentFolder = new JTable(data, columnNames);
        tableHelper = new TableHelper(tableCurrentFolder);
        tableHelper.initTable();
    }
    @Override
    public void onTreeClicked(String newDir) {tfAddress.setText(newDir);}
}

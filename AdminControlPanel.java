package Twitter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public class AdminControlPanel {
    private static AdminControlPanel instance = null;
    private JFrame frame;
    private JTextField userIdField, groupIdField;
    private JButton addUserButton, addGroupButton, openUserViewButton, showUserTotalButton, showGroupTotalButton, showMessageTotalButton, showPosPercentageButton;
    private JTree treeView;
    private DefaultMutableTreeNode rootNode;
    private UserGroup rootGroup;
    private Map<String, User> users;
    private PropertyChangeSupport pcs;

    private AdminControlPanel() {
        users = new HashMap<>();
        pcs = new PropertyChangeSupport(this);
        frame = new JFrame("Admin Control Panel");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        rootGroup = new UserGroup("Root");
        rootNode = new DefaultMutableTreeNode(rootGroup);
        treeView = new JTree(rootNode);
        frame.add(new JScrollPane(treeView), BorderLayout.CENTER);

        JPanel northPanel = new JPanel(new GridLayout(2, 2));
        frame.add(northPanel, BorderLayout.NORTH);
     // User ID input field
        userIdField = new JTextField();
        groupIdField = new JTextField();
        northPanel.add(new JLabel("User ID:"));
        northPanel.add(userIdField);
        northPanel.add(new JLabel("Group ID:"));
        northPanel.add(groupIdField);
     // Add User button
        addUserButton = new JButton("Add User");
        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
                if (!users.containsKey(userId) && selectedNode != null && selectedNode.getUserObject() instanceof UserGroup) {
                    User newUser = new User(userId);
                    users.put(userId, newUser);
                    UserGroup selectedGroup = (UserGroup) selectedNode.getUserObject();
                    selectedGroup.addUser(newUser);
                    selectedNode.add(new DefaultMutableTreeNode(newUser));
                    ((DefaultTreeModel) treeView.getModel()).reload();
                    userIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a User Group to add the user or ensure the User ID is unique.");
                }
            }
        });
        northPanel.add(addUserButton);
        // Add Group button
        addGroupButton = new JButton("Add Group");
        addGroupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String groupId = groupIdField.getText();
                UserGroup newGroup = new UserGroup(groupId);
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getUserObject() instanceof UserGroup) {
                    UserGroup selectedGroup = (UserGroup) selectedNode.getUserObject();
                    selectedGroup.addGroup(newGroup);
                    selectedNode.add(new DefaultMutableTreeNode(newGroup));
                    ((DefaultTreeModel) treeView.getModel()).reload();
                    groupIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a User Group to add the new group.");
                }
            }
        });
        northPanel.add(addGroupButton);
     // Open User View Button
        openUserViewButton = new JButton("Open User View");
        openUserViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getUserObject() instanceof User) {
                    User selectedUser = (User) selectedNode.getUserObject();
                    UserView userView = new UserView(selectedUser);
                    userView.getFrame().setVisible(true);
                }
            }
        });
        northPanel.add(openUserViewButton);

        JPanel southPanel = new JPanel(new GridLayout(1, 4));
        frame.add(southPanel, BorderLayout.SOUTH);

        showUserTotalButton = new JButton("Show User Total");
        showUserTotalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int userCount = countUsers(rootNode);
                JOptionPane.showMessageDialog(frame, "Total users: " + userCount);
            }
        });
        southPanel.add(showUserTotalButton);
     // Show User Total button
        showGroupTotalButton = new JButton("Show Group Total");
        showGroupTotalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int groupCount = countGroups(rootNode);
                JOptionPane.showMessageDialog(frame, "Total groups: " + groupCount);
            }
        });
        southPanel.add(showGroupTotalButton);
        // show message button
        showMessageTotalButton = new JButton("Show Message Total");
        showMessageTotalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int messageCount = countMessages(rootNode);
                JOptionPane.showMessageDialog(frame, "Total messages: " + messageCount);
            }
        });
        southPanel.add(showMessageTotalButton);
        // show positive percentage button
        showPosPercentageButton = new JButton("Show Positive Percentage");
        showPosPercentageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double[] posPercentage = calculatePositivePercentage(rootNode);
                JOptionPane.showMessageDialog(frame, "Positive messages percentage: " + (posPercentage[1] / posPercentage[0]) * 100 + "%");

            }
        });
        southPanel.add(showPosPercentageButton);
        
        Color lightBlue = new Color(135, 206, 235);  // Created a lighter shade of blue so it looks like twitter
   
        addUserButton = new JButton("Add User");
  
        frame.getContentPane().setBackground(lightBlue);
        
        // Set the background color of panels to blue
        northPanel.setBackground(lightBlue);
        southPanel.setBackground(lightBlue);
        
        // Set the background color of text fields to blue
        userIdField.setBackground(lightBlue);
        groupIdField.setBackground(lightBlue);
        
        // After initializing, set the background color of buttons to blue
        addUserButton.setBackground(lightBlue);
        addGroupButton.setBackground(lightBlue);
        openUserViewButton.setBackground(lightBlue);
        showUserTotalButton.setBackground(lightBlue);
        showGroupTotalButton.setBackground(lightBlue);
        showMessageTotalButton.setBackground(lightBlue);
        showPosPercentageButton.setBackground(lightBlue);

        frame.setVisible(true);
    }

    public static AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }
    //initializa GUI
    public void initializeGUI() {
        frame.setVisible(true);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public void addUser(String id) {
        if (!users.containsKey(id)) {
            User user = new User(id);
            users.put(id, user);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    private int countUsers(DefaultMutableTreeNode node) {
        int count = 0;
        if (node.getUserObject() instanceof User) {
            count++;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            count += countUsers((DefaultMutableTreeNode) node.getChildAt(i));
        }
        return count;
    }

    private int countGroups(DefaultMutableTreeNode node) {
        int count = 0;
        if (node.getUserObject() instanceof UserGroup) {
            count++;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            count += countGroups((DefaultMutableTreeNode) node.getChildAt(i));
        }
        return count;
    }

    private int countMessages(DefaultMutableTreeNode node) {
        int count = 0;
        if (node.getUserObject() instanceof User) {
            User user = (User) node.getUserObject();
            count += user.getNewsFeed().size();
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            count += countMessages((DefaultMutableTreeNode) node.getChildAt(i));
        }
        return count;
    }

    private double[] calculatePositivePercentage(DefaultMutableTreeNode node) {
        int totalMessages = 0;
        int positiveMessages = 0;
        if (node.getUserObject() instanceof User) {
            User user = (User) node.getUserObject();
            for (String message : user.getNewsFeed()) {
                totalMessages++;
                if (isPositive(message)) {
                    positiveMessages++;
                }
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            double[] results = calculatePositivePercentage((DefaultMutableTreeNode) node.getChildAt(i));
            totalMessages += results[0];
            positiveMessages += results[1];
        }
        return new double[]{totalMessages, positiveMessages};
    }
    //words used in order to determine whether a message is positive 
    private boolean isPositive(String message) {
        String[] positiveWords = {"good", "happy", "love", "great", "excellent", "amazing", "fantastic", "positive", "joy", "pleasure"};

        String[] messageWords = message.toLowerCase().split("\\W+");  // splits the message into words by non-word characters

        for (String word : messageWords) {
            for (String positiveWord : positiveWords) {
                if (word.equals(positiveWord)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Object getRootUserGroup() {
        return this.rootGroup;
    }
}

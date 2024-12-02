import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SportsManagementApp extends JFrame {
    private JTabbedPane mainTabbedPane;
    private LogisticsPanel logisticsPanel;
    private MarketingPanel marketingPanel;
    private PRMediaPanel prMediaPanel;
    private SponsorshipPanel sponsorshipPanel;
    private RegistrationsPanel registrationsPanel;

    public SportsManagementApp() {
        setTitle("Sports Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainTabbedPane = new JTabbedPane();

        logisticsPanel = new LogisticsPanel();
        marketingPanel = new MarketingPanel();
        prMediaPanel = new PRMediaPanel();
        sponsorshipPanel = new SponsorshipPanel();
        registrationsPanel = new RegistrationsPanel();

        mainTabbedPane.addTab("Logistics", logisticsPanel);
        mainTabbedPane.addTab("Marketing", marketingPanel);
        mainTabbedPane.addTab("PR & Media", prMediaPanel);
        mainTabbedPane.addTab("Sponsorship", sponsorshipPanel);
        mainTabbedPane.addTab("Registrations", registrationsPanel);

        add(mainTabbedPane);
    }

    class LogisticsPanel extends JPanel {
        private JTable inventoryTable;
        private JTable venueTable;
        private JTable transportTable;
        private JPanel rightPanel;
        private JList<String> menuList;

        public LogisticsPanel() {
            setLayout(new BorderLayout());

            String[] menuItems = {"Inventory", "Venues", "Transport"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18)); 
            menuList.setFixedCellHeight(50); 
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            rightPanel = new JPanel(new BorderLayout());

            inventoryTable = createTable(
                    new String[]{"Item", "Quantity", "Location", "Status"},
                    new Object[][]{
                            {"Jerseys", 100, "Warehouse A", "In Stock"},
                            {"Footballs", 50, "Equipment Room", "Low Stock"}
                    }
            );
            venueTable = createTable(
                    new String[]{"Venue", "Date", "Event", "Status"},
                    new Object[][]{
                            {"Main Stadium", "2024-07-15", "Summer Tournament", "Booked"},
                            {"Training Ground", "2024-06-20", "Team Practice", "Available"}
                    }
            );
            transportTable = createTable(
                    new String[]{"Vehicle", "Route", "Date", "Status"},
                    new Object[][]{
                            {"Bus 1", "Team Pickup", "2024-07-10", "Scheduled"},
                            {"Van 2", "Equipment Transport", "2024-07-12", "Pending"}
                    }
            );

            menuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    switch (selectedMenu) {
                        case "Inventory" -> updateRightPanel(inventoryTable, "Inventory");
                        case "Venues" -> updateRightPanel(venueTable, "Venues");
                        case "Transport" -> updateRightPanel(transportTable, "Transport");
                    }
                }
            });

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200); 

            add(splitPane, BorderLayout.CENTER);
        }

        private JTable createTable(String[] columns, Object[][] data) {
            return new JTable(new DefaultTableModel(data, columns));
        }

        private void updateRightPanel(JTable table, String title) {
            rightPanel.removeAll();
            rightPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add " + title);
            addButton.addActionListener(e -> addRow(table, title));
            buttonPanel.add(addButton);

            JButton updateButton = new JButton("Update " + title);
            updateButton.addActionListener(e -> updateRow(table, title));
            buttonPanel.add(updateButton);

            JButton deleteButton = new JButton("Delete Row");
            deleteButton.addActionListener(e -> deleteRow(table));
            buttonPanel.add(deleteButton);

            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            rightPanel.revalidate();
            rightPanel.repaint();
        }

        private void addRow(JTable table, String tabName) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField();
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                Object[] newRow = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    newRow[i] = fields[i].getText().trim();
                }
                model.addRow(newRow);
            }
        }

        private void updateRow(JTable table, String tabName) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField(model.getValueAt(selectedRow, i).toString());
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < columnCount; i++) {
                    model.setValueAt(fields[i].getText().trim(), selectedRow, i);
                }
            }
        }

        private void deleteRow(JTable table) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(selectedRow);
        }
    }
    class MarketingPanel extends JPanel {
        private JTable campaignTable;
        private JTable contentCalendar;
        private JPanel rightPanel;
        private JList<String> menuList;

        public MarketingPanel() {
            setLayout(new BorderLayout());

            String[] menuItems = {"Campaigns", "Content Calendar"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            campaignTable = createTable(
                    new String[]{"Campaign", "Start Date", "End Date", "Budget", "Status"},
                    new Object[][]{
                            {"Summer Sports Promo", "2024-06-01", "2024-08-31", "$50,000", "Active"},
                            {"Youth Engagement", "2024-09-01", "2024-11-30", "$30,000", "Planned"}
                    }
            );
            contentCalendar = createTable(
                    new String[]{"Content", "Platform", "Scheduled Date", "Status"},
                    new Object[][]{
                            {"Athlete Highlight", "Instagram", "2024-07-15", "Pending"},
                            {"Event Teaser", "Facebook", "2024-07-20", "Draft"}
                    }
            );

            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    if ("Campaigns".equals(selectedMenu)) {
                        updateRightPanel(campaignTable, "Campaigns");
                    } else if ("Content Calendar".equals(selectedMenu)) {
                        updateRightPanel(contentCalendar, "Content Calendar");
                    }
                }
            });

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);
        }

        private JTable createTable(String[] columns, Object[][] data) {
            return new JTable(new DefaultTableModel(data, columns));
        }

        private void updateRightPanel(JTable table, String title) {
            rightPanel.removeAll();
            rightPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add " + title);
            addButton.addActionListener(e -> addRow(table, title));
            buttonPanel.add(addButton);

            JButton updateButton = new JButton("Update " + title);
            updateButton.addActionListener(e -> updateRow(table, title));
            buttonPanel.add(updateButton);

            JButton deleteButton = new JButton("Delete Row");
            deleteButton.addActionListener(e -> deleteRow(table));
            buttonPanel.add(deleteButton);

            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            rightPanel.revalidate();
            rightPanel.repaint();
        }

        private void addRow(JTable table, String tabName) {
        }

        private void updateRow(JTable table, String tabName) {
        }

        private void deleteRow(JTable table) {
        }
    }




    class PRMediaPanel extends JPanel {
        private JTable pressReleaseTable;
        private JTable socialMediaTable;
        private JTable mediaAssetsTable;
        private JPanel rightPanel;
        private JList<String> menuList;

        public PRMediaPanel() {
            setLayout(new BorderLayout());

            String[] menuItems = {"Press Releases", "Social Media Posts", "Media Assets"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            pressReleaseTable = createTable(
                    new String[]{"Title", "Date", "Media Outlets", "Status"},
                    new Object[][]{
                            {"Tournament Announcement", "2024-06-15", "Sports Media", "Sent"},
                            {"Team Achievement", "2024-07-01", "Local Press", "Draft"}
                    }
            );
            socialMediaTable = createTable(
                    new String[]{"Platform", "Post Content", "Scheduled Date", "Status"},
                    new Object[][]{
                            {"Twitter", "Upcoming Event Details", "2024-07-10", "Scheduled"},
                            {"Instagram", "Behind-the-Scenes", "2024-07-15", "Draft"}
                    }
            );
            mediaAssetsTable = createTable(
                    new String[]{"Asset Type", "Filename", "Upload Date", "Category"},
                    new Object[][]{
                            {"Photo", "team_photo.jpg", "2024-06-20", "Team"},
                            {"Video", "highlights_reel.mp4", "2024-07-05", "Event"}
                    }
            );

            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    if ("Press Releases".equals(selectedMenu)) {
                        updateRightPanel(pressReleaseTable, "Press Releases");
                    } else if ("Social Media Posts".equals(selectedMenu)) {
                        updateRightPanel(socialMediaTable, "Social Media Posts");
                    } else if ("Media Assets".equals(selectedMenu)) {
                        updateRightPanel(mediaAssetsTable, "Media Assets");
                    }
                }
            });

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);
        }

        private JTable createTable(String[] columns, Object[][] data) {
            return new JTable(new DefaultTableModel(data, columns));
        }

        private void updateRightPanel(JTable table, String title) {
            rightPanel.removeAll();
            rightPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add " + title);
            addButton.addActionListener(e -> addRow(table, title));
            buttonPanel.add(addButton);

            JButton updateButton = new JButton("Update " + title);
            updateButton.addActionListener(e -> updateRow(table, title));
            buttonPanel.add(updateButton);

            JButton deleteButton = new JButton("Delete Row");
            deleteButton.addActionListener(e -> deleteRow(table));
            buttonPanel.add(deleteButton);

            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            rightPanel.revalidate();
            rightPanel.repaint();
        }

        private void addRow(JTable table, String tabName) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField();
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                Object[] newRow = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    newRow[i] = fields[i].getText().trim();
                }
                model.addRow(newRow);
            }
        }

        private void updateRow(JTable table, String tabName) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField(model.getValueAt(selectedRow, i).toString());
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < columnCount; i++) {
                    model.setValueAt(fields[i].getText().trim(), selectedRow, i);
                }
            }
        }

        private void deleteRow(JTable table) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(selectedRow);
        }
    }

    class SponsorshipPanel extends JPanel {
        private JTable sponsorDetailsTable;
        private JTable sponsorLeadsTable;
        private JTable benefitsTrackerTable;
        private JPanel rightPanel;
        private JList<String> menuList;

        public SponsorshipPanel() {
            setLayout(new BorderLayout());

            String[] menuItems = {"Sponsor Details", "Sponsor Leads", "Benefits Tracker"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            sponsorDetailsTable = createTable(
                    new String[]{"Sponsor Name", "Contact", "Contract Value", "Duration"},
                    new Object[][]{
                            {"SportsTech Inc", "John Doe", "$100,000", "2024-2026"},
                            {"HealthWear Brands", "Jane Smith", "$75,000", "2024-2025"}
                    }
            );
            sponsorLeadsTable = createTable(
                    new String[]{"Potential Sponsor", "Contact Person", "Initial Interest", "Follow-up Date"},
                    new Object[][]{
                            {"Tech Innovations", "Mike Johnson", "High", "2024-08-15"},
                            {"Fitness Gear Co", "Sarah Lee", "Medium", "2024-07-30"}
                    }
            );
            benefitsTrackerTable = createTable(
                    new String[]{"Sponsor", "Benefit Type", "Deliverables", "Status"},
                    new Object[][]{
                            {"SportsTech Inc", "Logo Placement", "Website, Jersey", "Completed"},
                            {"HealthWear Brands", "Social Media", "Monthly Posts", "Ongoing"}
                    }
            );

            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    if ("Sponsor Details".equals(selectedMenu)) {
                        updateRightPanel(sponsorDetailsTable, "Sponsor Details");
                    } else if ("Sponsor Leads".equals(selectedMenu)) {
                        updateRightPanel(sponsorLeadsTable, "Sponsor Leads");
                    } else if ("Benefits Tracker".equals(selectedMenu)) {
                        updateRightPanel(benefitsTrackerTable, "Benefits Tracker");
                    }
                }
            });

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);
        }

        private JTable createTable(String[] columns, Object[][] data) {
            return new JTable(new DefaultTableModel(data, columns));
        }

        private void updateRightPanel(JTable table, String title) {
            rightPanel.removeAll();
            rightPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add " + title);
            addButton.addActionListener(e -> addRow(table, title));
            buttonPanel.add(addButton);

            JButton updateButton = new JButton("Update " + title);
            updateButton.addActionListener(e -> updateRow(table, title));
            buttonPanel.add(updateButton);

            JButton deleteButton = new JButton("Delete Row");
            deleteButton.addActionListener(e -> deleteRow(table));
            buttonPanel.add(deleteButton);

            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            rightPanel.revalidate();
            rightPanel.repaint();
        }

        private void addRow(JTable table, String tabName) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField();
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                Object[] newRow = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    newRow[i] = fields[i].getText().trim();
                }
                model.addRow(newRow);
            }
        }

        private void updateRow(JTable table, String tabName) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField(model.getValueAt(selectedRow, i).toString());
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < columnCount; i++) {
                    model.setValueAt(fields[i].getText().trim(), selectedRow, i);
                }
            }
        }

        private void deleteRow(JTable table) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(selectedRow);
        }
    }


    class RegistrationsPanel extends JPanel {
        private JTable participantTable;
        private JTable paymentTable;
        private JPanel rightPanel;
        private JList<String> menuList;

        public RegistrationsPanel() {
            setLayout(new BorderLayout());

            String[] menuItems = {"Participants", "Payments", "ID Cards"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            participantTable = createTable(
                    new String[]{"Name", "Event", "Category", "Registration Status"},
                    new Object[][]{
                            {"Alex Johnson", "Summer Tournament", "Amateur", "Confirmed"},
                            {"Maria Rodriguez", "Youth Championship", "Junior", "Pending"}
                    }
            );
            paymentTable = createTable(
                    new String[]{"Participant", "Amount", "Payment Method", "Status"},
                    new Object[][]{
                            {"Alex Johnson", "$100", "Credit Card", "Verified"},
                            {"Maria Rodriguez", "$50", "Bank Transfer", "Pending"}
                    }
            );

            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    if ("Participants".equals(selectedMenu)) {
                        updateRightPanel(participantTable, "Participants");
                    } else if ("Payments".equals(selectedMenu)) {
                        updateRightPanel(paymentTable, "Payments");
                    } else if ("ID Cards".equals(selectedMenu)) {
                        rightPanel.removeAll();
                        rightPanel.add(new JLabel("ID Card Generation", JLabel.CENTER), BorderLayout.NORTH);
                        JButton generateButton = new JButton("Generate ID Cards");
                        generateButton.addActionListener(actionEvent -> generateIDCards());
                        rightPanel.add(generateButton, BorderLayout.CENTER);
                        rightPanel.revalidate();
                        rightPanel.repaint();
                    }
                }
            });

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);
        }

        private JTable createTable(String[] columns, Object[][] data) {
            return new JTable(new DefaultTableModel(data, columns));
        }

        private void updateRightPanel(JTable table, String title) {
            rightPanel.removeAll();
            rightPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add " + title);
            addButton.addActionListener(e -> addRow(table, title));
            buttonPanel.add(addButton);

            JButton updateButton = new JButton("Update " + title);
            updateButton.addActionListener(e -> updateRow(table, title));
            buttonPanel.add(updateButton);

            JButton deleteButton = new JButton("Delete Row");
            deleteButton.addActionListener(e -> deleteRow(table));
            buttonPanel.add(deleteButton);

            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            rightPanel.revalidate();
            rightPanel.repaint();
        }

        private void generateIDCards() {
            JOptionPane.showMessageDialog(this,
                    "ID Card Generation Functionality\n" +
                            "- Selects registered participants\n" +
                            "- Creates personalized ID cards\n" +
                            "- Supports printing and digital formats",
                    "ID Card Generation",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void addRow(JTable table, String tabName) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField();
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                Object[] newRow = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    newRow[i] = fields[i].getText().trim();
                }
                model.addRow(newRow);
            }
        }

        private void updateRow(JTable table, String tabName) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int columnCount = model.getColumnCount();

            JTextField[] fields = new JTextField[columnCount];
            JPanel inputPanel = new JPanel(new GridLayout(columnCount, 2));

            for (int i = 0; i < columnCount; i++) {
                inputPanel.add(new JLabel(model.getColumnName(i) + ":"));
                fields[i] = new JTextField(model.getValueAt(selectedRow, i).toString());
                inputPanel.add(fields[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update " + tabName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < columnCount; i++) {
                    model.setValueAt(fields[i].getText().trim(), selectedRow, i);
                }
            }
        }

        private void deleteRow(JTable table) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(selectedRow);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            SportsManagementApp app = new SportsManagementApp();
            app.setVisible(true);
        });
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SportsManagementApp extends JFrame {
    private JPanel logisticsPanel, marketingPanel, prMediaPanel, sponsorshipPanel, dashboardPanel;
    private JPanel registrationsPanel;
    public SportsManagementApp() {
        setTitle("Sports Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Panels
        logisticsPanel = new LogisticsPanel(this); // Pass the current instance to panels
        marketingPanel = new MarketingPanel(this);
        prMediaPanel = new PRMediaPanel(this);
        sponsorshipPanel = new SponsorshipPanel(this);
        registrationsPanel = new RegistrationsPanel(this);
        // Dashboard Panel
        dashboardPanel = new DashboardScreen(this);

        // Initially show the dashboard
        setContentPane(dashboardPanel);

        setVisible(true);
    }

    // Method to switch panels
    public void navigateToPanel(String panelName) {
        JPanel targetPanel = switch (panelName) {
            case "Logistics" -> logisticsPanel;
            case "Marketing" -> marketingPanel;
            case "PR & Media" -> prMediaPanel;
            case "Sponsorship" -> sponsorshipPanel;
            case "Registrations" -> registrationsPanel;
            case "Dashboard" -> dashboardPanel;  // Add case for dashboard
            default -> null;
        };

        if (targetPanel != null) {
            setContentPane(targetPanel);
            revalidate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Panel not found: " + panelName, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class LogisticsPanel extends JPanel {
        private JTable inventoryTable;
        private JTable venueTable;
        private JTable transportTable;
        private JPanel rightPanel;
        private JList<String> menuList;
        private SportsManagementApp parentFrame;
        private JButton backButton;

        public LogisticsPanel(SportsManagementApp parentFrame) {
            this.parentFrame = parentFrame; // Save the reference to the parent frame
            setLayout(new BorderLayout());

            // Initialize menu list
            String[] menuItems = {"Inventory", "Venues", "Transport"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Create tables
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

            // Right panel for showing tables
            rightPanel = new JPanel(new BorderLayout());
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

            // Split pane for menu and right panel
            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);

            // Back to Dashboard button
            backButton = new JButton("Back to Dashboard");
            backButton.addActionListener(e -> parentFrame.navigateToPanel("Dashboard"));

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);
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
        private SportsManagementApp parentFrame;
        private JButton backButton;

        public MarketingPanel(SportsManagementApp parentFrame) {
            this.parentFrame = parentFrame; // Save the reference to parent frame
            setLayout(new BorderLayout());

            // Initialize menu list
            String[] menuItems = {"Campaigns", "Content Calendar"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Create tables
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

            // Right panel for showing tables
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

            // Split pane for menu and right panel
            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);

            // Back to Dashboard button
            backButton = new JButton("Back to Dashboard");
            backButton.addActionListener(e -> parentFrame.navigateToPanel("Dashboard"));

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);
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

    class PRMediaPanel extends JPanel {
        private JTable pressReleaseTable;
        private JTable socialMediaTable;
        private JTable mediaAssetsTable;
        private JPanel rightPanel;
        private JList<String> menuList;
        private SportsManagementApp parentFrame;
        private JButton backButton;

        public PRMediaPanel(SportsManagementApp parentFrame) {
            this.parentFrame = parentFrame; // Save the reference to the parent frame
            setLayout(new BorderLayout());

            // Initialize menu list
            String[] menuItems = {"Press Releases", "Social Media Posts", "Media Assets"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Create tables
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

            // Right panel for showing tables
            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    switch (selectedMenu) {
                        case "Press Releases" -> updateRightPanel(pressReleaseTable, "Press Releases");
                        case "Social Media Posts" -> updateRightPanel(socialMediaTable, "Social Media Posts");
                        case "Media Assets" -> updateRightPanel(mediaAssetsTable, "Media Assets");
                    }
                }
            });

            // Split pane for menu and right panel
            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);

            // Back to Dashboard button
            backButton = new JButton("Back to Dashboard");
            backButton.addActionListener(e -> parentFrame.navigateToPanel("Dashboard"));

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);
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
        private SportsManagementApp parentFrame;
        private JButton backButton;

        public SponsorshipPanel(SportsManagementApp parentFrame) {
            this.parentFrame = parentFrame; // Save the reference to parent frame
            setLayout(new BorderLayout());

            // Initialize the menu list
            String[] menuItems = {"Sponsor Details", "Sponsor Leads", "Benefits Tracker"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Create the tables
            sponsorDetailsTable = createTable(
                    new String[]{"Sponsor Name", "Contact", "Contract Value", "Duration"},
                    new Object[][]{
                            {"SportsTech Inc", "Tapas Balu", "$100,000", "2024-2026"},
                            {"HealthWear Brands", "Tartineni", "$75,000", "2024-2025"}
                    }
            );
            sponsorLeadsTable = createTable(
                    new String[]{"Potential Sponsor", "Contact Person", "Initial Interest", "Follow-up Date"},
                    new Object[][]{
                            {"Tech Innovations", "Gagan", "High", "2024-08-15"},
                            {"Fitness Gear Co", "Priyanka", "Medium", "2024-07-30"}
                    }
            );
            benefitsTrackerTable = createTable(
                    new String[]{"Sponsor", "Benefit Type", "Deliverables", "Status"},
                    new Object[][]{
                            {"SportsTech Inc", "Logo Placement", "Website, Jersey", "Completed"},
                            {"HealthWear Brands", "Social Media", "Monthly Posts", "Ongoing"}
                    }
            );

            // Right panel for showing tables
            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    switch (selectedMenu) {
                        case "Sponsor Details" -> updateRightPanel(sponsorDetailsTable, "Sponsor Details");
                        case "Sponsor Leads" -> updateRightPanel(sponsorLeadsTable, "Sponsor Leads");
                        case "Benefits Tracker" -> updateRightPanel(benefitsTrackerTable, "Benefits Tracker");
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

            // Back to Dashboard button
            backButton = new JButton("Back to Dashboard");
            backButton.addActionListener(e -> parentFrame.navigateToPanel("Dashboard"));

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);
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
        private SportsManagementApp parentFrame;
        private JButton backButton;

        public RegistrationsPanel(SportsManagementApp parentFrame) {
            this.parentFrame = parentFrame; // Save the reference to the parent frame
            setLayout(new BorderLayout());

            // Initialize menu list
            String[] menuItems = {"Participants", "Payments", "ID Cards"};
            menuList = new JList<>(menuItems);
            menuList.setFont(new Font("Arial", Font.BOLD, 18));
            menuList.setFixedCellHeight(50);
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Create tables
            participantTable = createTable(
                    new String[]{"Name", "Event", "Category", "Registration Status"},
                    new Object[][]{
                            {"Sujala", "Summer Tournament", "Amateur", "Confirmed"},
                            {"Maria", "Youth Championship", "Junior", "Pending"}
                    }
            );
            paymentTable = createTable(
                    new String[]{"Participant", "Amount", "Payment Method", "Status"},
                    new Object[][]{
                            {"Sujala", "$100", "Credit Card", "Verified"},
                            {"Maria", "$50", "Bank Transfer", "Pending"}
                    }
            );

            // Right panel for displaying content
            rightPanel = new JPanel(new BorderLayout());
            menuList.addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting()) {
                    String selectedMenu = menuList.getSelectedValue();
                    switch (selectedMenu) {
                        case "Participants" -> updateRightPanel(participantTable, "Participants");
                        case "Payments" -> updateRightPanel(paymentTable, "Payments");
                        case "ID Cards" -> showIDCardGeneration();
                    }
                }
            });

            // Split pane for menu and right panel
            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(menuList),
                    rightPanel
            );
            splitPane.setDividerLocation(200);
            add(splitPane, BorderLayout.CENTER);

            // Back to Dashboard button
            backButton = new JButton("Back to Dashboard");
            backButton.addActionListener(e -> parentFrame.navigateToPanel("Dashboard"));

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backButton);
            add(bottomPanel, BorderLayout.SOUTH);
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

        private void showIDCardGeneration() {
            rightPanel.removeAll();
            rightPanel.add(new JLabel("ID Card Generation", JLabel.CENTER), BorderLayout.NORTH);

            JButton generateButton = new JButton("Generate ID Cards");
            generateButton.addActionListener(actionEvent -> generateIDCards());
            rightPanel.add(generateButton, BorderLayout.CENTER);

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

            // Launch the login page
            new LoginPage(() -> {
                // Launch the Sports Management App after successful login
                SportsManagementApp app = new SportsManagementApp();
                app.setVisible(true);
            });
        });
    }}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardScreen extends JPanel {
    private SportsManagementApp parentFrame;

    public DashboardScreen(SportsManagementApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());

        // Title at the top
        JLabel titleLabel = new JLabel("Sports Management Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel with navigation buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        buttonPanel.add(createDashboardButton("Logistics", "Logistics"));
        buttonPanel.add(createDashboardButton("Marketing", "Marketing"));
        buttonPanel.add(createDashboardButton("PR & Media", "PR & Media"));
        buttonPanel.add(createDashboardButton("Sponsorship", "Sponsorship"));
        buttonPanel.add(createDashboardButton("Registrations", "Registrations"));

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createDashboardButton(String title, String targetPanelName) {
        JButton button = new JButton(title);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(e -> navigateToPanel(targetPanelName));
        return button;
    }

    private void navigateToPanel(String panelName) {
        // Use the parentFrame instance to call navigateToPanel method
        parentFrame.navigateToPanel(panelName);
    }
}

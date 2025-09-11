import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    public StartPanel(AnimationPanel gamePanel) {
        setOpaque(true);
        setBackground(new Color(34, 206, 235, 230)); // Slightly transparent sky blue
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Bouncing Balls");
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setForeground(new Color(34, 139, 34));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 36));
        add(startButton, gbc);

        startButton.addActionListener(e -> {
            setVisible(false);
            gamePanel.setVisible(true);
            gamePanel.requestFocusInWindow();
        });

        // Add quit button with icon (top left)
        JButton quitButton = new JButton();
        try {
            ImageIcon quitIcon = new ImageIcon("quitGame.png");
            Image scaledQuit = quitIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            quitButton.setIcon(new ImageIcon(scaledQuit));
        } catch (Exception ex) {
            quitButton.setText("Quit");
        }
        quitButton.setToolTipText("Quit Game");
        quitButton.setBounds(10, 10, 56, 56);
        quitButton.setFocusPainted(false);
        quitButton.setContentAreaFilled(false);
        quitButton.setBorderPainted(false);
        quitButton.addActionListener(e -> {
            System.exit(0);
        });
        setLayout(null); // Switch to null layout for absolute positioning
        // Re-add start button and title with manual positioning
        title.setBounds(0, 120, 1200, 100);
        startButton.setBounds(500, 300, 200, 80);
        add(title);
        add(startButton);
        add(quitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Optionally, draw more decorations here
    }
}
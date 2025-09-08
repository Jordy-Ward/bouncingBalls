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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Optionally, draw more decorations here
    }
}
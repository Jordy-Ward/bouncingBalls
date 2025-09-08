import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    public StartPanel(AnimationPanel gamePanel) {
        setOpaque(true);
        setBackground(new Color(34, 206, 235, 230)); // Slightly transparent sky blue
        setLayout(null);

        JLabel title = new JLabel("Bouncing Balls");
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setForeground(new Color(34, 139, 34));
        title.setBounds(0, 120, 1200, 100);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 36));
        startButton.setBounds(500, 300, 200, 80);
        add(startButton);

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
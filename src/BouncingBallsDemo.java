import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class BouncingBallsDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bouncing Balls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); //null sets the frame center of screen

        AnimationPanel panel = new AnimationPanel();
        frame.add(panel);

        JButton shootButton = new JButton("Fire!");
        shootButton.addActionListener(e -> panel.fireBall());
        frame.add(shootButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}

class AnimationPanel extends JPanel {

    private boolean ballFired = false;
    private double x, y, vx, vy;
    private int ballRadius = 15;
    private final double gravity = 0.27;
    private final double energyLoss = 0.8;
    private ImageIcon cannIcon;
    private Image scaledCannon;
    private javax.swing.Timer timer;

    public AnimationPanel() {
        setBackground(Color.WHITE);
        cannIcon = new ImageIcon("cannon.png");
        int cannonWidth = 120;
        int cannonHeight = 90;
        scaledCannon = cannIcon.getImage().getScaledInstance(cannonWidth, cannonHeight, Image.SCALE_SMOOTH);
    }

    public void fireBall() {
        // if (ballFired) return;

        if (getWidth() > 0 && getHeight() > 0) {
            int cannonX = 10;
            int cannonWidth = 120;
            int cannonHeight = 90;
            int cannonY = getHeight() / 3 - cannonHeight / 2;
            x = cannonX + cannonWidth - ballRadius;
            y = cannonY + cannonHeight / 3 - ballRadius;

            vx = 10;
            vy = -8;
            ballFired = true;

            timer = new javax.swing.Timer(16, e -> {

                vy += gravity;
                x += vx;
                y += vy;
                //if ball hits left wall
                if (x < 0) {
                    x = 0;
                    vx = -vx * energyLoss;
                }
                //if ball hits right wall
                if (x + ballRadius * 2 > getWidth()) {
                    x = getWidth() - ballRadius * 2;
                    vx = -vx * energyLoss;
                }
                //if ball hits top wall
                if (y < 0) {
                    y = 0;
                    vy = -vy * energyLoss;
                }
                //if ball hits bottom wall
                if (y > getHeight()) {
                    y = getHeight() - ballRadius * 2;
                    vy = -vy * energyLoss;
                    //if velocity very small stop bounce
                    if (Math.abs(vy) < 0.8) {
                        vy = 0;
                    }
                }
                repaint();
            });
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        //draw the scaled cannon left edge centered vertically
        int cannonX = 10;
        int cannonY = getHeight() / 3 - 90 / 2;
        graphics.drawImage(scaledCannon, cannonX, cannonY, this);

        if (ballFired) {
            graphics.setColor(Color.RED);
            graphics.fillOval((int)x, (int)y, ballRadius * 2, ballRadius * 2);
        }
    }

}
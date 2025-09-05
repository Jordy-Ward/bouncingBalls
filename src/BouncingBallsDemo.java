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
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null); //null sets the frame center of screen

        AnimationPanel panel = new AnimationPanel();
        frame.add(panel);

        JButton shootButton = new JButton("Fire!");
        shootButton.addActionListener(e -> panel.fireBallWithAim());
        frame.add(shootButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}

class AnimationPanel extends JPanel {

    private boolean ballFired = false;
    private boolean aiming = false;
    private double x, y, vx, vy;
    private int ballRadius = 15;
    private final double gravity = 0.8;
    private final double energyLoss = 0.8;
    private ImageIcon cannonIcon;
    private Image scaledCannon;
    private javax.swing.Timer timer;
    private int aimStartX, aimStartY, aimEndX, aimEndY;

    public AnimationPanel() {
        setBackground(Color.WHITE);
        cannonIcon = new ImageIcon("cannon.png");
        int cannonWidth = 120;
        int cannonHeight = 90;
        scaledCannon = cannonIcon.getImage().getScaledInstance(cannonWidth, cannonHeight, Image.SCALE_SMOOTH);

        //mouse listeners for frag and aim
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!ballFired) {
                    aiming = true;
                    aimStartX = e.getX();
                    aimStartY = e.getY();
                    aimEndX = aimStartX;
                    aimEndY = aimStartY;
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (aiming && !ballFired) {
                    aimEndX = e.getX();
                    aimEndY = e.getY();
                    fireBallWithAim();
                    aiming = false;
                }
            } 
        });

        addMouseMotionListener(new java.awt.event.MouseMotionListener() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (aiming && !ballFired) {
                    aimEndX = e.getX();
                    aimEndY = e.getY();
                    repaint();
                }
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                // No action needed for mouseMoved in this context
            }
        });
    }

    private int cannonX() { return 180; }
    private int cannonWidth() { return 120; }
    private int cannonHeight() { return 90; }
    private int cannonY() { return getHeight() / 3 - cannonHeight() / 2;}

    public void fireBallWithAim() {
        
        int tipX = cannonX() + cannonWidth();
        int tipY = cannonY() + cannonHeight() / 4;
        x = tipX - ballRadius;
        y = tipY - ballRadius;

        //calculate velocity
        double dx = aimStartX - aimEndX;
        double dy = aimStartY - aimEndY;
        double scale = 0.1; //speed sensitivity

        vx = dx * scale;
        vy = dy * scale;
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
                if (y + ballRadius * 2 > getHeight()) {
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

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        //draw the scaled cannon left edge centered vertically
        int cannonX = cannonX();
        int cannonY = cannonY();
        graphics.drawImage(scaledCannon, cannonX, cannonY, this);

        if (!ballFired && aiming) {
            //drwa line
            graphics.setColor(Color.BLUE);
            graphics.drawLine(aimStartX, aimStartY, aimEndX, aimEndY);
            //draw ball at cannon
            int tipX = cannonX() + cannonWidth();
            int tipY = cannonY() + cannonHeight() / 4;
            graphics.setColor(Color.RED);
            graphics.fillOval(tipX - ballRadius, tipY - ballRadius, ballRadius * 2, ballRadius * 2);
        }

        if (ballFired) {
            
            graphics.setColor(Color.RED);
            graphics.fillOval((int)x, (int)y, ballRadius * 2, ballRadius * 2);
        }
    }

}
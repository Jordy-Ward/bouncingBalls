import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;;

public class BouncingBallsDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bouncing Balls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null); //null sets the frame center of screen

        AnimationPanel gamePanel = new AnimationPanel();
        gamePanel.setVisible(false); // Hide game panel initially
        frame.setLayout(null); // Use absolute positioning for overlay
        gamePanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(gamePanel);

        StartPanel startPanel = new StartPanel(gamePanel);
        startPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(startPanel);

        frame.setVisible(true);
    }
}

/**
 * 
 */
class Ball {
    double x, y, vx, vy;
    int radius;
    boolean active;

    Ball(double inX, double inY, double inVx, double inVy, int inRadius) {
        x = inX;
        y = inY;
        vx = inVx;
        vy = inVy;
        radius = inRadius;
        active = true;
    }
}

class AnimationPanel extends JPanel {

    private boolean aiming = false;
    private double x, y, vx, vy;
    private int ballRadius = 15;
    private final double gravity = 0.8;
    private final double energyLoss = 0.8;
    private int swipeOffSet = 0;
    private int swipeDirection = -1;
    private ArrayList<Ball> balls = new ArrayList<>();

    private ImageIcon cannonIcon;
    private ImageIcon towerIcon;
    private ImageIcon tennisBall;
    private ImageIcon dragIcon;
    private ImageIcon swipeIcon;
    private Image scaledtennisBall;
    private Image scaledCannon;
    private Image scaledTower;
    private Image scaledSwipeIcon;
    private javax.swing.Timer timer;
    private javax.swing.Timer swipeTimer;
    private int aimStartX, aimStartY, aimEndX, aimEndY;

    //constructor
    public AnimationPanel() {
        setBackground(Color.WHITE);

        //load and scale cannon
        cannonIcon = new ImageIcon("cannon.png");
        int cannonWidth = 120;
        int cannonHeight = 90;
        scaledCannon = cannonIcon.getImage().getScaledInstance(cannonWidth, cannonHeight, Image.SCALE_SMOOTH);
        //load and scale tower
        towerIcon = new ImageIcon("tower.png");
        int towerWidth = 200;
        int towerHeight = 320;
        scaledTower = towerIcon.getImage().getScaledInstance(towerWidth, towerHeight, Image.SCALE_SMOOTH);

        //load and scale tennis ball
        tennisBall = new ImageIcon("tennisBall.png");
        int tennisBallSize = ballRadius * 2;
        scaledtennisBall = tennisBall.getImage().getScaledInstance(tennisBallSize, tennisBallSize, Image.SCALE_SMOOTH);

        //load and scale the drag icon
        swipeIcon = new ImageIcon("swipe2.png");
        int swipeSize = ballRadius * 3;
        scaledSwipeIcon = swipeIcon.getImage().getScaledInstance(swipeSize, swipeSize, Image.SCALE_SMOOTH);

        //timer for drawing and moving the swipe icon
        swipeTimer = new javax.swing.Timer(20, e -> {
            if (!aiming) {
                swipeOffSet += swipeDirection * 2;
                if (swipeOffSet < - 60 || swipeOffSet > 0) swipeDirection *= -1;
                repaint();
            }
        });
        swipeTimer.start();
        //mouse listeners for drag and aim
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!aiming) {
                    aiming = true;
                    aimStartX = e.getX();
                    aimStartY = e.getY();
                    aimEndX = aimStartX;
                    aimEndY = aimStartY;
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (aiming) {
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
                if (aiming) {
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

    /**
     * update the list of balls with new x and y values
     * then repaint all balls
     */
    private void updateBalls() {
        for (Ball ball : balls) {
            if (!ball.active) continue;

            if (!ball.active) continue;
            ball.vy += gravity;
            ball.x += ball.vx;
            ball.y += ball.vy;
            // Left wall
            if (ball.x < 0) {
                ball.x = 0;
                ball.vx = -ball.vx * energyLoss;
            }
            // Right wall
            if (ball.x + ball.radius * 2 > getWidth()) {
                ball.x = getWidth() - ball.radius * 2;
                ball.vx = -ball.vx * energyLoss;
            }
            // Top wall
            if (ball.y < 0) {
                ball.y = 0;
                ball.vy = -ball.vy * energyLoss;
            }
            // Bottom wall
            if (ball.y + ball.radius * 2 > getHeight()) {
                ball.y = getHeight() - ball.radius * 2;
                ball.vy = -ball.vy * energyLoss;
                if (Math.abs(ball.vy) < 0.8) {
                    ball.vy = 0;
                    ball.active = false; // Ball comes to rest
                }
            }
        }
        repaint();
    }

    private int cannonX() { return 180; }
    private int cannonWidth() { return 120; }
    private int cannonHeight() { return 90; }
    private int cannonY() { return getHeight() / 3 - cannonHeight() / 2;}

    public void fireBallWithAim() {
        
        int tipX = cannonX() + cannonWidth() + 10;
        int tipY = cannonY() + cannonHeight() / 3;
        double x = tipX - ballRadius * 2;
        double y = tipY - ballRadius * 2;

        //calculate velocity
        double dx = aimStartX - aimEndX;
        double dy = aimStartY - aimEndY;
        double scale = 0.15; //speed sensitivity

        vx = dx * scale;
        vy = dy * scale;
        balls.add(new Ball(x, y, vx, vy, ballRadius));

        if (timer == null) {
            timer = new javax.swing.Timer(16, e -> updateBalls());
            timer.start();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        //draw sky
        graphics.setColor(new Color(34, 206, 235));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        //draw grass
        graphics.setColor(new Color(34, 139, 34));
        int grassHeight = 80;
        graphics.fillRect(0, getHeight() - grassHeight, getWidth(), grassHeight);

        //draw scaled tower
        int towerX = cannonX() - 35;
        int towerY = cannonY() + 42;
        graphics.drawImage(scaledTower, towerX, towerY, this);

        //draw the scaled cannon left edge centered vertically
        int cannonX = cannonX();
        int cannonY = cannonY();
        graphics.drawImage(scaledCannon, cannonX, cannonY, this);

        if (aiming) {
            //draw aiming line
            graphics.setColor(Color.BLUE);
            graphics.drawLine(aimStartX, aimStartY, aimEndX, aimEndY);

            int tipX = cannonX() + cannonWidth() + 10;
            int tipY = cannonY + cannonHeight() / 3;
            int tennisBallSize = ballRadius * 2;
            graphics.drawImage(scaledtennisBall, tipX - tennisBallSize, tipY - tennisBallSize, this);
        } else {
            //draw the swipe image
            int tipX = cannonX() + cannonWidth() + swipeOffSet;
            int tipY = cannonY() + cannonHeight() / 3;
            int swipeSize = ballRadius * 2;
            graphics.drawImage(scaledSwipeIcon, tipX - swipeSize / 2, tipY - swipeSize / 2, this);
        }

        //always draw all the balls in the ball list
        for (Ball ball : balls) {
            graphics.drawImage(scaledtennisBall, (int)ball.x, (int)ball.y, this);
        }
        
    }

}
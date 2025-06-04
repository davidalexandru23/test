import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple projectile game inspired by Angry Birds.
 * Adjust angle and power then press Launch to fire the bird.
 */
public class AngryBirdsGame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final double GRAVITY = 9.8; // pixels per second^2

    private JFrame frame;
    private GamePanel gamePanel;
    private JSlider angleSlider;
    private JSlider powerSlider;
    private JButton launchButton;

    public AngryBirdsGame() {
        frame = new JFrame("Angry Birds Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        angleSlider = new JSlider(0, 90, 45);
        powerSlider = new JSlider(10, 100, 60);
        launchButton = new JButton("Launch");

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Angle"));
        controlPanel.add(angleSlider);
        controlPanel.add(new JLabel("Power"));
        controlPanel.add(powerSlider);
        controlPanel.add(launchButton);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);

        launchButton.addActionListener(e -> {
            if (!gamePanel.isFlying()) {
                double angle = Math.toRadians(angleSlider.getValue());
                double power = powerSlider.getValue();
                gamePanel.launch(angle, power);
            }
        });
    }

    private class GamePanel extends JPanel implements ActionListener {
        private Timer timer;
        private double birdX, birdY;
        private double velocityX, velocityY;
        private double time;
        private boolean flying;
        private List<Point> trail = new ArrayList<>();
        private final int ground = HEIGHT - 100;
        private Rectangle target = new Rectangle(600, ground - 40, 40, 40);

        GamePanel() {
            setBackground(Color.white);
            timer = new Timer(20, this);
        }

        boolean isFlying() {
            return flying;
        }

        void launch(double angle, double power) {
            birdX = 50;
            birdY = ground;
            velocityX = power * Math.cos(angle);
            velocityY = -power * Math.sin(angle);
            time = 0;
            flying = true;
            trail.clear();
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.green);
            g.fillRect(0, ground, WIDTH, HEIGHT - ground);

            // draw target
            g.setColor(Color.red);
            g.fillRect(target.x, target.y, target.width, target.height);

            // draw trail
            g.setColor(Color.gray);
            for (Point p : trail) {
                g.fillRect(p.x, p.y, 2, 2);
            }

            // draw bird
            g.setColor(Color.blue);
            g.fillOval((int) birdX - 10, (int) birdY - 10, 20, 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            time += 0.02; // seconds per tick
            birdX += velocityX * 0.02;
            birdY += velocityY * 0.02 + 0.5 * GRAVITY * 0.02 * 0.02;
            velocityY += GRAVITY * 0.02;
            trail.add(new Point((int) birdX, (int) birdY));

            // check collisions
            if (target.contains(birdX, birdY)) {
                flying = false;
                timer.stop();
                JOptionPane.showMessageDialog(this, "You hit the target!");
            } else if (birdY >= ground) {
                flying = false;
                timer.stop();
                birdY = ground;
            }
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AngryBirdsGame::new);
    }
}

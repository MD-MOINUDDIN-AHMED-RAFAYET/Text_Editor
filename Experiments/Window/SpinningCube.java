//COPIED FROM AI
package Window;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpinningCube extends JPanel implements ActionListener {
    // 3D coordinates of the 8 vertices of a cube
    private double[][] vertices = {
            {-100, -100, -100}, {100, -100, -100}, {100, 100, -100}, {-100, 100, -100},
            {-100, -100,  100}, {100, -100,  100}, {100, 100,  100}, {-100, 100,  100}
    };

    // The 12 edges connecting the vertices
    private int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Back face
            {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Front face
            {0, 4}, {1, 5}, {2, 6}, {3, 7}  // Connecting edges
    };

    // Rotation angles for X, Y, and Z axes
    private double angleX = 0.01;
    private double angleY = 0.02;
    private double angleZ = 0.015;

    public SpinningCube() {
        // Background color of the panel
        setBackground(Color.BLACK);
        // Timer fires every 16ms (~60 frames per second) to update rotation
        Timer timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw the edges of the cube
        g2d.setColor(Color.CYAN);
        g2d.setStroke(new BasicStroke(2));

        for (int[] edge : edges) {
            double[] v1 = vertices[edge[0]];
            double[] v2 = vertices[edge[1]];

            // Basic orthographic projection (ignoring Z for 2D positioning)
            // Shift coordinates to the center of the screen
            int x1 = (int) v1[0] + centerX;
            int y1 = (int) v1[1] + centerY;
            int x2 = (int) v2[0] + centerX;
            int y2 = (int) v2[1] + centerY;

            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Rotate vertices around X-axis
        rotateX(angleX);
        // Rotate vertices around Y-axis
        rotateY(angleY);
        // Rotate vertices around Z-axis
        rotateZ(angleZ);

        // Repaint the screen
        repaint();
    }

    private void rotateX(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (double[] v : vertices) {
            double y = v[1] * cos - v[2] * sin;
            double z = v[1] * sin + v[2] * cos;
            v[1] = y;
            v[2] = z;
        }
    }

    private void rotateY(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (double[] v : vertices) {
            double x = v[0] * cos + v[2] * sin;
            double z = -v[0] * sin + v[2] * cos;
            v[0] = x;
            v[2] = z;
        }
    }

    private void rotateZ(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (double[] v : vertices) {
            double x = v[0] * cos - v[1] * sin;
            double y = v[0] * sin + v[1] * cos;
            v[0] = x;
            v[1] = y;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Spinning Cube 3D");
        SpinningCube cubePanel = new SpinningCube();

        frame.add(cubePanel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center window on screen
        frame.setVisible(true);
    }
}
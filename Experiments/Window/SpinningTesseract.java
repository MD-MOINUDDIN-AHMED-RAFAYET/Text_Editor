
//COPIED FROM AI
        package Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpinningTesseract extends JPanel implements ActionListener {

    // 4D coordinates of the 16 vertices of a tesseract
    // Each vertex is: {x, y, z, w}
    private double[][] vertices = {
            {-100, -100, -100, -100},
            { 100, -100, -100, -100},
            { 100,  100, -100, -100},
            {-100,  100, -100, -100},

            {-100, -100,  100, -100},
            { 100, -100,  100, -100},
            { 100,  100,  100, -100},
            {-100,  100,  100, -100},

            {-100, -100, -100,  100},
            { 100, -100, -100,  100},
            { 100,  100, -100,  100},
            {-100,  100, -100,  100},

            {-100, -100,  100,  100},
            { 100, -100,  100,  100},
            { 100,  100,  100,  100},
            {-100,  100,  100,  100}
    };

    // The 32 edges of a tesseract
    private int[][] edges = {

            // First cube
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7},

            // Second cube
            {8, 9}, {9, 10}, {10, 11}, {11, 8},
            {12, 13}, {13, 14}, {14, 15}, {15, 12},
            {8, 12}, {9, 13}, {10, 14}, {11, 15},

            // Connections through the 4th dimension
            {0, 8}, {1, 9}, {2, 10}, {3, 11},
            {4, 12}, {5, 13}, {6, 14}, {7, 15}
    };

    // Rotation speeds
    private double angleXW = 0.015;
    private double angleYW = 0.012;
    private double angleZW = 0.009;

    private double angleX = 0.01;
    private double angleY = 0.02;
    private double angleZ = 0.015;

    public SpinningTesseract() {

        setBackground(Color.BLACK);

        // Approximately 60 FPS
        Timer timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Store projected 2D coordinates
        int[][] projected = new int[vertices.length][2];

        /*
         * First:
         * 4D -> 3D
         *
         * Then:
         * 3D -> 2D
         */
        for (int i = 0; i < vertices.length; i++) {

            double x = vertices[i][0];
            double y = vertices[i][1];
            double z = vertices[i][2];
            double w = vertices[i][3];

            // Perspective projection from 4D to 3D
            double distance4D = 500;

            double wProjection = distance4D / (distance4D - w);

            double projectedX = x * wProjection;
            double projectedY = y * wProjection;
            double projectedZ = z * wProjection;

            // Perspective projection from 3D to 2D
            double distance3D = 500;

            double zProjection =
                    distance3D / (distance3D - projectedZ);

            projected[i][0] =
                    (int) (projectedX * zProjection) + centerX;

            projected[i][1] =
                    (int) (projectedY * zProjection) + centerY;
        }

        // Draw the tesseract
        g2d.setColor(Color.CYAN);
        g2d.setStroke(new BasicStroke(2));

        for (int[] edge : edges) {

            int x1 = projected[edge[0]][0];
            int y1 = projected[edge[0]][1];

            int x2 = projected[edge[1]][0];
            int y2 = projected[edge[1]][1];

            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Normal 3D rotations
        rotateX(angleX);
        rotateY(angleY);
        rotateZ(angleZ);

        // Rotations involving the 4th dimension
        rotateXW(angleXW);
        rotateYW(angleYW);
        rotateZW(angleZW);

        repaint();
    }

    // Rotate around the X axis
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

    // Rotate around the Y axis
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

    // Rotate around the Z axis
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

    // Rotate between X and W
    private void rotateXW(double angle) {

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (double[] v : vertices) {

            double x = v[0] * cos - v[3] * sin;
            double w = v[0] * sin + v[3] * cos;

            v[0] = x;
            v[3] = w;
        }
    }

    // Rotate between Y and W
    private void rotateYW(double angle) {

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (double[] v : vertices) {

            double y = v[1] * cos - v[3] * sin;
            double w = v[1] * sin + v[3] * cos;

            v[1] = y;
            v[3] = w;
        }
    }

    // Rotate between Z and W
    private void rotateZW(double angle) {

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (double[] v : vertices) {

            double z = v[2] * cos - v[3] * sin;
            double w = v[2] * sin + v[3] * cos;

            v[2] = z;
            v[3] = w;
        }
    }

    public static void main(String[] args) {

        JFrame frame =
                new JFrame("Spinning Tesseract 4D");

        SpinningTesseract tesseractPanel =
                new SpinningTesseract();

        frame.add(tesseractPanel);

        frame.setSize(700, 700);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
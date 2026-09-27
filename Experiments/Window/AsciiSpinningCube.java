
/*MADE USING CHATGPT*/

/*FOR STUDY PURPOSES ONLY*/

package Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class AsciiSpinningCube extends JPanel implements ActionListener {

    private final int width = 160;
    private final int height = 44;

    private final char[][] buffer = new char[height][width];
    private final double[][] zBuffer = new double[height][width];

    private double A = 0;
    private double B = 0;
    private double C = 0;

    private final double cubeWidth = 20;
    private final double distanceFromCam = 115;
    private final double K1 = 40;
    private final double incrementSpeed = 0.6;

    private final Timer timer;

    public AsciiSpinningCube() {
        setBackground(Color.BLACK);

        timer = new Timer(16, this);
        timer.start();
    }

    private double calculateX(double i, double j, double k) {
        return j * Math.sin(A) * Math.sin(B) * Math.cos(C)
                - k * Math.cos(A) * Math.sin(B) * Math.cos(C)
                + j * Math.cos(A) * Math.sin(C)
                + k * Math.sin(A) * Math.sin(C)
                + i * Math.cos(B) * Math.cos(C);
    }

    private double calculateY(double i, double j, double k) {
        return j * Math.cos(A) * Math.cos(C)
                + k * Math.sin(A) * Math.cos(C)
                - j * Math.sin(A) * Math.sin(B) * Math.sin(C)
                + k * Math.cos(A) * Math.sin(B) * Math.sin(C)
                - i * Math.cos(B) * Math.sin(C);
    }

    private double calculateZ(double i, double j, double k) {
        return k * Math.cos(A) * Math.cos(B)
                - j * Math.sin(A) * Math.cos(B)
                + i * Math.sin(B);
    }

    private void calculateForSurface(
            double cubeX,
            double cubeY,
            double cubeZ,
            char character) {

        double x = calculateX(cubeX, cubeY, cubeZ);
        double y = calculateY(cubeX, cubeY, cubeZ);
        double z = calculateZ(cubeX, cubeY, cubeZ) + distanceFromCam;

        if (z <= 0) {
            return;
        }

        double ooz = 1.0 / z;

        /*
         * The * 2 compensates for the rectangular
         * shape of a character cell.
         */
        int xp = (int) (width / 2.0 + K1 * ooz * x * 2);

        int yp = (int) (height / 2.0 + K1 * ooz * y);

        if (xp < 0 || xp >= width || yp < 0 || yp >= height) {
            return;
        }

        /*
         * Z-buffer:
         * the point closer to the camera wins.
         */
        if (ooz > zBuffer[yp][xp]) {
            zBuffer[yp][xp] = ooz;
            buffer[yp][xp] = character;
        }
    }

    private void drawCube() {
        for (double cubeX = -cubeWidth; cubeX < cubeWidth; cubeX += incrementSpeed) {
            for (double cubeY = -cubeWidth; cubeY < cubeWidth; cubeY += incrementSpeed) {

                // Front face
                calculateForSurface(cubeX, cubeY, -cubeWidth, '@');

                // Right face
                calculateForSurface(cubeWidth, cubeY, cubeX, '$');

                // Left face
                calculateForSurface(-cubeWidth, cubeY, -cubeX, '~');

                // Back face
                calculateForSurface(-cubeX, cubeY, cubeWidth, '#');

                // Bottom face
                calculateForSurface(cubeX, -cubeWidth, -cubeY, ';');

                // Top face
                calculateForSurface(cubeX, cubeWidth, cubeY, '+');
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
         * Clear the ASCII screen and depth buffer.
         */
        for (int y = 0; y < height; y++) {
            Arrays.fill(buffer[y], ' ');
            Arrays.fill(zBuffer[y], 0);
        }

        /*
         * Draw ONE cube.
         */
        drawCube();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);

        g2d.setFont(new Font("Consolas", Font.PLAIN, 16));

        FontMetrics metrics = g2d.getFontMetrics();

        int charWidth = metrics.charWidth('W');
        int charHeight = metrics.getHeight();

        /*
         * Center the entire ASCII grid
         * inside the Swing window.
         */
        int totalWidth = width * charWidth;
        int totalHeight = height * charHeight;

        int startX = (getWidth() - totalWidth) / 2;
        int startY = (getHeight() - totalHeight) / 2 + metrics.getAscent();
        /*
         * Draw the ASCII cube.
         */
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (buffer[y][x] != ' ') {
                    g2d.drawString(String.valueOf(buffer[y][x]), startX + x * charWidth, startY + y * charHeight);
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        A += 0.025;
        B += 0.025;
        C += 0.006;
        repaint();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("ASCII Spinning Cube");
        AsciiSpinningCube cubePanel = new AsciiSpinningCube();
        frame.add(cubePanel);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
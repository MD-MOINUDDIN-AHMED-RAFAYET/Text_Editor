
        /*MADE USING CHATGPT*/

        /*FOR STUDY PURPOSES ONLY*/

        package Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class AsciiSpinningPyramid extends JPanel implements ActionListener {

    private final int width = 160;
    private final int height = 44;

    private final char[][] buffer = new char[height][width];
    private final double[][] zBuffer = new double[height][width];

    private double A = 0;
    private double B = 0;
    private double C = 0;

    private final double pyramidSize = 20;
    private final double distanceFromCam = 115;
    private final double K1 = 40;
    private final double incrementSpeed = 0.6;

    private final Timer timer;

    public AsciiSpinningPyramid() {
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
            double pyramidX,
            double pyramidY,
            double pyramidZ,
            char character) {

        double x = calculateX(pyramidX, pyramidY, pyramidZ);
        double y = calculateY(pyramidX, pyramidY, pyramidZ);
        double z = calculateZ(pyramidX, pyramidY, pyramidZ)
                + distanceFromCam;

        if (z <= 0) {
            return;
        }

        double ooz = 1.0 / z;

        /*
         * The * 2 compensates for the rectangular
         * shape of a character cell.
         */
        int xp = (int) (
                width / 2.0
                        + K1 * ooz * x * 2
        );

        int yp = (int) (
                height / 2.0
                        + K1 * ooz * y
        );

        if (xp < 0 || xp >= width ||
                yp < 0 || yp >= height) {
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

    private void drawPyramid() {

        /*
         * Square base:
         *
         *        A -------- B
         *        |          |
         *        |          |
         *        D -------- C
         *
         * Apex is directly above the center.
         */

        double A_x = -pyramidSize;
        double A_y = pyramidSize;
        double A_z = -pyramidSize;

        double B_x = pyramidSize;
        double B_y = pyramidSize;
        double B_z = -pyramidSize;

        double C_x = pyramidSize;
        double C_y = pyramidSize;
        double C_z = pyramidSize;

        double D_x = -pyramidSize;
        double D_y = pyramidSize;
        double D_z = pyramidSize;

        /*
         * Apex of the pyramid.
         */
        double apexX = 0;
        double apexY = -pyramidSize;
        double apexZ = 0;


        /*
         * Draw the four triangular sides.
         *
         * We sample each triangle using two
         * parameters.
         */

        for (double u = 0;
             u <= 1;
             u += incrementSpeed / pyramidSize) {

            for (double v = 0;
                 v <= 1 - u;
                 v += incrementSpeed / pyramidSize) {

                /*
                 * Front face:
                 * A -> B -> Apex
                 */
                double x =
                        A_x * u
                                + B_x * v
                                + apexX * (1 - u - v);

                double y =
                        A_y * u
                                + B_y * v
                                + apexY * (1 - u - v);

                double z =
                        A_z * u
                                + B_z * v
                                + apexZ * (1 - u - v);

                calculateForSurface(x, y, z, '@');


                /*
                 * Right face:
                 * B -> C -> Apex
                 */
                x =
                        B_x * u
                                + C_x * v
                                + apexX * (1 - u - v);

                y =
                        B_y * u
                                + C_y * v
                                + apexY * (1 - u - v);

                z =
                        B_z * u
                                + C_z * v
                                + apexZ * (1 - u - v);

                calculateForSurface(x, y, z, '$');


                /*
                 * Back face:
                 * C -> D -> Apex
                 */
                x =
                        C_x * u
                                + D_x * v
                                + apexX * (1 - u - v);

                y =
                        C_y * u
                                + D_y * v
                                + apexY * (1 - u - v);

                z =
                        C_z * u
                                + D_z * v
                                + apexZ * (1 - u - v);

                calculateForSurface(x, y, z, '#');


                /*
                 * Left face:
                 * D -> A -> Apex
                 */
                x =
                        D_x * u
                                + A_x * v
                                + apexX * (1 - u - v);

                y =
                        D_y * u
                                + A_y * v
                                + apexY * (1 - u - v);

                z =
                        D_z * u
                                + A_z * v
                                + apexZ * (1 - u - v);

                calculateForSurface(x, y, z, '~');
            }
        }


        /*
         * Draw the square base.
         */
        for (double x = -pyramidSize;
             x <= pyramidSize;
             x += incrementSpeed) {

            for (double z = -pyramidSize;
                 z <= pyramidSize;
                 z += incrementSpeed) {

                calculateForSurface(
                        x,
                        pyramidSize,
                        z,
                        '+'
                );
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
         * Draw ONE pyramid.
         */
        drawPyramid();

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
        int startY = (getHeight() - totalHeight) / 2
                + metrics.getAscent();

        /*
         * Draw the ASCII pyramid.
         */
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (buffer[y][x] != ' ') {

                    g2d.drawString(
                            String.valueOf(buffer[y][x]),
                            startX + x * charWidth,
                            startY + y * charHeight
                    );
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

        JFrame frame =
                new JFrame("ASCII Spinning Pyramid");

        AsciiSpinningPyramid pyramidPanel =
                new AsciiSpinningPyramid();

        frame.add(pyramidPanel);

        frame.setSize(1000, 600);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
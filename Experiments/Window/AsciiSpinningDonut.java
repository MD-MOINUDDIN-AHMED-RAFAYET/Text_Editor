package Window;
import javax.swing.*;
import java.awt.*;

public class AsciiSpinningDonut extends JPanel {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private float A = 0;
    private float B = 0;
    private final JTextArea screen;

    public AsciiSpinningDonut() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        screen = new JTextArea(HEIGHT, WIDTH);
        screen.setFont(new Font("Monospaced", Font.PLAIN, 14));
        screen.setForeground(Color.WHITE);
        screen.setBackground(Color.BLACK);
        screen.setEditable(false);
        screen.setFocusable(false);
        screen.setLineWrap(false);
        screen.setWrapStyleWord(false);

        add(screen, BorderLayout.CENTER);

        Timer timer = new Timer(30, e -> render());
        timer.start();
    }

    private void render() {

        char[] b = new char[WIDTH * HEIGHT];
        float[] z = new float[WIDTH * HEIGHT];

        for (int i = 0; i < b.length; i++) {
            b[i] = ' ';
            z[i] = 0;
        }

        for (float j = 0; j < 6.28; j += 0.07) {

            for (float i = 0; i < 6.28; i += 0.02) {

                float c = (float) Math.sin(i);
                float d = (float) Math.cos(j);
                float e = (float) Math.sin(A);
                float f = (float) Math.sin(j);
                float g = (float) Math.cos(A);

                float h = d + 2;

                float D = 1 / (c * h * e + f * g + 5);

                float l = (float) Math.cos(i);
                float m = (float) Math.cos(B);
                float n = (float) Math.sin(B);

                float t = c * h * g - f * e;

                int x = (int) (WIDTH / 2 + 30 * D * (l * h * m - t * n));
                int y = (int) (HEIGHT / 2 + 15 * D * (l * h * n + t * m));

                int o = x + WIDTH * y;

                int N = (int) (
                        8 * (
                                (f * e - c * d * g) * m
                                        - c * d * e
                                        - f * g
                                        - l * d * n
                        )
                );

                if (y > 0 && y < HEIGHT && x > 0 && x < WIDTH && D > z[o]) {

                    z[o] = D;

                    String chars = ".,-~:;=!*#$@";

                    if (N > 0) {
                        b[o] = chars.charAt(Math.min(N, chars.length() - 1));
                    } else {
                        b[o] = chars.charAt(0);
                    }
                }
            }
        }

        StringBuilder output = new StringBuilder();

        for (int k = 0; k < WIDTH * HEIGHT; k++) {

            if (k % WIDTH == 0 && k != 0) {
                output.append('\n');
            }

            output.append(b[k]);
        }

        screen.setText(output.toString());

        A += 0.04;
        B += 0.02;
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("ASCII Donut");

        AsciiSpinningDonut donut = new AsciiSpinningDonut();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(donut);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
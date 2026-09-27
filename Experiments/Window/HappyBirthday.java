
//MADE USING CHATGPT
//FOR STUDY PURPOSES ONLY

        package Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class HappyBirthday extends JPanel implements ActionListener {

    /*
     * ASCII screen size.
     */
    private final int width = 120;
    private final int height = 40;

    private final char[][] buffer =
            new char[height][width];

    private final Random random =
            new Random();

    private final ArrayList<Firework> fireworks =
            new ArrayList<>();

    private final Timer timer;

    /*
     * Controls how often new fireworks
     * are launched.
     */
    private int launchTimer = 0;

    public HappyBirthday() {

        setBackground(Color.BLACK);

        /*
         * Start with a few fireworks.
         */
        for (int i = 0; i < 3; i++) {
            createFirework();
        }

        /*
         * Approximately 30 FPS.
         */
        timer = new Timer(33, this);
        timer.start();
    }

    /*
     * Create a new rocket.
     */
    private void createFirework() {

        double x =
                5 + random.nextInt(width - 10);

        /*
         * Fireworks explode in the upper
         * and middle part of the screen.
         */
        double targetY =
                4 + random.nextInt(15);

        fireworks.add(
                new Firework(
                        x,
                        height - 2,
                        targetY
                )
        );
    }

    /*
     * Clear the ASCII buffer.
     */
    private void clearBuffer() {

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                buffer[y][x] = ' ';
            }
        }
    }

    /*
     * Put a character into the buffer.
     */
    private void putCharacter(
            int x,
            int y,
            char character) {

        if (x >= 0 && x < width &&
                y >= 0 && y < height) {

            buffer[y][x] = character;
        }
    }

    /*
     * Draw the large HAPPY BIRTHDAY text.
     */
    private void drawBirthdayMessage() {

        /*
         * Large 5-row ASCII letters.
         *
         * HAPPY
         */
        String[] happy = {

                "##   ##   ####   ######  ######  ##   ##",
                "##   ##  ##  ##    ##    ##      ##   ##",
                "#######  ######    ##    ####    #######",
                "##   ##  ##  ##    ##    ##      ##   ##",
                "##   ##  ##  ##    ##    ######  ##   ##"
        };

        /*
         * BIRTHDAY
         */
        String[] birthday = {

                "######  ######  ######  ######  ##   ##  ######  ##   ##",
                "##      ##  ##  ##      ##      ##   ##  ##  ##  ##   ##",
                "#####   ######  ####    ####    #######  ######  ##   ##",
                "##      ##  ##  ##      ##      ##   ##  ##  ##   ## ##",
                "######  ##  ##  ######  ######  ##   ##  ##  ##    ###"
        };

        /*
         * Center HAPPY.
         */
        int happyWidth =
                happy[0].length();

        int happyStartX =
                (width - happyWidth) / 2;

        /*
         * Center BIRTHDAY.
         */
        int birthdayWidth =
                birthday[0].length();

        int birthdayStartX =
                (width - birthdayWidth) / 2;

        /*
         * Put the message around the middle
         * of the screen.
         */
        int startY = 14;

        for (int y = 0; y < happy.length; y++) {

            for (int x = 0;
                 x < happy[y].length();
                 x++) {

                if (happy[y].charAt(x) != ' ') {

                    putCharacter(
                            happyStartX + x,
                            startY + y,
                            happy[y].charAt(x)
                    );
                }
            }
        }

        /*
         * Small gap between HAPPY and BIRTHDAY.
         */
        startY += 7;

        for (int y = 0;
             y < birthday.length;
             y++) {

            for (int x = 0;
                 x < birthday[y].length();
                 x++) {

                if (birthday[y].charAt(x) != ' ') {

                    putCharacter(
                            birthdayStartX + x,
                            startY + y,
                            birthday[y].charAt(x)
                    );
                }
            }
        }
    }

    /*
     * Update and draw every firework.
     */
    private void drawFireworks() {

        for (Firework firework : fireworks) {

            firework.update();
            firework.draw();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        clearBuffer();

        /*
         * Fireworks first.
         */
        drawFireworks();

        /*
         * Birthday message afterward,
         * so fireworks cannot erase it.
         */
        drawBirthdayMessage();

        Graphics2D g2d =
                (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        );

        g2d.setColor(Color.WHITE);

        g2d.setFont(
                new Font(
                        "Consolas",
                        Font.BOLD,
                        18
                )
        );

        FontMetrics metrics =
                g2d.getFontMetrics();

        int charWidth =
                metrics.charWidth('W');

        int charHeight =
                metrics.getHeight();

        int totalWidth =
                width * charWidth;

        int totalHeight =
                height * charHeight;

        /*
         * Center ASCII screen in window.
         */
        int startX =
                (getWidth() - totalWidth) / 2;

        int startY =
                (getHeight() - totalHeight) / 2
                        + metrics.getAscent();

        /*
         * Draw ASCII buffer.
         */
        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                if (buffer[y][x] != ' ') {

                    g2d.drawString(
                            String.valueOf(
                                    buffer[y][x]
                            ),
                            startX + x * charWidth,
                            startY + y * charHeight
                    );
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /*
         * Count frames.
         */
        launchTimer++;

        /*
         * Launch fireworks continuously.
         *
         * The random delay prevents the
         * fireworks from looking synchronized.
         */
        if (launchTimer >=
                10 + random.nextInt(18)) {

            createFirework();

            launchTimer = 0;
        }

        /*
         * Remove dead fireworks.
         */
        Iterator<Firework> iterator =
                fireworks.iterator();

        while (iterator.hasNext()) {

            Firework firework =
                    iterator.next();

            if (firework.dead) {

                iterator.remove();
            }
        }

        repaint();
    }

    /*
     * =========================================================
     * FIREWORK
     * =========================================================
     */
    private class Firework {

        double x;
        double y;

        double targetY;

        /*
         * Rocket velocity.
         */
        double velocityY;

        /*
         * Whether the rocket has exploded.
         */
        boolean exploded = false;

        /*
         * Whether the entire firework has
         * disappeared.
         */
        boolean dead = false;

        /*
         * Rocket trail.
         */
        ArrayList<TrailParticle> trail =
                new ArrayList<>();

        /*
         * Explosion particles.
         */
        ArrayList<Particle> particles =
                new ArrayList<>();

        Firework(
                double x,
                double y,
                double targetY) {

            this.x = x;
            this.y = y;
            this.targetY = targetY;

            /*
             * Strong initial upward velocity.
             */
            velocityY = -1.7;
        }

        /*
         * Update the firework.
         */
        void update() {

            /*
             * =============================================
             * ROCKET PHASE
             * =============================================
             */
            if (!exploded) {

                /*
                 * Leave a trail particle behind.
                 */
                trail.add(
                        new TrailParticle(
                                x,
                                y + 1
                        )
                );

                /*
                 * Move rocket upward.
                 */
                y += velocityY;

                /*
                 * Gravity.
                 */
                velocityY += 0.018;

                /*
                 * Once we reach the target,
                 * explode immediately.
                 */
                if (y <= targetY) {

                    explode();
                }

                /*
                 * Update trail.
                 */
                Iterator<TrailParticle> trailIterator =
                        trail.iterator();

                while (trailIterator.hasNext()) {

                    TrailParticle particle =
                            trailIterator.next();

                    particle.update();

                    if (particle.life <= 0) {

                        trailIterator.remove();
                    }
                }
            }

            /*
             * =============================================
             * EXPLOSION PHASE
             * =============================================
             */
            else {

                Iterator<Particle> iterator =
                        particles.iterator();

                while (iterator.hasNext()) {

                    Particle particle =
                            iterator.next();

                    particle.update();

                    if (particle.life <= 0) {

                        iterator.remove();
                    }
                }

                /*
                 * Wait until every spark disappears.
                 */
                if (particles.isEmpty()) {

                    dead = true;
                }
            }
        }

        /*
         * Create the explosion.
         */
        void explode() {

            exploded = true;

            /*
             * Large number of sparks.
             */
            int particleCount =
                    90 + random.nextInt(40);

            for (int i = 0;
                 i < particleCount;
                 i++) {

                /*
                 * Spread sparks around a circle,
                 * but add randomness.
                 */
                double angle =
                        random.nextDouble()
                                * Math.PI * 2;

                /*
                 * Different particles have
                 * different speeds.
                 */
                double speed =
                        0.35 +
                                random.nextDouble() * 1.15;

                /*
                 * Make some sparks travel farther.
                 */
                if (random.nextInt(5) == 0) {

                    speed *= 1.4;
                }

                particles.add(
                        new Particle(
                                x,
                                y,
                                Math.cos(angle) * speed,
                                Math.sin(angle) * speed
                        )
                );
            }

            /*
             * Add several special long sparks.
             * These make the explosion look
             * more like a starburst.
             */
            for (int i = 0; i < 12; i++) {

                double angle =
                        (Math.PI * 2 * i) / 12.0;

                double speed =
                        1.2 +
                                random.nextDouble() * 0.7;

                particles.add(
                        new Particle(
                                x,
                                y,
                                Math.cos(angle) * speed,
                                Math.sin(angle) * speed
                        )
                );
            }
        }

        /*
         * Draw rocket, trail and explosion.
         */
        void draw() {

            /*
             * Rocket.
             */
            if (!exploded) {

                /*
                 * Rocket head.
                 */
                putCharacter(
                        (int) x,
                        (int) y,
                        '*'
                );

                /*
                 * Draw its trail.
                 */
                for (TrailParticle particle :
                        trail) {

                    particle.draw();
                }
            }

            /*
             * Explosion.
             */
            else {

                for (Particle particle :
                        particles) {

                    particle.draw();
                }
            }
        }
    }

    /*
     * =========================================================
     * ROCKET TRAIL PARTICLE
     * =========================================================
     */
    private class TrailParticle {

        double x;
        double y;

        int life;

        TrailParticle(
                double x,
                double y) {

            this.x = x;
            this.y = y;

            life =
                    5 +
                            random.nextInt(7);
        }

        void update() {

            /*
             * Trail slowly falls.
             */
            y += 0.05;

            life--;
        }

        void draw() {

            if (life <= 0) {
                return;
            }

            char character;

            if (life >= 8) {

                character = '|';

            } else if (life >= 5) {

                character = '+';

            } else {

                character = '.';
            }

            putCharacter(
                    (int) x,
                    (int) y,
                    character
            );
        }
    }

    /*
     * =========================================================
     * EXPLOSION PARTICLE
     * =========================================================
     */
    private class Particle {

        double x;
        double y;

        double velocityX;
        double velocityY;

        int life;

        /*
         * Different particles can behave
         * slightly differently.
         */
        double drag;

        Particle(
                double x,
                double y,
                double velocityX,
                double velocityY) {

            this.x = x;
            this.y = y;

            this.velocityX = velocityX;
            this.velocityY = velocityY;

            /*
             * Long-lasting sparks.
             */
            life =
                    22 +
                            random.nextInt(30);

            /*
             * Random air resistance.
             */
            drag =
                    0.965 +
                            random.nextDouble() * 0.02;
        }

        void update() {

            /*
             * Move particle.
             */
            x += velocityX;
            y += velocityY;

            /*
             * Gravity.
             */
            velocityY += 0.012;

            /*
             * Air resistance.
             */
            velocityX *= drag;
            velocityY *= drag;

            /*
             * Fade.
             */
            life--;
        }

        void draw() {

            if (life <= 0) {
                return;
            }

            /*
             * Bright sparks at the beginning.
             */
            char character;

            if (life > 40) {

                character = '*';

            } else if (life > 30) {

                character = '+';

            } else if (life > 18) {

                character = 'o';

            } else if (life > 8) {

                character = '.';

            } else {

                character = '`';
            }

            putCharacter(
                    (int) x,
                    (int) y,
                    character
            );
        }
    }

    public static void main(String[] args) {

        JFrame frame =
                new JFrame(
                        "ASCII Happy Birthday"
                );

        HappyBirthday panel =
                new HappyBirthday();

        frame.add(panel);

        frame.setSize(
                1200,
                600
        );

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}

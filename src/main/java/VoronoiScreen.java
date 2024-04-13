import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class VoronoiScreen extends Canvas implements Runnable, MouseListener {
    private final int screenWidth, screenHeight;
    private Graphics2D g;

    private final ArrayList<Point> points = new ArrayList<>();
    private final ArrayList<Integer> colorOfPoints = new ArrayList<>();
    private final int[][] pixels;

    public VoronoiScreen(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.pixels = new int[screenHeight][screenWidth];

        this.addMouseListener(this);
        this.setSize(screenWidth, screenHeight);
        this.generateColorForPoints();
        this.calculateVoronoi();
    }

    private void update() {
        clearScreen();
        renderVoronoi();
        renderPoints();
    }

    private void clearScreen() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenWidth, screenHeight);
    }

    private void renderVoronoi() {
        for (int y = 0; y < this.pixels.length; y++) {
            for (int x = 0; x < this.pixels[y].length; x++) {
                int color = this.pixels[y][x];
                if (color == 0) continue;

                g.setColor(new Color(color));
                g.drawRect(x, y, x, y);
            }
        }
    }

    private void renderPoints() {
        g.setColor(Color.BLACK);
        for (Point p : points) {
            g.fillOval(p.x, p.y, 10, 10);
        }
    }

    private void generateColorForPoints() {
        float h = 0, s = 0.5f, b = 1;
        for (int i = 0; i < colorOfPoints.size(); i++) {
            colorOfPoints.set(i, Color.getHSBColor(h, s, b).getRGB());
            h = (h + 0.05f) % 1;
        }
    }

    private void calculateVoronoi() {
        if (this.points.isEmpty()) return;
        new Thread(() -> {
            for (int y = 0; y <  this.pixels.length; y++) {
                for (int x = 0; x < this.pixels[y].length; x++) {
                    int closestPoint = 0;
                    double shortestDistance = Double.MAX_VALUE;

                    for (int i = 0; i < this.points.size(); i++) {
                        double currentDistance = this.points.get(i).distance(x, y);
                        if (currentDistance > shortestDistance) continue;
                        shortestDistance = currentDistance;
                        closestPoint = i;
                    }

                    this.pixels[y][x] = this.colorOfPoints.get(closestPoint);
                }
            }
        }).start();
    }

    private void addPoint(int x, int y) {
        this.points.add(new Point(x, y));
        this.colorOfPoints.add(0);

        this.generateColorForPoints();
        this.calculateVoronoi();
    }

    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait", "CallToPrintStackTrace"})
    @Override
    public void run() {
        while (true) {
            BufferStrategy bs = this.getBufferStrategy();
            if (bs != null) {
                this.g = (Graphics2D) bs.getDrawGraphics();
                this.update();
                this.g.dispose();
                bs.show();
            } else this.createBufferStrategy(3);

            try { Thread.sleep(1000/60);}
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.addPoint(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

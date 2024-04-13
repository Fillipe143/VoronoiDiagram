import javax.swing.*;

public class Main {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Voronoi");
        VoronoiScreen voronoiScreen = new VoronoiScreen(WIDTH, HEIGHT);

        jFrame.add(voronoiScreen);
        jFrame.setResizable(false);
        jFrame.pack();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);

        jFrame.setVisible(true);
        new Thread(voronoiScreen).start();
    }
}

package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.TERenderer;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import static edu.princeton.cs.algs4.StdDraw.*;


public class MainMenu {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public MainMenu() {
        initialDraw();
    }

    private void initialDraw() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering(); // prevents flickering and makes animations smoother
    }

    public void showMenu() {
        StdDraw.clear(StdDraw.BLACK);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 1.5, "CS61B: Game");

        StdDraw.setFont(new Font("Arial", Font.PLAIN, 25));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.5, "New Game (N)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 3.5, "Load Game (L)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 5.5, "Quit (Q)");

        StdDraw.show();
    }
}
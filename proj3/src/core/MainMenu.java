package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.TERenderer;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import static edu.princeton.cs.algs4.StdDraw.*;


public class MainMenu {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    public MainMenu() {
        initialDraw();
    }

    public void initialDraw() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering(); // prevents flickering and makes animations smoother
    }

    public int showMenu() {
        while (true) {
            StdDraw.clear(StdDraw.BLACK);

            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(new Font("Times new Roman", Font.BOLD, 40));
            StdDraw.text(WIDTH / 2.0, HEIGHT / 1.5, "CS61B: Game");

            StdDraw.setFont(new Font("Times new Roman", Font.PLAIN, 25));
            StdDraw.text(WIDTH / 2.0, HEIGHT / 2.5, "New Game (N)");
            StdDraw.text(WIDTH / 2.0, HEIGHT / 3.5, "Load Game (L)");
            StdDraw.text(WIDTH / 2.0, HEIGHT / 5.5, "Quit (Q)");

            StdDraw.show();

            if (hasNextKeyTyped()) {
                char gameStatus = nextKeyTyped();
                if (gameStatus == 'N' || gameStatus == 'n') {
                    return 1;
                } else if (gameStatus == 'L' || gameStatus == 'l') {
                    return 2;
                } else if (gameStatus == 'Q' || gameStatus == 'q') {
                    return 3;
                }
            }
        }
    }
    public long enterSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Times new Roman", Font.PLAIN, 20));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 7, "Enter Seed (Press S to finish):");
        StdDraw.show();
        String input = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'S' || c == 's') {
                    break; // Finish seed input
                } else if (Character.isDigit(c)) {
                    input += c; // Append digit to seed string
                    // Redraw the prompt with the current seed input
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(WIDTH / 2.0, HEIGHT / 7.0, "Enter Seed (Press S to finish): " + input);
                    StdDraw.show();
                }
            }
        }
        return Long.parseLong(input); // Convert string input to long
    }
}
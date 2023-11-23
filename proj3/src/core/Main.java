package core;


import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

import java.awt.*;

import static edu.princeton.cs.algs4.StdDraw.*;

public class Main {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        int userSelection = menu.showMenu();
        if (userSelection == 1) {
            long seed = menu.enterSeed();
            World newWorld = new World(seed);
            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(newWorld.getTiles());
            while (true) {
                StdDraw.clear(Color.BLACK);
                ter.drawTiles(newWorld.getTiles());
                int positionX = (int) StdDraw.mouseX();
                int positionY = (int) StdDraw.mouseY();
                World.displayHUD(newWorld, positionX, positionY);
                if (hasNextKeyTyped()) {
                    char characterMovement = nextKeyTyped();
                    if (characterMovement == 'W' || characterMovement == 'w') {
                        newWorld.tryMove(0, 1);
                    } else if (characterMovement == 'A' || characterMovement == 'a') {
                        newWorld.tryMove(-1, 0);
                    } else if (characterMovement == 'S' || characterMovement == 's') {
                        newWorld.tryMove(0, -1);
                    } else if (characterMovement == 'D' || characterMovement == 'd') {
                        newWorld.tryMove(1, 0);
                    }
                }
            }
        }
        if (userSelection == 2) {
            //use save and load feature to load that specific game
        }
        if (userSelection == 3) {
            boolean quitGameStart = false;
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();

                    if (key == ':') {
                        quitGameStart = true; // Set flag if ':' is pressed
                    } else if ((key == 'Q' || key == 'q') && quitGameStart) {
                        System.exit(0); // Exit if ':' was pressed before 'Q' or 'q'
                    } else if (key == 'Q' || key == 'q') {
                        System.exit(0); // Also exit if 'Q' or 'q' is pressed by itself
                    }
                }
            }
        }
    }
}




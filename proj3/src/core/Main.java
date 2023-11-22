package core;


import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

import java.awt.*;

public class Main {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        int userSelection = menu.showMenu();
        if (userSelection == 1) {
            long seed = menu.enterSeed();
            World newWorld = new World (seed);
            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(newWorld.getTiles());
            while (true) {
                ter.renderFrame(newWorld.getTiles());
                int positionX = (int) StdDraw.mouseX();
                int positionY = (int) StdDraw.mouseY();
                World.displayHUD(newWorld, positionX, positionY);
            }
        }
        if (userSelection == 2) {
            //use save and load feature to load that specific game
        }
        if (userSelection == 3) {

        }
    }
}




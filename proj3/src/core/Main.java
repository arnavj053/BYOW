package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static edu.princeton.cs.algs4.StdDraw.*;

public class Main {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    public static void main(String[] args) {
        String avatarName = null;
        MainMenu menu = new MainMenu();
        boolean mainMenuRunning = true;
        while (mainMenuRunning) {
            int userSelection = menu.showMenu();
            if (userSelection == 1) {
                long seed = menu.enterSeed();
                World newWorld = new World(seed);
                TERenderer ter = new TERenderer();
                ter.initialize(WIDTH, HEIGHT);
                ter.renderFrame(newWorld.getTiles());
                while (true) {
                    if (avatarName != null) {
                        newWorld.displayHUD(newWorld, avatarName);
                    }
                    StdDraw.clear(Color.BLACK);
                    ter.drawTiles(newWorld.getTiles());
                    int positionX = (int) StdDraw.mouseX();
                    int positionY = (int) StdDraw.mouseY();
                    newWorld.displayHUD(newWorld, positionX, positionY);
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
            } else if (userSelection == 2) {
                Object[] gameState = loadGame();
                long savedSeed = (long) gameState[0];
                int savedAvatarX = (int) gameState[1];
                int savedAvatarY = (int) gameState[2];
                String savedAvatarName = (String) gameState[3];
                if (savedSeed != -1) {
                    World newWorld = new World(savedSeed);
                    newWorld.savedAvatarPosition(savedAvatarX,savedAvatarY);
                    newWorld.savedAvatarName(savedAvatarName);
                    TERenderer ter = new TERenderer();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(newWorld.getTiles());
                    while (true) {
                        if (avatarName != null) {
                            newWorld.displayHUD(newWorld, avatarName);
                        }
                        StdDraw.clear(Color.BLACK);
                        ter.drawTiles(newWorld.getTiles());
                        int positionX = (int) StdDraw.mouseX();
                        int positionY = (int) StdDraw.mouseY();
                        newWorld.displayHUD(newWorld, positionX, positionY);
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
                        saveGame(newWorld, "gameState.txt");
                    }
                }
            } else if (userSelection == 3) {
                avatarName = menu.avatarName();
            }
        }
    }

    public static Object[] loadGame() {
        File file = new File("gameState.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                long seed = scanner.hasNextLong() ? scanner.nextLong() : -1;
                int avatarX = scanner.hasNextInt() ? scanner.nextInt() : -1;
                int avatarY = scanner.hasNextInt() ? scanner.nextInt() : -1;
                String avatarName = scanner.hasNext() ? scanner.next() : null; // Null if name not present
                return new Object[]{seed, avatarX, avatarY, avatarName};
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new Object[]{-1,-1,-1, null}; // Return a default or error value if no seed is found
    }
    public static void saveGame(World world, String filename) {
        world.saveGameState(filename);
    }
}
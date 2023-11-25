package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    public static void main(String[] args) {
        String avatarName = null;
        MainMenu menu = new MainMenu();
        boolean mainMenuRunning = true;

        while (mainMenuRunning) {
            int userSelection = menu.showMenu();

            if (userSelection == 1) { // New Game
                long seed = menu.enterSeed();
                avatarName = menu.avatarName(); // Get avatar name for new game
                World newWorld = new World(seed);
                runGameLoop(newWorld, avatarName);
            } else if (userSelection == 2) { // Load Game
                long savedSeed = loadSeed();
                avatarName = loadAvatarName(); // Load avatar name for saved game

                if (savedSeed != -1) {
                    World newWorld = new World(savedSeed);
                    runGameLoop(newWorld, avatarName);
                }
            } else if (userSelection == 3) {
                // Exit or other options
                mainMenuRunning = false;
            }
        }
    }

    private static void runGameLoop(World world, String avatarName) {
        saveAvatarName(avatarName);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world.getTiles());

        boolean quitGameStarted = false; // Flag to track quit sequence

        while (true) {
            if (avatarName != null) {
                world.displayHUD(world, avatarName);
            }
            StdDraw.clear(Color.BLACK);
            ter.drawTiles(world.getTiles());
            int positionX = (int) StdDraw.mouseX();
            int positionY = (int) StdDraw.mouseY();
            world.displayHUD(world, positionX, positionY);

            if (StdDraw.hasNextKeyTyped()) {
                char characterMovement = StdDraw.nextKeyTyped();

                // Check for quit sequence
                if (characterMovement == ':' && !quitGameStarted) {
                    quitGameStarted = true; // First part of quit sequence detected
                } else if ((characterMovement == 'Q' || characterMovement == 'q') && quitGameStarted) {
                    System.exit(0); // Quit if ':' was pressed before 'Q' or 'q'
                } else {
                    quitGameStarted = false; // Reset flag if other keys are pressed
                    handleMovement(world, characterMovement);
                }
            }
        }
    }

    private static void handleMovement(World world, char movement) {
        switch (Character.toLowerCase(movement)) {
            case 'w':
                world.tryMove(0, 1);
                break;
            case 'a':
                world.tryMove(-1, 0);
                break;
            case 's':
                world.tryMove(0, -1);
                break;
            case 'd':
                world.tryMove(1, 0);
                break;
            // Handle other keys if needed
        }
    }

    public static long loadSeed() {
        File file = new File("lastSeed.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLong()) {
                    return scanner.nextLong();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return -1; // Return a default or error value if no seed is found
    }
    private static String loadAvatarName() {
        File file = new File("avatar_name.txt");
        if (!file.exists()) {
            System.out.println("Avatar name file not found.");
            return null;
        }
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                System.out.println("Loaded Avatar Name: " + name);
                return name;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading avatar name file.");
            e.printStackTrace();
        }
        return null;
    }
    public static void saveAvatarName(String avatarName) {
        try (FileWriter writer = new FileWriter("avatar_name.txt")) {
            writer.write(avatarName);
        } catch (IOException e) {
            System.err.println("Error saving avatar name.");
            e.printStackTrace();
        }
    }
}
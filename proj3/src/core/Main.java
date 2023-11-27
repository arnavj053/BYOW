package core;
import tileengine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;
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
                saveAvatarName(avatarName); // Save the new avatar name
                World newWorld = new World(seed);
                runGameLoop(newWorld, avatarName);
            }
            if (userSelection == 2) { // Load Game
                World loadedWorld = loadGame(); // Use the loadGame() method
                if (loadedWorld != null) {
                    avatarName = loadAvatarName(); // Load the avatar name for the saved game
                    runGameLoop(loadedWorld, avatarName); // Continue the game from the loaded state
                }
            }
        }
    }

    private static void runGameLoop(World world, String avatarName) {
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

                if (characterMovement == ':' && !quitGameStarted) {
                    quitGameStarted = true; // First part of quit sequence detected
                } else if ((characterMovement == 'Q' || characterMovement == 'q') && quitGameStarted) {
                    saveGame(world); // Save the game state
                    System.exit(0); // Quit the game
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
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLine()) {
                    return scanner.nextLine();
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error reading avatar name file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Avatar name file not found.");
        }
        return null;
    }
    public static void saveAvatarName(String avatarName) {
        try (FileWriter writer = new FileWriter("avatar_name.txt", false)) { // false to overwrite
            writer.write(avatarName);
        } catch (IOException e) {
            System.err.println("Error saving avatar name.");
            e.printStackTrace();
        }
    }

    public static void saveGame(World world) {
        world.saveGameState("gameState.txt");
    }

    public static World loadGame() {
        File file = new File("gameState.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                long seed = 0;
                int avatarX = 0;
                int avatarY = 0;

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Seed:")) {
                        seed = Long.parseLong(line.substring(5));
                    } else if (line.startsWith("AvatarX:")) {
                        avatarX = Integer.parseInt(line.substring(8));
                    } else if (line.startsWith("AvatarY:")) {
                        avatarY = Integer.parseInt(line.substring(8));
                    }
                }
                World world = new World(seed);
                world.updateAvatarPosition(avatarX, avatarY);
                return world; // Return the loaded world
            } catch (FileNotFoundException e) {
                System.err.println("Error loading game state.");
                e.printStackTrace();
            }
        }
        return null; // Return null if the file does not exist
    }
}
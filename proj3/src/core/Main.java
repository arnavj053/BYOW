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
    static String avatarName = null; // Load the avatar name at the start
    static StringBuilder actions = new StringBuilder(); // To store actions
    static boolean isReplay = false;


    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        boolean mainMenuRunning = true;

        while (mainMenuRunning) {
            int userSelection = menu.showMenu();

            if (userSelection == 1) { // New Game
                actions.setLength(0); // Clear previous actions
                long seed = menu.enterSeed();// New Game
                avatarName = loadAvatarName();
                if (avatarName == null) {  // Only ask for avatar name if not already set
                    avatarName = menu.avatarName();
                }
                saveAvatarName(avatarName); // Save or update the avatar name
                World newWorld = new World(seed);
                runGameLoop(newWorld, avatarName);
            } else if (userSelection == 2) { // Load Game
                World loadedWorld = loadGame();
                if (loadedWorld != null) {
                    runGameLoop(loadedWorld, loadAvatarName()); // Use the avatar name loaded at the start
                }
            } else if (userSelection == 3) {
                avatarName = menu.avatarName();
                saveAvatarName(avatarName); // Save the new avatar name
                break;
            } if (userSelection == 4) { // Replay
                actions.setLength(0); // Clear any current actions
                long seed = loadSeed(); // Load the seed used for the new game
                String replayActions = loadActions(); // Load all actions since the new game
                isReplay = true;
                replayActions(replayActions, seed); // Replay all actions
            }
        }
    }

    private static void runGameLoop(World world, String avatarName) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        boolean quitGameStarted = false; // Flag to track quit sequence
        while (true) {
            StdDraw.clear(Color.BLACK);
            ter.drawTiles(world.getTiles());
            int positionX = (int) StdDraw.mouseX();
            int positionY = (int) StdDraw.mouseY();
            world.displayHUD(world, positionX, positionY);
            world.displayHUD(world,avatarName);
            if (StdDraw.hasNextKeyTyped()) {
                char characterMovement = StdDraw.nextKeyTyped();
                if (characterMovement == ':' && !quitGameStarted) {
                    quitGameStarted = true;
                } else if ((characterMovement == 'Q' || characterMovement == 'q') && quitGameStarted) {
                    saveGame(world); // Save the game state
                    saveActions(actions.toString()); // Save the actions
                    System.exit(0); // Quit the game
                } else {
                    // If not in the middle of a quit sequence, handle movement and record action
                    handleMovement(world, characterMovement);
                    quitGameStarted = false; // Reset flag if other keys are pressed
                }
            }
            StdDraw.show();
        }
    }

    private static void handleMovement(World world, char movement) {
        switch (Character.toLowerCase(movement)) {
            case 'w': world.tryMove(0, 1); break;
            case 'a': world.tryMove(-1, 0); break;
            case 's': world.tryMove(0, -1); break;
            case 'd': world.tryMove(1, 0); break;
        }
        if (!isReplay) { // Record actions only if it's not a replay
            actions.append(movement);
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
        saveActions(actions.toString());
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
        actions.setLength(0); // Clear the current actions
        String loadedActions = loadActions();
        if (loadedActions != null) {
            actions.append(loadedActions); // Append the loaded actions
        }
        return null;
    }
    private static void saveActions(String actions) {
        try (FileWriter writer = new FileWriter("action.txt", false)) { // false to overwrite
            writer.write(actions);
        } catch (IOException e) {
            System.err.println("Error saving avatar name.");
            e.printStackTrace();
        }
    }
    private static String loadActions() {
        File file = new File("action.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    actions.append(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error reading actions file.");
                e.printStackTrace();
                return null;
            }
        }
        return actions.toString();
    }
    private static void replayActions(String actions, long seed) {
        World world = loadGame();
        if (world == null) {
            world = new World(seed); // If no save, start a new game with the seed
        }
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        // Simulate and render each action
        for (char action : actions.toCharArray()) {
            handleMovement(world, action);
            ter.renderFrame(world.getTiles());
            StdDraw.show();
            StdDraw.pause(200); // This is just for visualization, adjust as needed
        }
        runGameLoop(world, loadAvatarName());
        isReplay = false;
    }
}





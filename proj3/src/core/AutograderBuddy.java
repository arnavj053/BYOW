package core;

import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        World world = null;
        int i = 0;
        StringBuilder seedBuilder = new StringBuilder();

        // Parse the input string
        while (i < input.length()) {
            char c = input.charAt(i);
            i++;

            if (c == 'N') {
                // Handle New Game
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    seedBuilder.append(input.charAt(i));
                    i++;
                }
                long seed = Long.parseLong(seedBuilder.toString());
                world = new World(seed); // Initialize with the new seed
                world.initializeWorld(seed); // Initialize the world
            } else if (c == 'L') {
                // Handle Load Game
                world = Main.loadGame(); // Use the existing loadGame method
                if (world == null) {
                    return null; // If loading fails, return null
                }
            } else {
                // Handle other actions like movement
                if (world != null) {
                    world.simulateMovement(c);
                }
            }
        }

        return world != null ? world.getTiles() : null;
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character();
    }
}
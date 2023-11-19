package core;

import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * This method should simulate the world generation process without actually
     * rendering anything to the screen. The input string will be used as a seed for
     * the random number generator to ensure reproducibility.
     *
     * @param input the input string to feed to the world generator
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        long seed = inputToSeed(input);
        World world = new World(seed); // Modify the World constructor to accept a seed parameter
        return world.getTiles();
    }

    /**
     * Converts the input string into a numeric seed.
     *
     * @param input the input string
     * @return the numeric seed
     */
    private static long inputToSeed(String input) {
        // Implement this method to convert the input string to a numeric seed
        // For example, by parsing the numeric part of the input.
        // This is a stub and needs proper implementation.
        return Long.parseLong(input.replaceAll("[^0-9]", ""));
    }

    /**
     * Checks whether the given tile is a floor/ground tile.
     *
     * @param t the tile to check
     * @return true if the tile is a floor tile, false otherwise
     */
    public static boolean isGroundTile(TETile t) {
        return t.equals(Tileset.FLOOR);
    }

    /**
     * Checks whether the given tile is a wall/boundary tile.
     *
     * @param t the tile to check
     * @return true if the tile is a wall tile, false otherwise
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.equals(Tileset.WALL);
    }

    // Additional methods to support autograding tests, if necessary...
}

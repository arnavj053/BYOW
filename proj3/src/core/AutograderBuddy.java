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
        long seed = 0;
        boolean isNewGame = false;
        StringBuilder seedBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == 'n' && !isNewGame) {
                isNewGame = true;
            } else if (isNewGame && Character.isDigit(c)) {
                seedBuilder.append(c);
            } else if (isNewGame && c == 's') {
                seed = Long.parseLong(seedBuilder.toString());
                world = new World(seed);
                world.initializeWorld(seed);
                isNewGame = false;
            } else if (c == 'l' && !isNewGame) {
                world = Main.loadGame();
                if (world == null) {
                    return null;
                }
            } else if (!isNewGame) {
                if (c == ':' && i + 1 < input.length() && input.charAt(i + 1) == 'q') {
                    Main.saveGame(world);
                    break;
                } else {
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
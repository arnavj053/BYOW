package core;
import tileengine.Tileset;
import tileengine.TETile;
import tileengine.TERenderer;
import java.util.Random;

public class World {
    public static Random random = new Random();
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int WINDOW_WIDTH = 80;
    public static final int WINDOW_HEIGHT = 45;
    public TETile[][] tiles;

    public World() {
        tiles = new TETile[WIDTH][HEIGHT];
        initializeTiles(WIDTH, HEIGHT);
    }

    private void initializeTiles(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public static void main(String[] args) {
        World newWorld = new World();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(newWorld.getTiles());
    }
}

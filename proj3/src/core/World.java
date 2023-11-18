package core;
import tileengine.Tileset;
import tileengine.TETile;
import tileengine.TERenderer;
import java.util.Random;

import core.Main;

public class World {
    public static Random random = new Random();
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int WINDOW_WIDTH = 80;
    public static final int WINDOW_HEIGHT = 45;
    public TETile[][] tiles;

    private int lastRoomWidth = 0;
    private int lastRoomHeight = 0;

    private static final double DESIRED_COVERAGE = 0.4; // 25% of the grid

    public World() {
        tiles = new TETile[WIDTH][HEIGHT];
        initializeTiles(WIDTH, HEIGHT);
        createRooms();
    }

    private void createRooms() {
        int areaCovered = 0;
        int totalArea = WIDTH * HEIGHT;
        int targetArea = (int) (totalArea * DESIRED_COVERAGE);

        while (areaCovered < targetArea) {
            if (createRandomRoom()) {
                areaCovered += calculateRoomArea(); // Implement this method to calculate the area of the last created room
            }
        }
    }

    private boolean createRandomRoom() {
        // Randomly determine room dimensions and position
        int roomWidth = random.nextInt(10) + 4; // Room width between 3 and 12
        int roomHeight = random.nextInt(10) + 4; // Room height between 3 and 8
        int roomStartX = random.nextInt(WIDTH - roomWidth); // X-coordinate of top-left corner
        int roomStartY = random.nextInt(HEIGHT - roomHeight); // Y-coordinate of top-left corner


        if (!isSpaceFree(roomStartX, roomStartY, roomWidth, roomHeight)) {
            return false;
        }

        // Fill in the room tiles and add walls
        for (int x = roomStartX; x < roomStartX + roomWidth; x++) {
            for (int y = roomStartY; y < roomStartY + roomHeight; y++) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
        addWalls(roomStartX, roomStartY, roomWidth, roomHeight);

        // Update the last room dimensions
        lastRoomWidth = roomWidth;
        lastRoomHeight = roomHeight;

        return true;
    }

    private int calculateRoomArea() {
        return lastRoomWidth * lastRoomHeight;
    }
    private boolean isSpaceFree(int startX, int startY, int width, int height) {
        // Check if the space is free for room placement
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addWalls(int startX, int startY, int width, int height) {
        // Top and bottom walls
        for (int x = startX; x < startX + width; x++) {
            tiles[x][startY] = Tileset.WALL;
            tiles[x][startY + height - 1] = Tileset.WALL;
        }
        // Left and right walls
        for (int y = startY; y < startY + height; y++) {
            tiles[startX][y] = Tileset.WALL;
            tiles[startX + width - 1][y] = Tileset.WALL;
        }
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

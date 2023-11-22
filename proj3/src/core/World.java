package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.Tileset;
import tileengine.TETile;
import tileengine.TERenderer;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

public class World {
    private Random random;
    private List<Room> rooms;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int WINDOW_WIDTH = 80;
    public static final int WINDOW_HEIGHT = 45;
    private TETile[][] tiles;
    private int lastRoomWidth = 0;
    private int lastRoomHeight = 0;
    private Map<Room, Room> parent; // To track the parent of each room
    private static final double DESIRED_COVERAGE = 0.25; // 25% of the grid
    private class Room {
        int x, y, width, height;

        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public World(long seed) {
        random = new Random(seed);
        tiles = new TETile[WIDTH][HEIGHT];
        rooms = new ArrayList<>();
        parent = new HashMap<>();
        initializeTiles(WIDTH, HEIGHT);
        createRooms();
        connectRooms();
        addWallsAroundHallways();
    }

    // Union-find methods
    private Room find(Room room) {
        // Path compression can be implemented here
        while (room != parent.get(room)) {
            room = parent.get(room);
        }
        return room;
    }

    private void union(Room a, Room b) {
        Room rootA = find(a);
        Room rootB = find(b);
        if (rootA != rootB) {
            parent.put(rootA, rootB); // Join the two sets
        }
    }

    private void createRooms() {
        int areaCovered = 0;
        int totalArea = WIDTH * HEIGHT;
        int targetArea = (int) (totalArea * DESIRED_COVERAGE);

        while (areaCovered < targetArea) {
            if (createRandomRoom()) {
                areaCovered += calculateRoomArea();
            }
        }
    }

    private boolean createRandomRoom() {
        // Randomly determine room dimensions and position
        int roomWidth = random.nextInt(10) + 4;
        int roomHeight = random.nextInt(10) + 4;
        int roomStartX = random.nextInt(WIDTH - roomWidth);
        // X-coordinate of top-left corner
        int roomStartY = random.nextInt(HEIGHT - roomHeight);
        // Y-coordinate of top-left corner

        if (!isSpaceFree(roomStartX, roomStartY, roomWidth, roomHeight)) {
            return false;
        }

        // Add room to the tiles array
        for (int x = roomStartX; x < roomStartX + roomWidth; x++) {
            for (int y = roomStartY; y < roomStartY + roomHeight; y++) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
        addWalls(roomStartX, roomStartY, roomWidth, roomHeight);

        // Create a new Room object and add it to the list of rooms
        Room room = new Room(roomStartX, roomStartY, roomWidth, roomHeight);
        rooms.add(room);

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
        parent.put(room, room);
        return true;
    }
    
    private void connectRooms() {
        // Sort the rooms by their x-coordinate (or y-coordinate) to make it simple
        rooms.sort(Comparator.comparingInt(r -> r.x));

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room roomA = rooms.get(i);
            Room roomB = rooms.get(i + 1);

            // Make sure the rooms are not already connected
            if (find(roomA) != find(roomB)) {
                // Connect the rooms by creating a hallway between them
                createHallwayBetweenRooms(roomA, roomB);
                // Now that the rooms are connected, union their sets
                union(roomA, roomB);
            }
        }
    }

    private void createHallwayBetweenRooms(Room roomA, Room roomB) {
        // Determine the center points of room A and room B
        Point centerA = new Point(roomA.x + roomA.width / 2, roomA.y + roomA.height / 2);
        Point centerB = new Point(roomB.x + roomB.width / 2, roomB.y + roomB.height / 2);

        // Connect horizontally first
        int horizontalStartX = Math.min(centerA.x, centerB.x);
        int horizontalEndX = Math.max(centerA.x, centerB.x);
        for (int x = horizontalStartX; x <= horizontalEndX; x++) {
            if (tiles[x][centerA.y] != Tileset.FLOOR) {
                tiles[x][centerA.y] = Tileset.FLOOR;
            }
        }

        // Then connect vertically
        int verticalStartY = Math.min(centerA.y, centerB.y);
        int verticalEndY = Math.max(centerA.y, centerB.y);
        for (int y = verticalStartY; y <= verticalEndY; y++) {
            if (tiles[centerB.x][y] != Tileset.FLOOR) {
                tiles[centerB.x][y] = Tileset.FLOOR;
            }
        }

        // Ensure the entry points of both rooms are floor, not walls
        tiles[centerA.x][centerA.y] = Tileset.FLOOR;
        tiles[centerB.x][centerB.y] = Tileset.FLOOR;
    }

    // Call this method after creating all rooms and hallways

    /**
     * @source chatgpt.com
     */
    private void addWallsAroundHallways() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                // Check if the current tile is a floor tile
                if (tiles[x][y] == Tileset.FLOOR) {
                    // Look at all adjacent tiles
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int adjX = x + i;
                            int adjY = y + j;
                            // Check if the adjacent tile is within bounds
                            if (adjX >= 0 && adjX < WIDTH && adjY >= 0 && adjY < HEIGHT) {
                                // If the adjacent tile is nothing, change it to a wall
                                if (tiles[adjX][adjY] == Tileset.NOTHING) {
                                    tiles[adjX][adjY] = Tileset.WALL;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("Point(x=%d, y=%d)", x, y);
        }
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
    /**
     * @source chatgpt.com
     */
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

    public static void displayHUD (World currentWorld, int posX, int posY) {
        String currentTile = currentWorld.tiles[posX][posY].description();
        if (currentTile == null) {
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 14));
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textRight(World.WIDTH - 4, World.HEIGHT - 3, "Nothing");
            StdDraw.show();
        }
        else {
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 14));
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textRight(World.WIDTH - 4, World.HEIGHT - 3, currentTile);
            StdDraw.show();
        }
    }

    public static void main(String[] args) {
        World newWorld = new World(32324324);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(newWorld.getTiles());
    }
}

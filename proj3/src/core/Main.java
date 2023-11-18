package core;

import java.util.Scanner;

public class Main {
    public void main(String[] args) {

    }
    public class GameMenu {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("CS61B: THE GAME");
                System.out.println("[N] New Game");
                System.out.println("[L] Load Game");
                System.out.println("[Q] Quit");
                System.out.print("Please enter an option: ");

                String input = scanner.nextLine().toUpperCase();

                switch (input) {
                    case "N":
                        System.out.println("Starting new game...");
                        // Add code here to start a new game
                        break;
                    case "L":
                        System.out.println("Loading game...");
                        // Add code here to load a game
                        break;
                    case "Q":
                        System.out.println("Quitting game. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }

            scanner.close();
        }
    }

}




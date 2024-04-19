package Model;

import java.util.Scanner;

public class GameController {
    private BlackBoxBoard board;
    private Player player;
    private Player setter;
    private Player experimenter;
    private int score;

    public GameController(Player setter, Player experimenter) {
        this.board = new BlackBoxBoard();
        this.setter = setter; // The player who places atoms
        this.experimenter = experimenter; // The player who guesses atom locations
        this.score = 0; // Initialize score at zero
    }
    //add validation checks so that only rays at edge cells


    public void setupBoard() {
        System.out.println("Setter is placing atoms.");
        board.placeRandomAtoms(6); // Use method from BlackBoxBoard to place atoms
        System.out.println("Atoms placed.");
    }

    /**
     * Method for experimenter to send rays and observe reactions.
     * The experimenter selects an entry point and direction for each ray,
     * observes the path, and notes whether the ray is absorbed or deflected.
     */
    public void sendRays() {
        System.out.println("Experimenter starts sending rays:");
        Scanner scanner = new Scanner(System.in);
        String sendRay = new String();

        do {
            System.out.print("Enter ray entry point coordinates on the edge of the board (x, y, z): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();


            System.out.print("Enter direction (0: YL, 1: YR, 2: XU, 3: XD, 4: ZU, 5: ZD): ");
            int dirIndex = scanner.nextInt();

            // Validate the direction index to ensure it's within the range of defined directions
            if (dirIndex < 0 || dirIndex >= Direction.values().length) {
                System.out.println("Invalid direction index. Please enter a number between 0 and 5.");
                continue;
            }

            Direction direction = Direction.values()[dirIndex];
            Ray ray = new Ray(board, new BlackBoxBoard.Point3D(x, y, z), direction);
            System.out.println("Ray's Path: " + ray.getPath());

            if (ray.isAbsorbed()) {
                System.out.println("The ray was absorbed!");
            } else if (ray.isDeflected60()) {
                System.out.println("The ray is deflected by 60 degrees.");
            } else if (ray.isDeflected120()) {
                System.out.println("The ray is deflected by 120 degrees.");
            } else if (ray.isRayReversed()) {
                System.out.println("The ray is reversed.");
            } else {
                System.out.println("The ray passed straight through without interactions.");
            }

            System.out.print("Send another ray? (yes/no): ");
            sendRay = scanner.next();
        } while(sendRay.equals("yes"));

        scanner.close();
    }

    /**
     * Method for experimenter to guess the locations of atoms.
     * The experimenter guesses the positions of atoms based on their observations from the rays sent.
     */
    public void guessAtoms() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Guess the locations of the atoms. Enter positions (x, y, z):");

        for (int i = 0; i < 6; i++) { // Assuming there are 6 atoms
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();

            BlackBoxBoard.Point3D guess = new BlackBoxBoard.Point3D(x, y, z);
            if (board.getCell(guess).hasAtom()) {
                System.out.println("Correct guess at " + guess);
                score += 5; // Increase score for each correct location guess
            } else {
                System.out.println("Incorrect guess at " + guess);
                score -= 5; // Decrement score for each incorrect location guess
            }
        }
        System.out.println("Round 1 is done. Your score: " + score);
        scanner.close();
    }

    public static void main(String[] args) {
        Player setter = new Player(Player.PlayerRole.SETTER);
        Player experimenter = new Player(Player.PlayerRole.EXPERIMENTER);
        GameController game = new GameController(setter, experimenter);
        game.setupBoard();
        game.sendRays();
        game.guessAtoms();
    }
}

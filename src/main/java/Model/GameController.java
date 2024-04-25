package Model;

import java.util.Scanner;

public class GameController {
    public enum Marker {
        BLACK, // for absorption
        WHITE, // for deflection
        PINK // for exit without interaction
    }


    private BlackBoxBoard board;
    private Player setter;
    private Player experimenter;
    private int score;

    public GameController(Player setter, Player experimenter) {
        this.board = new BlackBoxBoard();
        this.setter = setter;
        this.experimenter = experimenter;
        this.score = 0;
    }

    public void setupBoard() {
        System.out.println("Setter is placing atoms.");
        board.placeRandomAtoms(6);
        System.out.println("Atoms placed.");
    }

    /* Not needed anymore
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

            if (dirIndex < 0 || dirIndex >= Direction.values().length) {
                System.out.println("Invalid direction index. Please enter a number between 0 and 5.");
                continue;
            }

            Direction direction = Direction.values()[dirIndex];
            Ray ray = new Ray(board, new BlackBoxBoard.Point3D(x, y, z), direction);
            System.out.println("Ray's path: " + ray.getPath());
            updateScore(ray, x, y, z);

            System.out.print("Send another ray? (yes/no): ");
            sendRay = scanner.next();
        } while (sendRay.equals("yes"));

    }
     */


    private void updateScore(Ray ray, int x, int y, int z) {
        Marker markerType = Marker.PINK; // Default for passing straight through without interaction
        if (ray.isAbsorbed()) {
            System.out.println("The ray was absorbed!");
            markerType = Marker.BLACK;
            score += 1; // Increase score for correct absorption
        } else if (ray.isDeflected60() || ray.isDeflected120() || ray.isRayReversed()) {
            System.out.println("The ray is deflected, reversed, or had a similar interaction.");
            markerType = Marker.WHITE;
            score += 1; // Score for interactions other than absorption
        }
        placeMarker(x, y, z, markerType);
    }

    private void placeMarker(int x, int y, int z, Marker marker) {
        System.out.println("Placing " + marker + " marker at entry point (" + x + ", " + y + ", " + z + ").");
    }

    public void guessAtoms() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Guess the locations of the atoms. Enter positions (x, y, z):");

        for (int i = 0; i < 6; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();

            BlackBoxBoard.Point3D guess = new BlackBoxBoard.Point3D(x, y, z);
            if (board.getCell(guess).hasAtom()) {
                System.out.println("Correct guess at " + guess);
                score += 5; // Increase score for each correct location guess
            } else {
                System.out.println("Incorrect guess at " + guess);
                score -= 5; // Decrease score for each incorrect location guess
            }
        }
        System.out.println("Round 1 is done. Your score: " + score);
        scanner.close();
    }}
//hide the atoms and board by commenting the print in blackboxboard
//    public static void main(String[] args) {
//        Player setter = new Player(Player.PlayerRole.SETTER);
//        Player experimenter = new Player(Player.PlayerRole.EXPERIMENTER);
//        GameController game = new GameController(setter, experimenter);
//        game.setupBoard();
//        game.sendRays();
//        game.guessAtoms();
//    }
//}

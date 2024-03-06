package Model;

public class ModelMain {
    //testing to see Hashmap is mapped correctly.
    public static void main(String[] args) {

        BlackBoxBoard blackBoxBoard = new BlackBoxBoard();
        System.out.println("Before placing atoms");
        blackBoxBoard.printBoard();

        blackBoxBoard.placeRandomAtoms(6);

        System.out.println("After palcing atoms ");
        blackBoxBoard.printBoard();


        /*
        BlackBoxBoard bbBoard = new BlackBoxBoard();
        System.out.println(bbBoard.getBoardSize());*/
    }
}



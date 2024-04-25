package Model;
import java.util.HashMap;
import java.util.Map;

import static Model.BlackBoxBoard.edgeCells;
import static Model.Direction.XU;
import static Model.Direction.XD;
import static Model.Direction.YR;
import static Model.Direction.YL;
import static Model.Direction.ZU;
import static Model.Direction.ZD;

// Ray Node class to store each individual ray node and its specific direction and coordinates in 3D map
public class RayNode {
    public int nodeNumber;
    public BlackBoxBoard.Point3D coordinates;
    public Direction direction;
    static Map<Integer, RayNode> rayNodeMap = new HashMap<>();


    // method to print all the ray nodes in the map
    public static void printRayNodes() {
        for (Map.Entry<Integer, RayNode> entry : rayNodeMap.entrySet()) {
            int key = entry.getKey();
            RayNode value = entry.getValue();
            System.out.println("Ray Node " + key + ": " + value);
            System.out.println("Coordinates: " + value.coordinates + ", Direction: " + value.direction + "\n");
        }
    }

    // Getter method to retrieve the coordinates of a ray node by its key value
    public static BlackBoxBoard.Point3D getNodeCoordinates(int nodeNumber) {
        RayNode rayNode = rayNodeMap.get(nodeNumber);
        if (rayNode != null) {
            return rayNode.coordinates;
        } else {
            System.out.println("Incorrect node value.");
            return null;
        }
    }

    // Getter method to retrieve the direction of a ray node by its key value
    public static Direction getNodeDirection(int nodeNumber) {
        RayNode rayNode = rayNodeMap.get(nodeNumber);
        if (rayNode != null) {
            return rayNode.direction;
        } else {
            System.out.println("Incorrect node value.");
            return null;
        }
    }

    // Getter method to retrieve the node number by the coordinates and direction
    public static int getNodeNumber(BlackBoxBoard.Point3D coordinates, Direction direction) {
        // Traverse through the ray node map and find match
        for (Map.Entry<Integer, RayNode> entry : rayNodeMap.entrySet()) {
            RayNode rayNode = entry.getValue();
            if (rayNode.coordinates.equals(coordinates) && rayNode.direction == direction) {
                return entry.getKey();
            }
        }
        // If no matching node is found, return -1
        return -1;
    }

    public static void initializeNodes() {

        // initialize nodes 1, 2, and 54 because currently it's easiest to implement
        // the generation method with these nodes already set
        // otherwise it'll be complicated to figure out the edge cases
        BlackBoxBoard.Point3D coordinates = new BlackBoxBoard.Point3D(0, -4, 4);
        Direction direction = Direction.YL;
        RayNode rayNode = new RayNode(2, coordinates, direction);
        rayNodeMap.put(2, rayNode);

        direction = Direction.XU;
        rayNode = new RayNode(1, coordinates, direction);
        rayNodeMap.put(1, rayNode);

        direction = Direction.ZU;
        rayNode = new RayNode(54, coordinates, direction);
        rayNodeMap.put(54, rayNode);

        Direction prevDir = direction;

        int cell = 1;
        // initialize nodes 53 until 3
        for (int node = 53; node > 2; node --) {

            coordinates = edgeCells.get(cell);

            // each corner cell has 3 nodes, so that's why this loops thrice
            if (HexCell.isCornerCell(coordinates)) {
                for (int k = 0; k < 3; k ++) {
                    prevDir = getAdjDir(prevDir, coordinates);
                    rayNode = new RayNode(node, coordinates, prevDir);
                    rayNodeMap.put(node, rayNode);
                    // only decrement node twice bc it'll be decremented at the end of each loop anyway
                    if (k < 2) {
                        node -= 1;
                    }
                }
            }

            // all other cells have 2 nodes so loop twice
            else {
                for (int m = 0; m < 2; m++) {
                    prevDir = getAdjDir(prevDir, coordinates);
                    rayNode = new RayNode(node, coordinates, prevDir);
                    rayNodeMap.put(node, rayNode);
                    // only decrement node once bc it'll be decremented at the end of each loop anyway
                    if (m < 1) {
                        node -= 1;
                    }
                }
            }

            cell += 1;
        }

    }


    // sets the ray nodes
    public RayNode(int nodeNumber, BlackBoxBoard.Point3D coordinates, Direction direction) {
        this.nodeNumber = nodeNumber;
        this.coordinates = coordinates;
        this.direction = direction;
    }


    // returns the reverse direction
    public static Direction getRevDir (Direction dir) {
        Direction revDir = Direction.Error;

        revDir = switch (dir) {
            case XU -> XD;
            case XD -> XU;
            case ZU -> ZD;
            case ZD -> ZU;
            case YL -> YR;
            case YR -> YL;
            default -> revDir;
        };

        return revDir;
    }

    // returns the adjacent direction
    public static Direction getAdjDir (Direction dir, BlackBoxBoard.Point3D cell) {
        Direction adjDir = Direction.Error;

        int x = cell.x;
        int y = cell.y;
        int z = cell.z;

        switch (dir) {
            case XU:
                if(x >= 0) {
                    adjDir = ZU;
                }
                else {
                    adjDir = YL;
                }
                break;

            case XD:
                if (x > 0) {
                    adjDir = YR;
                }
                else {
                    adjDir = ZD;
                }
                break;

            case ZU:
                if (z > 0) {
                    adjDir = XU;
                }
                else {
                    adjDir = YR;
                }
                break;

            case ZD:
                if (z >= 0) {
                    adjDir = YL;
                }
                else {
                    adjDir = XD;
                }
                break;

            case YL:
                if (y > 0) {
                    adjDir = ZD;
                }
                else {
                    adjDir = XU;
                }
                break;

            case YR:
                if (y >= 0) {
                    adjDir = XD;
                }
                else {
                    adjDir = ZU;
                }
                break;

        }

        return adjDir;
    }

}
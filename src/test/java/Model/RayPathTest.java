package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RayPathTest {
    BlackBoxBoard testBoard = new BlackBoxBoard();
    BlackBoxBoard.Point3D exitPoint;
    BlackBoxBoard.Point3D atom;
    Direction exitDir;
    int exitNode;
    Ray ray;

    @BeforeEach
    void setTestBoard() {
        RayNode.initializeNodes();
    }

    @Test
    void straightPathTest() {
        ray = new Ray(testBoard, 37);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isAbsorbed());
        assertFalse(ray.isRayReversed());
        assertEquals(10, exitNode);
    }

    @Test
    void edgePathTest() {
        ray = new Ray(testBoard, 2);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isAbsorbed());
        assertFalse(ray.isRayReversed());
        assertEquals(45, exitNode);
    }

    @Test
    void edgePathWithAtomTest() {
        atom = new BlackBoxBoard.Point3D(-3,4,-1);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 29);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isAbsorbed());
        assertEquals(-1, exitNode);
    }

    @Test
    void edgePathWithCITest() {
        atom = new BlackBoxBoard.Point3D(2,-3,1);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 45);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected120());
        assertFalse(ray.isRayReversed());
        assertFalse(ray.isAbsorbed());
        assertTrue(ray.isDeflected60());
        assertEquals(49, exitNode);
    }

    @Test
    void deflection60Test() {
        atom = new BlackBoxBoard.Point3D(0,0,0);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 35);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected120());
        assertFalse(ray.isAbsorbed());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isDeflected60());
        assertEquals(21, exitNode);

        ray = new Ray(testBoard, 3);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected120());
        assertFalse(ray.isAbsorbed());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isDeflected60());
        assertEquals(17, exitNode);

        ray = new Ray(testBoard, 44);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertTrue(ray.isDeflected60());
        assertEquals(30, exitNode);

    }

    @Test
    void deflection120Test() {
        atom = new BlackBoxBoard.Point3D(0,-1,1);
        testBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(0,0,0);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 10);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isAbsorbed());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isDeflected120());
        assertEquals(17, exitNode);

        ray = new Ray(testBoard, 46);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isAbsorbed());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isDeflected120());
        assertEquals(39, exitNode);
    }

    @Test
    void reversalTest() {
        atom = new BlackBoxBoard.Point3D(0,-1,1);
        testBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(-1,1,0);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 37);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isAbsorbed());
        assertTrue(ray.isRayReversed());
        assertEquals(37, exitNode);

        ray = new Ray(testBoard, 10);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isAbsorbed());
        assertTrue(ray.isRayReversed());
        assertEquals(10, exitNode);
    }

    @Test
    void absorbedTest() {
        atom = new BlackBoxBoard.Point3D(0,0,0);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 37);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isAbsorbed());
        assertEquals(-1, exitNode); // should give error since there is no exitpoint

        ray = new Ray(testBoard, 10);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isDeflected60());
        assertFalse(ray.isDeflected120());
        assertFalse(ray.isRayReversed());
        assertTrue(ray.isAbsorbed());
        assertEquals(-1, exitNode);
    }

    @Test
    void complexPathTest() {
        atom = new BlackBoxBoard.Point3D(1,1,-2);
        testBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(-3,2,1);
        testBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(-3,3,0);
        testBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(1,-2,1);
        testBoard.placeAtom(atom);
        atom = new BlackBoxBoard.Point3D(2,-1,-1);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 28);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertFalse(ray.isAbsorbed());
        assertEquals(28, exitNode);
    }

    @Test
    void rayEnteringIntoAtomTest() {
        atom = new BlackBoxBoard.Point3D(3,1,-4);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 34);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertTrue(ray.isAbsorbed());
        assertEquals(-1, exitNode);
    }

    @Test
    void rayEnteringIntoCITest() {
        atom = new BlackBoxBoard.Point3D(3,1,-4);
        testBoard.placeAtom(atom);

        ray = new Ray(testBoard, 33);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertTrue(ray.isRayReversed());
        assertEquals(-1, exitNode);

        ray = new Ray(testBoard, 32);
        exitPoint = ray.getExitPoint();
        exitDir = ray.getExitDir();
        exitNode = RayNode.getNodeNumber(exitPoint, exitDir);

        assertTrue(ray.isRayReversed());
        assertEquals(-1, exitNode);
    }
}

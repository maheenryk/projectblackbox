package Model;

/* Ray class :
 */
public enum Direction {
    YR, // ray is on y axis going right
    YL, // ray is on y axis going left
    XU, // ray is on x axis going up
    XD, // ray is on x axis going down
    ZU, // ray is on z axis going up
    ZD, // ray is on z axis going down

    Absorbed,
    Error;
}

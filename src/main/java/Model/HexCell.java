package Model;

/* Define hex cell in our board
   Properties of HexCell :
    1.position
    2.contains info about neighbouring cells usually each  heex has 6 neighbours except the edge hexes
    3.contains info about if atom is present or not

 */


public class HexCell {
    //instance variables
    //define our xyz co-ords
    private int x;
    private int y;
    private int z;

    // hexagon with atom present or not first it
    private boolean hasAtom;

    //constructor
    public HexCell(int x,int  y,int z, boolean hasAtom ){
        //error handling for co-ords the sum must always add to 0
        if((x + y +z) != 0){
            throw  new IllegalArgumentException("Invalid sum must be equal to 0");
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.hasAtom = hasAtom;
    }

    //getters for co-ords


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean hasAtom() {
        return hasAtom;
    }

    public String getCoOrdinates(){
        return "[" + x + "," + y + "," + z + "]";
    }


    //set co-ords
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setHasAtom(boolean hasAtom) {
        this.hasAtom = hasAtom;
    }
}

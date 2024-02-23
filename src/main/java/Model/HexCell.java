package Model;

/* Define hex cell in our board
   Properties of HexCell :
    1.a reference to the Point3D (points of it co-ordinates)
    2.a reference to atom class to set an atom with appropriate validation checks
        >get the HexCell with the  atom details
        >check if cell has atom
        >a toString method to represent our HexCell object

 */


public class HexCell {
    //reference to its co-ordinates
    private  final BlackBoxBoard.Point3D points;
    //create reference to atom
    private Atom atom;

    public HexCell(BlackBoxBoard.Point3D points) {
        this.points = points;
        //at the start all HexCell do not contain an atom
        this.atom = null;

    }


    public boolean setAtom(Atom atom) {
        //prevents atom being placed in a HexCell that already contains an atom so essentially prevents overwritting
        if(this.atom == null){
            this.atom = atom;
            return true;
        }
        //HexCell already contains atom
        return false;

    }
    public Atom getAtom() {
        // returns the Atom details like location if there's no atom in the cell it will be null
        //will be beneficial when implementing ray logic
        //will add more details as game progresses
        return this.atom;
    }
    public boolean hasAtom() {

        return this.atom != null;
    }


    //returns a string rep of our HexCell
    @Override
    public String toString() {

        if(hasAtom()){
            return "HexCell{ " +"co-ordinates:" +points + ", atom:" +atom +"}";
        }else{
            return "HexCell{ " +"co-ordinates:" +points + ", atom: no atom is present"  +"}";
        }
    }

}

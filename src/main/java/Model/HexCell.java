package Model;

/* Define hex cell in our board
   Properties of HexCell :
    1.positions?
    2.create a reference to atom class to set or check if hex cell ha an atom present

 */


public class HexCell {
    /* 1.define instance variables
       2. make them immutable, so they cannot be changed once set.
    */

    /*private final int x;
      private final int y;
      private final int z;
      hexagon with atom present or not.
      also final as once game starts twe cannot add or remove atoms once they have been set.
    */

    //create an instance variable of type atom.
     private Atom atom;

     public HexCell() {
        //at the start all hex cells do not contain an atom
         this.atom = null;

     }

      //set atom
      public void setAtom(Atom atom) {
        this.atom = atom;
      }
      public boolean hasAtom() {
        return this.atom != null;
      }

      //returns a string rep of our HexCell

    @Override
    public String toString() {
         String atomCheck;
         if(this.atom == null){
           atomCheck= "atom is not present";
         }else{
            atomCheck ="atom is present";
         }
         return  "HexCell; " + atomCheck;
    }




  /*  public int getX() {
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

      public String getCoOrdinates() {
        return "[" + x + "," + y + "," + z + "]";
    }
    */




}

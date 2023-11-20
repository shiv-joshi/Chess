package Data;

public class Pair {
    public int x;
    public int y;
    /**
     * this method makes a pair based on coardinates
     * 
     * @param x x coardinates
     * @param y coardinate
     */
    public Pair(int x, int y){
        this.x = x;
        this.y = y;
    }

    
   /**
     *Checks to see if the pair is valid or not
     * 
     * @return .returns if pair is valid
     */
    public boolean isValidPair(){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    //overrides method in object class
    /**
     * returns string of x and y coardinates
     *
     * @param return returns x and y coardinates
     */
    @Override
    public String toString(){
        return "( " + x + " , " + y +" )";
    }

    /**
     * This is a comparative method to compare if its the same piece on the same boardinates
     * 
     * @param object current object being compared
     * @return returns true if it is the same object
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Pair)) {
            return false;
        }
        Pair p = (Pair) other;
        return (this.x == p.x && this.y == p.y);
    }
}

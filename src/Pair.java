/**
 * The class `Pair` represents a generic pair of two values with methods for getting, setting, and
 * comparing the values.
 */
public class Pair<T1, T2>{
    private T1 first;
    private T2 second;

    public Pair(T1 first, T2 second){
        this.first = first;
        this.second = second;
    }

    /**
     * The `getFirst` function in Java returns the first element.
     * 
     * @return T1
     */
    public T1 getFirst(){
        return first;
    }

    /**
     * The `getSecond()` function in Java returns the second element.
     * 
     * @return T2
     */
    public T2 getSecond(){
        return second;
    }

    /**
     * The function setFirst assigns a value to the variable first.
     * 
     * @param first The parameter 'first'.
     */
    public void setFirst(T1 first){
        this.first = first;
    }

    /**
     * The function setSecond assigns a value to the variable second.
     * 
     * @param first The parameter 'second'.
     */
    public void setSecond(T2 second){
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair<T1, T2> p = (Pair<T1, T2>) obj;
        return this.first.equals(p.first) && this.second.equals(p.second);
    }
}

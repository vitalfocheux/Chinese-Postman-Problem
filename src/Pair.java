public class Pair<T1, T2>{
    private T1 first;
    private T2 second;

    public Pair(T1 first, T2 second){
        this.first = first;
        this.second = second;
    }

    public T1 getFirst(){
        return first;
    }

    public T2 getSecond(){
        return second;
    }

    public void setFirst(T1 first){
        this.first = first;
    }

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

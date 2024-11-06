package m1graphs2024;

/**
 * A mutable integer class that allows the integer value to be modified
 */
public class MutableInteger {

    private int value;

    /**
     * Constructs a MutableInteger with the specified initial value
     *
     * @param value the initial value of the MutableInteger
     */
    public MutableInteger(int value) {
        this.value = value;
    }

    /**
     * Returns the current value of this MutableInteger
     *
     * @return the current value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of this MutableInteger to the speficied value
     *
     * @param value the new value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Increments the value of this MutableInteger by 1
     */
    public void increment() {
        value++;
    }
}

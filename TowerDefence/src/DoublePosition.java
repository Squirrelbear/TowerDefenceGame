/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * DoublePosition class:
 * Used to represent a single position x,y with double precision.
 */
public class DoublePosition {
    /**
     * Down moving unit vector.
     */
    public static final DoublePosition DOWN = new DoublePosition(0,1);
    /**
     * Up moving unit vector.
     */
    public static final DoublePosition UP = new DoublePosition(0,-1);
    /**
     * Left moving unit vector.
     */
    public static final DoublePosition LEFT = new DoublePosition(-1,0);
    /**
     * Right moving unit vector.
     */
    public static final DoublePosition RIGHT = new DoublePosition(1,0);
    /**
     * Zero unit vector.
     */
    public static final DoublePosition ZERO = new DoublePosition(0,0);

    /**
     * X coordinate.
     */
    public double x;
    /**
     * Y coordinate.
     */
    public double y;

    /**
     * Sets the value of Position.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public DoublePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor to create a new Position using the values in another.
     *
     * @param positionToCopy Position to copy values from.
     */
    public DoublePosition(DoublePosition positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }

    /**
     * Copy constructor to create a new Position using the values in another.
     *
     * @param positionToCopy Position to copy values from.
     */
    public DoublePosition(Position positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }

    /**
     * Sets the Position to the specified x and y coordinate.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates this position by adding the values from the otherPosition.
     *
     * @param otherPosition Other Position to add to this one.
     */
    public void add(DoublePosition otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    /**
     * Updates this position by adding the values from the otherPosition.
     *
     * @param otherPosition Other Position to add to this one.
     */
    public void add(Position otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    /**
     * Calculate the distance from this position to the other position.
     *
     * @param otherPosition Position to check distance to.
     * @return Distance between this position and the other position.
     */
    public double distanceTo(DoublePosition otherPosition) {
        return Math.sqrt(Math.pow(x-otherPosition.x,2)+Math.pow(y-otherPosition.y,2));
    }

    /**
     * Calculate the distance from this position to the other position.
     *
     * @param otherPosition Position to check distance to.
     * @return Distance between this position and the other position.
     */
    public double distanceTo(Position otherPosition) {
        return Math.sqrt(Math.pow(x-otherPosition.x,2)+Math.pow(y-otherPosition.y,2));
    }

    /**
     * Multiplies both components of the position by an amount.
     *
     * @param amount Amount to multiply vector by.
     */
    public void multiply(double amount) {
        x *= amount;
        y *= amount;
    }

    /**
     * Updates this position by subtracting the values from the otherPosition.
     *
     * @param otherPosition Other Position to add to this one.
     */
    public void subtract(DoublePosition otherPosition) {
        this.x -= otherPosition.x;
        this.y -= otherPosition.y;
    }

    /**
     * Updates this position by subtracting the values from the otherPosition.
     *
     * @param otherPosition Other Position to add to this one.
     */
    public void subtract(Position otherPosition) {
        this.x -= otherPosition.x;
        this.y -= otherPosition.y;
    }

    /**
     * Converts the position into a unit vector.
     */
    public void toUnitVector() {
        double magnitude = Math.sqrt(x*x+y*y);
        this.x /= magnitude;
        this.y /= magnitude;
    }

    /**
     * Converts the DoublePosition to a Position object.
     *
     * @return A new Position object with an int version of the position.
     */
    public Position toIntPosition() {
        return new Position((int)x, (int)y);
    }

    /**
     * Compares the Position object against another object.
     * Any non-Position object will return false. Otherwise compares x and y for equality.
     *
     * @param o Object to compare this Position against.
     * @return True if the object o is equal to this position for both x and y.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoublePosition position = (DoublePosition) o;
        return x == position.x && y == position.y;
    }

    /**
     * Gets a string version of the Position.
     *
     * @return A string in the form (x, y)
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}


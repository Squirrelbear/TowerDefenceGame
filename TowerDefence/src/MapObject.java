import java.awt.*;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * MapObject class:
 * Defines a simple object that could be represented on the map.
 */
public class MapObject extends Rectangle {
    /**
     * The types of different objects.
     */
    public enum ObjectType { Empty, Wall, TowerSpawn }

    /**
     * The type of object.
     */
    private ObjectType objectType;

    /**
     * @param objectType The type of object.
     * @param position The position to draw this element at.
     * @param width The width of the element.
     * @param height The height of the element.
     */
    public MapObject(ObjectType objectType, Position position, int width, int height) {
        super(position, width, height);
        this.objectType = objectType;
    }

    /**
     * Does nothing if it is empty. Otherwise draws a rectangle to show there is an object there.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        if(objectType == ObjectType.Empty) return;

        g.setColor(new Color(114, 64, 15));
        g.fillRect(position.x, position.y, width, height);
    }
}

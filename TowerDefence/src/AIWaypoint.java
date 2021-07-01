import java.awt.*;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * AIWaypoint class:
 * Defines a linked list of points that an AI can use to navigate a path.
 */
public class AIWaypoint extends Rectangle {
    /**
     * Reference to the next waypoint or null if this is the end of the path.
     */
    private AIWaypoint nextWaypoint;

    /**
     * Creates a waypoint that references the next waypoint.
     *
     * @param position Position on the panel.
     * @param nextWaypoint Reference to the next waypoint or null if this is the end of the path.
     */
    public AIWaypoint(Position position, AIWaypoint nextWaypoint) {
        super(position, Map.BLOCK_SIZE, Map.BLOCK_SIZE);
        this.nextWaypoint = nextWaypoint;
    }

    /**
     * Gets the next waypoint to travel to.
     *
     * @return The next waypoint (can be null if this is the end).
     */
    public AIWaypoint getNextWaypoint() {
        return nextWaypoint;
    }

    /**
     * Draw a debug marker to show where the waypoint is.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.drawOval(position.x, position.y, 4,4);
    }
}

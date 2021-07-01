import java.awt.*;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * HealthBar class:
 * Represents a simple visual percent bar with green to
 * show how much is remaining and red to show how much
 * has been lost.
 */
public class HealthBar extends Rectangle {
    /**
     * A number from 0 to 100 representing the percent.
     */
    private int percent;

    /**
     * Initialises the health bar defaulting to 100%.
     *
     * @param position Position to draw at.
     * @param width Width of the health bar.
     * @param height Height of the health bar.
     */
    public HealthBar(Position position, int width, int height) {
        super(position, width, height);
        percent = 100;
    }

    /**
     * Sets the percent of the bar to be displayed.
     *
     * @param percent The percent to set the bar to.
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * Draws a filled red background to represent the missing health,
     * then draws a percent based bar for the current health as green,
     * and puts a black border around it.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(position.x, position.y, width, height);
        g.setColor(Color.GREEN);
        g.fillRect(position.x, position.y, (int)(width*(percent/100.0)), height);
        g.setColor(Color.BLACK);
        g.drawRect(position.x, position.y, width, height);
    }
}

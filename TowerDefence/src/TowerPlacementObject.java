import java.awt.*;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * TowerPlacementObject class:
 * Defines a map object that represents a place where a tower can be added.
 * Controls the rendering of the tower or visual placement status.
 */
public class TowerPlacementObject extends MapObject {
    /**
     * Reference to the tower that has been placed on this object.
     */
    private Tower placedTower;
    /**
     * Reference to the game panel to check state.
     */
    private GamePanel gamePanel;

    /**
     * Prepares the object ready for drawing and interaction.
     *
     * @param position Position to draw the object.
     * @param width Width of the object.
     * @param height Height of the object.
     * @param gamePanel Reference to the game panel to check state.
     */
    public TowerPlacementObject(Position position, int width, int height, GamePanel gamePanel) {
        super(ObjectType.TowerSpawn, position, width, height);
        this.gamePanel = gamePanel;
    }

    /**
     * Stores a reference to a tower that has been placed on this object.
     *
     * @param placedTower Reference to the tower that has been placed on this object.
     */
    public void setPlacedTower(Tower placedTower) {
        this.placedTower = placedTower;
    }

    /**
     * Draws either the tower if one has been added to this object.
     * Or draws a marker to show a tower can be placed. Shows as green
     * if in the tower placement phase or orange otherwise.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if(placedTower != null) {
            placedTower.paint(g);
        } else {
            g.setColor(gamePanel.getGameState() == GamePanel.GameState.PlaceTower ? Color.GREEN : Color.ORANGE);
            g.drawRect(position.x+1, position.y+1, width-2, height-2);
            g.setFont(new Font("Arial",Font.BOLD, 20));
            g.drawString("T", position.x+width/2-5, position.y+height/2+5);
        }
    }
}

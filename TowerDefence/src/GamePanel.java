import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * GamePanel class:
 * Controls the primary structure of the game maintaining overall
 * game state and managing the timers/mouse interactions.
 */
public class GamePanel extends JPanel implements ActionListener, MouseListener {
    /**
     * GameState defines the state of the game.
     * Playing: Default state when everything is just running.
     * PlaceTower: Keeps the properties of the Playing state and allows placement of a tower.
     * GameOver: Stops the game from continuing once an end condition has been reached.
     */
    public enum GameState { Playing, PlaceTower, GameOver };
    /**
     * The timer interval to control deltaTime.
     */
    public static final int TIME_BETWEEN_UPDATES = 20;

    /**
     * Singleton reference for this class.
     */
    public static GamePanel instance;
    /**
     * Reference to the Game object for passing messages to the other panels.
     */
    private Game game;
    /**
     * Reference to the Map for managing the towers and scene elements.
     */
    private Map map;
    /**
     * Reference to the manager responsible for everything to do with enemies.
     */
    private EnemyUnitManager enemyUnitManager;
    /**
     * Timer responsible for keeping consistent updates ticking.
     */
    private Timer gameTimer;
    /**
     * The current game state.
     */
    private GameState gameState;
    /**
     * During the PlaceTower game state this indicates the tower that can be placed.
     */
    private Tower.TowerType towerTypeToPlace;
    /**
     * Amount of cash remaining.
     */
    private int cash;
    /**
     * Amount of score that has been gained.
     */
    private int score;
    /**
     * Current total base health percent.
     */
    private int baseHealth;
    /**
     * Message to display for the game over state.
     */
    private String gameOverMessage;

    /**
     * Sets up the default game state ready to start.
     *
     * @param game Reference to the Game object for passing information to the other panels.
     */
    public GamePanel(Game game) {
        this.game = game;
        this.instance = this;
        setPreferredSize(new Dimension(500,500));
        setBackground(new Color(199, 112, 27));

        map = new Map(this);
        List<AIWaypoint> waypointList = map.getWaypoints();
        enemyUnitManager = new EnemyUnitManager(waypointList.get(waypointList.size()-1).getPosition(),waypointList);

        gameTimer = new Timer(TIME_BETWEEN_UPDATES, this);
        addMouseListener(this);
    }

    /**
     * Starts the game timer and updates all the other panels.
     */
    public void startGame() {
        restart();
        gameTimer.start();
    }

    /**
     * Resets all data back to defaults and starts a new game.
     */
    public void restart() {
        gameState = GameState.Playing;
        cash = 150;
        score = 0;
        baseHealth = 100;
        game.setScore(score);
        game.setCash(cash);
        game.setBaseHealth(baseHealth);
        map.reset();
        enemyUnitManager.reset();
    }

    /**
     * Updates the map and enemyUnitManager.
     */
    public void update() {
        if(gameState == GameState.GameOver) return;
        map.update(TIME_BETWEEN_UPDATES);
        enemyUnitManager.update(TIME_BETWEEN_UPDATES);
        if(enemyUnitManager.hasGameEnded()) {
            gameState = GameState.GameOver;
            game.updateOptions();
            gameOverMessage = "Game Won!";
        }
        repaint();
    }

    /**
     * Draws the background colour, the map including towers, the enemies,
     * and last of all the projectiles on top of everything.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        super.paint(g);
        map.paint(g);
        enemyUnitManager.paint(g);
        map.paintProjectiles(g);
        if(gameState == GameState.GameOver) {
            drawGameOverMessage(g);
        }
    }

    /**
     * Sets the tower type to place, swaps the state to placing the tower,
     * and updates the options to prevent interaction that is not cancel.
     *
     * @param type Type of tower to be placed.
     */
    public void setTowerTypeToPlace(Tower.TowerType type) {
        this.towerTypeToPlace = type;
        gameState = GameState.PlaceTower;
        game.updateOptions();
    }

    /**
     * Cancels the tower placement by returning to the Playing state.
     */
    public void cancelTowerPlacement() {
        gameState = GameState.Playing;
        game.updateOptions();
    }

    /**
     * Gets the current Game State.
     *
     * @return The current Game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Modifies the base health by a specified amount.
     * If the health drops to 0 it will trigger the game over state.
     *
     * @param percentChange Amount to remove from the base health.
     */
    public void damageBase(int percentChange) {
        baseHealth = Math.max(baseHealth - percentChange, 0);
        game.setBaseHealth(baseHealth);
        if(baseHealth == 0) {
            gameState = GameState.GameOver;
            game.updateOptions();
            gameOverMessage = "Game Over! You Lost! :(";
        }
    }

    /**
     * Adds the specified amount of cash and displays it.
     *
     * @param cashToGain The amount of cash to add.
     */
    public void gainCash(int cashToGain) {
        cash += cashToGain;
        game.setCash(cash);
    }

    /**
     * Increases the score based on the type of enemy.
     *
     * @param enemyType Gains score depending on the type of enemy.
     */
    public void gainScore(EnemyUnit.EnemyType enemyType) {
        switch(enemyType) {
            case Normal:
            case Fast:
                score += 1;
            case Boss:
                score += 2;
        }
        game.setScore(score);
    }

    /**
     * Exits the game when Escape is pressed.
     *
     * @param keyCode Key that was pressed.
     */
    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    /**
     * Checks if there is enough cash to purchase the specified tower type.
     *
     * @param towerType Type of tower to check.
     * @return True if the specified tower type can be purchased.
     */
    public boolean canPurchaseTower(Tower.TowerType towerType) {
        return cash >= getTowerCost(towerType);
    }

    /**
     * Gets the cost of the specified tower.
     *
     * @param towerType Type of the tower to get a cost for.
     * @return Amount the specified tower costs.
     */
    public int getTowerCost(Tower.TowerType towerType) {
        if(towerType == Tower.TowerType.Normal) {
            return Tower.NORMAL_COST;
        } else if(towerType == Tower.TowerType.AoE) {
            return Tower.AOE_COST;
        } else if(towerType == Tower.TowerType.Slow) {
            return Tower.SLOW_COST;
        }
        return 0;
    }

    /**
     * Triggered by the game timer, forcing an update() call.
     *
     * @param e Information about the event that occurred.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }

    /**
     * Only does anything when in the PlaceTower state.
     * Will attempt to place the tower at the specified mouse click.
     * If it was successfully placed the state changes back to playing,
     * cash is deducted based on the cost of the tower.
     *
     * @param e Reference to the MouseEvent that was triggered.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(gameState != GameState.PlaceTower) return;
        Position mouseClickPosition = new Position(e.getX(), e.getY());
        boolean placingSuccess = map.placeTower(mouseClickPosition, towerTypeToPlace);
        if(placingSuccess) {
            gameState = GameState.Playing;
            cash -= getTowerCost(towerTypeToPlace);
            game.setCash(cash);
        }
    }

    /**
     * Draws a game over message to indicate whether the game was won or lost.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGameOverMessage(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,getHeight()/2-20, getWidth(), 70);
        g.setColor(Color.BLACK);
        g.drawRect(0,getHeight()/2-20, getWidth(), 70);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(gameOverMessage, getWidth()/2-g.getFontMetrics().stringWidth(gameOverMessage)/2, getHeight()/2+30);
    }

    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void mousePressed(MouseEvent e) {}
    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void mouseReleased(MouseEvent e) {}
    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void mouseEntered(MouseEvent e) {}
    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}

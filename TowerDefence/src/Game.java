import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * Game class:
 * Defines the entry point for the game by creating the frame,
 * and populating it with a GamePanel.
 */
public class Game implements KeyListener {
    /**
     * Entry point for the application to create an instance of the Game class.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        Game game = new Game();
    }

    /**
     * Reference to the GamePanel object to pass key events to.
     */
    private GamePanel gamePanel;

    /**
     * Reference to the statusPanel to pass status updates to.
     */
    private StatusPanel statusPanel;
    /**
     * Reference to the buyTowerPanel to modify which buttons are available.
     */
    private BuyTowerPanel buyTowerPanel;

    /**
     * Creates the JFrame with a GamePanel inside it, attaches a key listener,
     * and makes everything visible.
     */
    public Game() {
        JFrame frame = new JFrame("Tower Defence");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        gamePanel = new GamePanel(this);
        frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
        statusPanel = new StatusPanel();
        frame.getContentPane().add(statusPanel, BorderLayout.NORTH);
        buyTowerPanel = new BuyTowerPanel(gamePanel);
        frame.getContentPane().add(buyTowerPanel,BorderLayout.EAST);
        gamePanel.startGame();

        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Passes the current base health to the status panel.
     *
     * @param baseHealth New total base health to display.
     */
    public void setBaseHealth(int baseHealth) {
        statusPanel.setBaseHealth(baseHealth);
    }

    /**
     * Passes the current score to the status panel.
     *
     * @param score New score to display.
     */
    public void setScore(int score) {
        statusPanel.setScore(score);
    }

    /**
     * Passes the current cash to the status panel, and forces the buyTowerPanel to update available options.
     *
     * @param cash Current cash to display.
     */
    public void setCash(int cash) {
        statusPanel.setCash(cash);
        updateOptions();
    }

    /**
     * Forces the buyTowerPanel to update the state of all its buttons.
     */
    public void updateOptions() {
        buyTowerPanel.updateButtonStates();
    }

    /**
     * Called when the key is pressed down. Passes the key press on to the GamePanel.
     *
     * @param e Information about what key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.handleInput(e.getKeyCode());
    }

    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void keyReleased(KeyEvent e) {}
}

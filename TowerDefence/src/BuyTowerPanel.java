import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * BuyTowerPanel class:
 * Represents the buttons that can be clicked to purchase towers.
 */
public class BuyTowerPanel extends JPanel implements ActionListener {
    /**
     * Clicked to purchase a normal tower.
     */
    private JButton buyNormalTowerButton;
    /**
     * Clicked to purchase a slow tower.
     */
    private JButton buySlowTowerButton;
    /**
     * Clicked to purchase a aoe tower.
     */
    private JButton buyAoETowerButton;
    /**
     * Clicked to cancel the purchase of a tower.
     */
    private JButton cancelBuyButton;
    /**
     * Clicked to restart the game.
     */
    private JButton restartButton;
    /**
     * Clicked to quit the game.
     */
    private JButton quitButton;

    /**
     * Reference to the gamePanel for passing information from the button presses.
     */
    private GamePanel gamePanel;

    /**
     * Initialises all the labels to be ready for use.
     *
     * @param gamePanel Reference to the gamePanel for passing information from the button presses.
     */
    public BuyTowerPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(200,500));
        buyNormalTowerButton = new JButton("Buy Normal Tower ($"+Tower.NORMAL_COST+")");
        buyNormalTowerButton.addActionListener(this);
        buyNormalTowerButton.setPreferredSize(new Dimension(200,50));
        buyNormalTowerButton.setActionCommand("Normal");
        buySlowTowerButton = new JButton("Buy Slow Tower ($"+Tower.SLOW_COST+")");
        buySlowTowerButton.addActionListener(this);
        buySlowTowerButton.setPreferredSize(new Dimension(200,50));
        buySlowTowerButton.setActionCommand("Slow");
        buyAoETowerButton = new JButton("Buy AoE Tower ($"+Tower.AOE_COST+")");
        buyAoETowerButton.addActionListener(this);
        buyAoETowerButton.setPreferredSize(new Dimension(200,50));
        buyAoETowerButton.setActionCommand("AoE");
        cancelBuyButton = new JButton("Cancel Purchase");
        cancelBuyButton.addActionListener(this);
        cancelBuyButton.setPreferredSize(new Dimension(200,50));
        cancelBuyButton.setActionCommand("Cancel");
        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(this);
        restartButton.setPreferredSize(new Dimension(200,50));
        restartButton.setActionCommand("Restart");
        quitButton = new JButton("Quit Game");
        quitButton.addActionListener(this);
        quitButton.setPreferredSize(new Dimension(200,50));
        quitButton.setActionCommand("Quit");
        updateButtonStates();

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(200,60));

        JPanel centrePanel = new JPanel();
        centrePanel.setPreferredSize(new Dimension(200,400));
        centrePanel.add(buyNormalTowerButton);
        centrePanel.add(buyAoETowerButton);
        centrePanel.add(buySlowTowerButton);
        centrePanel.add(cancelBuyButton);
        centrePanel.add(restartButton);
        centrePanel.add(quitButton);

        add(topPanel);
        add(centrePanel);
    }

    /**
     * Updates which buttons can be interacted with based on the game state and how much cash is available.
     */
    public void updateButtonStates() {
        GamePanel.GameState curState = gamePanel.getGameState();
        cancelBuyButton.setEnabled(curState == GamePanel.GameState.PlaceTower);
        buyNormalTowerButton.setEnabled(curState == GamePanel.GameState.Playing
                                        && gamePanel.canPurchaseTower(Tower.TowerType.Normal));
        buyAoETowerButton.setEnabled(curState == GamePanel.GameState.Playing
                                        && gamePanel.canPurchaseTower(Tower.TowerType.AoE));
        buySlowTowerButton.setEnabled(curState == GamePanel.GameState.Playing
                                        && gamePanel.canPurchaseTower(Tower.TowerType.Slow));
    }

    /**
     * Gets the action command from the button that was pressed and passes the message to the gamePanel.
     *
     * @param e Information from the event that was triggered.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Normal":
                gamePanel.setTowerTypeToPlace(Tower.TowerType.Normal);
                break;
            case "AoE":
                gamePanel.setTowerTypeToPlace(Tower.TowerType.AoE);
                break;
            case "Slow":
                gamePanel.setTowerTypeToPlace(Tower.TowerType.Slow);
                break;
            case "Cancel":
                gamePanel.cancelTowerPlacement();
                break;
            case "Restart":
                if(JOptionPane.showConfirmDialog(null,"Are you sure you want to restart?") == JOptionPane.OK_OPTION) {
                    gamePanel.restart();
                }
                break;
            case "Quit":
                if(JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?") == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
                break;
        }
    }
}

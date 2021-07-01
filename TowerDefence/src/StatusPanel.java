import javax.swing.*;
import java.awt.*;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * StatusPanel class:
 * Shows status information about the game state.
 */
public class StatusPanel extends JPanel {
    /**
     * Shows the amount of currency the player has access to.
     */
    private JLabel cashLabel;
    /**
     * Shows the current score of the player.
     */
    private JLabel scoreLabel;
    /**
     * Shows the percent remaining for base health.
     */
    private JLabel baseHealthRemainingLabel;

    /**
     * Creates all the status panel elements and adds them to the panel.
     */
    public StatusPanel() {
        JLabel cashDescriptionLabel = new JLabel("Cash: $");
        cashLabel = new JLabel("0");
        JLabel scoreDescriptionLabel = new JLabel("Total Score: ");
        scoreLabel = new JLabel("0");
        JLabel baseHealthRemainingDescriptionLabel = new JLabel("Base Health: ");
        baseHealthRemainingLabel = new JLabel("100%");

        JPanel leftPanel = new JPanel();
        leftPanel.add(baseHealthRemainingDescriptionLabel);
        leftPanel.add(baseHealthRemainingLabel);

        JPanel middlePanel = new JPanel();
        middlePanel.add(scoreDescriptionLabel);
        middlePanel.add(scoreLabel);

        JPanel rightPanel = new JPanel();
        rightPanel.add(cashDescriptionLabel);
        rightPanel.add(cashLabel);

        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Changes the displayed cash to the specified number.
     *
     * @param cash The new cash value to set.
     */
    public void setCash(int cash) {
        this.cashLabel.setText(String.valueOf(cash));
    }

    /**
     * Changes the displayed score to the specified number.
     *
     * @param score The new score value to set.
     */
    public void setScore(int score) {
        this.scoreLabel.setText(String.valueOf(score));
    }

    /**
     * Changes the displayed base health % to the specified number.
     *
     * @param baseHPPercent The new base health value to set.
     */
    public void setBaseHealth(int baseHPPercent) {
        this.baseHealthRemainingLabel.setText(baseHPPercent + "%");
    }
}

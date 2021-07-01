import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * EnemyUnit class:
 * Defines an enemy that will move between a series of waypoints,
 * either until it dies, or until it reaches the last waypoint.
 */
public class EnemyUnit extends Rectangle {
    /**
     * Types of enemies.
     * Normal: Is a medium speed medium health enemy.
     * Fast: Is a fast speed low health enemy.
     * Boss: Is a slow speed high health enemy.
     */
    public enum EnemyType { Normal, Fast, Boss };

    /**
     * The type of this enemy.
     */
    private EnemyType enemyType;
    /**
     * The next waypoint to travel to, or null if the last point has been reached.
     */
    private AIWaypoint movingToWaypoint;
    /**
     * An enemy is expired when it reaches the last waypoint.
     */
    private boolean expired;
    /**
     * An enemy is dead when they run out of health.
     */
    private boolean dead;
    /**
     * Speed in distance to travel per second.
     */
    private int speed;
    /**
     * Maximum health used for determining the health percent..
     */
    private int healthMax;
    /**
     * Current health of the enemy.
     */
    private int healthCurrent;
    /**
     * Visual representation of the health.
     */
    private HealthBar healthBar;
    /**
     * Colour of the enemy. Changes depending on enemy type.
     */
    private Color unitColour;
    /**
     * List of active debuffs on the enemy.
     */
    private List<Debuff> debuffList;

    /**
     * Boolean variable to keep the state based on the slow debuff.
     */
    private boolean isSlowed;

    /**
     * Sets the enemy up ready to begin moving to the next waypoint with configuration based
     * on the type of enemy.
     *
     * @param enemyType Type of the enemy
     * @param firstWaypoint First waypoint to begin movement toward.
     * @param startPosition Position to start at.
     */
    public EnemyUnit(EnemyUnit.EnemyType enemyType, AIWaypoint firstWaypoint, Position startPosition) {
        super(startPosition, Map.BLOCK_SIZE, Map.BLOCK_SIZE);
        this.enemyType = enemyType;
        this.movingToWaypoint = firstWaypoint;
        debuffList = new ArrayList<>();
        setupEnemyUnit();
        healthBar = new HealthBar(new Position(startPosition), Map.BLOCK_SIZE, 7);
        expired = false;
        dead = false;
    }

    /**
     * Updates all the debuffs on this enemy, and then moves the enemy toward the next waypoint.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        updateDebuffs(deltaTime);
        move(deltaTime);
    }

    /**
     * Paints a circle to represent the enemy, and a health bar above to show current health.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(unitColour);
        g.fillOval(position.x, position.y, width, height);
        healthBar.paint(g);
    }

    /**
     * An enemy is considered expired if they are either dead or expired from reaching the last waypoint.
     *
     * @return True if the enemy should be destroyed.
     */
    public boolean isExpired() {
        return dead || expired;
    }

    /**
     * Applies the specified debuff. If the debuff is already on this enemy it will refresh the
     * duration timer instead.
     *
     * @param newDebuff The debuff to apply.
     */
    public void applyDebuff(Debuff newDebuff) {
        for(Debuff debuff : debuffList) {
            if(debuff.equals(newDebuff)) {
                debuff.refresh();
                return;
            }
        }
        // Was not found, so just insert the new debuff
        debuffList.add(newDebuff);
        applyDebuffEffect(newDebuff);
    }

    /**
     * Does nothing if already dead. Reduces health by amount specified capping at a
     * minimum of 0. If the enemy reached 0 the game is notified to increase score,
     * and gain cash for destroying the enemy.
     *
     * @param amount Amount of damage to remove from current health.
     */
    public void damage(int amount) {
        if(dead) return;

        healthCurrent = Math.max(healthCurrent-amount, 0);
        healthBar.setPercent(healthCurrent*100/healthMax);
        if(healthCurrent == 0) {
            GamePanel.instance.gainCash(10);
            GamePanel.instance.gainScore(enemyType);
            dead = true;
        }
    }

    /**
     * Moves the enemy toward the target waypoint until there are no more.
     *
     * @param deltaTime Time since last update.
     */
    private void move(int deltaTime) {
        if(movingToWaypoint == null) {
            expired = true;
            return;
        }

        // Get movement in direction toward target position
        int actualMoveSpeed = isSlowed ? speed / 2 : speed;
        Position directionVector = new Position(movingToWaypoint.getPosition());
        directionVector.subtract(getPosition());
        directionVector.toUnitVector();
        directionVector.multiply(actualMoveSpeed * deltaTime / 1000);

        position.add(directionVector);
        healthBar.getPosition().setPosition(position.x, position.y);
        // Has reached point?
        if(position.distanceTo(movingToWaypoint.getPosition()) <= actualMoveSpeed * deltaTime / 1000) {
            position.setPosition(movingToWaypoint.getPosition().x, movingToWaypoint.getPosition().y);
            movingToWaypoint = movingToWaypoint.getNextWaypoint();
            if(movingToWaypoint == null) {
                // Reached the end of the waypoint list
                GamePanel.instance.damageBase(5);
            }
            if(EnemyUnitManager.AI_DEBUG_MODE) {
                if (movingToWaypoint == null) System.out.println("Movement ended.");
                else System.out.println("Moving to waypoint: " + movingToWaypoint.getPosition());
            }
        }
    }

    /**
     * Updates the timer on all debuffs. If the debuff has expired it will be removed and
     * any status effects are also removed.
     *
     * @param deltaTime Time since last update.
     */
    private void updateDebuffs(int deltaTime) {
        for(int i = 0; i < debuffList.size(); i++) {
            debuffList.get(i).update(deltaTime);
            if(debuffList.get(i).isExpired()) {
                removeDebuffEffect(debuffList.get(i));
                debuffList.remove(i);
                i--;
            }
        }
    }

    /**
     * Applies a status change to the EnemyUnit based on a debuff type.
     *
     * @param debuff The debuff to apply an effect from.
     */
    private void applyDebuffEffect(Debuff debuff) {
        switch(debuff.getDebuffType()) {
            case Slow:
                isSlowed = true;
                break;
        }
    }

    /**
     * Removes a status change to the EnemyUnit based on a debuff type.
     *
     * @param debuff The debuff to remove an effect from.
     */
    private void removeDebuffEffect(Debuff debuff) {
        switch(debuff.getDebuffType()) {
            case Slow:
                isSlowed = false;
                break;
        }
    }

    /**
     * Applies the configuration for each different type of EnemyUnit type.
     * Including health, speed, and colour.
     */
    private void setupEnemyUnit() {
        switch(enemyType) {
            case Normal:
                healthMax = healthCurrent = 400;
                speed = 150;
                unitColour = Color.ORANGE;
                break;
            case Fast:
                healthMax = healthCurrent = 300;
                speed = 200;
                unitColour = Color.BLUE;
                break;
            case Boss:
                healthMax = healthCurrent = 600;
                speed = 100;
                unitColour = Color.BLACK;
                break;
        }
    }
}

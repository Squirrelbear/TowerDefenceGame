import java.awt.*;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * Projectile class:
 * Represents a projectile that moves toward a target and applies an effect on it.
 */
public class Projectile extends Rectangle {
    /**
     * The default speed that a projectile moves at.
     */
    private static final int DEFAULT_PROJECTILE_SPEED = 600;
    /**
     * The width and height of each projectile.
     */
    private static final int DEFAULT_PROJECTILE_SIZE = 4;

    /**
     * Reference to the behaviour being used for managing the projectile.
     */
    private AttackTargetBehaviour attackTargetBehaviour;
    /**
     * Reference to the target that is being moved toward.
     */
    private EnemyUnit target;
    /**
     * Indicates the projectile is ready to be deleted.
     */
    private boolean expired;
    /**
     * Speed of the projectile moving toward a target.
     */
    private int projectileSpeed;
    /**
     * Colour of the projectile.
     */
    private Color projectileColour;
    /**
     * Position used for more precise calculation of movement.
     */
    private DoublePosition precisePosition;

    /**
     * Initialises the projectile ready to move to a target.
     *
     * @param position Position of the projectile to start.
     * @param target Enemy target to move toward.
     * @param attackTargetBehaviour Behaviour used to spawn the projectile and manage it.
     * @param projectileColour Colour of the projectile.
     */
    public Projectile(Position position, EnemyUnit target, AttackTargetBehaviour attackTargetBehaviour, Color projectileColour) {
        super(position, DEFAULT_PROJECTILE_SIZE, DEFAULT_PROJECTILE_SIZE);
        this.target = target;
        this.attackTargetBehaviour = attackTargetBehaviour;
        this.projectileColour = projectileColour;
        this.projectileSpeed = DEFAULT_PROJECTILE_SPEED;
        precisePosition = new DoublePosition(position);
    }

    /**
     * Move the projectile to the target and apply a hit operation if it reaches the target.
     *
     * @param deltaTime Time since last update.
     */
    public void moveDirectlyToTarget(int deltaTime) {
        if(target == null) {
            expired = true;
            return;
        }

        // Calculate the change in position to apply
        DoublePosition directionVector = new DoublePosition(target.getCentre());
        directionVector.subtract(getPosition());
        directionVector.toUnitVector();
        directionVector.multiply(projectileSpeed * deltaTime / 1000);

        // Update the position and then store it to the int version for the rendering.
        precisePosition.add(directionVector);
        position = precisePosition.toIntPosition();

        // Check if the projectile has hit the target and apply the action if it has.
        if(precisePosition.distanceTo(target.getCentre()) <= projectileSpeed * deltaTime / 1000) {
            attackTargetBehaviour.handleProjectileHit(target);
            expired = true;
        }
    }

    /**
     * A projectile will be expired if the target no longer exists,
     * or if the projectile has hit the target.
     *
     * @return True when the projectile should be destroyed.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Draws the projectile with a border.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(projectileColour);
        g.fillRect(position.x, position.y, width, height);
        g.setColor(new Color(255, 169, 0));
        g.drawRect(position.x, position.y, width, height);
    }
}

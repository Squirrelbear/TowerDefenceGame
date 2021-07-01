import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * Tower class:
 * Defines what a Tower is including the properties for attack range,
 * colour, how often they fire, behaviours for choosing targets,
 * and firing at those targets. Each tower manages their own collection of
 */
public class Tower extends Rectangle {
    /**
     * Cost of the normal tower.
     */
    public static final int NORMAL_COST = 50;
    /**
     * Cost of the aoe tower.
     */
    public static final int AOE_COST = 60;
    /**
     * Cost of the slow tower.
     */
    public static final int SLOW_COST = 70;

    /**
     * Types of towers that can be created.
     * Normal: Fires single shots at individual enemies that deal damage on hit.
     * AoE: Fires single shots at individual enemies that explode on hit to damage all enemies nearby.
     * Slow: Fires multiple shots at all nearby enemies applying a slow debuff on hits.
     */
    public enum TowerType { Normal, AoE, Slow }

    /**
     * Type of the tower.
     */
    private TowerType towerType;
    /**
     * Behaviour used for firing projectiles, updating the projectile movement, and applying the hit behaviour.
     */
    private AttackTargetBehaviour attackTargetBehaviour;
    /**
     * Behaviour used for choosing valid targets to use the attack target behaviour on.
     */
    private TargetChooserBehaviour targetChooserBehaviour;
    /**
     * Colour to draw the tower with.
     */
    private Color drawColour;
    /**
     * Range of the tower that it can attack from.
     */
    private int range;
    /**
     * Timer between projectile firing.
     */
    private ActionTimer firingTimer;
    /**
     * List of all active projectiles.
     */
    private List<Projectile> activeProjectiles;

    /**
     * Initialises the tower based on the tower type.
     *
     * @param towerType Type of the tower.
     * @param position Position of the tower.
     * @param width Width of the tower.
     * @param height Height of the tower.
     */
    public Tower(TowerType towerType, Position position, int width, int height) {
        super(position, width, height);
        this.towerType = towerType;
        configureTower();
        activeProjectiles = new ArrayList<>();
    }

    /**
     * Updates the firing timer to fire additional projectiles,
     * and updates all active projectiles controlled by the tower.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        firingTimer.update(deltaTime);
        if(firingTimer.isTriggered()) {
            fire();
            firingTimer.reset();
        }
        updateProjectiles(deltaTime);
    }

    /**
     * Draws a tower made up of multiple rectangles.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(drawColour);
        g.fillRect(position.x+5, position.y+5, width/6, height/6);
        g.fillRect(position.x+width-5-width/6, position.y+5, width/6, height/6);
        g.fillRect(position.x+width/2-width/12, position.y+5, width/6, height/6);
        g.fillRect(position.x+5,position.y+5+height/6, width-10, height/3);
        g.fillRect(position.x + width/2 - width/4, position.y+5+height/6+height/3,width/2,height/3 );
    }

    /**
     * Draws all the projectiles to the screen.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paintProjectiles(Graphics g) {
        for(Projectile projectile : activeProjectiles) {
            projectile.paint(g);
        }
    }

    /**
     * Gets the range that the tower can attack from.
     *
     * @return The distance away from the tower that it can attack.
     */
    public int getRange() {
        return range;
    }

    /**
     * Finds all valid nearby targets using the targetChooserBehaviour,
     * and then fires projectiles at all the valid targets using the
     * attackTargetBehaviour to spawn the projectiles.
     */
    private void fire() {
        List<EnemyUnit> newTargets = targetChooserBehaviour.chooseTargets();
        activeProjectiles.addAll(attackTargetBehaviour.fireProjectiles(newTargets));
    }

    /**
     * Updates all the projectiles using the attackTargetBehaviour.
     * When a projectile expires it is removed.
     *
     * @param deltaTime Time since last update.
     */
    private void updateProjectiles(int deltaTime) {
        for(int i = 0; i < activeProjectiles.size(); i++) {
            attackTargetBehaviour.updateProjectileMovement(activeProjectiles.get(i), deltaTime);
            if(activeProjectiles.get(i).isExpired()) {
                activeProjectiles.remove(i);
                i--;
            }
        }
    }

    /**
     * Configures the towers to have the correct properties and behaviours based on their type.
     */
    private void configureTower() {
        switch(towerType) {
            case Normal:
                drawColour = Color.CYAN;
                firingTimer = new ActionTimer(300);
                attackTargetBehaviour = new AttackSingleTargetBehaviour(this);
                targetChooserBehaviour = new TargetChooserSingleBehaviour(this);
                range = 200;
                break;
            case AoE:
                drawColour = Color.ORANGE;
                firingTimer = new ActionTimer(1000);
                attackTargetBehaviour = new AttackAoEAtTargetBehaviour(this);
                targetChooserBehaviour = new TargetChooserSingleBehaviour(this);
                range = 300;
                break;
            case Slow:
                drawColour = Color.BLUE;
                firingTimer = new ActionTimer(1000);
                attackTargetBehaviour = new AttackSlowTargetBehaviour(this);
                targetChooserBehaviour = new TargetChooserAllInRangeBehaviour(this);
                range = 150;
                break;
        }
    }
}

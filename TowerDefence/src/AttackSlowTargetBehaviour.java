import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * AttackSlowTargetBehaviour class:
 * Defines a behaviour that first projectiles that move directly toward
 * the enemy target and slow the target for a period on hit.
 */
class AttackSlowTargetBehaviour implements AttackTargetBehaviour {
    /**
     * Reference to the tower that will be using this behaviour.
     */
    private Tower towerReference;

    /**
     * Creates the behaviour ready to use.
     *
     * @param towerReference Reference to the tower that will be using this behaviour.
     */
    public AttackSlowTargetBehaviour(Tower towerReference) {
        this.towerReference = towerReference;
    }

    /**
     * Fires projectiles at all the specified targets.
     *
     * @param targets All targets to fire a projectile at.
     * @return A list of the projectiles fired at the list of targets.
     */
    @Override
    public List<Projectile> fireProjectiles(List<EnemyUnit> targets) {
        List<Projectile> result = new ArrayList<>();
        for(EnemyUnit target : targets) {
            result.add(new Projectile(towerReference.getCentre(),target, this, new Color(28, 62, 163)));
        }
        return result;
    }

    /**
     * Moves the projectile directly toward the target.
     *
     * @param projectileToMove Reference to the projectile to update.
     * @param deltaTime Time since last update.
     */
    @Override
    public void updateProjectileMovement(Projectile projectileToMove, int deltaTime) {
        projectileToMove.moveDirectlyToTarget(deltaTime);
    }

    /**
     * Applies a slow debuff to the target.
     *
     * @param target Reference to the enemy enemy to apply a hit on.
     */
    @Override
    public void handleProjectileHit(EnemyUnit target) {
        target.applyDebuff(new Debuff(Debuff.DebuffType.Slow, 3000));
    }
}
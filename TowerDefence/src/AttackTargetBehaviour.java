import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * AttackTargetBehaviour interface:
 * Defines a template to allow firing of projectiles, handling of movement of projectiles,
 * and handling of the event when the target is hit.
 */
interface AttackTargetBehaviour {
    /**
     * Fires projectiles at all the specified targets.
     *
     * @param targets All targets to fire a projectile at.
     * @return A list of the projectiles fired at the list of targets.
     */
    List<Projectile> fireProjectiles(List<EnemyUnit> targets);
    /**
     * Moves the projectile toward the target.
     *
     * @param projectileToMove Reference to the projectile to update.
     * @param deltaTime Time since last update.
     */
    void updateProjectileMovement(Projectile projectileToMove, int deltaTime);

    /**
     * Apply any effects or damage when the projectile collides with the enemy.
     *
     * @param target Reference to the enemy enemy to apply a hit on.
     */
    void handleProjectileHit(EnemyUnit target);
}
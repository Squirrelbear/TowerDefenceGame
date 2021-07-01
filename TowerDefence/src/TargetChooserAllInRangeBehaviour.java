import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * TargetChooserAllInRangeBehaviour class:
 * Defines a behaviour that chooses all nearby enemies in range.
 */
public class TargetChooserAllInRangeBehaviour implements TargetChooserBehaviour{
    /**
     * Reference to the tower that will be using this behaviour.
     */
    private Tower towerReference;

    /**
     * Creates the behaviour ready to use.
     *
     * @param towerReference Reference to the tower that will be using this behaviour.
     */
    public TargetChooserAllInRangeBehaviour(Tower towerReference) {
        this.towerReference = towerReference;
    }

    /**
     * Gets a list of all nearby enemies.
     *
     * @return A list of all nearby enemies.
     */
    @Override
    public List<EnemyUnit> chooseTargets() {
        return TowerAI.getTargetsInRange(towerReference.getCentre(), towerReference.getRange());
    }
}

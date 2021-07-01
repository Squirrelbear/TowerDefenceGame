import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * TargetChooserSingleBehaviour class:
 * Defines a behaviour that chooses a single closest target.
 */
class TargetChooserSingleBehaviour implements TargetChooserBehaviour {
    /**
     * Reference to the tower that will be using this behaviour.
     */
    private Tower towerReference;

    /**
     * Creates the behaviour ready to use.
     *
     * @param towerReference Reference to the tower that will be using this behaviour.
     */
    public TargetChooserSingleBehaviour(Tower towerReference) {
        this.towerReference = towerReference;
    }

    /**
     * Gets a list with up to one enemy based on the closest target.
     *
     * @return A list with one or no enemy targets.
     */
    @Override
    public List<EnemyUnit> chooseTargets() {
        List<EnemyUnit> targetList = new ArrayList<>();
        EnemyUnit singleTarget = TowerAI.getClosestTargetInRange(towerReference.getCentre(),towerReference.getRange());
        if(singleTarget != null) {
            targetList.add(singleTarget);
        }
        return targetList;
    }
}
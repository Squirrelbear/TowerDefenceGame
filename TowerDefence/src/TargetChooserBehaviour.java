import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell
 *
 * TargetChooserBehaviour interface:
 * Defines a behaviour for target selection of enemies.
 */
interface TargetChooserBehaviour {
    /**
     * Gets a list of possible targets to attack.
     *
     * @return A list of enemies that could be attacked.
     */
    List<EnemyUnit> chooseTargets();
}
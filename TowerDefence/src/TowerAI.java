import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * TowerAI class:
 * A helper class with methods useful for managing the TowerAI.
 */
public class TowerAI {
    /**
     * Gets a list of all valid enemies within the specified range.
     *
     * @param fromPosition Position to test from.
     * @param range Range to search for targets within.
     * @return A list of all enemies within range of the position.
     */
    public static List<EnemyUnit> getTargetsInRange(Position fromPosition, int range) {
        List<EnemyUnit> result = new ArrayList<>();
        List<EnemyUnit> allEnemies = EnemyUnitManager.getInstance().getActiveEnemies();
        for(EnemyUnit enemyUnit : allEnemies) {
            if(fromPosition.distanceTo(enemyUnit.getPosition()) <= range) {
                result.add(enemyUnit);
            }
        }
        return result;
    }

    /**
     * Searches all active enemies for the closest target in range.
     *
     * @param fromPosition Position to test from.
     * @param range Range to search for targets within.
     * @return A single enemy unit that is the closest enemy, or null if there are none in range.
     */
    public static EnemyUnit getClosestTargetInRange(Position fromPosition, int range) {
        List<EnemyUnit> allEnemies = EnemyUnitManager.getInstance().getActiveEnemies();
        EnemyUnit bestResult = null;
        double bestDistance = 0;
        for(EnemyUnit enemyUnit : allEnemies) {
            double distanceToEnemy = fromPosition.distanceTo(enemyUnit.getPosition());
            if(distanceToEnemy <= range) {
                if(bestResult == null || distanceToEnemy < bestDistance) {
                    bestResult = enemyUnit;
                    bestDistance = distanceToEnemy;
                }
            }
        }
        return bestResult;
    }
}

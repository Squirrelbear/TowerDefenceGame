import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * EnemyUnitManager class:
 * Handles all the management of enemy units and their interactions with waypoints.
 */
public class EnemyUnitManager {
    /**
     * The default time between enemy unit spawns.
     */
    private static final int TIME_BETWEEN_SPAWNS = 3000;
    /**
     * When true additional debug information will be shown.
     */
    public static final boolean AI_DEBUG_MODE = false;
    /**
     * When true it will use the spawn manager to spawn enemies until it runs out.
     * When false it will just keep spawning random enemies infinitely.
     */
    private static final boolean SPAWN_USING_SPAWN_MANAGER = true;
    /**
     * Reference to this object as a singleton.
     */
    private static EnemyUnitManager instance;

    /**
     * Position to use as the start for all enemy spawns.
     */
    private Position enemyStartPosition;
    /**
     * A list of all the waypoints for AI to use.
     */
    private List<AIWaypoint> waypoints;
    /**
     * A list of all active enemies that can be updated.
     */
    private List<EnemyUnit> activeEnemies;
    /**
     * A shared reference to the random object for spawning random enemies.
     */
    private Random rand;
    /**
     * Timer for spawning random enemy units.
     */
    private ActionTimer spawnTimer;
    /**
     * Manager to control spawning of enemies.
     */
    private SpawnManager spawnManager;
    /**
     * True when the spawn manager has run out of enemies to spawn.
     */
    private boolean finishedSpawning;

    /**
     * Initialises the manager ready to spawn enemy units and manage their status.
     *
     * @param enemyStartPosition Position to use as the start for all enemy spawns.
     * @param waypoints A list of all the waypoints for AI to use.
     */
    public EnemyUnitManager(Position enemyStartPosition, List<AIWaypoint> waypoints) {
        this.enemyStartPosition = enemyStartPosition;
        this.waypoints = waypoints;
        activeEnemies = new ArrayList<>();
        rand = new Random();
        spawnTimer = new ActionTimer(TIME_BETWEEN_SPAWNS);
        spawnManager = new SpawnManager(this);
        spawnManager.applyExampleCommandList();
        instance = this;
        reset();
    }

    /**
     * Resets all the enemy management properties.
     */
    public void reset() {
        finishedSpawning = false;
        activeEnemies.clear();
        spawnManager.applyExampleCommandList();
    }

    /**
     * Updates the spawner to check if a new enemy should be spawned.
     * Then updates all active enemies and removes any that have expired.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        if(SPAWN_USING_SPAWN_MANAGER) {
            spawnManager.update(deltaTime);
            finishedSpawning = !spawnManager.hasMoreSpawnCommands();
        } else {
            spawnTimer.update(deltaTime);
            if (spawnTimer.isTriggered()) {
                spawnRandomEnemy();
                spawnTimer.reset();
            }
        }

        for(int i = 0; i < activeEnemies.size(); i++) {
            activeEnemies.get(i).update(deltaTime);
            if(activeEnemies.get(i).isExpired()) {
                activeEnemies.remove(i);
                i--;
            }
        }
    }

    /**
     * Draws all the enemies. Will also draw all the waypoints if the AI_DEBUG_MODE is true.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        for(EnemyUnit enemy : activeEnemies) {
            enemy.paint(g);
        }
        if(AI_DEBUG_MODE) {
            for (AIWaypoint aiWaypoint : waypoints) {
                aiWaypoint.paint(g);
            }
        }
    }

    /**
     * Test if the game should end based on enemy spawn conditions.
     *
     * @return True if no more enemies to spawn and all enemies have been removed.
     */
    public boolean hasGameEnded() {
        return finishedSpawning && activeEnemies.size() == 0;
    }

    /**
     * Gets the list of all active enemies.
     *
     * @return A list of all active enemies.
     */
    public List<EnemyUnit> getActiveEnemies() {
        return activeEnemies;
    }

    /**
     * Gets the singleton for this object.
     *
     * @return A singleton representing the EnemyUnitManager.
     */
    public static EnemyUnitManager getInstance() {
        return instance;
    }

    /**
     * Spawns a new random enemy and adds it to the activeEnemies list.
     */
    public void spawnRandomEnemy() {
        spawnEnemy(getRandomEnemyType());
    }

    /**
     * Spawns an enemy of the specified type.
     *
     * @param enemyType Type of enemy to spawn.
     */
    public void spawnEnemy(EnemyUnit.EnemyType enemyType) {
        activeEnemies.add(new EnemyUnit(enemyType,waypoints.get(waypoints.size()-1),new Position(enemyStartPosition)));
    }

    /**
     * Randomly selects between the valid enemy types.
     *
     * @return A random enemy type.
     */
    private EnemyUnit.EnemyType getRandomEnemyType() {
        EnemyUnit.EnemyType enemyType = EnemyUnit.EnemyType.Normal;
        switch(rand.nextInt(3)) {
            case 0:
                enemyType = EnemyUnit.EnemyType.Normal;
                break;
            case 1:
                enemyType = EnemyUnit.EnemyType.Fast;
                break;
            case 2:
                enemyType = EnemyUnit.EnemyType.Boss;
                break;
        }
        return enemyType;
    }
}

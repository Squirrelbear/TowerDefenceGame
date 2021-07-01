import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell (2021)
 *
 * SpawnManager class:
 * Defines a manager to process commands to spawn different enemies on variable timers.
 */
public class SpawnManager {
    /**
     * An example command that shows how the structure works.
     * Structure of each command is: Command,Value
     * T,number = Set interval frequency for spawning to number
     * N,number = Spawn normal enemy for number times
     * F,number = Spawn fast enemy for number times
     * B,number = Spawn boss enemy for number times
     */
    private static final String exampleCommand = "T,5000,N,5,F,3,B,1,"
                                                +"T,2000,N,5,F,3,B,2,"
                                                +"T,1500,F,5,N,3,B,10";

    /**
     * Reference to the unit manager to spawn enemies.
     */
    private EnemyUnitManager enemyUnitManager;
    /**
     * List of all the commands that are being processed.
     */
    private List<SpawnCommand> spawnCommandList;
    /**
     * Timer for delaying the time between commands.
     */
    private ActionTimer spawnTimer;

    /**
     * Configures the spawn manager ready to spawn enemies.
     *
     * @param enemyUnitManager Reference to the unit manager for spawning enemies.
     */
    public SpawnManager(EnemyUnitManager enemyUnitManager) {
        this.enemyUnitManager = enemyUnitManager;
        spawnCommandList = new ArrayList<>();
        spawnTimer = new ActionTimer(5);
    }

    /**
     * A method to apply the example command list. (clears all existing commands).
     * You can call addCommandsFromString() to add your custom commands.
     */
    public void applyExampleCommandList() {
        clearCommands();
        addCommandsFromString(exampleCommand);
        spawnTimer.setTimer(5);
    }

    /**
     * Clears all the commands.
     */
    public void clearCommands() {
        spawnCommandList.clear();
    }

    /**
     * Updates the spawn timer and if it is triggered
     * the next command is executed and it is prepared to
     * wait for the next trigger.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        spawnTimer.update(deltaTime);
        if(spawnTimer.isTriggered()) {
            spawnTimer.reset();
            executeNextCommand();
        }
    }

    /**
     * Separates commands out by splitting on commas and then generating
     * a list of spawn commands.
     *
     * @param spawnData Correctly formatted spawnData to be loaded in.
     */
    public void addCommandsFromString(String spawnData) {
        String[] spawnDataSplit = spawnData.split(",");
        for(int i = 0; i < spawnDataSplit.length; i+=2) {
            if(i+1 == spawnDataSplit.length) {
                System.out.println("ERROR INVALID SPAWN COMMAND DATA LENGTH");
                return;
            }
            spawnCommandList.add(new SpawnCommand(
                    SpawnCommand.convertStringToCommand(spawnDataSplit[i]),
                    Integer.parseInt(spawnDataSplit[i+1])));
        }
    }

    /**
     * Tests if the command list for spawning still has commands to run.
     *
     * @return True if there are still commands to be run.
     */
    public boolean hasMoreSpawnCommands() {
        return spawnCommandList.size() != 0;
    }

    /**
     * Peeks the top element of the command list.
     * Executes the command based on the specified command id, and
     * then pops it if the command is no longer needed.
     */
    private void executeNextCommand() {
        if(spawnCommandList.size() == 0) return;
        boolean removeCommand = true;
        SpawnCommand currentCommand = spawnCommandList.get(0);

        switch (currentCommand.getCommand()) {
            case 1:
            case 2:
            case 3:
                spawnTowerByID(currentCommand);
                if(currentCommand.getValue() > 0) removeCommand = false;
                break;
            case 4:
                spawnTimer.setTimer(currentCommand.getValue());
                break;
        }

        if(removeCommand) {
            spawnCommandList.remove(0);
        }
    }

    /**
     * Executes the command to spawn a tower and subtracts 1 from the
     * number of enemies of this type to spawn.
     *
     * @param command The command to execute.
     */
    private void spawnTowerByID(SpawnCommand command) {
        switch (command.getCommand()) {
            case 1:
                enemyUnitManager.spawnEnemy(EnemyUnit.EnemyType.Normal);
                break;
            case 2:
                enemyUnitManager.spawnEnemy(EnemyUnit.EnemyType.Fast);
                break;
            case 3:
                enemyUnitManager.spawnEnemy(EnemyUnit.EnemyType.Boss);
                break;
        }
        command.setValue(command.getValue()-1);
    }
}

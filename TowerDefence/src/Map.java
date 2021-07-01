import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tower Defence
 * Author: Peter Mitchell
 *
 * Map class:
 * Defines a class that represents the objects shown on the map.
 * Reads the map data to generate this and tracks towers that
 * have been placed on the map.
 */
public class Map {
    /**
     * Visual width and height of each element.
     */
    public static final int BLOCK_SIZE = 50;
    /**
     * Map data to load as the map.
     */
    private static String[] map = { "ETTW  WTTWS",
                                    " .. TT .. ",
                                    " TT .. TT ",
                                    " .. TT .. ",
                                    " TT .. TT ",
                                    " .. TT .. ",
                                    " TT .. TT ",
                                    " .. TT .. ",
                                    " TT .. TT ",
                                    "W  WTTW  W" };

    /**
     * A 2d grid of all the map objects.
     */
    private MapObject[][] mapObjects;
    /**
     * Reference to the waypoints that were found on the map.
     */
    private List<AIWaypoint> waypoints;
    /**
     * All the remaining positions that can have a tower placed on them.
     */
    private List<TowerPlacementObject> openTowerPositions;
    /**
     * A list of all currently active towers.
     */
    private List<Tower> activeTowers;
    /**
     * Reference to the gamePanel for passing data.
     */
    private GamePanel gamePanel;

    /**
     * Initialises and loads the map ready to use.
     *
     * @param gamePanel Reference to the gamePanel for passing information.
     */
    public Map(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        loadMapFromArray(map);
        activeTowers = new ArrayList<>();
        reset();
    }

    /**
     * Resets all the map properties back to defaults.
     */
    public void reset() {
        activeTowers.clear();
        openTowerPositions.clear();

        for(int y = 0; y < mapObjects[0].length; y++) {
            for(int x = 0; x < mapObjects.length; x++) {
                if(mapObjects[x][y] instanceof TowerPlacementObject) {
                    TowerPlacementObject temp = (TowerPlacementObject)mapObjects[x][y];
                    openTowerPositions.add(temp);
                    temp.setPlacedTower(null);
                }
            }
        }
    }

    /**
     * Updates all the towers that are active.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        for(Tower tower : activeTowers) {
            tower.update(deltaTime);
        }
    }

    /**
     * Draws all the map objects.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        for(int y = 0; y < mapObjects[0].length; y++) {
            for(int x = 0; x < mapObjects.length; x++) {
                mapObjects[x][y].paint(g);
            }
        }
    }

    /**
     * Draws all projectiles. This is separate to control the draw order.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paintProjectiles(Graphics g) {
        for(Tower tower : activeTowers) {
            tower.paintProjectiles(g);
        }
    }

    /**
     * Gets a list of all waypoints ready to use for AI.
     *
     * @return A list of all waypoints that were loaded from the map.
     */
    public List<AIWaypoint> getWaypoints() {
        return waypoints;
    }

    /**
     * Tests the click to find if it matches any valid position to place a tower.
     * If it does it will place the tower and return true.
     * Otherwise it will return false.
     *
     * @param clickPosition The mouse click position.
     * @param towerType Type of tower to be placed.
     * @return True if the tower was successfully placed with the click.
     */
    public boolean placeTower(Position clickPosition, Tower.TowerType towerType) {
        // Find if there is an open tower position that can take this tower placement
        int i = 0;
        for(; i < openTowerPositions.size(); i++) {
            if(openTowerPositions.get(i).isPositionInside(clickPosition)) {
                break;
            }
        }
        // None was found, so do nothing else.
        if(i == openTowerPositions.size()) {
            return false;
        }
        // Valid position was found, create the tower.
        Tower newTower = new Tower(towerType, new Position(openTowerPositions.get(i).getPosition()),
                                        openTowerPositions.get(i).getWidth(), openTowerPositions.get(i).getHeight());
        activeTowers.add(newTower);
        openTowerPositions.get(i).setPlacedTower(newTower);
        openTowerPositions.remove(i);
        return true;
    }

    /**
     * Loads the map from supplied string data.
     *
     * @param mapData Data to generate the map.
     */
    private void loadMapFromArray(String[] mapData) {
        int width = mapData[0].length();
        // S is assumed to be outside the normal map size on the right
        if(mapData[0].contains("S")) width--;
        int height = mapData.length;
        mapObjects = new MapObject[width][height];
        openTowerPositions = new ArrayList<>();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                mapObjects[x][y] = createMapObject(mapData[y].charAt(x),x,y);
            }
        }
        loadWaypointsFromArray(mapData);
    }

    /**
     * Searches for the End (E) and Start (S) then tries to create a path
     * between waypoints (W) by moving along empty spaces ( ).
     *
     * @param mapData Data to generate the waypoints.
     */
    private void loadWaypointsFromArray(String[] mapData) {
        Position currentPos = Position.ZERO, start = Position.ZERO, lastDirection = Position.ZERO;
        AIWaypoint nextWaypoint;

        // Find start and end
        for(int y = 0; y < mapData.length; y++) {
            for(int x = 0; x < mapData[y].length(); x++) {
                if(mapData[y].charAt(x) == 'E') currentPos = new Position(x,y);
                else if(mapData[y].charAt(x) == 'S') start = new Position(x,y);
            }
        }

        waypoints = new ArrayList<>();
        nextWaypoint = new AIWaypoint(new Position(currentPos.x*BLOCK_SIZE,currentPos.y*BLOCK_SIZE),null);
        waypoints.add(nextWaypoint);
        boolean found = true;
        // Keep looping while a valid move is still found.
        while(found && currentPos.distanceTo(start) != 1) {
            List<Position> validDirections = getValidTranslations(currentPos, lastDirection);
            found = false;
            for(int i = 0; i < validDirections.size(); i++) {
                Position tempPosition = new Position(currentPos);
                tempPosition.add(validDirections.get(i));
                while(tempPosition.y >= 0 && tempPosition.y < mapObjects[0].length
                      && tempPosition.x >= 0 && tempPosition.x < mapObjects.length
                      && mapData[tempPosition.y].charAt(tempPosition.x) == ' ') {
                    tempPosition.add(validDirections.get(i));
                }
                if (tempPosition.y >= 0 && tempPosition.y < mapObjects[0].length
                        && tempPosition.x >= 0 && tempPosition.x < mapObjects.length &&
                        mapData[tempPosition.y].charAt(tempPosition.x) == 'W') {
                    currentPos = tempPosition;
                    lastDirection = validDirections.get(i);
                    found = true;
                    break;
                }
            }
            if(found) {
                nextWaypoint = new AIWaypoint(new Position(currentPos.x*BLOCK_SIZE,currentPos.y*BLOCK_SIZE),nextWaypoint);
                waypoints.add(nextWaypoint);
            }
        }
        waypoints.add(new AIWaypoint(new Position(start.x*BLOCK_SIZE,start.y*BLOCK_SIZE), nextWaypoint));
    }

    /**
     * Creates a map object based on the types supplied in the form a character.
     *
     * @param character The character to set the type to.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return The new map object that was created with specified properties.
     */
    private MapObject createMapObject(char character, int x, int y) {
        MapObject.ObjectType type = MapObject.ObjectType.Empty;
        switch(character) {
            case '.': type = MapObject.ObjectType.Wall; break;
            case 'T': return new TowerPlacementObject(new Position(x*BLOCK_SIZE, y *BLOCK_SIZE),
                                                      BLOCK_SIZE, BLOCK_SIZE, gamePanel);
        }
        return new MapObject(type, new Position(x*BLOCK_SIZE, y *BLOCK_SIZE), BLOCK_SIZE, BLOCK_SIZE);
    }

    /**
     * Finds all the direction vectors that can be moved from the current position.
     * Excludes any that move off the map, or that move backwards the way it came.
     *
     * @param curPosition Current position to move from.
     * @param previousDirection The last direction of movement.
     * @return A list of all valid movement vectors from the current position.
     */
    private List<Position> getValidTranslations(Position curPosition, Position previousDirection) {
        List<Position> result = new ArrayList<>();
        if(curPosition.x != mapObjects.length && !previousDirection.equals(Position.LEFT)) result.add(Position.RIGHT);
        if(curPosition.x != 0 && !previousDirection.equals(Position.RIGHT)) result.add(Position.LEFT);
        if(curPosition.y != 0 && !previousDirection.equals(Position.DOWN)) result.add(Position.UP);
        if(curPosition.y != mapObjects[0].length && !previousDirection.equals(Position.UP)) result.add(Position.DOWN);
        return result;
    }

    /**
     * Debug method to print a list of Positions.
     *
     * @param messagePrefix Debug message to show before the data.
     * @param data A list of elements to show in the form [,,,]
     */
    private void printPositionList(String messagePrefix, List<Position> data) {
        String result = "[";
        for(int i = 0; i < data.size(); i++) {
            result += data.get(i);
            if(i != data.size()-1) {
                result += ", ";
            }
        }
        result += "]";
        System.out.println(messagePrefix + " " + result);
    }
}

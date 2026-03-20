import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

// The map: 54 corners (intersections), 19 hexes with resources and numbers. Tracks where buildings and roads are.
class Board {

    private int[][] tilesNodes = { 
        { 41, 42, 40, 18, 17, 39 }, { 40, 44, 43, 21, 16, 18 }, { 43, 45, 47, 46, 19, 21 },
        { 38, 39, 17, 15, 14, 37 }, { 17, 18, 16, 5, 4, 15 }, { 16, 21, 19, 20, 0, 5 },
        { 19, 46, 48, 49, 22, 20 }, { 36, 37, 14, 13, 32, 35 }, { 14, 15, 4, 3, 12, 13 },
        { 4, 5, 0, 1, 2, 3 }, { 0, 20, 22, 23, 6, 1 }, { 22, 49, 50, 51, 52, 23 },
        { 34, 13, 12, 11, 32, 33 }, { 12, 3, 2, 9, 10, 11 }, { 2, 1, 6, 7, 8, 9 },
        { 6, 23, 52, 53, 24, 7 }, { 32, 11, 10, 29, 30, 31 }, { 10, 9, 8, 27, 28, 29 },
        { 8, 7, 24, 25, 26, 27 } 
    };

    private Map<Integer, Intersection> intersections;
    private Map<Integer, HexTerrain> tiles;
    private List<int[]> builtEdges = new ArrayList<>();
    private GameRules rules = new GameRules();
    private int robberHexID;

    public Board() {
        intersections = new HashMap<>();
        robberHexID = 9;

        for (int i = 0; i <= 53; i++) {
            intersections.put(i, new Intersection(i));
        }

        tiles = new HashMap<>();

        ResourceType[] resources = {
            ResourceType.WOOD, ResourceType.WOOD, ResourceType.WOOD, ResourceType.WOOD,
            ResourceType.BRICK, ResourceType.BRICK, ResourceType.BRICK,
            ResourceType.WHEAT, ResourceType.WHEAT, ResourceType.WHEAT, ResourceType.WHEAT,
            ResourceType.SHEEP, ResourceType.SHEEP, ResourceType.SHEEP, ResourceType.SHEEP,
            ResourceType.ORE, ResourceType.ORE, ResourceType.ORE
        };

        int[] numbers = { 2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12 };

        tiles.put(9, new DesertHex(9));

        int resourceIndex = 0;

        for (int i = 0; i < 19; i++) {
            if (i == 9) {
                continue;
            }
            tiles.put(i, new ResourceHex(i, new HexBoardNum(numbers[resourceIndex]), resources[resourceIndex]));
            resourceIndex++;
        }
    }

    // True if the two corners are next to each other on the hex map (share an edge).
    public boolean isValidEdge(int start, int end) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 6; j++) {
                if (j < 5) {
                    if ((tilesNodes[i][j] == start && tilesNodes[i][j + 1] == end) || (tilesNodes[i][j] == end && tilesNodes[i][j + 1] == start)) {
                        return true;
                    }
                }
                
                if ((tilesNodes[i][0] == start && tilesNodes[i][5] == end) || (tilesNodes[i][0] == end && tilesNodes[i][5] == start)) {
                    return true;
                }
            }
        }
        return false;
    }

    // All corners that are one edge away from this corner.
    public List<Integer> getNeighbouringIntersections(int intersectionID) {
        List<Integer> neighbours = new ArrayList<>();

        for (int i = 0; i <= 53; i++) {
            if (i == intersectionID) {
                continue;
            }

            if (isValidEdge(intersectionID, i)) {
                neighbours.add(i);
            }
        }

        return neighbours;
    }

    public Intersection getIntersection(int intersectionID) {
        return intersections.get(intersectionID);
    }

    // Ok if spot is empty and no neighbour has a building (and player has < 2), or if spot is connected by player's road.
    public boolean placeSettlement(Intersection placeIntersection, Player player) {
        if (rules.checkEmptyIntersections(placeIntersection.getIntersectionLocation(), this)) {
            if (player.getSettlements().size() < 2) {
                Settlement settlement = new Settlement(placeIntersection, player);
                placeIntersection.setBuilding(settlement);
                placeIntersection.setOwner(player);
                player.getPlayerBuildings().add(settlement);
                return true;
            }
        } else {
            if (rules.isConnected(placeIntersection.getIntersectionLocation(), player, this)) {
                Settlement settlement = new Settlement(placeIntersection, player);
                placeIntersection.setBuilding(settlement);
                placeIntersection.setOwner(player);
                player.getPlayerBuildings().add(settlement);
                return true;
            }
        }
        return false;
    }

    // Only works if there is your settlement there; replaces it with a city.
    public boolean placeCity(Intersection placeIntersection, Player player) {
        Building existing = placeIntersection.getBuilding();
        if (existing != null && existing.getBuildingType() == Building.BuildingType.SETTLEMENT && existing.getOwner() == player) {
            City city = new City(placeIntersection, player);
            placeIntersection.setBuilding(city);
            return true;
        }
        return false;
    }

    public boolean isEdgeOccupied(int start, int end) {
        for (int[] edge : builtEdges) {
            if ((edge[0] == start && edge[1] == end) || (edge[0] == end && edge[1] == start)) {
                return true;
            }
        }
        return false;
    }

    // Road must be on a valid edge, not taken, and next to your building or your road.
    public boolean placeRoad(Edge placeEdge, Player player) {
        if (rules.checkRoadPlacement(placeEdge, player, this)) {
            Road road = new Road(player, placeEdge);
            player.getPlayerRoads().add(road);
            builtEdges.add(new int[] { placeEdge.getStart(), placeEdge.getEnd() });
            return true;
        }
        return false;
    }

    public List<HexTerrain> getHexes() {
        return new ArrayList<>(tiles.values());
    }

    public HexTerrain getHex(int hexID) {
        return tiles.get(hexID);
    }

    public List<Integer> getHexIntersections(int hexID) {
        List<Integer> hexes = new ArrayList<>();

        for (int hex : tilesNodes[hexID]) {
            hexes.add(hex);
        }

        return hexes;
    }

    public int getRobberHexID() {
        return robberHexID;
    }

    public void moveRobber(Random random) {
        int moveTile;
        do {
            moveTile = random.nextInt(19);
        }
        while (moveTile == robberHexID);
        robberHexID = moveTile;
    }

    public List<Player> getNearPlayers(List<Player> players) {
        List<Integer> nearbyIntersections = getHexIntersections(robberHexID);
        List<Player> playersList = new ArrayList<>();

        for (Player player: players) {
            for (int intersectionID: nearbyIntersections) {
                Intersection intersection = getIntersection(intersectionID);
                if (intersection != null && intersection.getPlayer() == player) {
                    if (!playersList.contains(player)) {
                        playersList.add(player);
                    }
                }
            }
        }
        return playersList;
    }

    public void removeRoad(int start, int end, Player player) {
        builtEdges.removeIf(e -> (e[0] == start && e[1] == end) || (e[0] == end && e[1] == start));
        player.getPlayerRoads().removeIf(r ->
            (r.getLocation().getStart() == start && r.getLocation().getEnd() == end) ||
            (r.getLocation().getStart() == end && r.getLocation().getEnd() == start));
    }
}
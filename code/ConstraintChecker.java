import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintChecker {

    private Board board;
    private List<Player> allPlayers;

    public ConstraintChecker(Board board, List<Player> allPlayers) {
        this.board = board;
        this.allPlayers = allPlayers;
    }

    // Returns action string if a constraint fired, null if none apply
    public String check(Player player) {

        // R3.3 Constraint 1: more than 7 cards
        if (player.getTotalResources() > 7) {
            return "forced spend: " + spendCards(player);
        }

        // R3.3 Constraint 2: two road segments within 2 units
        if (hasRoadGap(player)) {
            return "road gap: " + buildConnectedRoad(player);
        }

        // R3.3 Constraint 3: opponent longest road within 1 of ours
        if (longestRoadThreat(player)) {
            return "road defense: " + buildConnectedRoad(player);
        }

        return null;
    }

    // Constraint 1: spend cards 
    private String spendCards(Player player) {
        for (Building b : player.getSettlements()) {
            String result = player.buildCity(board, b.getBuildlocation());
            if (result.startsWith("upgraded")) {
                return result;
            }
        }
        
        for (int i = 0; i <= 53; i++) {
            Intersection spot = board.getIntersection(i);
            if (spot != null && spot.getBuilding() == null) {
                String result = player.buildSettlement(board, spot);
                if (result.startsWith("built")) {
                    return result;
                }
            }
        }

        for (int i = 0; i <= 53; i++) {
            for (int j = i + 1; j <= 53; j++) {
                if (board.isValidEdge(i, j)) {
                    String result = player.buildRoad(board, new Edge(i, j));
                    if (result.startsWith("built")) {
                        return result;
                    }
                }
            }
        }

        return "not enough resources to build anything";
    }

    // Constraint 2: check if two of player's road endpoints are less than or equal to 2 edges apart
    private boolean hasRoadGap(Player player) {
        List<Road> roads = player.getPlayerRoads();
        
        // need at least 2 roads for a gap to exist
        if (roads.size() < 2) return false;
        
        for (int a = 0; a < roads.size(); a++) {
            for (int b = a + 1; b < roads.size(); b++) {
                int endA = roads.get(a).getLocation().getEnd();
                int startA = roads.get(a).getLocation().getStart();
                int endB = roads.get(b).getLocation().getEnd();
                int startB = roads.get(b).getLocation().getStart();

                if (endA != startB && withinTwoEdges(endA, startB)) {
                    return true;
                }

                if (startA != endB && withinTwoEdges(startA, endB)) {
                    return true;
                }

                if (startA != startB && withinTwoEdges(startA, startB)) {
                    return true;
                }

                if (endA != endB && withinTwoEdges(endA, endB)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean withinTwoEdges(int from, int to) {
        if (board.isValidEdge(from, to)) {
            return true;
        }
        
        for (int mid : board.getNeighbouringIntersections(from)) {
            if (board.isValidEdge(mid, to)) {
                return true;
            }
        }

        return false;
    }

    // Constraint 3: check if any opponent is within 1 road of our longest
    private boolean longestRoadThreat(Player player) {
        int myLongest = calcLongestRoad(player);

        if (myLongest == 0) {
            return false;
        }

        for (Player other : allPlayers) {
            if (other == player) {
                continue;
            }

            if (calcLongestRoad(other) >= myLongest - 1) {
                return true;
            }
        }
        return false;
    }

    private String buildConnectedRoad(Player player) {
        for (int i = 0; i <= 53; i++) {
            for (int j = i + 1; j <= 53; j++) {
                if (board.isValidEdge(i, j)) {
                    String result = player.buildRoad(board, new Edge(i, j));
                    if (result.startsWith("built")) {
                        return result;
                    }
                }
            }
        }

        return "couldn't afford or place road";
    }

    private int calcLongestRoad(Player player) {
        int max = 0;
        List<Road> roads = player.getPlayerRoads();
        for (Road road : roads) {
            int len = dfs(road.getLocation().getStart(), roads, new HashSet<>());
            max = Math.max(max, len);
        }

        return max;
    }

    private int dfs(int node, List<Road> roads, Set<String> visited) {
        int max = 0;
        for (Road road : roads) {
            int s = road.getLocation().getStart();
            int e = road.getLocation().getEnd();
            String key = Math.min(s, e) + "-" + Math.max(s, e);
            int next = -1;
            
            if (s == node) {
                next = e;
            } 
            
            else if (e == node) {
                next = s;
            }

            if (next != -1 && !visited.contains(key)) {
                visited.add(key);
                max = Math.max(max, 1 + dfs(next, roads, visited));
                visited.remove(key);
            }
        }

        return max;
    }
}
package code;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class LongestRoadHandler extends ConstraintHandler {

    @Override
    public String handle(Player player, Board board, List<Player> allPlayers) {
        if (longestRoadThreat(player, allPlayers)) {
            return "road defense: " + buildConnectedRoad(player, board);
        }
        return super.handle(player, board, allPlayers);
    }

    private boolean longestRoadThreat(Player player, List<Player> allPlayers) {
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
            } else if (e == node) {
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

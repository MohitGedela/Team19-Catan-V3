package code;

import java.util.List;

class RoadGapHandler extends ConstraintHandler {

    @Override
    public String handle(Player player, Board board, List<Player> allPlayers) {
        if (hasRoadGap(player, board)) {
            return "road gap: " + buildConnectedRoad(player, board);
        }
        return super.handle(player, board, allPlayers);
    }

    private boolean hasRoadGap(Player player, Board board) {
        List<Road> roads = player.getPlayerRoads();

        if (roads.size() < 2)
            return false;

        for (int a = 0; a < roads.size(); a++) {
            for (int b = a + 1; b < roads.size(); b++) {
                int endA = roads.get(a).getLocation().getEnd();
                int startA = roads.get(a).getLocation().getStart();
                int endB = roads.get(b).getLocation().getEnd();
                int startB = roads.get(b).getLocation().getStart();

                if (endA != startB && withinTwoEdges(endA, startB, board)) {
                    return true;
                }

                if (startA != endB && withinTwoEdges(startA, endB, board)) {
                    return true;
                }

                if (startA != startB && withinTwoEdges(startA, startB, board)) {
                    return true;
                }

                if (endA != endB && withinTwoEdges(endA, endB, board)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean withinTwoEdges(int from, int to, Board board) {
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
}

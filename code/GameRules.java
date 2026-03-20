import java.util.List;

// Checks if a spot is ok to build on (empty, not next to another building, or connected by your road).
class GameRules {

    public boolean checkBuildingPlacement(int intersectionID, Player player, Board board) {
        if (checkEmptyIntersections(intersectionID, board) == false) {
            return false;
        }

        return true;
    }

    // True if this corner is empty and every neighbour corner is also empty (distance rule).
    public boolean checkEmptyIntersections(int intersectionID, Board board) {
        Intersection currentIntersection = board.getIntersection(intersectionID);

        if (currentIntersection.getBuilding() == null) {
            List<Integer> neighbourIntersections = board.getNeighbouringIntersections(intersectionID);

            for (Integer neighbourID : neighbourIntersections) {
                Intersection neighbour = board.getIntersection(neighbourID);

                if (neighbour.getBuilding() != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // True if the player has at least one road that touches this corner.
    public boolean isConnected(int intersectionID, Player player, Board board) {
        List<Road> playerRoads = player.getPlayerRoads();

        for (Road road : playerRoads) {
            int roadStart = road.getLocation().getStart();
            int roadEnd = road.getLocation().getEnd();

            if (roadStart == intersectionID || roadEnd == intersectionID) {
                return true;
            }
        }
        return false;
    }

    // Edge must exist on board, not be built yet, and touch your settlement or one of your roads.
    public boolean checkRoadPlacement(Edge roadEdge, Player player, Board board) {
        int startID = roadEdge.getStart();
        int endID = roadEdge.getEnd();

        if (!board.isValidEdge(startID, endID)) {
            return false;
        }

        if (board.isEdgeOccupied(startID, endID)) {
            return false;
        }

        Intersection intersectionStart = board.getIntersection(startID);
        Intersection intersectionEnd = board.getIntersection(endID);

        if (intersectionStart.getBuilding() != null && intersectionStart.getPlayer() == player) {
            return true;
        }
        if (intersectionEnd.getBuilding() != null && intersectionEnd.getPlayer() == player) {
            return true;
        }

        for (Road road : player.getPlayerRoads()) {
            int startPoint = road.getLocation().getStart();
            int endPoint = road.getLocation().getEnd();

            if (startPoint == startID || startPoint == endID) {
                int sharedNode;
                if (startPoint == startID) {
                    sharedNode = startID;
                } else {
                    sharedNode = endID;
                }
                Intersection shared = board.getIntersection(sharedNode);
                if (shared.getBuilding() == null || shared.getPlayer() == player) {
                    return true;
                }
                
            } else if (endPoint == startID || endPoint == endID) {
                int sharedNode;
                if (endPoint == startID) {
                    sharedNode = startID;
                } else {
                    sharedNode = endID;
                }
                Intersection shared = board.getIntersection(sharedNode);
                if (shared.getBuilding() == null || shared.getPlayer() == player) {
                    return true;
                }
            }
        }
        return false;
    }
}
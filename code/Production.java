package code;

import java.util.List;

// When dice are rolled, gives resources to players who have a building on a hex with that number.
class Production {

    private Board board;

    public Production(Board gameBoard) {
        board = gameBoard;
    }

    public void generateResources(int diceRollNum) {

        for (HexTerrain hex : board.getHexes()) {
            if (!hex.productionStatus() || hex.getHexID() == board.getRobberHexID()) {
                continue;
            }

            HexBoardNum boardNumber = hex.getHexNumber();
            if (boardNumber != null && boardNumber.getHexNum() == diceRollNum) {
                ResourceType resource = hex.produceResource();

                List<Integer> intersectionIDs = board.getHexIntersections(hex.getHexID());
                for (Integer intersectionID : intersectionIDs) {
                    Intersection intersection = board.getIntersection(intersectionID);

                    if (intersection.getBuilding() != null) {
                        Player intersectionOwner = intersection.getPlayer();
                        int quantity = intersection.getBuilding().getResourceMultiplier();

                        intersectionOwner.addResource(resource, quantity);
                    }
                }
            }
        }
    }
}
package code;

import java.util.List;

class CardLimitHandler extends ConstraintHandler {

    @Override
    public String handle(Player player, Board board, List<Player> allPlayers) {
        if (player.getTotalResources() > 7) {
            return "forced spend: " + spendCards(player, board);
        }
        return super.handle(player, board, allPlayers);
    }

    private String spendCards(Player player, Board board) {
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
}

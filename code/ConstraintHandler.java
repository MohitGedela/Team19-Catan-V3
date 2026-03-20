package code;

import java.util.List;

abstract class ConstraintHandler {

    private ConstraintHandler next;

    public ConstraintHandler setNext(ConstraintHandler next) {
        this.next = next;
        return next;
    }

    public String handle(Player player, Board board, List<Player> allPlayers) {
        if (next != null) {
            return next.handle(player, board, allPlayers);
        }
        return null;
    }

    protected String buildConnectedRoad(Player player, Board board) {
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
}

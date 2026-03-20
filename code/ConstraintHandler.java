package code;

import java.util.List;

/**
 * R3.3 — Chain of Responsibility: each handler tries its condition; if it does not apply, it forwards
 * to {@link #next} via {@link #handle(Player, Board, List)}. Wired in {@link ComputerPlayer#takeAction}.
 */
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

public class SpendingCards implements Rule {
    private static final double VALUE = 0.5;
    private static final int MAX_CARDS = 5;

    @Override
    public double evaluate(Player player) {
        if (player.getTotalResources() >= MAX_CARDS) {
            return VALUE;
        }
        return 0.0;
    }

    @Override
    public void execute(Player player, Board board) {
        for (Building b : player.getSettlements()) {
            String result = player.buildCity(board, b.getBuildlocation());
            if (result.startsWith("upgraded")) {
                return;
            }
        }
        for (int i = 0; i <= 53; i++) {
            Intersection spot = board.getIntersection(i);
            if (spot != null) {
                String result = player.buildSettlement(board, spot);
                if (result.startsWith("built")) {
                    return;
                }
            }
        }
        for (int i = 0; i <= 53; i++) {
            for (int j = i + 1; j <= 53; j++) {
                String result = player.buildRoad(board, new Edge(i, j));
                if (result.startsWith("built")) {
                    return;
                }
            }
        }
    }
}
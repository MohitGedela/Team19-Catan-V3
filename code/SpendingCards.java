package code;

public class SpendingCards implements Strategy {
    private final double VALUE = 0.5;
    private final int MAX_CARDS = 5;

    @Override
    public double evaluate(Player player) {
        int totalCards = player.getTotalResources();

        if (player.checkResource(ResourceType.WHEAT, 2) && player.checkResource(ResourceType.ORE, 3)
                && totalCards - 5 < MAX_CARDS) {
            return VALUE;
        }

        if (player.checkResource(ResourceType.WOOD, 1) && player.checkResource(ResourceType.BRICK, 1)
                && player.checkResource(ResourceType.SHEEP, 1)
                && player.checkResource(ResourceType.WHEAT, 1)
                && totalCards - 4 < MAX_CARDS) {
            return VALUE;
        }

        if (player.checkResource(ResourceType.BRICK, 1) && player.checkResource(ResourceType.WOOD, 1)
                && totalCards - 2 < MAX_CARDS) {
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
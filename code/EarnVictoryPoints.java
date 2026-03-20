package code;

public class EarnVictoryPoints implements Rule {

    private static final double VALUE = 1.0;

    @Override
    public double evaluate(Player player) {
        if (player.checkResource(ResourceType.WOOD, 1)
                && player.checkResource(ResourceType.BRICK, 1)
                && player.checkResource(ResourceType.SHEEP, 1)
                && player.checkResource(ResourceType.WHEAT, 1)) {
            return VALUE;
        }
        return 0.0;
    }

    @Override
    public void execute(Player player, Board board) {
        for (int i = 0; i <= 53; i++) {
            Intersection spot = board.getIntersection(i);
            if (spot != null) {
                String result = player.buildSettlement(board, spot);
                if (result.startsWith("built")) {
                    return;
                }
            }
        }
    }
}
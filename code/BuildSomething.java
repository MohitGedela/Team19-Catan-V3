package code;

public class BuildSomething implements Rule {

    private static final double VALUE = 0.8;

    @Override
    public double evaluate(Player player) {
        if (player.checkResource(ResourceType.BRICK, 1)
                && player.checkResource(ResourceType.WOOD, 1)) {
            return VALUE;
        }
        return 0.0;
    }

    @Override
    public void execute(Player player, Board board) {
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
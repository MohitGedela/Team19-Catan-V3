package code;

class Build implements Command {

    private String input;

    public Build(String input) {
        this.input = input;
    }

    @Override
    public boolean endsTurn() {
        return false;
    }

    @Override
    public boolean requiresRoll() {
        return true;
    }

    @Override
    public String execute(Player player, Board board, Turn turn) {
        if (input.matches("(?i)Build\\s+settlement\\s+\\d+")) {
            String[] parts = input.split("\\s+");
            int nodeId = Integer.parseInt(parts[2]);
            Intersection spot = board.getIntersection(nodeId);
            if (spot == null) {
                return "invalid node ID: " + nodeId;
            }
            return player.buildSettlement(board, spot);

        } else if (input.matches("(?i)Build\\s+city\\s+\\d+")) {
            String[] parts = input.split("\\s+");
            int nodeId = Integer.parseInt(parts[2]);
            Intersection spot = board.getIntersection(nodeId);
            if (spot == null) {
                return "invalid node ID: " + nodeId;
            }
            return player.buildCity(board, spot);

        } else if (input.matches("(?i)Build\\s+road\\s+\\d+\\s+\\d+")) {
            String[] parts = input.split("\\s+");
            int startNode = Integer.parseInt(parts[2]);
            int endNode = Integer.parseInt(parts[3]);
            if (!board.isValidEdge(startNode, endNode)) {
                return "invalid edge: " + startNode + "-" + endNode;
            }
            return player.buildRoad(board, new Edge(startNode, endNode));
        }
        return "build attempted";
    }
}
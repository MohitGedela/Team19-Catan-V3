package code;
class PlaceInitialRoad extends Command {
    private int startNode;
    private int endNode;

    PlaceInitialRoad(int startNode, int endNode, Player player, Board board, Turn turn) {
        super(player, board, turn);
        this.startNode = startNode;
        this.endNode = endNode;
    }

    @Override
    public boolean execute() {
        Edge edge = new Edge(startNode, endNode);
        if (board.placeRoad(edge, player)) {
            lastAction = "road";
            lastStart = startNode;
            lastEnd = endNode;
            return true;
        }
        return false;
    }

    @Override
    public void undo() {
        if (lastAction.equals("road")) {
            board.removeRoad(lastStart, lastEnd, player);
            System.out.println("Undid initial road from " + lastStart + " to " + lastEnd);
        }
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean endsTurn() { 
        return false; 
    }

    @Override
    public boolean requiresRoll() { 
        return false; 
    }
}
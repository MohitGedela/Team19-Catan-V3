class PlaceInitialSettlement extends Command {
    private int nodeID;

    PlaceInitialSettlement(int nodeID, Player player, Board board, Turn turn) {
        super(player, board, turn);
        this.nodeID = nodeID;
    }

    @Override
    public boolean execute() {
        Intersection spot = board.getIntersection(nodeID);
        if (board.placeSettlement(spot, player)) {
            player.addVictoryPoint();
            lastAction = "settlement";
            lastNode = nodeID;
            return true;
        }
        return false;
    }

    @Override
    public void undo() {
        if (lastAction.equals("settlement")) {
            Intersection spot = board.getIntersection(lastNode);
            spot.setBuilding(null);
            spot.setOwner(null);
            player.getPlayerBuildings().removeIf(b -> b.getBuildlocation() == spot);
            player.removeVictoryPoint();
            System.out.println("Undid initial settlement at node " + lastNode);
        }
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
package code;

class Build extends Command {
    private String input;

    Build(String input, Player player, Board board, Turn turn) {
        super(player, board, turn);
        this.input = input;
    }

    @Override
    public boolean execute() {
        if (input.matches("(?i)Build\\s+settlement\\s+\\d+")) {
            String[] parts = input.split("\\s+");
            int nodeId = Integer.parseInt(parts[2]);
            Intersection spot = board.getIntersection(nodeId);
            if (spot == null) {
                System.out.println(turn.formatAction(player, "invalid node ID: " + nodeId));
                return false;
            }
            String result = player.buildSettlement(board, spot);
            System.out.println(turn.formatAction(player, result));
            if (result.startsWith("built")) {
                lastAction = "settlement";
                lastNode = nodeId;
                return true;
            }
            return false;

        } else if (input.matches("(?i)Build\\s+city\\s+\\d+")) {
            String[] parts = input.split("\\s+");
            int nodeId = Integer.parseInt(parts[2]);
            Intersection spot = board.getIntersection(nodeId);
            if (spot == null) {
                System.out.println(turn.formatAction(player, "invalid node ID: " + nodeId));
                return false;
            }
            String result = player.buildCity(board, spot);
            System.out.println(turn.formatAction(player, result));
            if (result.startsWith("upgraded")) {
                lastAction = "city";
                lastNode = nodeId;
                return true;
            }
            return false;

        } else if (input.matches("(?i)Build\\s+road\\s+\\d+\\s+\\d+")) {
            String[] parts = input.split("\\s+");
            int startNode = Integer.parseInt(parts[2]);
            int endNode = Integer.parseInt(parts[3]);
            if (!board.isValidEdge(startNode, endNode)) {
                System.out.println(turn.formatAction(player, "invalid edge: " + startNode + "-" + endNode));
                return false;
            }
            String result = player.buildRoad(board, new Edge(startNode, endNode));
            System.out.println(turn.formatAction(player, result));
            if (result.startsWith("built")) {
                lastAction = "road";
                lastStart = startNode;
                lastEnd = endNode;
                return true;
            }
            return false;
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
            player.addResource(ResourceType.WOOD, 1);
            player.addResource(ResourceType.BRICK, 1);
            player.addResource(ResourceType.SHEEP, 1);
            player.addResource(ResourceType.WHEAT, 1);
            player.removeVictoryPoint();
            System.out.println(turn.formatAction(player, "undid settlement at node " + lastNode));

        } else if (lastAction.equals("city")) {
            Intersection spot = board.getIntersection(lastNode);
            Settlement s = new Settlement(spot, player);
            spot.setBuilding(s);
            player.getPlayerBuildings()
                    .removeIf(b -> b.getBuildlocation() == spot && b.getBuildingType() == Building.BuildingType.CITY);
            player.getPlayerBuildings().add(s);
            player.addResource(ResourceType.WHEAT, 2);
            player.addResource(ResourceType.ORE, 3);
            player.removeVictoryPoint();
            System.out.println(turn.formatAction(player, "undid city upgrade at node " + lastNode));

        } else if (lastAction.equals("road")) {
            board.removeRoad(lastStart, lastEnd, player);
            player.addResource(ResourceType.BRICK, 1);
            player.addResource(ResourceType.WOOD, 1);
            System.out.println(turn.formatAction(player, "undid road from " + lastStart + " to " + lastEnd));
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
        return true;
    }

    @Override
    public void undo() {
        if (lastAction.equals("settlement")) {
            Intersection spot = board.getIntersection(lastNode);
            spot.setBuilding(null);
            spot.setOwner(null);
            player.getPlayerBuildings().removeIf(b -> b.getBuildlocation() == spot);
            player.addResource(ResourceType.WOOD, 1);
            player.addResource(ResourceType.BRICK, 1);
            player.addResource(ResourceType.SHEEP, 1);
            player.addResource(ResourceType.WHEAT, 1);
            player.removeVictoryPoint();
            System.out.println(turn.formatAction(player, "undid settlement at node " + lastNode));

        } else if (lastAction.equals("city")) {
            Intersection spot = board.getIntersection(lastNode);
            Settlement s = new Settlement(spot, player);
            spot.setBuilding(s);
            player.getPlayerBuildings().removeIf(b -> b.getBuildlocation() == spot && b.getBuildingType() == Building.BuildingType.CITY);
            player.getPlayerBuildings().add(s);
            player.addResource(ResourceType.WHEAT, 2);
            player.addResource(ResourceType.ORE, 3);
            player.removeVictoryPoint();
            System.out.println(turn.formatAction(player, "undid city upgrade at node " + lastNode));

        } else if (lastAction.equals("road")) {
            board.removeRoad(lastStart, lastEnd, player);
            player.addResource(ResourceType.BRICK, 1);
            player.addResource(ResourceType.WOOD, 1);
            System.out.println(turn.formatAction(player, "undid road from " + lastStart + " to " + lastEnd));
        }
    }

    @Override
    public boolean endsTurn() { return false; }

    @Override
    public boolean requiresRoll() { return true; }
}
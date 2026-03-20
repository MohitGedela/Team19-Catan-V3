package code;

import java.util.*;

class ComputerPlayer extends Player {
    private Random random = new Random();
    private Agent agent;

    public ComputerPlayer(int playerNum, int playerVP, ArrayList<Building> buildings, ArrayList<Road> roads,
            Map<ResourceType, Integer> resources) {
        super(playerNum, playerVP, buildings, roads, resources);
        this.agent = new Agent();
    }

    @Override
    public String takeAction(Board board, Turn turn) {
        String rollResult = turn.doRoll(this);

        // if robber was activated, skip constraints and R3.2
        if (rollResult.contains("Robber activated")) {
            return rollResult;
        }

        // R3.3: check constraints via Chain of Responsibility
        ConstraintHandler chain = new CardLimitHandler();
        chain.setNext(new RoadGapHandler()).setNext(new LongestRoadHandler());

        String constraintResult = chain.handle(this, board, turn.getPlayers());
        if (constraintResult != null) {
            return rollResult + ", " + constraintResult;
        }

        // R3.2: no constraint fired, use agent rule evaluation
        return agent.act(this, board, turn);
    }

    @Override
    public void initialSetup(Board board, Scanner scanner, Visualizer visualizer) {
        List<Integer> validSpots = new ArrayList<>();
        for (int i = 0; i <= 53; i++) {
            Intersection spot = board.getIntersection(i);
            if (spot == null)
                continue;
            boolean valid = spot.getBuilding() == null;
            for (int neighbour : board.getNeighbouringIntersections(i)) {
                Intersection n = board.getIntersection(neighbour);
                if (n == null)
                    continue;
                if (n.getBuilding() != null) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                validSpots.add(i);
            }
        }

        int picked = validSpots.get(random.nextInt(validSpots.size()));
        board.placeSettlement(board.getIntersection(picked), this);
        addVictoryPoint();
        System.out.println("\nPlayer " + playerID + " placed settlement at node " + picked);
        visualizer.refresh();

        List<Integer> neighbours = board.getNeighbouringIntersections(picked);
        int roadEnd = neighbours.get(random.nextInt(neighbours.size()));
        board.placeRoad(new Edge(picked, roadEnd), this);
        System.out.println("Player " + playerID + " placed road at " + picked + "-" + roadEnd);
        visualizer.refresh();

        System.out.println("Enter Go to continue.");
        while (!scanner.nextLine().trim().matches("(?i)Go")) {
            System.out.println("Enter Go to continue.");
        }
    }

    @Override
    public boolean requiresGoPrompt() {
        return true;
    }
}
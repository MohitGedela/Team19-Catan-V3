package code;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// One player: their cards (resources), buildings (settlements/cities/roads), and VP. Can build if they have the right cards.
class ComputerPlayer extends Player {

    private Random random = new Random();

    public ComputerPlayer(int playerNum, int playerVP, List<Building> buildings, List<Road> roads, Map<ResourceType, Integer> resources) {
        super(playerNum, playerVP, buildings, roads, resources);
    }

    @Override
    public String takeAction(Board board, Turn turn) {
        String rollResult = turn.doRoll(this);

        if (getTotalResources() > 7) {
            for (int i = 0; i < getSettlements().size(); i++) {
                Intersection spot = getSettlements().get(i).getBuildlocation();
                String result = buildCity(board, spot);
                if (result.startsWith("upgraded")) {
                    return rollResult + ", forced spend: " + result;
                }
            }

            for (int i = 0; i <= 53; i++) {
                Intersection spot = board.getIntersection(i);
                if (spot != null && spot.getBuilding() == null) {
                    String result = buildSettlement(board, spot);
                    if (result.startsWith("built")) {
                        return rollResult + ", forced spend: " + result;
                    }
                }
            }

            for (int i = 0; i <= 53; i++) {
                for (int j = i + 1; j <= 53; j++) {
                    if (board.isValidEdge(i, j)) {
                        Edge edge = new Edge(i, j);
                        String result = buildRoad(board, edge);
                        if (result.startsWith("built")) {
                            return rollResult + ", forced spend: " + result;
                        }
                    }
                }
            }
            return rollResult + ", forced spend: not enough resources to build anything";
        }

        int action = random.nextInt(3);

        if (action == 0) {
            List<Integer> validSpots = new ArrayList<>();
            for (int i = 0; i <= 53; i++) {
                Intersection intersection = board.getIntersection(i);
                if (intersection != null) {
                    validSpots.add(i);
                }
            }
            
            if (!validSpots.isEmpty()) {
                int randomIndex = random.nextInt(validSpots.size());
                int nodeId = validSpots.get(randomIndex);
                Intersection target = board.getIntersection(nodeId);
                String result = buildSettlement(board, target);
                return rollResult + ", " + result;
            }

        } else if (action == 1) {
            if (!getSettlements().isEmpty()) {
                int randomIndex = random.nextInt(getSettlements().size());
                Intersection target = getSettlements().get(randomIndex).getBuildlocation();
                String result = buildCity(board, target);
                return rollResult + ", " + result;
            }

        } else {
            List<int[]> validEdges = new ArrayList<>();
            for (int i = 0; i <= 53; i++) {
                for (int j = i + 1; j <= 53; j++) {
                    if (board.isValidEdge(i, j)) {
                        validEdges.add(new int[] { i, j });
                    }
                }
            }
            if (!validEdges.isEmpty()) {
                int randomIndex = random.nextInt(validEdges.size());
                int[] picked = validEdges.get(randomIndex);
                Edge edge = new Edge(picked[0], picked[1]);
                String result = buildRoad(board, edge);
                return rollResult + ", " + result;
            }
        }

        return rollResult + ", no action taken";
    }

    @Override
    public void initialSetup(Board board, Scanner scanner, Visualizer visualizer) {
        List<Integer> validSpots = new ArrayList<>();
        for (int i = 0; i <= 53; i++) {
            Intersection spot = board.getIntersection(i);
            if (spot == null) continue;
            boolean valid = spot.getBuilding() == null;
            for (int neighbour : board.getNeighbouringIntersections(i)) {
                Intersection n = board.getIntersection(neighbour);
                if (n == null) continue;
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

        Edge edge = new Edge(picked, roadEnd);
        board.placeRoad(edge, this);
        System.out.println("Player " + playerID + " placed road at " + picked + "-" + roadEnd);
        visualizer.refresh();

        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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
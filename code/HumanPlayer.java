package code;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

class HumanPlayer extends Player {

    private ParseCommand parser;

    public HumanPlayer(int playerNum, int playerVP, List<Building> buildings, List<Road> roads,
            Map<ResourceType, Integer> resources, ParseCommand parser) {
        super(playerNum, playerVP, buildings, roads, resources);
        this.parser = parser;
    }

    @Override
    public String takeAction(Board board, Turn turn) {
        Scanner scanner = new Scanner(System.in);
        boolean hasRolled = false;

        while (true) {
            System.out.println("\nPlease enter one of the following commands:");
            System.out.println("Roll");
            System.out.println("Go");
            System.out.println("List");
            System.out.println("Build settlement [nodeID]");
            System.out.println("Build city [nodeID]");
            System.out.println("Build road [fromNodeID] [toNodeID]");

            String input = scanner.nextLine().trim();
            Command command = parser.parse(input);

            if (command == null) {
                System.out.println("Invalid command, try again!");
                continue;
            }

            if (!hasRolled && command.requiresRoll()) {
                System.out.println("You must roll first!");
                continue;
            }

            if (!command.requiresRoll() && hasRolled) {
                System.out.println("You already rolled this turn!");
                continue;
            }

            String result = command.execute(this, board, turn);

            if (command.endsTurn())
                break;

            if (!command.requiresRoll() && !hasRolled) {
                hasRolled = true;
            }
            System.out.println(turn.formatAction(this, result));
        }
        return "";
    }

    @Override
    public boolean requiresGoPrompt() {
        return false;
    }

    @Override
    public void initialSetup(Board board, Scanner scanner, Visualizer visualizer) {
        int settlementNode = -1;

        while (true) {
            System.out.print("\nPlayer " + playerID + ", place your settlement (node 0-53): ");
            String input = scanner.nextLine().trim();
            try {
                int nodeID = Integer.parseInt(input);
                Intersection spot = board.getIntersection(nodeID);
                if (spot == null || nodeID < 0 || nodeID > 53) {
                    System.out.println("Invalid node.");
                    continue;
                }
                if (board.placeSettlement(spot, this)) {
                    addVictoryPoint();
                    settlementNode = nodeID;
                    visualizer.refresh();
                    break;
                } else {
                    System.out.println("Invalid spot, try another.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }

        while (true) {
            System.out.print(
                    "Player " + playerID + ", place your road (enter adjacent node to " + settlementNode + "): ");
            String input = scanner.nextLine().trim();
            try {
                int endNode = Integer.parseInt(input);
                if (!board.isValidEdge(settlementNode, endNode)) {
                    System.out.println("Not adjacent to your settlement.");
                    continue;
                }
                Edge edge = new Edge(settlementNode, endNode);
                if (board.placeRoad(edge, this)) {
                    visualizer.refresh();
                    break;
                } else {
                    System.out.println("Invalid road placement.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}
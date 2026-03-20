package code;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

class HumanPlayer extends Player {

    private ParseCommand parser;
    private CommandHistory history = new CommandHistory();
    private Turn currentTurn;

    public HumanPlayer(int playerNum, int playerVP, List<Building> buildings, List<Road> roads,
            Map<ResourceType, Integer> resources, ParseCommand parser) {
        super(playerNum, playerVP, buildings, roads, resources);
        this.parser = parser;
    }

    public void setTurn(Turn turn) {
        this.currentTurn = turn;
    }

    private boolean executeCommand(Command command) {
        if (command.execute()) {
            if (command.canUndo()) {
                history.push(command);
            }
            return true;
        }
        return false;
    }

    private void undo() {
        if (!history.canUndo()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Command command = history.undoPop();
        if (command != null) {
            command.undo();
        }
    }

    private void redo() {
        if (!history.canRedo()) {
            System.out.println("Nothing to redo.");
            return;
        }
        Command command = history.redoPop();
        if (command != null) {
            command.execute();
        }
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
            System.out.println("Undo");
            System.out.println("Redo");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Undo")) {
                undo();
                continue;
            }

            if (input.equalsIgnoreCase("Redo")) {
                redo();
                continue;
            }

            Command command = parser.parse(input, this, board, turn);

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

            executeCommand(command);

            if (command.endsTurn()) break;

            if (!command.requiresRoll() && !hasRolled) {
                hasRolled = true;
            }
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

            if (input.equalsIgnoreCase("Undo")) {
                undo();
                settlementNode = -1;
                continue;
            }

            try {
                int nodeID = Integer.parseInt(input);
                Intersection spot = board.getIntersection(nodeID);
                if (spot == null || nodeID < 0 || nodeID > 53) {
                    System.out.println("Invalid node.");
                    continue;
                }
                PlaceInitialSettlement cmd = new PlaceInitialSettlement(nodeID, this, board, currentTurn);
                if (executeCommand(cmd)) {
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
            System.out.print("Place your road (enter adjacent node to " + settlementNode + ") or type Undo: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Undo")) {
                undo(); // undoes the settlement
                settlementNode = -1;
                // drop back into settlement placement loop
                while (true) {
                    System.out.print("\nPlayer " + playerID + ", place your settlement (node 0-53): ");
                    String settlementInput = scanner.nextLine().trim();

                    if (settlementInput.equalsIgnoreCase("Undo")) {
                        undo();
                        continue;
                    }

                    try {
                        int nodeID = Integer.parseInt(settlementInput);
                        Intersection spot = board.getIntersection(nodeID);
                        if (spot == null || nodeID < 0 || nodeID > 53) {
                            System.out.println("Invalid node.");
                            continue;
                        }
                        PlaceInitialSettlement cmd = new PlaceInitialSettlement(nodeID, this, board, currentTurn);
                        if (executeCommand(cmd)) {
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
                continue;
            }

            try {
                int endNode = Integer.parseInt(input);
                if (!board.isValidEdge(settlementNode, endNode)) {
                    System.out.println("Not adjacent to your settlement.");
                    continue;
                }
                PlaceInitialRoad cmd = new PlaceInitialRoad(settlementNode, endNode, this, board, currentTurn);
                if (executeCommand(cmd)) {
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
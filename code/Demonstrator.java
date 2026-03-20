import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This demonstrator class demonstrates the key functionality of the Catan
 * simulator,
 * including the players - human player and computer player, robber activation,
 * resource production, building placement, and visualizer integration.
 *
 * R2.1 - Human player accepts typed commands validated by regex
 * R2.2/3 - Visualizer writes board state to visualize/state.json after each
 * action
 * R2.4 - Human player uses "Go" command to step forward between turns
 * R2.5 - Rolling 7 triggers the Robber: discard, move, steal
 * R1.8 - Players with >7 cards are forced to spend before normal action
 */
class Demonstrator {

    public static void main(String[] args) throws Exception {

        // *** BOARD SETUP ***
        // The board holds 19 hex tiles, 54 intersections and edges. Production
        // maps dice rolls to resource-generating tiles and dice rolls two six-sided
        // dice each turn.
        Board board = new Board();
        Dice dice = new Dice();
        Production production = new Production(board);

        // *** CONFIG LOADING ***
        // Read maxRounds from config.txt so the game length is configurable
        // without recompiling. Demonstrates separation of configuration from logic.
        Scanner scanner = null;
        java.io.File cfgFile = new java.io.File("config.txt");
        if (!cfgFile.exists()) {
            cfgFile = new java.io.File("Task3/config.txt");
        }
        if (cfgFile.exists()) {
            scanner = new Scanner(cfgFile);
        } else {
            java.io.InputStream in = Demonstrator.class.getResourceAsStream("/Task3/config.txt");
            if (in == null) {
                throw new java.io.FileNotFoundException("config.txt not found in working dir, Task3/, or classpath");
            }
            scanner = new Scanner(in);
        }
        String line = scanner.nextLine();
        int maxRounds = Integer.parseInt(line.split(":")[1].trim());
        scanner.close();

        // *** PLAYER CREATION ***
        // Demonstrates the Open/Closed and Liskov Substitution principles:
        // Players 1-3 are ComputerPlayers (automated AI).
        // Player 4 is a HumanPlayer (command-line input).
        // Both extend the abstract Player class and are stored as List<Player>,
        // so the simulator treats them uniformly via polymorphism (LSP).
        List<Player> players = new ArrayList<Player>();

        Player p1 = new ComputerPlayer(1, 0, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
        Player p2 = new ComputerPlayer(2, 0, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
        Player p3 = new ComputerPlayer(3, 0, new ArrayList<>(), new ArrayList<>(), new HashMap<>());

        // *** HUMAN PLAYER (R2.1, R2.4) ***
        // HumanPlayer.takeAction() reads commands from stdin using regex validation.
        // Valid commands: Roll, Go, List, Build settlement [n], Build city [n], Build
        // road [n] [n]
        // "Go" steps the game forward (R2.4). All other commands block until Roll is
        // used first.
        ParseCommand parser = new ParseCommand();
        Player p4 = new HumanPlayer(4, 0, new ArrayList<>(), new ArrayList<>(), new HashMap<>(), parser);

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        // *** VISUALIZER SETUP (R2.2, R2.3) ***
        // Visualizer writes current board state (settlements, cities, roads, resources)
        // to visualize/state.json after every player action.
        // Turn now holds the visualizer reference and calls refresh() after each
        // action, keeping visualization logic out of Player (SRP).
        Visualizer visualizer = new Visualizer("visualize/state.json", players, board);
        visualizer.clear();
        Thread.sleep(600);

        // Turn is created after the visualizer so it can be passed in directly.
        // Turn.execute() calls visualizer.refresh() after each player action,
        // so Player no longer needs a reference to the visualizer.
        Turn turn = new Turn(dice, production, board, players, visualizer);

        // give HumanPlayer access to turn for initial placement commands
        for (Player p : players) {
            if (p instanceof HumanPlayer) {
                ((HumanPlayer) p).setTurn(turn);
            }
        }

        Scanner inputScanner = new Scanner(System.in);

        // *** INITIAL PLACEMENT PHASE ***
        // Each player places 2 settlements and 2 roads before the game begins.
        // ComputerPlayers place randomly on valid intersections.
        // HumanPlayer is prompted for node IDs via console input.
        // This demonstrates polymorphic dispatch: same initialSetup() call,
        // different behavior depending on player type (OCP + LSP).
        System.out.println("=== INITIAL PLACEMENT PHASE ===");
        System.out.println("Each player places 2 settlements and 2 roads.");
        System.out.println("Computer players place randomly. Human player (P4) enters node IDs.\n");

        for (int round = 0; round < 2; round++) {
            for (Player player : players) {
                player.initialSetup(board, inputScanner, visualizer);
            }
        }

        // *** BOARD STATE SUMMARY ***
        // Print tile assignments and initial settlement positions so the
        // human player knows the board layout before the game starts.
        System.out.println("\n=== TILE NUMBER ASSIGNMENTS ===");
        for (int i = 0; i < 19; i++) {
            HexTerrain hex = board.getHex(i);
            if (hex.productionStatus()) {
                System.out.println("Tile " + i + ": " + hex.produceResource() + " #" + hex.getHexNumber().getHexNum());
            } else {
                System.out.println("Tile " + i + ": DESERT");
            }
        }

        System.out.println("\n=== PLAYER SETTLEMENTS ===");
        for (Player p : players) {
            for (Building b : p.getSettlements()) {
                System.out.println("Player " + p.getPlayerID() + " settlement at node "
                        + b.getBuildlocation().getIntersectionLocation());
            }
        }

        // *** MAIN GAME LOOP (R2.5, R1.8) ***
        // Simulator runs rounds up to maxRounds or until a player reaches 10 VP.
        // Each turn: dice roll → resource production OR robber (if 7) → player action.
        //
        // R2.5: Rolling 7 triggers the Robber:
        // 1. All players with >7 cards discard half (R1.8)
        // 2. Robber moves to a new random non-desert tile
        // 3. Roller steals one random resource from an adjacent player
        //
        // R1.8: If a player has >7 cards on their turn (non-robber),
        // they are forced to spend before taking a normal action.
        //
        // Human player turn: type commands, "Go" to end turn.
        // Computer player turn: automated, press "Go" to advance.
        System.out.println("\n=== GAME START ===");
        System.out.println("Game runs for up to " + maxRounds + " rounds.");
        System.out.println("First player to 10 VP wins.\n");

        Simulator simulator = new Simulator(players, turn, maxRounds);
        simulator.runGame();

        // *** GAME END ***
        System.out.println("\n=== DEMONSTRATION COMPLETE ===");
        System.out.println("Final Victory Points:");
        for (Player p : players) {
            System.out.println("  Player " + p.getPlayerID() + ": " + p.getVictoryPoints() + " VP");
        }
    }
}
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class Visualizer {
    
    // Colours Valid for the Visualizer
    private static final String[] PLAYER_COLORS = { "RED", "BLUE", "ORANGE", "WHITE" };
    private String filePath;
    private List<Player> players;
    private Board board;

    public Visualizer(String filePath, List<Player> players, Board board) {
        this.filePath = filePath;
        this.players = players;
        this.board = board;
    }

    public void clear() {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.write("{\n  \"roads\": [],\n  \"buildings\": []\n}");
        } catch (IOException e) {
            System.out.println("Failed to clear state.json: " + e.getMessage());
        }
    }

    public void refresh() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // Roads
        json.append("  \"roads\": [\n");
        boolean firstRoad = true;
        for (Player player : players) {
            String color = PLAYER_COLORS[player.getPlayerID() - 1];
            for (Road road : player.getPlayerRoads()) {
                if (!firstRoad) json.append(",\n");
                int a = road.getLocation().getStart();
                int b = road.getLocation().getEnd();
                json.append("    { \"a\": " + a + ", \"b\": " + b + ", \"owner\": \"" + color + "\" }");
                firstRoad = false;
            }
        }
        json.append("\n  ],\n");

        // Buildings
        json.append("  \"buildings\": [\n");
        boolean firstBuilding = true;
        for (Player player : players) {
            String color = PLAYER_COLORS[player.getPlayerID() - 1];
            for (int i = 0; i <= 53; i++) {
                Intersection intersection = board.getIntersection(i);
                Building building = intersection.getBuilding();
                if (building != null && building.getOwner() == player) {
                    if (!firstBuilding) json.append(",\n");
                    String type = building.getBuildingType().name();
                    json.append("    { \"node\": " + i + ", \"owner\": \"" + color + "\", \"type\": \"" + type + "\" }");
                    firstBuilding = false;
                }
            }
        }
        json.append("\n  ]\n");

        json.append("}");

        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Failed to write state.json: " + e.getMessage());
        }
    }
}
import java.util.List;

class Turn {

    private Dice dice;
    private Production production;
    private Board board;
    private Robber robber;
    private List<Player> players;
    private int currentRound;
    private Visualizer visualizer;

    public Turn(Dice dice, Production production, Board board, List<Player> players, Visualizer visualizer) {
        this.dice = dice;
        this.production = production;
        this.board = board;
        this.players = players;
        this.robber = new Robber(board, new java.util.Random());
        this.visualizer = visualizer;
    }

    public String execute(Player player, int roundNumber) {
        this.currentRound = roundNumber;
        String actionResult = player.takeAction(board, this);
        visualizer.refresh();
        if (actionResult.isEmpty()) {
            return "";
        }
        return "[" + roundNumber + "] / [" + player.getPlayerID() + "]: " + actionResult;
    }

    public String doRoll(Player player) {
        int roll = dice.roll();
        if (roll == 7) {
            String robberResult = robber.runRobber(player, players);
            return "Rolled 7, Robber activated" + robberResult;
        } else {
            production.generateResources(roll);
            return "Rolled " + roll;
        }
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public String formatAction(Player player, String action) {
        return "[" + currentRound + "] / [" + player.getPlayerID() + "]: " + action;
    }

    public Board getBoard() {
        return board;
    }
}
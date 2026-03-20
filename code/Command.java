abstract class Command {
    protected Player player;
    protected Board board;
    protected Turn turn;
    protected String lastAction = "";
    protected int lastNode = -1;
    protected int lastStart = -1;
    protected int lastEnd = -1;

    Command(Player player, Board board, Turn turn) {
        this.player = player;
        this.board = board;
        this.turn = turn;
    }

    public abstract boolean execute();

    public void undo() {}

    public abstract boolean endsTurn();
    public abstract boolean requiresRoll();
}
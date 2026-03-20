package code;

/**
 * R3.1 — Command pattern: encapsulates a player action as an object with {@link #execute()},
 * {@link #undo()}, and metadata ({@link #endsTurn()}, {@link #requiresRoll()}, {@link #canUndo()}).
 * {@link HumanPlayer} uses {@link CommandHistory} for undo/redo; {@link ParseCommand} builds commands from text.
 */
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

    public void undo() {
    }

    public boolean canUndo() {
        return false;
    }

    public abstract boolean endsTurn();

    public abstract boolean requiresRoll();
}
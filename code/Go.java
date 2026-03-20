package code;

class Go extends Command {

    Go(Player player, Board board, Turn turn) {
        super(player, board, turn);
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void undo() {
    }

    @Override
    public boolean endsTurn() {
        return true;
    }

    @Override
    public boolean requiresRoll() {
        return true;
    }
}
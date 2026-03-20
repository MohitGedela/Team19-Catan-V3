package code;

class Roll implements Command {
    @Override
    public String execute(Player player, Board board, Turn turn) {
        return turn.doRoll(player);
    }

    @Override
    public boolean endsTurn() {
        return false;
    }

    @Override
    public boolean requiresRoll() {
        return false;
    }
}
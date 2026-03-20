package code;

class Go implements Command {

    @Override
    public String execute(Player player, Board board, Turn turn) {
        return "ended turn";
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

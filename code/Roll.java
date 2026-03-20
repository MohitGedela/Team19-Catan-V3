class Roll extends Command {

    Roll(Player player, Board board, Turn turn) {
        super(player, board, turn);
    }

    @Override
    public boolean execute() {
        String result = turn.doRoll(player);
        System.out.println(turn.formatAction(player, result));
        return true;
    }

    @Override
    public void undo() {}

    @Override
    public boolean endsTurn() { 
        return false; 
    }

    @Override
    public boolean requiresRoll() { 
        return false; 
    }
}
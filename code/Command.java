interface Command {

    public String execute(Player player, Board board, Turn turn);
    public boolean endsTurn();
    public boolean requiresRoll();
}
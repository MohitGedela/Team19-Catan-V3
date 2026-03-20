class ParseCommand {

    public Command parse(String input, Player player, Board board, Turn turn) {
        if (input.matches("(?i)Roll")) {
            return new Roll(player, board, turn);
        }
        if (input.matches("(?i)Go")) {
            return new Go(player, board, turn);
        }
        if (input.matches("(?i)List")) {
            return new List(player, board, turn);
        }
        if (input.matches("(?i)Build\\s+settlement\\s+\\d+")) {
            return new Build(input, player, board, turn);
        }
        if (input.matches("(?i)Build\\s+city\\s+\\d+")) {
            return new Build(input, player, board, turn);
        }
        if (input.matches("(?i)Build\\s+road\\s+\\d+\\s+\\d+")) {
            return new Build(input, player, board, turn);
        }
        return null;
    }
}
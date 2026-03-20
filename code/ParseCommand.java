package code;

class ParseCommand {

    public Command parse(String input) {

        if (input.matches("(?i)Roll")) {
            return new Roll();
        }

        if (input.matches("(?i)Go")) {
            return new Go();
        }

        if (input.matches("(?i)List")) {
            return new List();
        }

        if (input.matches("(?i)Build\\s+settlement\\s+\\d+")) {
            return new Build(input);
        }

        if (input.matches("(?i)Build\\s+city\\s+\\d+")) {
            return new Build(input);
        }

        if (input.matches("(?i)Build\\s+road\\s+\\d+\\s+\\d+")) {
            return new Build(input);
        }

        return null;
    }
}
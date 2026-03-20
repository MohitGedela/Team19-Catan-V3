package code;

class List extends Command {

    List(Player player, Board board, Turn turn) {
        super(player, board, turn);
    }

    @Override
    public boolean execute() {
        StringBuilder sb = new StringBuilder("Resources: ");
        java.util.Map<ResourceType, Integer> resources = player.getResourceMap();
        boolean first = true;
        for (java.util.Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            if (entry.getValue() > 0) {
                if (!first) sb.append(", ");
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
        }
        if (first)
            sb.append("(empty)");
        System.out.println(turn.formatAction(player, sb.toString()));
        return true;
    }

    @Override
    public void undo() {
    }

    @Override
    public boolean endsTurn() {
        return false;
    }

    @Override
    public boolean requiresRoll() {
        return true;
    }
}
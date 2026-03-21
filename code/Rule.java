package code;

/**
 * Evaluates and executes one AI tactic; extended by {@link Strategy} for R3.2.
 */
public interface Rule {
    double evaluate(Player player);

    void execute(Player player, Board board);
}
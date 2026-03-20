package code;

public interface Rule {
    double evaluate(Player player);

    void execute(Player player, Board board);
}
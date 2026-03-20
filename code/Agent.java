package code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * R3.2 — Strategy pattern: context that holds concrete {@link Strategy} objects and picks one to run.
 */
public class Agent {

    private List<Strategy> strategies;
    private Random random;

    public Agent() {
        this.random = new Random();
        this.strategies = new ArrayList<>();
        strategies.add(new EarnVictoryPoints());
        strategies.add(new BuildSomething());
        strategies.add(new SpendingCards());
    }

    public Strategy chooseBestStrategy(Player player) {
        Strategy best = strategies.get(0);
        double bestValue = best.evaluate(player);

        for (int i = 1; i < strategies.size(); i++) {
            double value = strategies.get(i).evaluate(player);
            if (value > bestValue) {
                bestValue = value;
                best = strategies.get(i);
            }
        }

        // Tie: collect all strategies with the same top score, pick randomly (R3.2)
        List<Strategy> tied = new ArrayList<>();
        for (Strategy s : strategies) {
            if (s.evaluate(player) == bestValue) {
                tied.add(s);
            }
        }
        return tied.get(random.nextInt(tied.size()));
    }

    public String act(Player player, Board board, Turn turn) {
        Strategy best = chooseBestStrategy(player);
        best.execute(player, board);
        return "agent executed: " + best.getClass().getSimpleName();
    }
}
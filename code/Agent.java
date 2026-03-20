package code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Agent {

    private List<Rule> rules;
    private Random random;

    public Agent() {
        this.random = new Random();
        this.rules = new ArrayList<>();
        rules.add(new EarnVictoryPoints());
        rules.add(new BuildSomething());
        rules.add(new SpendingCards());
    }

    public Rule chooseBestRule(Player player) {
        Rule bestRule = rules.get(0);
        double bestValue = bestRule.evaluate(player);

        for (int i = 1; i < rules.size(); i++) {
            double value = rules.get(i).evaluate(player);
            if (value > bestValue) {
                bestValue = value;
                bestRule = rules.get(i);
            }
        }

        // Tie: collect all rules with the same top score, pick randomly (R3.2)
        List<Rule> tied = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.evaluate(player) == bestValue) {
                tied.add(rule);
            }
        }
        return tied.get(random.nextInt(tied.size()));
    }

    public String act(Player player, Board board, Turn turn) {
        Rule best = chooseBestRule(player);
        best.execute(player, board);
        return "agent executed: " + best.getClass().getSimpleName();
    }
}
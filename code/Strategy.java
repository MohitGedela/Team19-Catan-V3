package code;

/**
 * R3.2 — Strategy pattern: each concrete class ({@link EarnVictoryPoints}, {@link BuildSomething},
 * {@link SpendingCards}) encapsulates one AI behaviour. The {@link Agent} context selects the
 * strategy with the highest {@link Rule#evaluate(Player)} score (ties broken at random).
 */
public interface Strategy extends Rule {
}

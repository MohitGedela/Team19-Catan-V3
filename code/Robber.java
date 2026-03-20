package code;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Robber {

    private Board board;
    private Random random;

    public Robber(Board board, Random random) {
        this.board = board;
        this.random = random;
    }

    public String runRobber(Player rolledPlayer, List<Player> players) {
        for (Player player : players) {
            player.discardCards(random);
        }

        board.moveRobber(random);

        List<Player> availablePlayers = board.getNearPlayers(players);
        List<Player> playersToRemove = new ArrayList<>();
        availablePlayers.remove(rolledPlayer);

        for (Player currentPlayer : availablePlayers) {
            if (currentPlayer.getTotalResources() == 0) {
                playersToRemove.add(currentPlayer);
            }
        }

        for (Player removePlayer : playersToRemove) {
            availablePlayers.remove(removePlayer);
        }

        if (!availablePlayers.isEmpty()) {
            Player stealPlayer = availablePlayers.get(random.nextInt(availablePlayers.size()));
            return stealPlayer.giveRandomResource(rolledPlayer, random);
        } else {
            return ", No players to steal from";
        }
    }
}

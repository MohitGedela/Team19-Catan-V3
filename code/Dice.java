package code;

import java.util.Random;

class Dice {
    private int dice1;
    private int dice2;
    private Random random;

    public Dice() {
        this.random = new Random();
    }

    public int roll() {
        dice1 = random.nextInt(6) + 1;
        dice2 = random.nextInt(6) + 1;
        return dice1 + dice2;
    }
}

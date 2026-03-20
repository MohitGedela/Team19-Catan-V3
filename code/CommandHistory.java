import java.util.Stack;

class CommandHistory {
    private Stack<Command> history = new Stack<>();

    public void push(Command c) {
        this.history.push(c);
    }

    public Command pop() {
        return this.history.pop();
    }

    public boolean isEmpty() {
        return this.history.isEmpty();
    }
}
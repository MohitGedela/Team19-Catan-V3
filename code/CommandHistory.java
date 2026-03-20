package code;

import java.util.Stack;

/** R3.1 — stores executed commands for undo/redo (used by {@link HumanPlayer}). */
class CommandHistory {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void push(Command c) {
        undoStack.push(c);
        redoStack.clear();
    }

    public Command undoPop() {
        if (undoStack.isEmpty()) return null;
        Command c = undoStack.pop();
        redoStack.push(c);
        return c;
    }

    public Command redoPop() {
        if (redoStack.isEmpty()) return null;
        Command c = redoStack.pop();
        undoStack.push(c);
        return c;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public boolean isEmpty() {
        return undoStack.isEmpty();
    }
}

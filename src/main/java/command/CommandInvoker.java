package command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandInvoker {
    private Deque<RestaurantCommand> stack = new ArrayDeque<>();

    public void runCommand(RestaurantCommand cmd) {
        if (cmd.execute()) {
            stack.push(cmd);
        }
    }

    public boolean undoLast() {
        if (stack.isEmpty()) {
            return false;
        }
        stack.pop().undo();
        return true;
    }
}


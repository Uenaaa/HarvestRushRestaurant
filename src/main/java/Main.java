import command.CommandInvoker;
import manager.RestaurantManager;
import observer.RestaurantSubject;
import ui.RestaurantFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestaurantManager manager = RestaurantManager.getInstance();
            RestaurantSubject subject = new RestaurantSubject();
            manager.setSubject(subject);
            CommandInvoker invoker = new CommandInvoker();

            RestaurantFrame frame = new RestaurantFrame(subject, manager, invoker);
            frame.setVisible(true);
        });
    }
}

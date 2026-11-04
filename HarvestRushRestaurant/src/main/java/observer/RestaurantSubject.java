package observer;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSubject {
    private List<RestaurantObserver> observerList = new ArrayList<>();

    public void attach(RestaurantObserver observer) {
        observerList.add(observer);
    }

    public void notifyObserver(String msg) {
        for (RestaurantObserver o : observerList) {
            o.update(msg);
        }
    }
}


package order;

import customer.Customer;
import kitchen.CookStrategy;

public class Order {
    public Customer customer;
    public CookStrategy strategy;
    public int remainCookTime;
    public int priceModify;
    public boolean cooking;
    public boolean done;

    public Order(Customer customer, CookStrategy strategy) {
        this.customer = customer;
        this.strategy = strategy;
        this.remainCookTime = strategy.getCookTime();
        this.cooking = false;
        this.done = false;
    }
}


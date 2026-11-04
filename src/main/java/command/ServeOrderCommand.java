package command;

import manager.RestaurantManager;
import order.Order;

public class ServeOrderCommand implements RestaurantCommand {
    private final Order order;
    private final RestaurantManager manager;
    private final int earnGold;

    public ServeOrderCommand(Order order, RestaurantManager manager) {
        this.order = order;
        this.manager = manager;
        double finalPrice = (order.strategy.getBasePrice() + order.priceModify) * order.customer.tipRate;
        earnGold = (int) finalPrice;
    }

    @Override
    public boolean execute() {
        if (!order.done) {
            manager.notify("菜品尚未完成，无法上菜");
            return false;
        }
        manager.addGold(earnGold);
        manager.removeOrder(order);
        manager.notify("上菜成功：【" + order.strategy.getDishName() + "】，获得 " + earnGold + " 金币");
        return true;
    }

    @Override
    public void undo() {
        manager.subGold(earnGold);
        manager.addOrder(order);
        manager.notify("撤销上菜，退回 " + earnGold + " 金币");
    }
}

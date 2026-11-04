package command;

import kitchen.Stove;
import manager.RestaurantManager;
import order.Order;

public class StartCookCommand implements RestaurantCommand {
    private final Order order;
    private final RestaurantManager manager;
    private int oldRemainCookTime;
    private int oldPriceModify;

    public StartCookCommand(Order order, RestaurantManager manager) {
        this.order = order;
        this.manager = manager;
    }

    @Override
    public boolean execute() {
        if (order.cooking || order.done) {
            manager.notify("该订单已开始烹饪或已完成");
            return false;
        }

        String material = order.strategy.getNeedMaterial();
        if (!manager.consumeMaterial(material)) {
            manager.notify("食材不足：【" + material + "】，无法烹饪【" + order.strategy.getDishName() + "】");
            return false;
        }

        Stove stove = manager.getStove();
        oldRemainCookTime = order.remainCookTime;
        oldPriceModify = order.priceModify;

        order.remainCookTime = Math.max(1, order.strategy.getCookTime() + stove.getTimeModify());
        order.priceModify = stove.getPriceModify();
        order.cooking = true;

        manager.notify("开始烹饪【" + order.strategy.getDishName() + "】，剩余 " + order.remainCookTime + " 回合");
        return true;
    }

    @Override
    public void undo() {
        order.cooking = false;
        order.done = false;
        order.remainCookTime = oldRemainCookTime;
        order.priceModify = oldPriceModify;
        manager.addMaterial(order.strategy.getNeedMaterial(), 1);
        manager.notify("撤销烹饪：【" + order.strategy.getDishName() + "】，食材已退回");
    }
}

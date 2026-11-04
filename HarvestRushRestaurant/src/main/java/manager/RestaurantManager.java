package manager;

import crop.Crop;
import customer.Customer;
import customer.CustomerFactory;
import kitchen.Bake;
import kitchen.CookStrategy;
import kitchen.Steam;
import kitchen.StirFry;
import kitchen.Stove;
import kitchen.StoveFactory;
import observer.RestaurantSubject;
import order.Order;

import java.util.*;

public class RestaurantManager {
    private static final int SPAWN_INTERVAL = 5;

    private static RestaurantManager instance;

    private final Random random = new Random();
    private final StoveFactory stoveFactory = new StoveFactory();
    private int ticksUntilSpawn = 0;

    // 游戏数据
    public int gold;
    public Map<String, Integer> stock;
    public List<Crop> growingCrops;
    public List<Order> orderList;
    public List<Customer> customerList;
    public RestaurantSubject subject;

    // 灶台装饰器开关
    public boolean nonStickEnabled;
    public boolean seasonedEnabled;

    private RestaurantManager() {
        gold = 100;
        stock = new HashMap<>();
        growingCrops = new ArrayList<>();
        orderList = new ArrayList<>();
        customerList = new ArrayList<>();
    }

    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void setSubject(RestaurantSubject subject) {
        this.subject = subject;
    }

    public void notify(String message) {
        if (subject != null) {
            subject.notifyObserver(message);
        }
    }

    // 全局时间步进，核心tick
    public void timeTick() {
        updateCropGrow();
        updateCustomerPatience();
        updateCookingOrder();
        spawnCustomerIfNeeded();
    }

    // 更新作物生长
    private void updateCropGrow() {
        Iterator<Crop> it = growingCrops.iterator();
        while (it.hasNext()) {
            Crop crop = it.next();
            crop.growTime--;
            if (crop.growTime <= 0) {
                addMaterial(crop.material, 1);
                notify("作物成熟：收获【" + crop.material + "】");
                it.remove();
            }
        }
    }

    // 更新客人耐心
    private void updateCustomerPatience() {
        Iterator<Order> it = orderList.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            if (order.done) {
                continue;
            }
            order.customer.patience--;
            if (order.customer.patience <= 0) {
                it.remove();
                customerList.remove(order.customer);
                notify("客人超时离开，订单【" + order.strategy.getDishName() + "】作废！");
            }
        }
    }

    // 更新烹饪进度
    private void updateCookingOrder() {
        for (Order order : orderList) {
            if (order.cooking && !order.done) {
                order.remainCookTime--;
                if (order.remainCookTime <= 0) {
                    order.done = true;
                    order.cooking = false;
                    notify("菜品烹饪完成：【" + order.strategy.getDishName() + "】可以上菜！");
                }
            }
        }
    }

    // 按间隔自动生成客人
    private void spawnCustomerIfNeeded() {
        if (ticksUntilSpawn > 0) {
            ticksUntilSpawn--;
            return;
        }
        spawnCustomer();
        ticksUntilSpawn = SPAWN_INTERVAL;
    }

    public void spawnCustomer() {
        int roll = random.nextInt(100);
        String customerType = roll < 50 ? "normal" : (roll < 80 ? "rush" : "vip");
        Customer customer = new CustomerFactory().create(customerType);

        CookStrategy[] strategies = {new StirFry(), new Steam(), new Bake()};
        CookStrategy strategy = strategies[random.nextInt(strategies.length)];

        addOrder(new Order(customer, strategy));
        notify("新客人到店：【" + customer.getCustomerType() + "】点单【" + strategy.getDishName() + "】");
    }

    // 根据当前装饰器开关构建灶台
    public Stove getStove() {
        return stoveFactory.create(nonStickEnabled, seasonedEnabled);
    }

    // 校验食材是否充足
    public boolean hasMaterial(String mat) {
        return stock.getOrDefault(mat, 0) > 0;
    }

    // 消耗食材
    public boolean consumeMaterial(String mat) {
        if (!hasMaterial(mat)) return false;
        stock.put(mat, stock.get(mat) - 1);
        return true;
    }

    public void addMaterial(String mat, int num) {
        stock.merge(mat, num, Integer::sum);
    }

    public void addGold(int num) {
        gold += num;
    }

    public void subGold(int num) {
        gold -= num;
    }

    public void addCrop(Crop crop) {
        growingCrops.add(crop);
    }

    public void addOrder(Order order) {
        orderList.add(order);
        if (!customerList.contains(order.customer)) {
            customerList.add(order.customer);
        }
    }

    public void removeOrder(Order order) {
        orderList.remove(order);
        customerList.remove(order.customer);
    }
}

package customer;

public class CustomerFactory {
    public Customer create(String type) {
        switch (type) {
            case "normal":
                return new NormalCus();
            case "rush":
                return new RushCus();
            case "vip":
                return new VipCus();
            default:
                return new NormalCus();
        }
    }
}


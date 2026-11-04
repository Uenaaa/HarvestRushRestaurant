package customer;

public abstract class Customer {
    public int patience;
    public double tipRate;
    public abstract String getCustomerType();
}

class NormalCus extends Customer {
    public NormalCus() {
        patience = 60;
        tipRate = 1.0;
    }
    @Override
    public String getCustomerType() {
        return "normal";
    }
}

class RushCus extends Customer {
    public RushCus() {
        patience = 30;
        tipRate = 1.2;
    }
    @Override
    public String getCustomerType() {
        return "rush";
    }
}

class VipCus extends Customer {
    public VipCus() {
        patience = 80;
        tipRate = 1.5;
    }
    @Override
    public String getCustomerType() {
        return "vip";
    }
}

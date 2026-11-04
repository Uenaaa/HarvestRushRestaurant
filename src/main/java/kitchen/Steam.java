package kitchen;

public class Steam implements CookStrategy {
    @Override
    public int getCookTime() {
        return 6;
    }

    @Override
    public int getBasePrice() {
        return 15;
    }

    @Override
    public String getNeedMaterial() {
        return "wheat";
    }

    @Override
    public String getDishName() {
        return "蒸麦饼";
    }
}

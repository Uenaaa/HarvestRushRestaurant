package kitchen;

public class Bake implements CookStrategy {
    @Override
    public int getCookTime() {
        return 8;
    }

    @Override
    public int getBasePrice() {
        return 25;
    }

    @Override
    public String getNeedMaterial() {
        return "tomato";
    }

    @Override
    public String getDishName() {
        return "烤番茄";
    }
}

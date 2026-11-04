package kitchen;

public class StirFry implements CookStrategy {
    @Override
    public int getCookTime() {
        return 4;
    }

    @Override
    public int getBasePrice() {
        return 18;
    }

    @Override
    public String getNeedMaterial() {
        return "veg";
    }

    @Override
    public String getDishName() {
        return "炒蔬菜";
    }
}

package kitchen;

public interface CookStrategy {
    int getCookTime();
    int getBasePrice();
    String getNeedMaterial();

    String getDishName();
}

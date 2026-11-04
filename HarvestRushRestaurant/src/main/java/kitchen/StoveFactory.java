package kitchen;

public class StoveFactory {
    public Stove create(boolean nonStick, boolean seasoned) {
        Stove stove = new BaseStove();
        if (nonStick) {
            stove = new NonStickDecorator(stove);
        }
        if (seasoned) {
            stove = new SeasonDecorator(stove);
        }
        return stove;
    }
}

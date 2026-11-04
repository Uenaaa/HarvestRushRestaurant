package kitchen;

public abstract class Stove {
    public abstract int getTimeModify();
    public abstract int getPriceModify();
}

class BaseStove extends Stove {
    @Override
    public int getTimeModify() {
        return 0;
    }
    @Override
    public int getPriceModify() {
        return 0;
    }
}

abstract class StoveDecorator extends Stove {
    protected Stove stove;
    public StoveDecorator(Stove stove) {
        this.stove = stove;
    }
}

class NonStickDecorator extends StoveDecorator {
    public NonStickDecorator(Stove stove) {
        super(stove);
    }
    @Override
    public int getTimeModify() {
        return stove.getTimeModify() - 1;
    }
    @Override
    public int getPriceModify() {
        return stove.getPriceModify();
    }
}

class SeasonDecorator extends StoveDecorator {
    public SeasonDecorator(Stove stove) {
        super(stove);
    }
    @Override
    public int getTimeModify() {
        return stove.getTimeModify();
    }
    @Override
    public int getPriceModify() {
        return stove.getPriceModify() + 3;
    }
}


package crop;

public abstract class Crop {
    public int growTime;
    public String material;
    public abstract String getCropType();
}

class Wheat extends Crop {
    public Wheat() {
        growTime = 3;
        material = "wheat";
    }
    @Override
    public String getCropType() {
        return "wheat";
    }
}

class Veg extends Crop {
    public Veg() {
        growTime = 4;
        material = "veg";
    }
    @Override
    public String getCropType() {
        return "veg";
    }
}

class Tomato extends Crop {
    public Tomato() {
        growTime = 5;
        material = "tomato";
    }
    @Override
    public String getCropType() {
        return "tomato";
    }
}

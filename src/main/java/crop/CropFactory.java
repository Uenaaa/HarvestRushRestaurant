package crop;

public class CropFactory {
    public Crop create(String type) {
        switch (type) {
            case "wheat":
                return new Wheat();
            case "veg":
                return new Veg();
            case "tomato":
                return new Tomato();
            default:
                return new Wheat();
        }
    }
}

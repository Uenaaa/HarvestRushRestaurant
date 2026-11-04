package command;

import crop.Crop;
import manager.RestaurantManager;

public class PlantCropCommand implements RestaurantCommand {
    private final Crop crop;
    private final RestaurantManager manager;

    public PlantCropCommand(Crop crop, RestaurantManager manager) {
        this.crop = crop;
        this.manager = manager;
    }

    @Override
    public boolean execute() {
        manager.addCrop(crop);
        manager.notify("播种成功：【" + crop.getCropType() + "】，" + crop.growTime + " 回合后成熟");
        return true;
    }

    @Override
    public void undo() {
        if (manager.growingCrops.remove(crop)) {
            manager.notify("撤销播种：【" + crop.getCropType() + "】");
        } else {
            manager.notify("作物已收获，无法撤销播种");
        }
    }
}

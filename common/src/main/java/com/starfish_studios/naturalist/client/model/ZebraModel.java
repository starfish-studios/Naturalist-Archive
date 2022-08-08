package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.entity.Zebra;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.HorseEntityModel;

@Environment(value= EnvType.CLIENT)
public class ZebraModel extends HorseEntityModel<Zebra> {
    private final ModelPart leftChest;
    private final ModelPart rightChest;

    public ZebraModel(ModelPart root) {
        super(root);
        this.leftChest = this.body.getChild("left_chest");
        this.rightChest = this.body.getChild("right_chest");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData meshDefinition = HorseEntityModel.getModelData(Dilation.NONE);
        ModelPartData partDefinition = meshDefinition.getRoot();
        ModelPartData partDefinition2 = partDefinition.getChild("body");
        ModelPartBuilder cubeListBuilder = ModelPartBuilder.create().uv(26, 21).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 8.0f, 3.0f);
        partDefinition2.addChild("left_chest", cubeListBuilder, ModelTransform.of(6.0f, -8.0f, 0.0f, 0.0f, -1.5707964f, 0.0f));
        partDefinition2.addChild("right_chest", cubeListBuilder, ModelTransform.of(-6.0f, -8.0f, 0.0f, 0.0f, 1.5707964f, 0.0f));
        return TexturedModelData.of(meshDefinition, 64, 64);
    }

    @Override
    public void setAngles(Zebra zebra, float f, float g, float h, float i, float j) {
        super.setAngles(zebra, f, g, h, i, j);
        if (zebra.hasChest()) {
            this.leftChest.visible = true;
            this.rightChest.visible = true;
        } else {
            this.leftChest.visible = false;
            this.rightChest.visible = false;
        }
    }
}

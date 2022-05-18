package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Snake;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoralSnakeModel extends SnakeModel {

    @Override
    public ResourceLocation getTextureLocation(Snake snake) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/coral_snake.png");
    }
}

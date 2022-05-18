package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Snake;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RattleSnakeModel extends SnakeModel {

    @Override
    public ResourceLocation getTextureLocation(Snake snake) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/rattlesnake.png");
    }

    @Override
    public void setLivingAnimations(Snake snake, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(snake, uniqueID, customPredicate);
        IBone tail4 = this.getAnimationProcessor().getBone("tail4");
        tail4.setHidden(false);
    }
}

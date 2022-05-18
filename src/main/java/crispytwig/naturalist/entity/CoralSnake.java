package crispytwig.naturalist.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;

public class CoralSnake extends Snake implements IAnimatable {
    public CoralSnake(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (pEntity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 40));
        }
        return super.doHurtTarget(pEntity);
    }
}

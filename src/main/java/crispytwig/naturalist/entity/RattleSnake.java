package crispytwig.naturalist.entity;

import crispytwig.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;

public class RattleSnake extends Snake implements IAnimatable {
    public RattleSnake(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    private boolean isPlayerNearby() {
        List<Player> players = this.getLevel().getNearbyPlayers(TargetingConditions.forNonCombat().range(8.0D), this, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
        return !players.isEmpty();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isPlayerNearby() && !this.isSleeping()) {
            this.playSound(NaturalistSoundEvents.SNAKE_RATTLE.get(), 0.15F, 1.0F);
        }
    }

    private <E extends IAnimatable> PlayState rattlePredicate(AnimationEvent<E> event) {
        if (this.isPlayerNearby() && !this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.rattle", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(new AnimationController<>(this, "rattleController", 0, this::rattlePredicate));
    }
}

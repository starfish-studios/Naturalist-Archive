package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.entity.ai.goal.CloseMeleeAttackGoal;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class Alligator extends Animal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.ALLIGATOR_FOOD_ITEMS);

    public Alligator(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0f);
        this.maxUpStep = 1.0f;
    }
    
    @javax.annotation.Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_HURT.get();
    }

    @javax.annotation.Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? super.getVoicePitch() * 0.65F : super.getVoicePitch();
    }

    @javax.annotation.Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_AMBIENT.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return NaturalistEntityTypes.ALLIGATOR.get().create(level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ATTACK_DAMAGE, 10.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new CloseMeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> !this.isBaby() && entity.isInWater()));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity) -> {
            if(entity instanceof Alligator) return false;
            Iterable<BlockPos> list = BlockPos.betweenClosed(entity.blockPosition().offset(-2, -2, -2), entity.blockPosition().offset(2, 2, 2));
            boolean isEntityNearAlligatorEggs = false;
            for (BlockPos pos : list) {
                if (level.getBlockState(pos).is(NaturalistBlocks.ALLIGATOR_EGG.get())) {
                    isEntityNearAlligatorEggs = true;
                    break;
                }
            }
            return !this.isBaby() && isEntityNearAlligatorEggs;
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity) -> !this.isBaby() && entity.getType().is(NaturalistTags.EntityTypes.ALLIGATOR_HOSTILES)));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public int getMaxHeadYRot() {
        return 40;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isInWater()) {
                event.getController().setAnimation(new AnimationBuilder().loop("alligator.swim"));
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop("alligator.walk"));
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("alligator.idle"));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().playOnce("alligator.bite"));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

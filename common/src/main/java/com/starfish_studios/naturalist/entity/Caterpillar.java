package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.block.ChrysalisBlock;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Caterpillar extends ClimbingAnimal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Caterpillar(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CocoonGoal(this, 1.0F, 5, 2));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0F));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.setAge(0);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return this.isBaby() && pStack.is(ItemTags.FLOWERS);
    }

    @Override
    public float getScale() {
        return 1.0f;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() > -0.05F && event.getLimbSwingAmount() < 0.05F)) {
            event.getController().setAnimation(new AnimationBuilder().loop("caterpillar.move"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5);
        data.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private static class CocoonGoal extends MoveToBlockGoal {
        private final Caterpillar caterpillar;
        private Direction facing = Direction.NORTH;
        private BlockPos logPos = BlockPos.ZERO;

        public CocoonGoal(Caterpillar pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.caterpillar = pMob;
        }

        @Override
        public boolean canUse() {
            return !caterpillar.isBaby() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            if (pLevel.getBlockState(pPos).isAir()) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (pLevel.getBlockState(pPos.relative(direction)).is(BlockTags.LOGS) && pLevel.getBlockState(pPos.relative(direction).below()).is(BlockTags.LOGS)) {
                        this.facing = direction;
                        this.logPos = pPos.relative(direction);
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override
        public void tick() {
            BlockPos targetPos = this.getMoveToTarget();
            if (!targetPos.closerToCenterThan(caterpillar.position(), this.acceptedDistance())) {
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    caterpillar.getNavigation().moveTo(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, this.speedModifier);
                }
            } else {
                --this.tryTicks;
            }
            caterpillar.getLookControl().setLookAt(logPos.getX() + 0.5D, logPos.getY() + 1, logPos.getZ() + 0.5D, 10.0F, this.caterpillar.getMaxHeadXRot());
            Level level = caterpillar.level;
            if (this.isValidTarget(level, caterpillar.blockPosition())) {
                if (!level.isClientSide) {
                    ((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, NaturalistBlocks.CHRYSALIS.get().defaultBlockState()), caterpillar.getX(), caterpillar.getY(), caterpillar.getZ(), 50, caterpillar.getBbWidth() / 4.0F, caterpillar.getBbHeight() / 4.0F, caterpillar.getBbWidth() / 4.0F, 0.05D);
                }
                caterpillar.discard();
                level.setBlockAndUpdate(caterpillar.blockPosition(), NaturalistBlocks.CHRYSALIS.get().defaultBlockState().setValue(ChrysalisBlock.FACING, facing));
                level.playSound(null, caterpillar.blockPosition(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
            }
        }

        @Override
        protected void moveMobToBlock() {
            caterpillar.getNavigation().moveTo(logPos.getX() + 0.5D, logPos.getY() + 1.0D, logPos.getZ() + 0.5D, this.speedModifier);
        }

        @Override
        protected BlockPos getMoveToTarget() {
            return logPos.above();
        }
    }
}

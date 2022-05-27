package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.block.ChrysalisBlock;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Caterpillar extends ClimbingAnimal implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

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
        this.goalSelector.addGoal(1, new CocoonGoal(this, 1.0F, 8, 2));
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
        return false;
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("caterpillar.move", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private static class CocoonGoal extends MoveToBlockGoal {
        private final Caterpillar caterpillar;
        private Direction facing = Direction.NORTH;

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
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override
        public void tick() {
            BlockPos moveToTarget = this.blockPos;
            boolean reachedTarget;
            if (!moveToTarget.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
                reachedTarget = false;
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    this.mob.getNavigation().moveTo(moveToTarget.getX() + 0.5D + (facing.getStepX() * 0.5D), moveToTarget.getY(), moveToTarget.getZ() + 0.5D + (facing.getStepZ() * 0.5D), this.speedModifier);
                }
            } else {
                reachedTarget = true;
                --this.tryTicks;
            }
            if (reachedTarget) {
                Level level = caterpillar.level;
                if (this.isValidTarget(level, blockPos)) {
                    if (!level.isClientSide) {
                        ((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, NaturalistBlocks.CHRYSALIS.get().defaultBlockState()), caterpillar.getX(), caterpillar.getY(), caterpillar.getZ(), 50, caterpillar.getBbWidth() / 4.0F, caterpillar.getBbHeight() / 4.0F, caterpillar.getBbWidth() / 4.0F, 0.05D);
                    }
                    caterpillar.discard();
                    level.setBlockAndUpdate(blockPos, NaturalistBlocks.CHRYSALIS.get().defaultBlockState().setValue(ChrysalisBlock.FACING, facing));
                    level.playSound(null, blockPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
                }
            }
        }

        @Override
        protected void moveMobToBlock() {
            this.mob.getNavigation().moveTo(blockPos.getX() + 0.5D + (facing.getStepX() * 0.5D), blockPos.getY(), blockPos.getZ() + 0.5D + (facing.getStepZ() * 0.5D), this.speedModifier);
        }
    }
}

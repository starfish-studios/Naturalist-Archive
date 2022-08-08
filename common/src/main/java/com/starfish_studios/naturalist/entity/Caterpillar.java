package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.block.ChrysalisBlock;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.entity.*;
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

    public Caterpillar(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new CocoonGoal(this, 1.0F, 5, 2));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0F));
    }

    @Override
    public EntityData initialize(ServerWorldAccess pLevel, LocalDifficulty pDifficulty, SpawnReason pReason, @javax.annotation.Nullable EntityData pSpawnData, @javax.annotation.Nullable NbtCompound pDataTag) {
        this.setBreedingAge(0);
        return super.initialize(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld level, PassiveEntity mob) {
        return null;
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return this.isBaby() && pStack.isIn(ItemTags.FLOWERS);
    }

    @Override
    public float getScaleFactor() {
        return 1.0f;
    }

    @Override
    public boolean handleFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void fall(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
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
        data.setResetSpeedInTicks(5);
        data.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private static class CocoonGoal extends MoveToTargetPosGoal {
        private final Caterpillar caterpillar;
        private Direction facing = Direction.NORTH;
        private BlockPos logPos = BlockPos.ORIGIN;

        public CocoonGoal(Caterpillar pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.caterpillar = pMob;
        }

        @Override
        public boolean canStart() {
            return !caterpillar.isBaby() && super.canStart();
        }

        @Override
        protected boolean isTargetPos(WorldView pLevel, BlockPos pPos) {
            if (pLevel.getBlockState(pPos).isAir()) {
                for (Direction direction : Direction.Type.HORIZONTAL) {
                    if (pLevel.getBlockState(pPos.offset(direction)).isIn(BlockTags.LOGS) && pLevel.getBlockState(pPos.offset(direction).down()).isIn(BlockTags.LOGS)) {
                        this.facing = direction;
                        this.logPos = pPos.offset(direction);
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override
        public void tick() {
            BlockPos targetPos = this.getTargetPos();
            if (!targetPos.isWithinDistance(caterpillar.getPos(), this.getDesiredDistanceToTarget())) {
                ++this.tryingTime;
                if (this.shouldResetPath()) {
                    caterpillar.getNavigation().startMovingTo(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, this.speed);
                }
            } else {
                --this.tryingTime;
            }
            caterpillar.getLookControl().lookAt(logPos.getX() + 0.5D, logPos.getY() + 1, logPos.getZ() + 0.5D, 10.0F, this.caterpillar.getMaxLookPitchChange());
            World level = caterpillar.world;
            if (this.isTargetPos(level, caterpillar.getBlockPos())) {
                if (!level.isClient) {
                    ((ServerWorld)level).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, NaturalistBlocks.CHRYSALIS.get().getDefaultState()), caterpillar.getX(), caterpillar.getY(), caterpillar.getZ(), 50, caterpillar.getWidth() / 4.0F, caterpillar.getHeight() / 4.0F, caterpillar.getWidth() / 4.0F, 0.05D);
                }
                caterpillar.discard();
                level.setBlockState(caterpillar.getBlockPos(), NaturalistBlocks.CHRYSALIS.get().getDefaultState().with(ChrysalisBlock.FACING, facing));
                level.playSound(null, caterpillar.getBlockPos(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
            }
        }

        @Override
        protected void startMovingToTarget() {
            caterpillar.getNavigation().startMovingTo(logPos.getX() + 0.5D, logPos.getY() + 1.0D, logPos.getZ() + 0.5D, this.speed);
        }

        @Override
        protected BlockPos getTargetPos() {
            return logPos.up();
        }
    }
}

package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.EggLayingAnimal;
import com.starfish_studios.naturalist.common.entity.core.HidingAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.EggLayingBreedGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.HideGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.LayEggGoal;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
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

import java.util.List;

public class Tortoise extends TamableAnimal implements IAnimatable, HidingAnimal, EggLayingAnimal {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final Ingredient TEMPT_ITEMS = Ingredient.of(NaturalistTags.ItemTags.TORTOISE_TEMPT_ITEMS);
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    int layEggCounter;
    boolean isDigging;

    public Tortoise(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.17f).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SHIELD_BLOCK;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        Tortoise tortoise = NaturalistEntityTypes.TORTOISE.get().create(level);
        if (otherParent instanceof Tortoise tortoiseParent) {
            if (this.getVariant() == tortoiseParent.getVariant()) {
                tortoise.setVariant(this.getVariant());
            } else {
                tortoise.setVariant(this.random.nextBoolean() ? tortoiseParent.getVariant() : this.getVariant());
            }
            tortoise.setOwnerUUID(this.random.nextBoolean() ? tortoiseParent.getOwnerUUID() : this.getOwnerUUID());
        }
        return tortoise;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(Biomes.SWAMP) || holder.is(Biomes.MANGROVE_SWAMP)) {
            this.setVariant(1);
        } else if (holder.is(BiomeTags.IS_JUNGLE) || holder.is(Biomes.DARK_FOREST)) {
            this.setVariant(2);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);
            this.setHealth(30.0f);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new HideGoal<>(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0, TEMPT_ITEMS, false));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0, 10.0f, 5.0f, false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0f));
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if (!this.isTame()) {
            return false;
        }
        if (!(otherAnimal instanceof Tortoise tortoise)) {
            return false;
        }
        return tortoise.isTame() && super.canMate(otherAnimal);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return TEMPT_ITEMS.test(stack);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.isInSittingPose() || this.canHide() ? strength / 4 : strength, x, z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, this.canHide() ? amount * 0.8F : amount);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult interactionResult;
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            }
            if (this.isFood(itemStack) && (this.getHealth() < this.getMaxHealth() || !this.isTame())) {
                return InteractionResult.SUCCESS;
            }
            if (itemStack.getItem() instanceof ShearsItem) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isOwnedBy(player)) {
                if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.heal(3.0F);
                    return InteractionResult.CONSUME;
                }
                InteractionResult interactionResult2 = super.mobInteract(player, hand);
                if (!interactionResult2.consumesAction() || this.isBaby()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
                return interactionResult2;
            }
        } else if (this.isFood(itemStack)) {
            this.usePlayerItem(player, hand, itemStack);
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.setOrderedToSit(true);
                this.level.broadcastEntityEvent(this, (byte)7);
            } else {
                this.level.broadcastEntityEvent(this, (byte)6);
            }
            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        }
        if ((interactionResult = super.mobInteract(player, hand)).consumesAction()) {
            this.setPersistenceRequired();
        }
        return interactionResult;
    }

    @Override
    public boolean canHide() {
        if (this.isTame()) {
            return false;
        }
        List<Player> players = this.level.getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D).selector(livingEntity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isDiscrete() && !livingEntity.isHolding(TEMPT_ITEMS)), this, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D));
        return !players.isEmpty();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96f;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.3F;
    }

    @Override
    public double getFluidJumpThreshold() {
        return 0.4;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    // ENTITY DATA

    public int getVariant() {
        return Mth.clamp(this.entityData.get(VARIANT_ID), 0, 2);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, 0);
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(LAYING_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHasEgg(compound.getBoolean("HasEgg"));
    }

    @Override
    public void animateHurt() {
        super.animateHurt();
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.setResetSpeedInTicks(5);
        animationData.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
        // hurtPredicate
        animationData.addAnimationController(new AnimationController<>(this, "hurtController", 5, this::hurtPredicate));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if (this.isInSittingPose()) {
            event.getController().setAnimation(builder.loop("tortoise.sit"));
            return PlayState.CONTINUE;
        } else if (this.canHide()) {
            event.getController().setAnimation(builder.loop("tortoise.hide"));
            return PlayState.CONTINUE;
        } else if (this.isLayingEgg())  {
            event.getController().setAnimation(builder.loop("tortoise.dig"));
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            event.getController().setAnimation(builder.loop("tortoise.walk"));
            if (this.isBaby()) {
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimationSpeed(1.1D);
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <T extends IAnimatable> PlayState hurtPredicate(AnimationEvent<T> event) {
        AnimationBuilder builder = new AnimationBuilder();
        if(this.hurtTime > 0) {
            event.getController().setAnimation(builder.loop("tortoise.hurt"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.entityData.set(LAYING_EGG, isLayingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public Block getEggBlock() {
        return NaturalistBlocks.TORTOISE_EGG.get();
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return NaturalistTags.BlockTags.TORTOISE_EGG_LAYABLE_ON;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0 && this.level.getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level.levelEvent(2001, pos, Block.getId(this.level.getBlockState(pos.below())));
        }
    }
}

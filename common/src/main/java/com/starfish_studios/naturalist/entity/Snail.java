package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.HideGoal;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;

public class Snail extends Animal implements IAnimatable, Bucketable, HidingAnimal {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);

    public Snail(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    // ATTRIBUTES/BREEDING/AI

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.MOVEMENT_SPEED, 0.06F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new HideGoal(this));
        this.goalSelector.addGoal(1, new SnailStrollGoal(this, 1.0D, 0.0F));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        for (Player player : level.getEntitiesOfClass(Player.class, this.getBoundingBox())) {
            if (!player.isOnGround() && EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, player) == 0 && !this.hasCustomName()) {
                this.hurt(DamageSource.playerAttack(player), 5.0F);
            }
        }
    }

    // BUCKETING

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setFromBucket(pCompound.getBoolean("FromBucket"));
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        return bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }

    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.BUCKET && entity.isAlive()) {
            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            ItemStack bucketStack = entity.getBucketItemStack();
            entity.saveToBucketTag(bucketStack);
            ItemStack resultStack = ItemUtils.createFilledResult(stack, player, bucketStack, false);
            player.setItemInHand(hand, resultStack);
            Level level = entity.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucketStack);
            }

            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistRegistry.SNAIL_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return NaturalistSoundEvents.BUCKET_FILL_SNAIL.get();
    }

    // SOUNDS

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SNAIL_CRUSH.get();
    }

    // ANIMATION

    @Override
    public boolean canHide() {
        List<Player> players = this.level.getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D).selector(EntitySelector.NO_CREATIVE_OR_SPECTATOR::test), this, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D));
        return !players.isEmpty();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.canHide()) {
            event.getController().setAnimation(new AnimationBuilder().loop("snail.retreat"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("snail.move"));
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Snail> event) {
        Snail snail = event.getEntity();
        if (snail.level.isClientSide) {
            if (event.sound.equals("forward")) {
                snail.level.playLocalSound(snail.getX(), snail.getY(), snail.getZ(), NaturalistSoundEvents.SNAIL_FORWARD.get(), snail.getSoundSource(), 0.5F, 1.0F, false);
            }
            if (event.sound.equals("back")) {
                snail.level.playLocalSound(snail.getX(), snail.getY(), snail.getZ(), NaturalistSoundEvents.SNAIL_BACK.get(), snail.getSoundSource(), 0.5F, 1.0F, false);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        AnimationController<Snail> controller = new AnimationController<>(this, "controller", 10, this::predicate);
        controller.registerSoundListener(this::soundListener);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class SnailStrollGoal extends WaterAvoidingRandomStrollGoal {
        public SnailStrollGoal(PathfinderMob pMob, double pSpeedModifier, float pProbability) {
            super(pMob, pSpeedModifier, pProbability);
            this.forceTrigger = true;
            this.interval = 1;
        }
    }
}

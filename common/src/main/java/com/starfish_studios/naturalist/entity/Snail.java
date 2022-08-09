package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.registry.NaturalistItems;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class Snail extends AnimalEntity implements IAnimatable, Bucketable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(Snail.class, TrackedDataHandlerRegistry.BOOLEAN);

    public Snail(EntityType<? extends AnimalEntity> type, World level) {
        super(type, level);
    }

    // ATTRIBUTES/BREEDING/AI

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.06F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new HideGoal(this));
        this.goalSelector.add(1, new SnailStrollGoal(this, 1.0D, 0.0F));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld level, PassiveEntity mob) {
        return null;
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        for (PlayerEntity player : world.getNonSpectatingEntities(PlayerEntity.class, this.getBoundingBox())) {
            if (!player.isOnGround() && EnchantmentHelper.getEquipmentLevel(Enchantments.FEATHER_FALLING, player) == 0 && !this.hasCustomName()) {
                this.damage(DamageSource.player(player), 5.0F);
            }
        }
    }

    // BUCKETING

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FROM_BUCKET, false);
    }

    @Override
    public boolean isFromBucket() {
        return this.dataTracker.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.dataTracker.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound pCompound) {
        super.writeCustomDataToNbt(pCompound);
        pCompound.putBoolean("FromBucket", this.isFromBucket());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.setFromBucket(pCompound.getBoolean("FromBucket"));
    }

    @Override
    public ActionResult interactMob(PlayerEntity pPlayer, Hand pHand) {
        return tryBucket(pPlayer, pHand, this).orElse(super.interactMob(pPlayer, pHand));
    }

    static <T extends LivingEntity & Bucketable> Optional<ActionResult> tryBucket(PlayerEntity player, Hand hand, T entity) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == Items.BUCKET && entity.isAlive()) {
            entity.playSound(entity.getBucketedSound(), 1.0F, 1.0F);
            ItemStack bucketStack = entity.getBucketItem();
            entity.copyDataToStack(bucketStack);
            ItemStack resultStack = ItemUsage.exchangeStack(stack, player, bucketStack, false);
            player.setStackInHand(hand, resultStack);
            World level = entity.world;
            if (!level.isClient) {
                Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity)player, bucketStack);
            }

            entity.discard();
            return Optional.of(ActionResult.success(level.isClient));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
    }

    @Override
    public void copyDataFromNbt(NbtCompound tag) {
        Bucketable.copyDataFromNbt(this, tag);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(NaturalistItems.SNAIL_BUCKET.get());
    }

    @Override
    public SoundEvent getBucketedSound() {
        return NaturalistSoundEvents.BUCKET_FILL_SNAIL.get();
    }

    // SOUNDS

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SNAIL_CRUSH.get();
    }

    // ANIMATION

    private boolean canHide() {
        List<PlayerEntity> players = this.world.getPlayers(TargetPredicate.createNonAttackable().setBaseMaxDistance(5.0D).setPredicate(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test), this, this.getBoundingBox().expand(5.0D, 3.0D, 5.0D));
        return !players.isEmpty();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.canHide()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snail.retreat", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snail.move", true));
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Snail> event) {
        Snail snail = event.getEntity();
        if (snail.world.isClient) {
            if (event.sound.equals("forward")) {
                snail.world.playSound(snail.getX(), snail.getY(), snail.getZ(), NaturalistSoundEvents.SNAIL_FORWARD.get(), snail.getSoundCategory(), 0.5F, 1.0F, false);
            }
            if (event.sound.equals("back")) {
                snail.world.playSound(snail.getX(), snail.getY(), snail.getZ(), NaturalistSoundEvents.SNAIL_BACK.get(), snail.getSoundCategory(), 0.5F, 1.0F, false);
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

    static class HideGoal extends Goal {
        private final Snail snail;

        public HideGoal(Snail mob) {
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
            this.snail = mob;
        }

        @Override
        public boolean canStart() {
            return snail.canHide();
        }

        @Override
        public void start() {
            snail.setJumping(false);
            snail.getNavigation().stop();
            snail.getMoveControl().moveTo(snail.getX(), snail.getY(), snail.getZ(), 0.0D);
        }
    }

    static class SnailStrollGoal extends WanderAroundFarGoal {
        public SnailStrollGoal(PathAwareEntity pMob, double pSpeedModifier, float pProbability) {
            super(pMob, pSpeedModifier, pProbability);
            this.ignoringChance = true;
            this.chance = 1;
        }
    }
}

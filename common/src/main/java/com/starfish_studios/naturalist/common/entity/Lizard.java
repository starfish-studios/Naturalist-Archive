package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Lizard extends TamableAnimal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_TAIL = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.BOOLEAN);
    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(NaturalistTags.ItemTags.LIZARD_TEMPT_ITEMS);
    private LizardAvoidEntityGoal<Player> avoidPlayersGoal;
    private int tailRegrowCooldown = 0;

    public Lizard(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new LizardTemptGoal(this, 0.6, TEMPT_INGREDIENT, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    protected void reassessTameGoals() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new LizardAvoidEntityGoal<>(this, Player.class, 16.0f, 0.8, 1.33);
        }
        this.goalSelector.removeGoal(this.avoidPlayersGoal);
        if (!this.isTame()) {
            this.goalSelector.addGoal(4, this.avoidPlayersGoal);
        }
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            this.setHealth(20.0f);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || TEMPT_INGREDIENT.test(stack) && !this.isTame();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (TEMPT_INGREDIENT.test(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(5);
                if (!this.hasTail() && this.getHealth() >= this.getMaxHealth()) {
                    this.setHasTail(true);
                    this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = super.mobInteract(player, hand);
            if (interactionResult.consumesAction() && !this.isBaby() || !this.isOwnedBy(player)) return interactionResult;
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }
        if (!TEMPT_INGREDIENT.test(stack)) return super.mobInteract(player, hand);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level.broadcastEntityEvent(this, (byte)7);
            return InteractionResult.SUCCESS;
        } else {
            this.level.broadcastEntityEvent(this, (byte)6);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public int getVariant() {
        return Mth.clamp(this.entityData.get(VARIANT_ID), 0, 3);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    public boolean hasTail() {
        return this.entityData.get(HAS_TAIL);
    }

    public void setHasTail(boolean hasTail) {
        this.entityData.set(HAS_TAIL, hasTail);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, 0);
        this.entityData.define(HAS_TAIL, true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("HasTail", this.hasTail());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHasTail(compound.getBoolean("HasTail"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hasTail() && this.getHealth() <= this.getMaxHealth() / 2) {
            this.setHasTail(false);
            this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
            LizardTail lizardTail = NaturalistEntityTypes.LIZARD_TAIL.get().create(this.level);
            if (lizardTail != null) {
                lizardTail.setVariant(this.getVariant());
                lizardTail.setPos(this.getX(), this.getY(), this.getZ());
                this.level.addFreshEntity(lizardTail);
            }
            for (Mob mob : this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(8.0), entity -> entity.getTarget() == this)) {
                mob.setTarget(lizardTail);
            }
            this.tailRegrowCooldown = 12000;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.hasTail() && !this.level.isClientSide()) {
            if (this.tailRegrowCooldown > 0) {
                --this.tailRegrowCooldown;
            } else {
                this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
                this.setHasTail(true);
            }
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnData, @javax.annotation.Nullable CompoundTag dataTag) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(Biomes.SAVANNA)) {
            this.setVariant(3);
        } else if (holder.is(BiomeTags.IS_JUNGLE)) {
            this.setVariant(0);
        } else if (holder.is(Biomes.DESERT)) {
            this.setVariant(2);
        } else {
            this.setVariant(1);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(new AnimationBuilder().loop("sit"));
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            event.getController().setAnimation(new AnimationBuilder().loop("walk"));
            event.getController().setAnimationSpeed(2.0D);
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class LizardTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Lizard lizard;

        public LizardTemptGoal(Lizard lizard, double speedModifier, Ingredient ingredient, boolean canScare) {
            super(lizard, speedModifier, ingredient, canScare);
            this.lizard = lizard;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
                this.selectedPlayer = this.player;
            } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
                this.selectedPlayer = null;
            }
        }

        @Override
        protected boolean canScare() {
            if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player)) {
                return false;
            }
            return super.canScare();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.lizard.isTame();
        }
    }

    static class LizardAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Lizard lizard;

        public LizardAvoidEntityGoal(Lizard lizard, Class<T> class_, float f, double d, double e) {
            super(lizard, class_, f, d, e, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.lizard = lizard;
        }

        @Override
        public boolean canUse() {
            return !this.lizard.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.lizard.isTame() && super.canContinueToUse();
        }
    }

    static class LizardMeleeAttackGoal extends MeleeAttackGoal {
        private final Lizard lizard;

        public LizardMeleeAttackGoal(Lizard lizard, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(lizard, speedModifier, followingTargetEvenIfNotSeen);
            this.lizard = lizard;
        }

        @Override
        public boolean canUse() {
            return this.lizard.hasTail() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.lizard.hasTail() && super.canContinueToUse();
        }
    }
}

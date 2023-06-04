package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.ai.goal.FollowAdultGoal;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Bird extends ShoulderRidingEntity implements FlyingAnimal, IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private BirdAvoidEntityGoal<Player> avoidPlayersGoal;
    private static final Ingredient TAME_FOOD = Ingredient.of(NaturalistTags.ItemTags.BIRD_FOOD_ITEMS);
    // private static final EntityDataAccessor<Boolean> IS_PECKING = SynchedEntityData.defineId(Bird.class, EntityDataSerializers.BOOLEAN);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    private boolean isWet;

    public Bird(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BirdTemptGoal(this, 1.0D, TAME_FOOD, true));
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(4, new BirdWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new BirdFlockGoal(this, 1.0D, 6.0F, 12.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        // this.goalSelector.addGoal(7, new BirdPeckAtGroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, 0.8F).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    public static boolean checkBirdSpawnRules(EntityType<Bird> entityType, LevelAccessor state, MobSpawnType type, BlockPos pos, RandomSource random) {
        return state.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(state, pos);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    public boolean isBaby() {
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.6f;
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (!this.isTame() && TAME_FOOD.test(stack)) {
            if (!pPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (!this.isSilent()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), NaturalistSoundEvents.BIRD_EAT.get(), this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.level.isClientSide) {
                if (this.random.nextInt(10) == 0) {
                    this.tame(pPlayer);
                    this.level.broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (this.isTame() && this.isOwnedBy(pPlayer)) {
            if (TAME_FOOD.test(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(1.0F);
                if (this.getHealth() == this.getMaxHealth()) {
                    this.spawnTamingParticles(true);
                }
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            } else if (!this.isFlying()) {
                if (!this.level.isClientSide) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            if (!this.level.isClientSide) {
                this.setOrderedToSit(false);
            }

            return super.hurt(pSource, pAmount);
        }
    }


    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    protected void reassessTameGoals() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new BirdAvoidEntityGoal<>(this, Player.class, 16.0F, 2.0D, 2.0D);
        }

        this.goalSelector.removeGoal(this.avoidPlayersGoal);
        if (!this.isTame()) {
            this.goalSelector.addGoal(2, this.avoidPlayersGoal);
        }

    }

    @Override
    public boolean isFlying() {
        return !this.isOnGround();
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, pLevel);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.playSound(NaturalistSoundEvents.BIRD_FLY.get(), 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void doPush(Entity pEntity) {
        if (!(pEntity instanceof Player)) {
            super.doPush(pEntity);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        // this.entityData.define(IS_PECKING, false);
    }

    /*
    public boolean isPecking() {
        return this.entityData.get(IS_PECKING);
    }

    public void setPecking(boolean isPecking) {
        this.entityData.set(IS_PECKING, isPecking);
    }
     */

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return NaturalistSoundEvents.BIRD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BIRD_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.level.isNight()) {
            return null;
        } else {
            if (this.getType().equals(NaturalistEntityTypes.BLUEJAY.get())) {
                return NaturalistSoundEvents.BIRD_AMBIENT_BLUEJAY.get();
            } else if (this.getType().equals(NaturalistEntityTypes.CANARY.get())) {
                return NaturalistSoundEvents.BIRD_AMBIENT_CANARY.get();
            } else if (this.getType().equals(NaturalistEntityTypes.CARDINAL.get())) {
                return NaturalistSoundEvents.BIRD_AMBIENT_CARDINAL.get();
            } else {
                return NaturalistSoundEvents.BIRD_AMBIENT_ROBIN.get();
            }
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bird.sit"));
            return PlayState.CONTINUE;
        } else if (this.isFlying()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bird.fly"));
            return PlayState.CONTINUE;
        } /* else if (this.isPecking()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bird.peck"));
            return PlayState.CONTINUE;
        */
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

    static class BirdWanderGoal extends WaterAvoidingRandomFlyingGoal {
        private final Bird bird;

        public BirdWanderGoal(Bird mob, double speedModifier) {
            super(mob, speedModifier);
            this.bird = mob;
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos mobPos = this.mob.blockPosition();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutable1 = new BlockPos.MutableBlockPos();

            for(BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D), Mth.floor(this.mob.getY() - 6.0D), Mth.floor(this.mob.getZ() - 3.0D), Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D), Mth.floor(this.mob.getZ() + 3.0D))) {
                if (!mobPos.equals(pos)) {
                    BlockState blockstate = this.mob.level.getBlockState(mutable1.setWithOffset(pos, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level.isEmptyBlock(pos) && this.mob.level.isEmptyBlock(mutable.setWithOffset(pos, Direction.UP))) {
                        return Vec3.atBottomCenterOf(pos);
                    }
                }
            }

            return null;
        }


        @Override
        public boolean canUse() {
            return !this.bird.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && super.canContinueToUse();
        }
    }

    static class BirdFlockGoal extends FollowAdultGoal {
        private final Bird bird;

        public BirdFlockGoal(Bird pMob, double pSpeedModifier, float pStopDistance, float pAreaSize) {
            super(pMob, pSpeedModifier, pStopDistance, pAreaSize);
            this.bird = pMob;
        }

        @Override
        public boolean canUse() {
            return !this.bird.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && super.canContinueToUse();
        }
    }

        /*
    static class BirdPeckAtGroundGoal extends Goal {
        private final Bird bird;

        public BirdPeckAtGroundGoal(Bird bird) {
            this.bird = bird;
        }
        @Override
        public boolean canUse() {
            return this.bird.isOnGround() && this.bird.getRandom().nextInt(100) == 0;
        }

        @Override
        public void tick() {
            this.bird.setPecking(true);
            if(this.bird.getRandom().nextInt(100) <= 25 ) {
                ItemStack worm = new ItemStack(NaturalistItems.WORM.get());
                this.bird.spawnAtLocation(worm);
                this.bird.playSound(SoundEvents.ITEM_PICKUP);
                this.bird.setPecking(false);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }
    }
         */

    static class BirdTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Bird bird;

        public BirdTemptGoal(Bird bird, double speedModifier, Ingredient temptItems, boolean canScare) {
            super(bird, speedModifier, temptItems, canScare);
            this.bird = bird;
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
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.player) ? false : super.canScare();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.bird.isTame();
        }
    }

    static class BirdAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final Bird bird;

        public BirdAvoidEntityGoal(Bird bird, Class<T> toAvoid, float maxDistance, double walkSpeed, double sprintSpeed) {
            super(bird, toAvoid, maxDistance, walkSpeed, sprintSpeed, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.bird = bird;
        }

        @Override
        public boolean canUse() {
            return !this.bird.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.bird.isTame() && super.canContinueToUse();
        }
    }
}

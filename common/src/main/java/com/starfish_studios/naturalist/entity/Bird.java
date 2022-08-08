package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.FollowAdultGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

public class Bird extends TameableShoulderEntity implements Flutterer, IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private BirdAvoidEntityGoal<PlayerEntity> avoidPlayersGoal;
    private static final Ingredient TAME_FOOD = Ingredient.fromTag(NaturalistTags.ItemTags.BIRD_FOOD_ITEMS);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public Bird(EntityType<? extends TameableShoulderEntity> entityType, World level) {
        super(entityType, level);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new BirdTemptGoal(this, 1.0D, TAME_FOOD, true));
        this.goalSelector.add(3, new SitGoal(this));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.5D, 5.0F, 1.0F, true));
        this.goalSelector.add(4, new BirdWanderGoal(this, 1.0D));
        this.goalSelector.add(5, new BirdFlockGoal(this, 1.0D, 6.0F, 12.0F));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D);
    }

    public static boolean checkBirdSpawnRules(EntityType<Bird> entityType, WorldAccess state, SpawnReason type, BlockPos pos, Random random) {
        return state.getBlockState(pos.down()).isIn(BlockTags.PARROTS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(state, pos);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld p_146743_, PassiveEntity p_146744_) {
        return null;
    }

    public boolean isBaby() {
        return false;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions size) {
        return size.height * 0.6f;
    }

    @Override
    public boolean canBreedWith(AnimalEntity pOtherAnimal) {
        return false;
    }

    @Override
    public ActionResult interactMob(PlayerEntity pPlayer, Hand pHand) {
        ItemStack stack = pPlayer.getStackInHand(pHand);
        if (!this.isTamed() && TAME_FOOD.test(stack)) {
            if (!pPlayer.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            if (!this.isSilent()) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), NaturalistSoundEvents.BIRD_EAT.get(), this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.world.isClient) {
                if (this.random.nextInt(10) == 0) {
                    this.setOwner(pPlayer);
                    this.world.sendEntityStatus(this, (byte)7);
                } else {
                    this.world.sendEntityStatus(this, (byte)6);
                }
            }

            return ActionResult.success(this.world.isClient);
        } else if (this.isTamed() && this.isOwner(pPlayer)) {
            if (TAME_FOOD.test(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                this.heal(1.0F);
                if (this.getHealth() == this.getMaxHealth()) {
                    this.showEmoteParticle(true);
                }
                return ActionResult.success(this.world.isClient);
            } else if (!this.isInAir()) {
                if (!this.world.isClient) {
                    this.setSitting(!this.isSitting());
                }
                return ActionResult.success(this.world.isClient);
            }

        }
        return super.interactMob(pPlayer, pHand);
    }

    @Override
    public boolean damage(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            if (!this.world.isClient) {
                this.setSitting(false);
            }

            return super.damage(pSource, pAmount);
        }
    }


    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return false;
    }

    @Override
    protected void onTamedChanged() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new BirdAvoidEntityGoal<>(this, PlayerEntity.class, 16.0F, 2.0D, 2.0D);
        }

        this.goalSelector.remove(this.avoidPlayersGoal);
        if (!this.isTamed()) {
            this.goalSelector.add(2, this.avoidPlayersGoal);
        }

    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    @Override
    protected EntityNavigation createNavigation(World pLevel) {
        BirdNavigation navigation = new BirdNavigation(this, pLevel);
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(true);
        navigation.setCanEnterOpenDoors(true);
        return navigation;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround && !this.hasVehicle() ? 4 : -1) * 0.3F;
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3d vec3 = this.getVelocity();
        if (!this.onGround && vec3.y < 0.0D) {
            this.setVelocity(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    protected boolean hasWings() {
        return this.speed > this.nextFlap;
    }

    @Override
    protected void addFlapEffects() {
        this.playSound(NaturalistSoundEvents.BIRD_FLY.get(), 0.15F, 1.0F);
        this.nextFlap = this.speed + this.flapSpeed / 2.0F;
    }

    @Override
    public boolean handleFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void fall(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void pushAway(Entity pEntity) {
        if (!(pEntity instanceof PlayerEntity)) {
            super.pushAway(pEntity);
        }
    }

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

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bird.sit", true));
            return PlayState.CONTINUE;
        } else if (this.isInAir()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bird.fly", true));
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

    static class BirdWanderGoal extends FlyGoal {
        private final Bird bird;

        public BirdWanderGoal(Bird mob, double speedModifier) {
            super(mob, speedModifier);
            this.bird = mob;
        }

        @Nullable
        protected Vec3d getWanderTarget() {
            Vec3d vec3 = null;
            if (this.mob.isTouchingWater()) {
                vec3 = FuzzyTargeting.find(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getWanderTarget() : vec3;
        }

        @Nullable
        private Vec3d getTreePos() {
            BlockPos mobPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable1 = new BlockPos.Mutable();

            for(BlockPos pos : BlockPos.iterate(MathHelper.floor(this.mob.getX() - 3.0D), MathHelper.floor(this.mob.getY() - 6.0D), MathHelper.floor(this.mob.getZ() - 3.0D), MathHelper.floor(this.mob.getX() + 3.0D), MathHelper.floor(this.mob.getY() + 6.0D), MathHelper.floor(this.mob.getZ() + 3.0D))) {
                if (!mobPos.equals(pos)) {
                    BlockState blockstate = this.mob.world.getBlockState(mutable1.set(pos, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.isIn(BlockTags.LOGS);
                    if (flag && this.mob.world.isAir(pos) && this.mob.world.isAir(mutable.set(pos, Direction.UP))) {
                        return Vec3d.ofBottomCenter(pos);
                    }
                }
            }

            return null;
        }


        @Override
        public boolean canStart() {
            return !this.bird.isTamed() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !this.bird.isTamed() && super.shouldContinue();
        }
    }

    static class BirdFlockGoal extends FollowAdultGoal {
        private final Bird bird;

        public BirdFlockGoal(Bird pMob, double pSpeedModifier, float pStopDistance, float pAreaSize) {
            super(pMob, pSpeedModifier, pStopDistance, pAreaSize);
            this.bird = pMob;
        }

        @Override
        public boolean canStart() {
            return !this.bird.isTamed() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !this.bird.isTamed() && super.shouldContinue();
        }
    }

    static class BirdTemptGoal extends TemptGoal {
        @Nullable
        private PlayerEntity selectedPlayer;
        private final Bird bird;

        public BirdTemptGoal(Bird bird, double speedModifier, Ingredient temptItems, boolean canScare) {
            super(bird, speedModifier, temptItems, canScare);
            this.bird = bird;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.getTickCount(600)) == 0) {
                this.selectedPlayer = this.closestPlayer;
            } else if (this.mob.getRandom().nextInt(this.getTickCount(500)) == 0) {
                this.selectedPlayer = null;
            }
        }

        @Override
        protected boolean canBeScared() {
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.closestPlayer) ? false : super.canBeScared();
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.bird.isTamed();
        }
    }

    static class BirdAvoidEntityGoal<T extends LivingEntity> extends FleeEntityGoal<T> {
        private final Bird bird;

        public BirdAvoidEntityGoal(Bird bird, Class<T> toAvoid, float maxDistance, double walkSpeed, double sprintSpeed) {
            super(bird, toAvoid, maxDistance, walkSpeed, sprintSpeed, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
            this.bird = bird;
        }

        @Override
        public boolean canStart() {
            return !this.bird.isTamed() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !this.bird.isTamed() && super.shouldContinue();
        }
    }
}

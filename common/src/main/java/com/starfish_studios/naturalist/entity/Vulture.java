package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.FlyingWanderGoal;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
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

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class Vulture extends PathAwareEntity implements IAnimatable, Flutterer {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.ofItems(Items.ROTTEN_FLESH);
    private int ticksSinceEaten;

    public Vulture(EntityType<? extends PathAwareEntity> entityType, World level) {
        super(entityType, level);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setCanPickUpLoot(true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_CACTUS, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_CACTUS, 0.0F);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0F).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new VultureAttackGoal(this, 1.2F, true));
        this.goalSelector.add(2, new VultureSearchForFoodGoal(this, 1.2F, FOOD_ITEMS, 10, 20));
        this.goalSelector.add(3, new VultureWanderGoal(this));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, HostileEntity.class, 10, false, false, entity -> entity.getType().isIn(NaturalistTags.EntityTypes.VULTURE_HOSTILES) && !FOOD_ITEMS.test(this.getMainHandStack())));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, false, false, entity -> entity.getHealth() <= 6 && !entity.getMainHandStack().isEmpty() && !FOOD_ITEMS.test(this.getMainHandStack())));
    }

    public static boolean checkVultureSpawnRules(EntityType<Vulture> entityType, WorldAccess state, SpawnReason type, BlockPos pos, Random random) {
        return state.getBlockState(pos.down()).isIn(NaturalistTags.BlockTags.VULTURES_SPAWNABLE_ON) && state.getBaseLightLevel(pos, 0) > 8;
    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void fall(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effectInstance) {
        if (effectInstance.getEffectType() == StatusEffects.HUNGER) {
            return false;
        }
        return super.canHaveStatusEffect(effectInstance);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return NaturalistSoundEvents.VULTURE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.VULTURE_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.VULTURE_AMBIENT.get();
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.equals(DamageSource.CACTUS) || super.isInvulnerableTo(pSource);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean shouldHurt;
        float damage = (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float knockback = (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity livingEntity) {
            damage += livingEntity.getGroup().equals(EntityGroup.UNDEAD) ? damage : 0;
            knockback += (float)EnchantmentHelper.getKnockback(this);
        }
        if (shouldHurt = target.damage(DamageSource.mob(this), damage)) {
            if (knockback > 0.0f && target instanceof LivingEntity) {
                ((LivingEntity)target).takeKnockback(knockback * 0.5f, MathHelper.sin(this.getYaw() * ((float)Math.PI / 180)), -MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)));
                this.setVelocity(this.getVelocity().multiply(0.6, 1.0, 0.6));
            }
            this.applyDamageEffects(this, target);
            this.onAttacking(target);
        }
        return shouldHurt;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.world.getProfiler().push("looting");
        if (!this.world.isClient && this.canPickUpLoot() && this.isAlive() && !this.dead && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            for(ItemEntity itementity : this.world.getNonSpectatingEntities(ItemEntity.class, this.getBoundingBox().expand(1.0D, 1.0D, 1.0D))) {
                if (!itementity.isRemoved() && !itementity.getStack().isEmpty() && this.canGather(itementity.getStack())) {
                    this.loot(itementity);
                }
            }
        }
        this.world.getProfiler().pop();
        if (!this.world.isClient && this.isAlive() && this.canMoveVoluntarily()) {
            ++this.ticksSinceEaten;
            ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (stack.getItem().isFood()) {
                if (this.ticksSinceEaten > 600) {
                    ItemStack finishedStack = stack.finishUsing(this.world, this);
                    if (!finishedStack.isEmpty()) {
                        this.equipStack(EquipmentSlot.MAINHAND, finishedStack);
                    }
                    this.ticksSinceEaten = 0;
                } else if (this.ticksSinceEaten > 560 && this.random.nextFloat() < 0.1f) {
                    this.playSound(this.getEatSound(stack), 1.0f, 1.0f);
                    this.world.sendEntityStatus(this, (byte)45);
                }
            }
        }
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 45) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!itemStack.isEmpty()) {
                for (int i = 0; i < 8; ++i) {
                    Vec3d vec3 = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0).rotateX(-this.getPitch() * ((float)Math.PI / 180)).rotateY(-this.getYaw() * ((float)Math.PI / 180));
                    this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), this.getX() + this.getRotationVector().x / 2.0, this.getY(), this.getZ() + this.getRotationVector().z / 2.0, vec3.x, vec3.y + 0.05, vec3.z);
                }
            }
        } else {
            super.handleStatus(id);
        }
    }

    @Override
    public boolean canEquip(ItemStack pItemstack) {
        return !FOOD_ITEMS.test(this.getMainHandStack());
    }

    @Override
    public boolean canPickupItem(ItemStack pStack) {
        return FOOD_ITEMS.test(pStack) && !FOOD_ITEMS.test(this.getMainHandStack());
    }

    @Override
    protected void loot(ItemEntity pItemEntity) {
        ItemStack itemstack = pItemEntity.getStack();
        if (this.canPickupItem(itemstack)) {
            if (!this.getMainHandStack().isEmpty() && !FOOD_ITEMS.test(this.getMainHandStack())) {
                this.dropItemStack(this.getMainHandStack());
            }
            this.triggerItemPickedUpByEntityCriteria(pItemEntity);
            this.equipStack(EquipmentSlot.MAINHAND, itemstack);
            this.sendPickup(pItemEntity, itemstack.getCount());
            pItemEntity.discard();
            this.ticksSinceEaten = 0;
        }
    }

    private void dropItemStack(ItemStack pStack) {
        ItemEntity itementity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), pStack);
        this.world.spawnEntity(itementity);
    }

    @Override
    protected EntityNavigation createNavigation(World pLevel) {
        VulturePathNavigation navigation = new VulturePathNavigation(this, pLevel);
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(true);
        navigation.setCanEnterOpenDoors(true);
        return navigation;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInAir()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vulture.fly", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("vulture.idle", true));
        }
        return PlayState.CONTINUE;
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

    static class VulturePathNavigation extends BirdNavigation {
        public VulturePathNavigation(MobEntity mob, World level) {
            super(mob, level);
        }

        @Override
        public boolean isValidPosition(BlockPos pos) {
            return super.isValidPosition(pos) && this.entity.world.getBlockState(pos).isIn(NaturalistTags.BlockTags.VULTURE_PERCH_BLOCKS);
        }
    }

    static class VultureWanderGoal extends FlyingWanderGoal {
        private final PathAwareEntity mob;

        public VultureWanderGoal(PathAwareEntity mob) {
            super(mob);
            this.mob = mob;
        }

        @Override
        public void start() {
            Vec3d vec3 = this.findPos();
            if (vec3 != null) {
                mob.getNavigation().startMovingAlong(mob.getNavigation().findPathTo(new BlockPos(vec3), 1), 1.0D);
            }

        }

        @Nullable
        private Vec3d findPos() {
            Vec3d viewVector = mob.getRotationVec(0.0F);
            return NoPenaltySolidTargeting.find(mob, 12, 12, -1, viewVector.x, viewVector.z, Math.PI);
        }
    }

    static class VultureAttackGoal extends MeleeAttackGoal {

        public VultureAttackGoal(PathAwareEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof PlayerEntity player) {
                if (player.getHealth() > 6 || player.getMainHandStack().isEmpty())  {
                    return false;
                }
            }
            return this.mob.getMainHandStack().isEmpty() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof PlayerEntity player) {
                if (player.getHealth() > 6 || player.getMainHandStack().isEmpty())  {
                    return false;
                }
            }
            return this.mob.getMainHandStack().isEmpty() && super.shouldContinue();
        }

        @Override
        protected void attack(LivingEntity enemy, double distToEnemySqr) {
            double reach = this.getSquaredMaxAttackDistance(enemy);
            if (distToEnemySqr <= reach && this.getCooldown() <= 0) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                if (!(enemy instanceof PlayerEntity)) {
                    this.mob.tryAttack(enemy);
                }
                if (enemy instanceof PlayerEntity && this.mob.getMainHandStack().isEmpty() && !enemy.getMainHandStack().isEmpty()) {
                    this.mob.equipStack(EquipmentSlot.MAINHAND, enemy.getMainHandStack().split(1));
                    World level = this.mob.world;
                    level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    this.mob.setTarget(null);
                    this.mob.setAttacking(false);
                }
            }
        }
    }

    static class VultureSearchForFoodGoal extends Goal {
        private final Vulture mob;
        private final double speedModifier;
        private final double horizontalSearchRange;
        private final double verticalSearchRange;
        private final Ingredient ingredient;

        public VultureSearchForFoodGoal(Vulture mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
            this.setControls(EnumSet.of(Control.MOVE));
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.ingredient = ingredient;
            this.horizontalSearchRange = horizontalSearchRange;
            this.verticalSearchRange = verticalSearchRange;
        }

        @Override
        public boolean canStart() {
            if (!Vulture.FOOD_ITEMS.test(mob.getMainHandStack())) {
                List<ItemEntity> list = mob.world.getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
                return !list.isEmpty() && !Vulture.FOOD_ITEMS.test(mob.getMainHandStack());
            }
            return false;
        }

        @Override
        public void tick() {
            List<ItemEntity> list = mob.world.getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
            if (!Vulture.FOOD_ITEMS.test(mob.getMainHandStack()) && !list.isEmpty()) {
                mob.getNavigation().startMovingTo(list.get(0), speedModifier);
            }

        }

        @Override
        public void start() {
            List<ItemEntity> list = mob.world.getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
            if (!list.isEmpty()) {
                mob.getNavigation().startMovingTo(list.get(0), speedModifier);
            }
        }
    }

}

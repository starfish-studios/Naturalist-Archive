package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Zebra extends AbstractDonkeyEntity {
    private static final Ingredient FOOD_ITEMS = Ingredient.ofItems(Items.WHEAT, Items.SUGAR, Blocks.HAY_BLOCK.asItem(), Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);

    public Zebra(EntityType<? extends AbstractDonkeyEntity> entityType, World level) {
        super(entityType, level);
    }

    @Override
    protected void initAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(this.getChildHealthBonus());
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(this.getChildMovementSpeedBonus());
        this.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).setBaseValue(this.getChildJumpStrengthBonus());
    }

    @Override
    protected float getChildHealthBonus() {
        return 15.0f + this.random.nextInt(8) + this.random.nextInt(9);
    }

    @Override
    protected double getChildJumpStrengthBonus() {
        return 0.4f + this.random.nextDouble() * 0.1;
    }

    @Override
    protected double getChildMovementSpeedBonus() {
        return (0.5f + this.random.nextDouble() * 0.3 + this.random.nextDouble() * 0.3 + this.random.nextDouble() * 0.3) * 0.25;
    }

    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.96f;
    }

    @Override
    public boolean canBreedWith(AnimalEntity otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (otherAnimal instanceof Zebra zebra) {
            return this.canBreed() && zebra.canBreed();
        }
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverLevel, PassiveEntity ageableMob) {
        HorseBaseEntity zebra = NaturalistEntityTypes.ZEBRA.get().create(serverLevel);
        this.setChildAttributes(ageableMob, zebra);
        return zebra;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.6));
        this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.6));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0, HorseBaseEntity.class));
        this.goalSelector.add(3, new ZebraTemptGoal(this, 1.25, FOOD_ITEMS, true));
        this.goalSelector.add(4, new ZebraAvoidPlayersGoal(this, 16.0f, 1.6D, 1.6D));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void playWalkSound(BlockSoundGroup soundType) {
        super.playWalkSound(soundType);
        if (this.random.nextInt(10) == 0) {
            this.playSound(NaturalistSoundEvents.ZEBRA_BREATHE.get(), soundType.getVolume() * 0.6f, soundType.getPitch());
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return super.getMountedHeightOffset() + 0.25;
    }

    @Override
    protected void playJumpSound() {
        this.playSound(NaturalistSoundEvents.ZEBRA_JUMP.get(), 0.4F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return NaturalistSoundEvents.ZEBRA_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.ZEBRA_DEATH.get();
    }

    @Override
    @Nullable
    protected SoundEvent getEatSound() {
        return NaturalistSoundEvents.ZEBRA_EAT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        super.getHurtSound(damageSource);
        return NaturalistSoundEvents.ZEBRA_HURT.get();
    }

    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return NaturalistSoundEvents.ZEBRA_ANGRY.get();
    }

    @Override
    protected void playAddChestSound() {
        this.playSound(SoundEvents.ENTITY_MULE_CHEST, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    static class ZebraAvoidPlayersGoal extends FleeEntityGoal<PlayerEntity> {
        private final Zebra zebra;

        public ZebraAvoidPlayersGoal(Zebra zebra, float maxDistance, double walkSpeed, double sprintSpeed) {
            super(zebra, PlayerEntity.class, maxDistance, walkSpeed, sprintSpeed, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
            this.zebra = zebra;
        }

        @Override
        public boolean canStart() {
            return !this.zebra.isTame() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !this.zebra.isTame() && super.shouldContinue();
        }
    }

    static class ZebraTemptGoal extends TemptGoal {
        private final Zebra zebra;

        public ZebraTemptGoal(Zebra zebra, double speedModifier, Ingredient items, boolean canScare) {
            super(zebra, speedModifier, items, canScare);
            this.zebra = zebra;
        }

        @Override
        protected boolean canBeScared() {
            return super.canBeScared() && !this.zebra.isTame();
        }
    }
}

package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

public class Zebra extends AbstractChestedHorse {
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT, Items.SUGAR, Blocks.HAY_BLOCK.asItem(), Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);

    public Zebra(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.generateRandomMaxHealth(randomSource));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed(randomSource));
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength(randomSource));
    }

    @Override
    protected float generateRandomMaxHealth(RandomSource randomSource) {
        return 15.0f + randomSource.nextInt(8) + randomSource.nextInt(9);
    }

    @Override
    protected double generateRandomJumpStrength(RandomSource randomSource) {
        return 0.4f + randomSource.nextDouble() * 0.1;
    }

    @Override
    protected double generateRandomSpeed(RandomSource randomSource) {
        return (0.5f + randomSource.nextDouble() * 0.3 + randomSource.nextDouble() * 0.3 + randomSource.nextDouble() * 0.3) * 0.25;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96f;
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (otherAnimal instanceof Zebra zebra) {
            return this.canParent() && zebra.canParent();
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        AbstractHorse zebra = NaturalistEntityTypes.ZEBRA.get().create(serverLevel);
        this.setOffspringAttributes(ageableMob, zebra);
        return zebra;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.6));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, AbstractHorse.class));
        this.goalSelector.addGoal(3, new ZebraTemptGoal(this, 1.25, FOOD_ITEMS, true));
        this.goalSelector.addGoal(4, new ZebraAvoidPlayersGoal(this, 16.0f, 1.6D, 1.6D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void playGallopSound(SoundType soundType) {
        super.playGallopSound(soundType);
        if (this.random.nextInt(10) == 0) {
            this.playSound(NaturalistSoundEvents.ZEBRA_BREATHE.get(), soundType.getVolume() * 0.6f, soundType.getPitch());
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.25;
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
    protected SoundEvent getEatingSound() {
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
    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.MULE_CHEST, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    static class ZebraAvoidPlayersGoal extends AvoidEntityGoal<Player> {
        private final Zebra zebra;

        public ZebraAvoidPlayersGoal(Zebra zebra, float maxDistance, double walkSpeed, double sprintSpeed) {
            super(zebra, Player.class, maxDistance, walkSpeed, sprintSpeed, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.zebra = zebra;
        }

        @Override
        public boolean canUse() {
            return !this.zebra.isTamed() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.zebra.isTamed() && super.canContinueToUse();
        }
    }

    static class ZebraTemptGoal extends TemptGoal {
        private final Zebra zebra;

        public ZebraTemptGoal(Zebra zebra, double speedModifier, Ingredient items, boolean canScare) {
            super(zebra, speedModifier, items, canScare);
            this.zebra = zebra;
        }

        @Override
        protected boolean canScare() {
            return super.canScare() && !this.zebra.isTamed();
        }
    }
}

package com.starfish_studios.naturalist.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.MathHelper;
import java.util.List;

public class DistancedFollowParentGoal extends Goal {
    protected final AnimalEntity animal;
    @Nullable
    protected AnimalEntity parent;
    private final double speedModifier;
    private int timeToRecalcPath;
    protected final double horizontalScanRange;
    protected final double verticalScanRange;
    protected final double followDistanceThreshold;

    public DistancedFollowParentGoal(AnimalEntity animal, double speedModifier, double horizontalScanRange, double verticalScanRange, double followDistanceThreshold) {
        this.animal = animal;
        this.speedModifier = speedModifier;
        this.horizontalScanRange = horizontalScanRange;
        this.verticalScanRange = verticalScanRange;
        this.followDistanceThreshold = followDistanceThreshold;
    }

    @Override
    public boolean canStart() {
        if (this.animal.getBreedingAge() >= 0) {
            return false;
        } else {
            List<? extends AnimalEntity> adults = this.animal.world.getNonSpectatingEntities(this.animal.getClass(), this.animal.getBoundingBox().expand(horizontalScanRange, verticalScanRange, horizontalScanRange));
            AnimalEntity parent = null;
            double distance = Double.MAX_VALUE;

            for(AnimalEntity adult : adults) {
                if (adult.getBreedingAge() >= 0) {
                    double distanceToAdult = this.animal.squaredDistanceTo(adult);
                    if (!(distanceToAdult > distance)) {
                        distance = distanceToAdult;
                        parent = adult;
                    }
                }
            }

            if (parent == null) {
                return false;
            } else if (distance < MathHelper.square(followDistanceThreshold)) {
                return false;
            } else {
                this.parent = parent;
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        if (this.animal.getBreedingAge() >= 0) {
            return false;
        } else if (!this.parent.isAlive()) {
            return false;
        } else {
            double distanceToParent = this.animal.squaredDistanceTo(this.parent);
            return !(distanceToParent < MathHelper.square(followDistanceThreshold)) && !(distanceToParent > 256.0D);
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.parent = null;
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.getTickCount(10);
            this.animal.getNavigation().startMovingTo(this.parent, this.speedModifier);
        }
    }
}

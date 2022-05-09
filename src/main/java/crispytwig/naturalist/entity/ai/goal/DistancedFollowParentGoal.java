package crispytwig.naturalist.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

import javax.annotation.Nullable;
import java.util.List;

public class DistancedFollowParentGoal extends Goal {
    private final Animal animal;
    @Nullable
    private Animal parent;
    private final double speedModifier;
    private int timeToRecalcPath;
    private final double horizontalScanRange;
    private final double verticalScanRange;
    private final double followDistanceThreshold;

    public DistancedFollowParentGoal(Animal animal, double speedModifier, double horizontalScanRange, double verticalScanRange, double followDistanceThreshold) {
        this.animal = animal;
        this.speedModifier = speedModifier;
        this.horizontalScanRange = horizontalScanRange;
        this.verticalScanRange = verticalScanRange;
        this.followDistanceThreshold = followDistanceThreshold;
    }

    @Override
    public boolean canUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else {
            List<? extends Animal> adults = this.animal.level.getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(horizontalScanRange, verticalScanRange, horizontalScanRange));
            Animal parent = null;
            double distance = Double.MAX_VALUE;

            for(Animal adult : adults) {
                if (adult.getAge() >= 0) {
                    double distanceToAdult = this.animal.distanceToSqr(adult);
                    if (!(distanceToAdult > distance)) {
                        distance = distanceToAdult;
                        parent = adult;
                    }
                }
            }

            if (parent == null) {
                return false;
            } else if (distance < Mth.square(followDistanceThreshold)) {
                return false;
            } else {
                this.parent = parent;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else if (!this.parent.isAlive()) {
            return false;
        } else {
            double distanceToParent = this.animal.distanceToSqr(this.parent);
            return !(distanceToParent < Mth.square(followDistanceThreshold)) && !(distanceToParent > 256.0D);
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
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.animal.getNavigation().moveTo(this.parent, this.speedModifier);
        }
    }
}

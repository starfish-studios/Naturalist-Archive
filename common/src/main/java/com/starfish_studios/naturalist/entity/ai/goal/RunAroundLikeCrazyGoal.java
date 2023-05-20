//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.starfish_studios.naturalist.entity.ai.goal;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RunAroundLikeCrazyGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private double posX;
    private double posY;
    private double posZ;

    public RunAroundLikeCrazyGoal(PathfinderMob mob, double d) {
        this.mob = mob;
        this.speedModifier = d;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob.isVehicle()) {
            Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
            if (vec3 == null) {
                return false;
            } else {
                this.posX = vec3.x;
                this.posY = vec3.y;
                this.posZ = vec3.z;
                return true;
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && this.mob.isVehicle();
    }

    public void tick() {
        if (this.mob.getRandom().nextInt(this.adjustedTickDelay(50)) == 0) {
            Entity entity = this.mob.getPassengers().get(0);
            if (entity == null) {
                return;
            }
            this.mob.ejectPassengers();
            this.mob.level.broadcastEntityEvent(this.mob, (byte)6);
        }

    }
}

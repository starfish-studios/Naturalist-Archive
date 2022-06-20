package com.starfish_studios.naturalist.entity.ai.navigation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BetterWallClimberNavigation extends MobNavigation {
    @Nullable
    private BlockPos targetPos;

    public BetterWallClimberNavigation(MobEntity mobEntity, World world) {
        super(mobEntity, world);
    }

    @Override
    public Path findPathTo(BlockPos target, int distance) {
        this.targetPos = target;
        return super.findPathTo(target, distance);
    }

    @Override
    public Path findPathTo(Entity entity, int distance) {
        this.targetPos = entity.getBlockPos();
        return super.findPathTo(entity, distance);
    }

    @Override
    public boolean startMovingTo(Entity entity, double speed) {
        Path path = this.findPathTo((Entity)entity, 0);
        if (path != null) {
            return this.startMovingAlong(path, speed);
        } else {
            this.targetPos = entity.getBlockPos();
            this.speed = speed;
            return true;
        }
    }

    @Override
    public void tick() {
        if (!this.isIdle()) {
            super.tick();
        } else {
            if (this.targetPos != null) {
                if (!this.targetPos.isWithinDistance(this.entity.getPos(),
                        Math.max(this.entity.getWidth(), 1.0D))
                        && (!(this.entity.getY() > (double) this.targetPos.getY())
                        || !(new BlockPos(this.targetPos.getX(), this.entity.getY(),
                        this.targetPos.getZ())).isWithinDistance(this.entity.getPos(),
                        Math.max(this.entity.getWidth(), 1.0D)))) {
                    this.entity.getMoveControl().moveTo(this.targetPos.getX(), this.targetPos.getY(),
                            this.targetPos.getZ(), this.speed);
                } else {
                    this.targetPos = null;
                }
            }
        }
    }
}

package com.starfish_studios.naturalist.entity.projectile;

import com.starfish_studios.naturalist.entity.Duck;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownDuckEgg extends ThrowableItemProjectile {
    public ThrownDuckEgg(EntityType<ThrownDuckEgg> thrownDuckEggEntityType, Level level) {
        super(thrownDuckEggEntityType, level);
    }

    public ThrownDuckEgg(Level level, LivingEntity livingEntity) {
        super(NaturalistEntityTypes.DUCK_EGG.get(), livingEntity, level);
    }

    public ThrownDuckEgg(Level level, double d, double e, double f) {
        super(NaturalistEntityTypes.DUCK_EGG.get(), d, e, f, level);
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
            double d = 0.08;

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08);
            }
        }

    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    Duck duck = NaturalistEntityTypes.DUCK.get().create(this.level);
                    duck.setAge(-24000);
                    duck.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    this.level.addFreshEntity(duck);
                }
            }
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return NaturalistRegistry.DUCK_EGG.get();
    }
}

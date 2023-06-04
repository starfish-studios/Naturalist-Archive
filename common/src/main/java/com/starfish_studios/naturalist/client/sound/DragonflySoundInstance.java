package com.starfish_studios.naturalist.client.sound;

import com.starfish_studios.naturalist.common.entity.Dragonfly;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class DragonflySoundInstance extends AbstractTickableSoundInstance {
    protected final Dragonfly dragonfly;

    public DragonflySoundInstance(Dragonfly dragonfly) {
        super(NaturalistSoundEvents.DRAGONFLY_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.dragonfly = dragonfly;
        this.x = (float)dragonfly.getX();
        this.y = (float)dragonfly.getY();
        this.z = (float)dragonfly.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
    }

    @Override
    public void tick() {
        if (this.dragonfly.isRemoved()) {
            this.stop();
            return;
        }
        this.x = (float)this.dragonfly.getX();
        this.y = (float)this.dragonfly.getY();
        this.z = (float)this.dragonfly.getZ();
        float horizontalDistance = (float)this.dragonfly.getDeltaMovement().horizontalDistance();
        if (horizontalDistance >= 0.01f) {
            this.pitch = Mth.lerp(Mth.clamp(horizontalDistance, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
            this.volume = Mth.lerp(Mth.clamp(horizontalDistance, 0.0f, 0.5f), 0.0f, 1.2f);
        } else {
            this.pitch = 0.0f;
            this.volume = 0.0f;
        }
    }

    private float getMinPitch() {
        return 1.1f;
    }

    private float getMaxPitch() {
        return 1.5f;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.dragonfly.isSilent();
    }
}
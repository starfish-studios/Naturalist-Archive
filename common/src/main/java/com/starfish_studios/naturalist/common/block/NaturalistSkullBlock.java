package com.starfish_studios.naturalist.common.block;

import net.minecraft.world.level.block.SkullBlock;

public class NaturalistSkullBlock extends SkullBlock {
    public NaturalistSkullBlock(Types type, Properties properties) {
        super(type, properties);
    }

    public interface Type {
    }

    public static enum Types implements SkullBlock.Type {
        BEAR,
        DEER;
        private Types() {
        }
    }
}

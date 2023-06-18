package com.starfish_studios.naturalist.common.block.crate;

import com.starfish_studios.naturalist.common.item.*;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class AnimalCrateBlockEntity extends BlockEntity {

    public static final String ANIMAL_CRATE_DATA = "stored_animal";

    public CompoundTag entityData;

    public AnimalCrateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NaturalistBlockEntities.ANIMAL_CRATE.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (entityData != null) {
            tag.put(ANIMAL_CRATE_DATA, entityData);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(ANIMAL_CRATE_DATA)) {
            entityData = tag.getCompound(ANIMAL_CRATE_DATA);
        }
    }


    @SuppressWarnings("DataFlowIssue")
    public void onPlace(@Nullable LivingEntity placer, ItemStack stack) {
        entityData = stack.hasTag() ? stack.getTag().getCompound(ANIMAL_CRATE_DATA).copy() : null;
    }

    public InteractionResult onUse(Player player, InteractionHand hand) {
        if (entityData != null) {
            Entity releasedEntity = AnimalCrateBlockItem.createEntityFromNBT(level, entityData);
            if (releasedEntity != null) {
                if (!player.level.isClientSide) {
                    Vec3 position = new Vec3(getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1.5f, getBlockPos().getZ() + 0.5f);
                    releasedEntity.absMoveTo(position.x, position.y, position.z, level.random.nextFloat() * 360f, 0);
                    releasedEntity.setDeltaMovement(new Vec3(0, 0.25f, 0));
                    level.addFreshEntity(releasedEntity);
                    entityData = null;
                    AnimalCrateBlockItem.playReleaseSound(level, worldPosition);
                }
                player.swing(hand, true);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}

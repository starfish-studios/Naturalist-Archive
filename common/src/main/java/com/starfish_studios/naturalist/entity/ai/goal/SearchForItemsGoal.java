package com.starfish_studios.naturalist.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

public class SearchForItemsGoal extends Goal {
    private final PathAwareEntity mob;
    private final double speedModifier;
    private final double horizontalSearchRange;
    private final double verticalSearchRange;
    private final Ingredient ingredient;

    public SearchForItemsGoal(PathAwareEntity mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
        this.setControls(EnumSet.of(Control.MOVE));
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.ingredient = ingredient;
        this.horizontalSearchRange = horizontalSearchRange;
        this.verticalSearchRange = verticalSearchRange;
    }

    @Override
    public boolean canStart() {
        if (mob.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
            List<ItemEntity> list = mob.world.getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
            return !list.isEmpty() && mob.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty();
        }
        return false;
    }

    @Override
    public void tick() {
        List<ItemEntity> list = mob.world.getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
        ItemStack itemstack = mob.getEquippedStack(EquipmentSlot.MAINHAND);
        if (itemstack.isEmpty() && !list.isEmpty()) {
            mob.getNavigation().startMovingTo(list.get(0), speedModifier);
        }

    }

    @Override
    public void start() {
        List<ItemEntity> list = mob.world.getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
        if (!list.isEmpty()) {
            mob.getNavigation().startMovingTo(list.get(0), speedModifier);
        }

    }
}

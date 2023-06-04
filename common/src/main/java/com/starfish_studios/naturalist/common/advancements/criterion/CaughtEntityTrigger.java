//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.starfish_studios.naturalist.common.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CaughtEntityTrigger extends SimpleCriterionTrigger<CaughtEntityTrigger.TriggerInstance> {
    static final ResourceLocation ID = new ResourceLocation("caught_entity");

    public CaughtEntityTrigger() {
    }

    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public @NotNull TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new TriggerInstance(entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, (triggerInstance) -> {
            return triggerInstance.matches(stack);
        });
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public TriggerInstance(EntityPredicate.Composite composite, ItemPredicate itemPredicate) {
            super(CaughtEntityTrigger.ID, composite);
            this.item = itemPredicate;
        }

        /*
        public static TriggerInstance caughtMob(ItemPredicate item) {
            return new TriggerInstance(Composite.ANY, item);
        }
        */

        public boolean matches(ItemStack stack) {
            return this.item.matches(stack);
        }

        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            jsonObject.add("item", this.item.serializeToJson());
            return jsonObject;
        }
    }
}

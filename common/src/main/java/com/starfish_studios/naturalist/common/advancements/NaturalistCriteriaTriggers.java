package com.starfish_studios.naturalist.common.advancements;

import com.google.common.collect.Maps;
import com.starfish_studios.naturalist.common.advancements.criterion.CaughtEntityTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class NaturalistCriteriaTriggers {
    private static final Map<ResourceLocation, CriterionTrigger<?>> CRITERIA = Maps.newHashMap();
    public static final CaughtEntityTrigger CAUGHT_ENTITY = register(new CaughtEntityTrigger());

    public void CriteriaTriggers() {
    }

    public static <T extends CriterionTrigger<?>> T register(T criterion) {
        if (CRITERIA.containsKey(criterion.getId())) {
            throw new IllegalArgumentException("Duplicate criterion id " + criterion.getId());
        } else {
            CRITERIA.put(criterion.getId(), criterion);
            return criterion;
        }
    }

    public static <T extends CriterionTriggerInstance> CriterionTrigger getCriterion(ResourceLocation id) {
        return CRITERIA.get(id);
    }

    public static Iterable<? extends CriterionTrigger<?>> all() {
        return CRITERIA.values();
    }
}

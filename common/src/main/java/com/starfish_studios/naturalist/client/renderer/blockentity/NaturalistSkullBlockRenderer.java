package com.starfish_studios.naturalist.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.starfish_studios.naturalist.client.model.geom.NaturalistModelLayers;
import com.starfish_studios.naturalist.common.block.NaturalistSkullBlock;
import net.minecraft.Util;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.dragon.DragonHeadModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class NaturalistSkullBlockRenderer extends SkullBlockRenderer {
    private static final Map SKIN_BY_TYPE = Util.make(Maps.newHashMap(), (hashMap) -> {
        hashMap.put(NaturalistSkullBlock.Types.BEAR, new ResourceLocation("naturalist:textures/entity/bear/bear.png"));
    });

    public NaturalistSkullBlockRenderer(BlockEntityRendererProvider.Context context, Map<SkullBlock.Type, SkullModelBase> modelByType) {
        super(context);
    }

    public static @NotNull Map<SkullBlock.Type, SkullModelBase> createSkullRenderers(EntityModelSet entityModelSet) {
        ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> builder = ImmutableMap.builder();
        builder.put(NaturalistSkullBlock.Types.BEAR, new SkullModel(entityModelSet.bakeLayer(NaturalistModelLayers.BEAR)));
        return builder.build();
    }
}

package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.NaturalistClient;
import com.starfish_studios.naturalist.client.renderer.ZebraRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class NaturalistFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NaturalistClient.init();
        EntityModelLayerRegistry.registerModelLayer(ZebraRenderer.LAYER_LOCATION, () -> LayerDefinition.create(HorseModel.createBodyMesh(CubeDeformation.NONE), 64, 64));
    }
}

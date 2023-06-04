package com.starfish_studios.naturalist.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Elephant;
import com.starfish_studios.naturalist.common.world.inventory.ElephantInventoryMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class ElephantInventoryScreen extends AbstractContainerScreen<ElephantInventoryMenu> {
    private static final ResourceLocation ELEPHANT_INVENTORY_LOCATION = new ResourceLocation(Naturalist.MOD_ID, "textures/gui/container/elephant.png");
    private final Elephant elephant;
    private float xMouse;
    private float yMouse;

    public ElephantInventoryScreen(ElephantInventoryMenu elephantInventoryMenu, Inventory inventory, Elephant elephant) {
        super(elephantInventoryMenu, inventory, elephant.getDisplayName());
        this.elephant = elephant;
        this.passEvents = false;
    }

    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ELEPHANT_INVENTORY_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (this.elephant != null) {
            if (elephant.hasChest()) {
                this.blit(poseStack, i + 79, j + 17, 0, this.imageHeight, elephant.getInventoryColumns() * 18, 54);
            }
        }

        if (this.elephant.isSaddleable()) {
            this.blit(poseStack, i + 7, j + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }

        if (this.elephant.canWearArmor()) {
            this.blit(poseStack, i + 7, j + 35, 0, this.imageHeight + 54, 18, 18);
        }

        InventoryScreen.renderEntityInInventory(i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.elephant);
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}
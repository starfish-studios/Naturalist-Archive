package com.starfish_studios.naturalist.mixin;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multisets;
import com.starfish_studios.naturalist.entity.Giraffe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItem.class)
public class MapItemMixin {

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void onUpdate(Level level, Entity viewer, MapItemSavedData data, CallbackInfo ci) {
        if (viewer.getVehicle() instanceof Giraffe) {
            ci.cancel();
            if (level.dimension() != data.dimension || !(viewer instanceof Player)) {
                return;
            }
            int i = 1 << data.scale;
            int j = data.x;
            int k = data.z;
            int l = Mth.floor(viewer.getX() - (double)j) / i + 64;
            int m = Mth.floor(viewer.getZ() - (double)k) / i + 64;
            int n = 192 / i; // +50% range increase
            if (level.dimensionType().hasCeiling()) {
                n /= 2;
            }
            MapItemSavedData.HoldingPlayer holdingPlayer = data.getHoldingPlayer((Player)viewer);
            ++holdingPlayer.step;
            boolean bl = false;
            for (int o = l - n + 1; o < l + n; ++o) {
                if ((o & 0xF) != (holdingPlayer.step & 0xF) && !bl) continue;
                bl = false;
                double d = 0.0;
                for (int p = m - n - 1; p < m + n; ++p) {
                    double f;
                    if (o < 0 || p < -1 || o >= 128 || p >= 128) continue;
                    int q = o - l;
                    int r = p - m;
                    boolean bl2 = q * q + r * r > (n - 2) * (n - 2);
                    int s = (j / i + o - 64) * i;
                    int t = (k / i + p - 64) * i;
                    LinkedHashMultiset<MaterialColor> multiset = LinkedHashMultiset.create();
                    LevelChunk levelChunk = level.getChunkAt(new BlockPos(s, 0, t));
                    if (levelChunk.isEmpty()) continue;
                    ChunkPos chunkPos = levelChunk.getPos();
                    int u = s & 0xF;
                    int v = t & 0xF;
                    int w = 0;
                    double e = 0.0;
                    if (level.dimensionType().hasCeiling()) {
                        int x = s + t * 231871;
                        if (((x = x * x * 31287121 + x * 11) >> 20 & 1) == 0) {
                            multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
                        } else {
                            multiset.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
                        }
                        e = 100.0;
                    } else {
                        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                        BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
                        for (int y = 0; y < i; ++y) {
                            for (int z = 0; z < i; ++z) {
                                BlockState blockState;
                                int aa = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, y + u, z + v) + 1;
                                if (aa > level.getMinBuildHeight() + 1) {
                                    do {
                                        mutableBlockPos.set(chunkPos.getMinBlockX() + y + u, --aa, chunkPos.getMinBlockZ() + z + v);
                                    } while ((blockState = levelChunk.getBlockState(mutableBlockPos)).getMapColor(level, mutableBlockPos) == MaterialColor.NONE && aa > level.getMinBuildHeight());
                                    if (aa > level.getMinBuildHeight() && !blockState.getFluidState().isEmpty()) {
                                        BlockState blockState2;
                                        int ab = aa - 1;
                                        mutableBlockPos2.set(mutableBlockPos);
                                        do {
                                            mutableBlockPos2.setY(ab--);
                                            blockState2 = levelChunk.getBlockState(mutableBlockPos2);
                                            ++w;
                                        } while (ab > level.getMinBuildHeight() && !blockState2.getFluidState().isEmpty());
                                        blockState = this.getCorrectStateForFluidBlock(level, blockState, mutableBlockPos);
                                    }
                                } else {
                                    blockState = Blocks.BEDROCK.defaultBlockState();
                                }
                                data.checkBanners(level, chunkPos.getMinBlockX() + y + u, chunkPos.getMinBlockZ() + z + v);
                                e += (double)aa / (double)(i * i);
                                multiset.add(blockState.getMapColor(level, mutableBlockPos));
                            }
                        }
                    }
                    MaterialColor materialColor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MaterialColor.NONE);
                    MaterialColor.Brightness brightness = materialColor == MaterialColor.WATER ? ((f = (double)(w /= i * i) * 0.1 + (double)(o + p & 1) * 0.2) < 0.5 ? MaterialColor.Brightness.HIGH : (f > 0.9 ? MaterialColor.Brightness.LOW : MaterialColor.Brightness.NORMAL)) : ((f = (e - d) * 4.0 / (double)(i + 4) + ((double)(o + p & 1) - 0.5) * 0.4) > 0.6 ? MaterialColor.Brightness.HIGH : (f < -0.6 ? MaterialColor.Brightness.LOW : MaterialColor.Brightness.NORMAL));
                    d = e;
                    if (p < 0 || q * q + r * r >= n * n || bl2 && (o + p & 1) == 0) continue;
                    bl |= data.updateColor(o, p, materialColor.getPackedId(brightness));
                }
            }
        }
    }

    private BlockState getCorrectStateForFluidBlock(Level level, BlockState state, BlockPos pos) {
        FluidState fluidState = state.getFluidState();
        if (!fluidState.isEmpty() && !state.isFaceSturdy(level, pos, Direction.UP)) {
            return fluidState.createLegacyBlock();
        }
        return state;
    }
}

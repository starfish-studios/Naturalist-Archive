package com.starfish_studios.naturalist.mixin;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.starfish_studios.naturalist.entity.Giraffe;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FilledMapItem.class)
public class MapItemMixin {

    @Inject(method = "updateColors", at = @At("HEAD"), cancellable = true)
    public void onUpdate(World world, Entity entity, MapState state, CallbackInfo ci) {
        if (entity.getVehicle() instanceof Giraffe) {
            ci.cancel();
            if (world.getRegistryKey() == state.dimension && entity instanceof PlayerEntity) {
                int i = 1 << state.scale;
                int j = state.centerX;
                int k = state.centerZ;
                int l = MathHelper.floor(entity.getX() - (double)j) / i + 64;
                int m = MathHelper.floor(entity.getZ() - (double)k) / i + 64;
                int n = 128 / i;
                if (world.getDimension().hasCeiling()) {
                    n /= 2;
                }

                MapState.PlayerUpdateTracker playerUpdateTracker = state.getPlayerSyncData((PlayerEntity)entity);
                ++playerUpdateTracker.field_131;
                boolean bl = false;

                for(int o = l - n + 1; o < l + n; ++o) {
                    if ((o & 15) == (playerUpdateTracker.field_131 & 15) || bl) {
                        bl = false;
                        double d = 0.0;

                        for(int p = m - n - 1; p < m + n; ++p) {
                            if (o >= 0 && p >= -1 && o < 128 && p < 128) {
                                int q = o - l;
                                int r = p - m;
                                boolean bl2 = q * q + r * r > (n - 2) * (n - 2);
                                int s = (j / i + o - 64) * i;
                                int t = (k / i + p - 64) * i;
                                Multiset<MapColor> multiset = LinkedHashMultiset.create();
                                WorldChunk worldChunk = world.getWorldChunk(new BlockPos(s, 0, t));
                                if (!worldChunk.isEmpty()) {
                                    ChunkPos chunkPos = worldChunk.getPos();
                                    int u = s & 15;
                                    int v = t & 15;
                                    int w = 0;
                                    double e = 0.0;
                                    if (world.getDimension().hasCeiling()) {
                                        int x = s + t * 231871;
                                        x = x * x * 31287121 + x * 11;
                                        if ((x >> 20 & 1) == 0) {
                                            multiset.add(Blocks.DIRT.getDefaultState().getMapColor(world, BlockPos.ORIGIN), 10);
                                        } else {
                                            multiset.add(Blocks.STONE.getDefaultState().getMapColor(world, BlockPos.ORIGIN), 100);
                                        }

                                        e = 100.0;
                                    } else {
                                        BlockPos.Mutable mutable = new BlockPos.Mutable();
                                        BlockPos.Mutable mutable2 = new BlockPos.Mutable();

                                        for(int y = 0; y < i; ++y) {
                                            for(int z = 0; z < i; ++z) {
                                                int aa = worldChunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, y + u, z + v) + 1;
                                                BlockState blockState;
                                                if (aa <= world.getBottomY() + 1) {
                                                    blockState = Blocks.BEDROCK.getDefaultState();
                                                } else {
                                                    do {
                                                        --aa;
                                                        mutable.set(chunkPos.getStartX() + y + u, aa, chunkPos.getStartZ() + z + v);
                                                        blockState = worldChunk.getBlockState(mutable);
                                                    } while(blockState.getMapColor(world, mutable) == MapColor.CLEAR && aa > world.getBottomY());

                                                    if (aa > world.getBottomY() && !blockState.getFluidState().isEmpty()) {
                                                        int ab = aa - 1;
                                                        mutable2.set(mutable);

                                                        BlockState blockState2;
                                                        do {
                                                            mutable2.setY(ab--);
                                                            blockState2 = worldChunk.getBlockState(mutable2);
                                                            ++w;
                                                        } while(ab > world.getBottomY() && !blockState2.getFluidState().isEmpty());

                                                        blockState = this.getFluidStateIfVisible(world, blockState, mutable);
                                                    }
                                                }

                                                state.removeBanner(world, chunkPos.getStartX() + y + u, chunkPos.getStartZ() + z + v);
                                                e += (double)aa / (double)(i * i);
                                                multiset.add(blockState.getMapColor(world, mutable));
                                            }
                                        }
                                    }

                                    w /= i * i;
                                    MapColor mapColor = (MapColor)Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.CLEAR);
                                    MapColor.Brightness brightness;
                                    double f;
                                    if (mapColor == MapColor.WATER_BLUE) {
                                        f = (double)w * 0.1 + (double)(o + p & 1) * 0.2;
                                        if (f < 0.5) {
                                            brightness = MapColor.Brightness.HIGH;
                                        } else if (f > 0.9) {
                                            brightness = MapColor.Brightness.LOW;
                                        } else {
                                            brightness = MapColor.Brightness.NORMAL;
                                        }
                                    } else {
                                        f = (e - d) * 4.0 / (double)(i + 4) + ((double)(o + p & 1) - 0.5) * 0.4;
                                        if (f > 0.6) {
                                            brightness = MapColor.Brightness.HIGH;
                                        } else if (f < -0.6) {
                                            brightness = MapColor.Brightness.LOW;
                                        } else {
                                            brightness = MapColor.Brightness.NORMAL;
                                        }
                                    }

                                    d = e;
                                    if (p >= 0 && q * q + r * r < n * n && (!bl2 || (o + p & 1) != 0)) {
                                        bl |= state.putColor(o, p, mapColor.getRenderColorByte(brightness));
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }


    private BlockState getFluidStateIfVisible(World world, BlockState state, BlockPos pos) {
        FluidState fluidState = state.getFluidState();
        return !fluidState.isEmpty() && !state.isSideSolidFullSquare(world, pos, Direction.UP) ? fluidState.getBlockState() : state;
    }
}

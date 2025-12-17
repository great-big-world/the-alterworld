package dev.creoii.greatbigworld.thealterworld.mixin.world.structure;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DesertPyramidPiece.class)
public abstract class DesertTempleGeneratorMixin extends ScatteredFeaturePiece {
    @Unique
    private boolean hasPlacedPortal = false;

    protected DesertTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void gbw$getBoolean(CompoundTag nbt, CallbackInfo ci) {
        hasPlacedPortal = nbt.getBooleanOr("hasPlacedPortal", false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void gbw$putBoolean(StructurePieceSerializationContext context, CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("hasPlacedPortal", hasPlacedPortal);
    }

    @Inject(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/DesertPyramidPiece;placeBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/block/state/BlockState;IIILnet/minecraft/world/level/levelgen/structure/BoundingBox;)V", ordinal = 32, shift = At.Shift.AFTER))
    private void gbw$placeDesertTemplePortal1(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci, @Local int l) {
        if (!hasPlacedPortal && l > 7 && l < 17) {
            BlockState frameState = world.registryAccess().lookup(Registries.DIMENSION_TYPE).get().wrapAsHolder(world.dimensionType()).is(BuiltinDimensionTypes.OVERWORLD) ? TheAlterworldBlocks.REINFORCED_DEEPSLATE.defaultBlockState().setValue(ReinforcedDeepslateBlock.CAN_FRACTURE, true) : Blocks.CHISELED_SANDSTONE.defaultBlockState();
            int x = 4;

            placeBlock(world, frameState, x, 0, l, chunkBox);
            placeBlock(world, frameState, x, 0, l - 1, chunkBox);
            placeBlock(world, frameState, x, 0, l - 2, chunkBox);
            placeBlock(world, frameState, x, 0, l - 3, chunkBox);
            placeBlock(world, frameState, x, 1, l, chunkBox);
            placeBlock(world, frameState, x, 1, l - 3, chunkBox);

            placeBlock(world, frameState, x, 2, l - 3, chunkBox);
            placeBlock(world, frameState, x, 2, l, chunkBox);

            placeBlock(world, frameState, x, 3, l, chunkBox);
            placeBlock(world, frameState, x, 3, l - 3, chunkBox);
            placeBlock(world, frameState, x, 4, l, chunkBox);
            placeBlock(world, frameState, x, 4, l - 1, chunkBox);
            placeBlock(world, frameState, x, 4, l - 2, chunkBox);
            placeBlock(world, frameState, x, 4, l - 3, chunkBox);

            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 1, l - 1, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 1, l - 2, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 2, l - 1, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 2, l - 2, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 3, l - 1, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 3, l - 2, chunkBox);

            hasPlacedPortal = true;
        }
    }

    @Inject(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/DesertPyramidPiece;placeBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/block/state/BlockState;IIILnet/minecraft/world/level/levelgen/structure/BoundingBox;)V", ordinal = 34, shift = At.Shift.AFTER))
    private void gbw$placeDesertTemplePortal2(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci, @Local int l) {
        if (!hasPlacedPortal && l > 7 && l < 17) {
            BlockState frameState = world.registryAccess().lookup(Registries.DIMENSION_TYPE).get().wrapAsHolder(world.dimensionType()).is(BuiltinDimensionTypes.OVERWORLD) ? TheAlterworldBlocks.REINFORCED_DEEPSLATE.defaultBlockState().setValue(ReinforcedDeepslateBlock.CAN_FRACTURE, true) : Blocks.CHISELED_SANDSTONE.defaultBlockState();
            int x = width - 5;

            placeBlock(world, frameState, x, 0, l, chunkBox);
            placeBlock(world, frameState, x, 0, l - 1, chunkBox);
            placeBlock(world, frameState, x, 0, l - 2, chunkBox);
            placeBlock(world, frameState, x, 0, l - 3, chunkBox);
            placeBlock(world, frameState, x, 1, l, chunkBox);
            placeBlock(world, frameState, x, 1, l - 3, chunkBox);

            placeBlock(world, frameState, x, 2, l - 3, chunkBox);
            placeBlock(world, frameState, x, 2, l, chunkBox);

            placeBlock(world, frameState, x, 3, l, chunkBox);
            placeBlock(world, frameState, x, 3, l - 3, chunkBox);
            placeBlock(world, frameState, x, 4, l, chunkBox);
            placeBlock(world, frameState, x, 4, l - 1, chunkBox);
            placeBlock(world, frameState, x, 4, l - 2, chunkBox);
            placeBlock(world, frameState, x, 4, l - 3, chunkBox);

            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 1, l - 1, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 1, l - 2, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 2, l - 1, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 2, l - 2, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 3, l - 1, chunkBox);
            placeBlock(world, Blocks.AIR.defaultBlockState(), x, 3, l - 2, chunkBox);

            hasPlacedPortal = true;
        }
    }
}

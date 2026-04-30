package dev.creoii.greatbigworld.thealterworld.mixin.world.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DesertPyramidPiece.class)
public abstract class DesertTempleGeneratorMixin extends ScatteredFeaturePiece {
    protected DesertTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @WrapOperation(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/DesertPyramidPiece;createChest(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/resources/ResourceKey;)Z"))
    private boolean gbw$generatePortalRoom(DesertPyramidPiece instance, WorldGenLevel worldGenLevel, BoundingBox boundingBox, RandomSource randomSource, int i, int j, int k, ResourceKey<LootTable> resourceKey, Operation<Boolean> original, @Local Direction direction) {
        if (direction == Direction.NORTH) {
            return createPortalRoom(worldGenLevel, boundingBox, i, j, k);
        }
        return original.call(instance, worldGenLevel, boundingBox, randomSource, i, j, k, resourceKey);
    }

    @Unique
    private boolean createPortalRoom(WorldGenLevel worldGenLevel, BoundingBox boundingBox, int i, int j, int k) {
        generateBox(worldGenLevel, boundingBox, i - 2, j - 2, k - 3, i + 3, j + 4, k, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i, j, k, boundingBox);
        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i, j + 1, k, boundingBox);
        placeBlock(worldGenLevel, Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH), i, j - 1, k, boundingBox);
        placeBlock(worldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i - 1, j + 1, k, boundingBox);
        placeBlock(worldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i + 1, j + 1, k, boundingBox);
        placeBlock(worldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i, j + 2, k, boundingBox);

        BlockState runeState = worldGenLevel.registryAccess().lookup(Registries.DIMENSION_TYPE).get().wrapAsHolder(worldGenLevel.dimensionType()).is(BuiltinDimensionTypes.OVERWORLD) ? TheAlterworldBlocks.REINFORCED_DEEPSLATE.defaultBlockState().setValue(ReinforcedDeepslateBlock.CAN_FRACTURE, true).setValue(ReinforcedDeepslateBlock.RUNE, ReinforcedDeepslateBlock.Rune.JUNGLE) : Blocks.CHISELED_SANDSTONE.defaultBlockState();
        BlockState frameState = worldGenLevel.registryAccess().lookup(Registries.DIMENSION_TYPE).get().wrapAsHolder(worldGenLevel.dimensionType()).is(BuiltinDimensionTypes.OVERWORLD) ? Blocks.REINFORCED_DEEPSLATE.defaultBlockState() : Blocks.CHISELED_SANDSTONE.defaultBlockState();

        placeBlock(worldGenLevel, frameState, i - 1, j - 1, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i, j - 1, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i + 1, j - 1, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i + 2, j - 1, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i - 1, j, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i - 1, j + 2, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i + 2, j, k - 3, boundingBox);

        placeBlock(worldGenLevel, runeState, i + 2, j + 1, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i - 1, j + 1, k - 3, boundingBox);

        placeBlock(worldGenLevel, frameState, i + 2, j + 2, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i - 1, j + 3, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i, j + 3, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i + 1, j + 3, k - 3, boundingBox);
        placeBlock(worldGenLevel, frameState, i + 2, j + 3, k - 3, boundingBox);

        generateAirBox(worldGenLevel, boundingBox, i, j, k - 3, i + 1, j + 2, k - 3);

        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i + 1, j - 1, k - 1, boundingBox);
        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i, j - 1, k - 1, boundingBox);
        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i - 1, j - 1, k - 1, boundingBox);

        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i - 1, j - 1, k - 2, boundingBox);
        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i, j - 1, k - 2, boundingBox);

        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i - 1, j, k - 2, boundingBox);
        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i - 1, j, k - 1, boundingBox);
        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i, j, k - 1, boundingBox);

        placeBlock(worldGenLevel, Blocks.SAND.defaultBlockState(), i - 1, j + 1, k - 1, boundingBox);

        return true;
    }
}

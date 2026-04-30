package dev.creoii.greatbigworld.thealterworld.mixin.world.structure;

import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.JungleTemplePiece;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JungleTemplePiece.class)
public abstract class JungleTempleGeneratorMixin extends ScatteredFeaturePiece {
    @Shadow @Final private static JungleTemplePiece.MossStoneSelector STONE_SELECTOR;

    protected JungleTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @ModifyConstant(method = "<init>(Lnet/minecraft/util/RandomSource;II)V", constant = @Constant(intValue = 15))
    private static int gbw$expandJungleTempleZSize(int constant) {
        return constant + 1;
    }

    @Inject(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/JungleTemplePiece;generateAirBox(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;IIIIII)V", ordinal = 10))
    private void gbw$placeJungleTemplePortal(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        BlockState runeState = world.registryAccess().lookup(Registries.DIMENSION_TYPE).get().wrapAsHolder(world.dimensionType()).is(BuiltinDimensionTypes.OVERWORLD) ? TheAlterworldBlocks.REINFORCED_DEEPSLATE.defaultBlockState().setValue(ReinforcedDeepslateBlock.CAN_FRACTURE, true).setValue(ReinforcedDeepslateBlock.RUNE, ReinforcedDeepslateBlock.Rune.JUNGLE) : STONE_SELECTOR.getNext();
        BlockState frameState = world.registryAccess().lookup(Registries.DIMENSION_TYPE).get().wrapAsHolder(world.dimensionType()).is(BuiltinDimensionTypes.OVERWORLD) ? Blocks.REINFORCED_DEEPSLATE.defaultBlockState() : STONE_SELECTOR.getNext();

        placeBlock(world, frameState, 4, -4, 14, chunkBox);
        placeBlock(world, frameState, 5, -4, 14, chunkBox);
        placeBlock(world, frameState, 6, -4, 14, chunkBox);
        placeBlock(world, frameState, 7, -4, 14, chunkBox);
        placeBlock(world, frameState, 4, -3, 14, chunkBox);
        placeBlock(world, frameState, 4, -1, 14, chunkBox);
        placeBlock(world, frameState, 7, -3, 14, chunkBox);

        placeBlock(world, runeState, 7, -2, 14, chunkBox);
        placeBlock(world, frameState, 4, -2, 14, chunkBox);

        placeBlock(world, frameState, 7, -1, 14, chunkBox);
        placeBlock(world, frameState, 4, 0, 14, chunkBox);
        placeBlock(world, frameState, 5, 0, 14, chunkBox);
        placeBlock(world, frameState, 6, 0, 14, chunkBox);
        placeBlock(world, frameState, 7, 0, 14, chunkBox);

        generateAirBox(world, chunkBox, 5, -3, 14, 6, -1, 14);

        generateBox(world, chunkBox, 4, -4, 15, 7, 0, 15, false, random, STONE_SELECTOR);
        generateBox(world, chunkBox, 4, 1, 13, 7, 1, 15, false, random, STONE_SELECTOR);
    }
}

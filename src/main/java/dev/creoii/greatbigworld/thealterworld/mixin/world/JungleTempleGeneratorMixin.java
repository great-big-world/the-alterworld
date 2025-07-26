package dev.creoii.greatbigworld.thealterworld.mixin.world;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JungleTempleGenerator;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JungleTempleGenerator.class)
public abstract class JungleTempleGeneratorMixin extends ShiftableStructurePiece {
    @Shadow @Final private static JungleTempleGenerator.CobblestoneRandomizer COBBLESTONE_RANDOMIZER;

    protected JungleTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/JungleTempleGenerator;fill(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIII)V", ordinal = 10))
    private void gbw$placeJungleTemplePortal(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        if (random.nextInt(4) != 0) {
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 4, -4, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 5, -4, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 6, -4, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 7, -4, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 4, -3, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 4, -2, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 4, -1, 14, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), 5, -3, 14, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), 6, -3, 14, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), 5, -2, 14, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), 6, -2, 14, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), 5, -1, 14, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), 6, -1, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 7, -3, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 7, -2, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 7, -1, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 4, 0, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 5, 0, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 6, 0, 14, chunkBox);
            addBlock(world, TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState(), 7, 0, 14, chunkBox);
            fillWithOutline(world, chunkBox, 4, -4, 15, 7, 0, 15, false, random, COBBLESTONE_RANDOMIZER);
        }
    }
}

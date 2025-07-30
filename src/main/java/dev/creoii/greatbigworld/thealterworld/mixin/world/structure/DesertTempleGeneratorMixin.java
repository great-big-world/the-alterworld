package dev.creoii.greatbigworld.thealterworld.mixin.world.structure;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.DesertTempleGenerator;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DesertTempleGenerator.class)
public abstract class DesertTempleGeneratorMixin extends ShiftableStructurePiece {
    @Unique
    private boolean hasPlacedPortal = false;

    protected DesertTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void gbw$getBoolean(NbtCompound nbt, CallbackInfo ci) {
        hasPlacedPortal = nbt.getBoolean("hasPlacedPortal", false);
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void gbw$putBoolean(StructureContext context, NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("hasPlacedPortal", hasPlacedPortal);
    }

    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/DesertTempleGenerator;addBlock(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/block/BlockState;IIILnet/minecraft/util/math/BlockBox;)V", ordinal = 34, shift = At.Shift.AFTER))
    private void gbw$placeDesertTemplePortal(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci, @Local int l) {
        if (!hasPlacedPortal && random.nextInt(3) == 0 && l > 7) {
            BlockState frameState = world.getRegistryManager().getOptional(RegistryKeys.DIMENSION_TYPE).get().getEntry(world.getDimension()).matchesKey(DimensionTypes.OVERWORLD) ? TheAlterworldBlocks.ANCIENT_MOSAIC.getDefaultState() : Blocks.CHISELED_SANDSTONE.getDefaultState();
            int x = random.nextBoolean() ? 4 : width - 5;
            addBlock(world, frameState, x, 0, l, chunkBox);
            addBlock(world, frameState, x, 0, l - 1, chunkBox);
            addBlock(world, frameState, x, 0, l - 2, chunkBox);
            addBlock(world, frameState, x, 0, l - 3, chunkBox);
            addBlock(world, frameState, x, 1, l, chunkBox);
            addBlock(world, frameState, x, 1, l - 3, chunkBox);
            addBlock(world, frameState, x, 2, l, chunkBox);
            addBlock(world, frameState, x, 2, l - 3, chunkBox);
            addBlock(world, frameState, x, 3, l, chunkBox);
            addBlock(world, frameState, x, 3, l - 3, chunkBox);
            addBlock(world, frameState, x, 4, l, chunkBox);
            addBlock(world, frameState, x, 4, l - 1, chunkBox);
            addBlock(world, frameState, x, 4, l - 2, chunkBox);
            addBlock(world, frameState, x, 4, l - 3, chunkBox);

            addBlock(world, Blocks.AIR.getDefaultState(), x, 1, l - 1, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), x, 1, l - 2, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), x, 2, l - 1, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), x, 2, l - 2, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), x, 3, l - 1, chunkBox);
            addBlock(world, Blocks.AIR.getDefaultState(), x, 3, l - 2, chunkBox);

            hasPlacedPortal = true;
        }
    }
}

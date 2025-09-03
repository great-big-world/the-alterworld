package dev.creoii.greatbigworld.thealterworld.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.world.feature.SculkPatchFeature;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;

@Mixin(SculkBlock.class)
public class SculkBlockMixin {
    @WrapOperation(method = "getExtraBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;", ordinal = 0))
    private BlockState gbw$applySculkShriekerRotation(Block instance, Operation<BlockState> original, @Local(argsOnly = true) WorldAccess world, @Local(argsOnly = true) BlockPos pos) {
        Collections.shuffle(SculkPatchFeature.DIRECTIONS);
        for (Direction direction : SculkPatchFeature.DIRECTIONS) {
            if (world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(world, pos.offset(direction), direction.getOpposite())) {
                return Blocks.SCULK_SHRIEKER.getDefaultState().with(SculkShriekerBlock.CAN_SUMMON, true).with(Properties.FACING, direction.getOpposite());
            }
        }
        return original.call(instance);
    }

    @WrapOperation(method = "getExtraBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;", ordinal = 1))
    private BlockState gbw$applySculkSensorRotation(Block instance, Operation<BlockState> original, @Local(argsOnly = true) WorldAccess world, @Local(argsOnly = true) BlockPos pos) {
        Collections.shuffle(SculkPatchFeature.DIRECTIONS);
        for (Direction direction : SculkPatchFeature.DIRECTIONS) {
            if (world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(world, pos.offset(direction), direction.getOpposite())) {
                return Blocks.SCULK_SENSOR.getDefaultState().with(Properties.FACING, direction.getOpposite());
            }
        }
        return original.call(instance);
    }
}

package dev.creoii.greatbigworld.thealterworld.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.world.FracturedAlterworldPortal;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {
    @Inject(method = "onBlockAdded", at = @At("TAIL"))
    private void gbw$createAlterworldPortal(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (!oldState.isOf(state.getBlock())) {
            if (world.getRegistryKey() == World.OVERWORLD || world.getRegistryKey() == GreatBigWorld.ALTERWORLD_KEY) {
                if (!state.isOf(Blocks.FIRE))
                    return;
                BlockState newState = world.getBlockState(pos);

                boolean anyTrue = FireBlock.DIRECTION_PROPERTIES.entrySet().stream().anyMatch(directionBooleanPropertyEntry -> state.get(directionBooleanPropertyEntry.getValue()));
                boolean fractured = false;
                if (!anyTrue) {
                    BlockState down = world.getBlockState(pos.down());
                    fractured = down.getBlock() instanceof ReinforcedDeepslateBlock && ReinforcedDeepslateBlock.isFractured(down);
                } else {
                    for (Map.Entry<Direction, BooleanProperty> entry : FireBlock.DIRECTION_PROPERTIES.entrySet()) {
                        if (state.get(entry.getValue())) {
                            BlockState offset = world.getBlockState(pos.offset(entry.getKey()));
                            fractured = offset.getBlock() instanceof ReinforcedDeepslateBlock && ReinforcedDeepslateBlock.isFractured(offset);
                        }

                        if (fractured)
                            break;
                    }
                }

                if (fractured) {
                    Optional<FracturedAlterworldPortal> optional2 = FracturedAlterworldPortal.getNewPortal(world, pos, Direction.Axis.X);
                    optional2.ifPresent(portal -> {
                        if (newState.getBlock() instanceof FireBlock) {
                            portal.createPortal(world);
                        }
                    });
                }
            }
        }
    }

    @Inject(method = "isOverworldOrNether", at = @At("HEAD"), cancellable = true)
    private static void gbw$allowNetherPortalsInAlterworld(World world, CallbackInfoReturnable<Boolean> cir) {
        if (world.getRegistryKey() == GreatBigWorld.ALTERWORLD_KEY)
            cir.setReturnValue(true);
    }

    @ModifyExpressionValue(method = "shouldLightPortalAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private static boolean gbw$lightableAlterworldPortals(boolean original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos, @Local BlockPos.Mutable mutable, @Local(ordinal = 1) Direction direction2) {
        BlockState state = world.getBlockState(mutable.set(pos).move(direction2));
        return original || state.isOf(TheAlterworldBlocks.REINFORCED_DEEPSLATE);
    }
}

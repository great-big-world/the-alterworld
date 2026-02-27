package dev.creoii.greatbigworld.thealterworld.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.world.FracturedAlterworldPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

@Mixin(BaseFireBlock.class)
public class AbstractFireBlockMixin {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void gbw$createAlterworldPortal(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (!oldState.is(state.getBlock())) {
            if (world.dimension() == Level.OVERWORLD || world.dimension() == GreatBigWorld.ALTERWORLD_KEY) {
                if (!state.is(Blocks.FIRE))
                    return;

                boolean anyTrue = FireBlock.PROPERTY_BY_DIRECTION.entrySet().stream().anyMatch(directionBooleanPropertyEntry -> state.getValue(directionBooleanPropertyEntry.getValue()));
                boolean canFracture = false;
                if (!anyTrue) {
                    BlockState down = world.getBlockState(pos.below());
                    canFracture = down.getValueOrElse(ReinforcedDeepslateBlock.CAN_FRACTURE, false);
                } else {
                    for (Map.Entry<Direction, BooleanProperty> entry : FireBlock.PROPERTY_BY_DIRECTION.entrySet()) {
                        if (state.getValue(entry.getValue())) {
                            BlockState offset = world.getBlockState(pos.relative(entry.getKey()));
                            canFracture = offset.getValueOrElse(ReinforcedDeepslateBlock.CAN_FRACTURE, false);
                        }

                        if (canFracture)
                            break;
                    }
                }

                if (canFracture) {
                    /*Optional<FracturedAlterworldPortal> optional2 = FracturedAlterworldPortal.getNewPortal(world, pos, Direction.Axis.X);
                    optional2.ifPresent(portal -> {
                        if (newState.getBlock() instanceof FireBlock) {
                            portal.createPortal(world);
                        }
                    });*/
                }
            }
        }
    }

    @Inject(method = "inPortalDimension", at = @At("HEAD"), cancellable = true)
    private static void gbw$allowNetherPortalsInAlterworld(Level world, CallbackInfoReturnable<Boolean> cir) {
        if (world.dimension() == GreatBigWorld.ALTERWORLD_KEY)
            cir.setReturnValue(true);
    }

    @ModifyExpressionValue(method = "isPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean gbw$lightableAlterworldPortals(boolean original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos pos, @Local BlockPos.MutableBlockPos mutable, @Local(ordinal = 1) Direction direction2) {
        BlockState state = world.getBlockState(mutable.set(pos).move(direction2));
        return original || state.is(TheAlterworldBlocks.REINFORCED_DEEPSLATE);
    }
}

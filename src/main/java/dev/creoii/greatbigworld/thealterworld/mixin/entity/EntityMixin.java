package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.PlanarFractureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract RandomSource getRandom();
    @Shadow public abstract BlockState getInBlockState();
    @Shadow private Level level;
    @Shadow private BlockPos blockPosition;
    @Shadow private Vec3 position;

    @Inject(method = "handlePortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    private void gbw$applyFracturedRealmEffect(CallbackInfo ci) {
        BlockState state = getInBlockState();
        if (state.is(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living) {
                if (state.getValue(AlterworldPortalBlock.FRACTURED)) {
                    living.removeVehicle();
                    int ticks = getRandom().nextIntBetweenInclusive(2480, 7440); /* 2 minutes to 6 minutes */
                    living.addEffect(new MobEffectInstance(TheAlterworldStatusEffects.PLANAR_FRACTURE, ticks, 0, false, false));
                    level.destroyBlock(blockPosition, false);

                    if (state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X) {
                        BlockPos.betweenClosed(blockPosition.getX() - 2, blockPosition.getY() - 1, blockPosition.getZ(), blockPosition.getX() + 2, blockPosition.getY() + 3, blockPosition.getZ()).forEach(pos -> {
                            BlockState state1 = level.getBlockState(pos);
                            if (state1.getBlock() instanceof ReinforcedDeepslateBlock) {
                                if (!ReinforcedDeepslateBlock.isFractured(state1)) {
                                    ReinforcedDeepslateBlock.fractureAt((ServerLevel) level, pos, ticks * 2);
                                }
                            }
                        });
                    } else {
                        BlockPos.betweenClosed(blockPosition.getX(), blockPosition.getY() - 1, blockPosition.getZ() - 2, blockPosition.getX(), blockPosition.getY() + 3, blockPosition.getZ() + 2).forEach(pos -> {
                            BlockState state1 = level.getBlockState(pos);
                            if (state1.getBlock() instanceof ReinforcedDeepslateBlock) {
                                if (!ReinforcedDeepslateBlock.isFractured(state1)) {
                                    ReinforcedDeepslateBlock.fractureAt((ServerLevel) level, pos, ticks * 2);
                                }
                            }
                        });
                    }

                    if (level instanceof ServerLevel serverWorld && living instanceof Player player) {
                        PlanarFractureManager manager = PlanarFractureManager.getServerState(serverWorld.getServer());
                        manager.setReturnPos(player, position);
                    }
                } else {
                    if (living.hasEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE)) {
                        living.removeVehicle();
                        living.removeEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE);
                    }
                }
            }
        } else if (state.is(Blocks.NETHER_PORTAL) || state.is(Blocks.END_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living && living.hasEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE)) {
                ci.cancel();
            }
        }
    }
}

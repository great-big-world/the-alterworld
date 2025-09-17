package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.PlanarFractureManager;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Random getRandom();
    @Shadow public abstract BlockState getBlockStateAtPos();
    @Shadow private World world;
    @Shadow private BlockPos blockPos;
    @Shadow private Vec3d pos;
    @Shadow public abstract UUID getUuid();
    @Shadow public abstract boolean isPlayer();

    @Inject(method = "tickPortalTeleportation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"), cancellable = true)
    private void gbw$applyFracturedRealmEffect(CallbackInfo ci) {
        BlockState state = getBlockStateAtPos();
        if (state.isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living) {
                if (state.get(AlterworldPortalBlock.FRACTURED)) {
                    living.dismountVehicle();
                    int ticks = getRandom().nextBetween(620, 3620);
                    living.addStatusEffect(new StatusEffectInstance(TheAlterworldStatusEffects.PLANAR_FRACTURE, ticks /* 30 seconds to 3 minutes */, 0, false, false));
                    world.breakBlock(blockPos, false);

                    if (state.get(Properties.HORIZONTAL_AXIS) == Direction.Axis.X) {
                        BlockPos.iterate(blockPos.getX() - 2, blockPos.getY() - 1, blockPos.getZ(), blockPos.getX() + 2, blockPos.getY() + 3, blockPos.getZ()).forEach(pos -> {
                            BlockState state1 = world.getBlockState(pos);
                            if (state1.getBlock() instanceof ReinforcedDeepslateBlock reinforcedDeepslateBlock) {
                                if (!ReinforcedDeepslateBlock.isFractured(state1))
                                    world.setBlockState(pos, TheAlterworldBlocks.REINFORCED_DEEPSLATE.getDefaultState().with(ReinforcedDeepslateBlock.FRACTURE, 8), 18);
                            }
                        });
                    } else {
                        BlockPos.iterate(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ() - 2, blockPos.getX(), blockPos.getY() + 3, blockPos.getZ() + 2).forEach(pos -> {
                            BlockState state1 = world.getBlockState(pos);
                            if (state1.getBlock() instanceof ReinforcedDeepslateBlock reinforcedDeepslateBlock) {
                                if (!ReinforcedDeepslateBlock.isFractured(state1))
                                    world.setBlockState(pos, TheAlterworldBlocks.REINFORCED_DEEPSLATE.getDefaultState().with(ReinforcedDeepslateBlock.FRACTURE, 8), 18);
                            }
                        });
                    }

                    if (world instanceof ServerWorld serverWorld && living instanceof PlayerEntity player) {
                        PlanarFractureManager manager = PlanarFractureManager.getServerState(serverWorld.getServer());
                        manager.setReturnPos(player, pos);
                    }
                } else {
                    if (living.hasStatusEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE)) {
                        living.dismountVehicle();
                        living.removeStatusEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE);
                    }
                }
            }
        } else if (state.isOf(Blocks.NETHER_PORTAL) || state.isOf(Blocks.END_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living && living.hasStatusEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void gbw$cleanPreviousDimensions(Entity.RemovalReason reason, CallbackInfo ci) {
        if (reason.shouldDestroy() && world.getServer() != null && !isPlayer()) {
            PreviousDimensionManager manager = PreviousDimensionManager.getServerState(world.getServer());
            manager.remove(getUuid());
        }
    }
}

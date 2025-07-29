package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.TheAlterworld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.PlanarFractureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onStatusEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"))
    private void gbw$removeFracturedRealmEffect(Collection<StatusEffectInstance> effects, CallbackInfo ci, @Local StatusEffectInstance statusEffectInstance) {
        if (statusEffectInstance.equals(TheAlterworldStatusEffects.PLANAR_FRACTURE) && getWorld().getRegistryKey() == GreatBigWorld.ALTERWORLD_KEY) {
            TeleportTarget target = createTeleportTarget((ServerWorld) getWorld(), this, getBlockPos());
            if (target != null) {
                ServerWorld serverWorld2 = target.world();
                if (getWorld().getServer().isWorldAllowed(serverWorld2) && (serverWorld2.getRegistryKey() == getWorld().getRegistryKey() || canTeleportBetween(getWorld(), serverWorld2))) {
                    teleportTo(target);

                    PlanarFractureManager manager = PlanarFractureManager.getServerState(serverWorld2.getServer());
                    manager.clearReturnPos((LivingEntity) (Object) this);
                }
            }
        }
    }

    @Unique
    public @Nullable TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos) {
        ServerWorld serverWorld = world.getServer().getWorld(World.OVERWORLD);
        if (serverWorld == null) {
            return null;
        } else {
            BlockPos blockPos = serverWorld.getSpawnPos();
            Set<PositionFlag> set = PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT);
            if (entity instanceof LivingEntity living) {
                PlanarFractureManager manager = PlanarFractureManager.getServerState(serverWorld.getServer());

                Vec3d returnPos = manager.getReturnPos(living);
                if (returnPos != null) {
                    return new TeleportTarget(serverWorld, returnPos, Vec3d.ZERO, 0f, 0f, set, TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET));
                }

                if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                    return serverPlayerEntity.getRespawnTarget(false, TeleportTarget.NO_OP);
                }
            }
            return new TeleportTarget(serverWorld, entity.getWorldSpawnPos(serverWorld, blockPos).toBottomCenterPos(), Vec3d.ZERO, 0f, 0f, set, TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET));
        }
    }
}

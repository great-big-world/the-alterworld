package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.PlanarFractureManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "onEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"))
    private void gbw$removeFracturedRealmEffect(Collection<MobEffectInstance> effects, CallbackInfo ci, @Local MobEffectInstance statusEffectInstance) {
        if (statusEffectInstance.is(TheAlterworldStatusEffects.PLANAR_FRACTURE) && level().dimension() == GreatBigWorld.ALTERWORLD_KEY) {
            TeleportTransition target = createTeleportTarget((ServerLevel) level(), this, blockPosition());
            if (target != null) {
                ServerLevel serverWorld2 = target.newLevel();
                if (/*level().getServer().isEnterableWithPortal(serverWorld2) && */(serverWorld2.dimension() == level().dimension() || canTeleport(level(), serverWorld2))) {
                    teleport(target);

                    PlanarFractureManager manager = PlanarFractureManager.getServerState(serverWorld2.getServer());
                    manager.clearReturnPos((LivingEntity) (Object) this);
                }
            }
        }
    }

    @Unique
    public @Nullable TeleportTransition createTeleportTarget(ServerLevel world, Entity entity, BlockPos pos) {
        ServerLevel serverWorld = world.getServer().getLevel(Level.OVERWORLD);
        if (serverWorld == null) {
            return null;
        } else {
            BlockPos blockPos = serverWorld.getRespawnData().pos();
            Set<Relative> set = Relative.union(Relative.DELTA, Relative.ROTATION);
            if (entity instanceof LivingEntity living) {
                PlanarFractureManager manager = PlanarFractureManager.getServerState(serverWorld.getServer());

                Vec3 returnPos = manager.getReturnPos(living);
                if (returnPos != null) {
                    return new TeleportTransition(serverWorld, returnPos, Vec3.ZERO, 0f, 0f, set, TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET));
                }

                if (entity instanceof ServerPlayer serverPlayerEntity) {
                    return serverPlayerEntity.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
                }
            }
            return new TeleportTransition(serverWorld, entity.adjustSpawnLocation(serverWorld, blockPos).getBottomCenter(), Vec3.ZERO, 0f, 0f, set, TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET));
        }
    }
}

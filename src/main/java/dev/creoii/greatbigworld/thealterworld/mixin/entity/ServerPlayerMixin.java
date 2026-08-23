package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    @Shadow
    public abstract void removeVehicle();

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @WrapWithCondition(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V"))
    private boolean gbw$dontSetSpawnWhenFractured(ServerPlayer instance, ServerPlayer.RespawnConfig respawnConfig, boolean bl) {
        return !instance.hasEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE);
    }

    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getLevelData()Lnet/minecraft/world/level/storage/LevelData;"))
    private void gbw$clearPlanarFractureInOverworld(TeleportTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
        if (hasEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE)) {
            if (!isCreative())
                removeVehicle();

            if (teleportTransition.newLevel().dimension() != GreatBigWorld.ALTERWORLD_KEY) {
                removeEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE);
            }
        }
    }
}

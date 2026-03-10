package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @WrapWithCondition(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V"))
    private boolean gbw$dontSetSpawnWhenFractured(ServerPlayer instance, ServerPlayer.RespawnConfig respawnConfig, boolean bl) {
        return !instance.hasEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE);
    }
}

package dev.creoii.greatbigworld.thealterworld.mixin.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {
    @Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
    private void gbw$removeWanderingTraderFromOverworld(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
        if (world.getRegistryKey() == ServerWorld.OVERWORLD)
            ci.cancel();
    }
}

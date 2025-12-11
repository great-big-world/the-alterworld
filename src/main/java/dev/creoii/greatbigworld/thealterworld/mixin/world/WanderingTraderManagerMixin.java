package dev.creoii.greatbigworld.thealterworld.mixin.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderSpawner.class)
public class WanderingTraderManagerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void gbw$removeWanderingTraderFromOverworld(ServerLevel world, boolean spawnMonsters, CallbackInfo ci) {
        if (world.dimension() == ServerLevel.OVERWORLD)
            ci.cancel();
    }
}

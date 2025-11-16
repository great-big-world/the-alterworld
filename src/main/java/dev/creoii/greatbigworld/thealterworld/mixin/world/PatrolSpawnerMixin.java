package dev.creoii.greatbigworld.thealterworld.mixin.world;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {
    @Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
    private void gbw$cancelPatrolsInOverworld(ServerWorld world, boolean spawnMonsters, CallbackInfo ci) {
        if (!world.getRegistryKey().equals(GreatBigWorld.ALTERWORLD_KEY))
            ci.cancel();
    }
}

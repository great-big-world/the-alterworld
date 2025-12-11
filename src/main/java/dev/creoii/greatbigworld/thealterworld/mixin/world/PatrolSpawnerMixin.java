package dev.creoii.greatbigworld.thealterworld.mixin.world;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void gbw$cancelPatrolsInOverworld(ServerLevel world, boolean spawnMonsters, CallbackInfo ci) {
        if (!world.dimension().equals(GreatBigWorld.ALTERWORLD_KEY))
            ci.cancel();
    }
}

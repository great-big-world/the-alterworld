package dev.creoii.greatbigworld.thealterworld.mixin.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkGeneratorSettings.class)
public class ChunkGeneratorSettingsMixin implements ExtendedChunkGeneratorSettings {
    @Unique
    private int gbw$lavaHeight = -54;

    @ModifyExpressionValue(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;createSurfaceSettings(Lnet/minecraft/registry/Registerable;ZZ)Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;", ordinal = 0))
    private static ChunkGeneratorSettings gbw$lowerOverworldLavaHeight(ChunkGeneratorSettings original) {
        ((ExtendedChunkGeneratorSettings) (Object) original).gbw$setLavaHeight(-182);
        return original;
    }

    @Override
    public int gbw$getLavaHeight() {
        return gbw$lavaHeight;
    }

    @Override
    public void gbw$setLavaHeight(int lavaHeight) {
        this.gbw$lavaHeight = lavaHeight;
    }
}

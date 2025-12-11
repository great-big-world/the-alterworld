package dev.creoii.greatbigworld.thealterworld.mixin.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoiseGeneratorSettings.class)
public class ChunkGeneratorSettingsMixin implements ExtendedChunkGeneratorSettings {
    @Unique
    private int gbw$lavaHeight = -54;

    @ModifyExpressionValue(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;overworld(Lnet/minecraft/data/worldgen/BootstrapContext;ZZ)Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;", ordinal = 0))
    private static NoiseGeneratorSettings gbw$lowerOverworldLavaHeight(NoiseGeneratorSettings original) {
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

package dev.creoii.greatbigworld.thealterworld.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NoiseChunkGenerator.class)
public class NoiseChunkGeneratorMixin {
    @ModifyConstant(method = "createFluidLevelSampler", constant = @Constant(intValue = -54))
    private static int gbw$lowerLavaHeight(int constant, @Local(argsOnly = true) ChunkGeneratorSettings settings) {
        return ((ExtendedChunkGeneratorSettings) (Object) settings).gbw$getLavaHeight();
    }
}

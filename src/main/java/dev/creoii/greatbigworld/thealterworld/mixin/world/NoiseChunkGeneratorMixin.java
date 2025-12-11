package dev.creoii.greatbigworld.thealterworld.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseChunkGeneratorMixin {
    @ModifyConstant(method = "createFluidPicker", constant = @Constant(intValue = -54))
    private static int gbw$lowerLavaHeight(int constant, @Local(argsOnly = true) NoiseGeneratorSettings settings) {
        return ((ExtendedChunkGeneratorSettings) (Object) settings).gbw$getLavaHeight();
    }
}

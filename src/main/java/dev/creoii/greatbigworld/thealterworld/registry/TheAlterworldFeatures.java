package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.world.feature.SculkPatchFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SculkPatchFeatureConfig;

public class TheAlterworldFeatures {
    public static final Feature<SculkPatchFeatureConfig> SCULK_PATCH = new SculkPatchFeature(SculkPatchFeatureConfig.CODEC);

    public static void register() {
        Registry.register(Registries.FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "sculk_patch"), SCULK_PATCH);
    }
}

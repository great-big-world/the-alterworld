package dev.creoii.greatbigworld.thealterworld.mixin.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public abstract class VanillaBiomeParametersMixin {
    @Mutable @Shadow @Final private RegistryKey<Biome>[][] commonBiomes;
    @Mutable @Shadow @Final private RegistryKey<Biome>[][] uncommonBiomes;
    @Mutable @Shadow @Final private RegistryKey<Biome>[][] nearMountainBiomes;
    @Mutable @Shadow @Final private RegistryKey<Biome>[][] specialNearMountainBiomes;
    @Mutable @Shadow @Final private RegistryKey<Biome>[][] windsweptBiomes;
    @Shadow @Final private MultiNoiseUtil.ParameterRange[] temperatureParameters;
    @Shadow @Final private MultiNoiseUtil.ParameterRange defaultParameter;
    @Shadow @Final private MultiNoiseUtil.ParameterRange nearInlandContinentalness;
    @Shadow @Final private MultiNoiseUtil.ParameterRange farInlandContinentalness;
    @Shadow @Final private MultiNoiseUtil.ParameterRange[] erosionParameters;
    @Shadow protected abstract void writeBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome);

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$replaceOldGrowthBirchForest(CallbackInfo ci) {
        commonBiomes = new RegistryKey[][]{{BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.TAIGA}, {BiomeKeys.PLAINS, BiomeKeys.PLAINS, BiomeKeys.FOREST, BiomeKeys.TAIGA, BiomeKeys.TAIGA}, {BiomeKeys.PLAINS, BiomeKeys.PLAINS, BiomeKeys.FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.BIRCH_FOREST}, {BiomeKeys.SAVANNA, BiomeKeys.SAVANNA, BiomeKeys.FOREST, BiomeKeys.JUNGLE, BiomeKeys.JUNGLE}, {BiomeKeys.DESERT, BiomeKeys.DESERT, BiomeKeys.DESERT, BiomeKeys.DESERT, BiomeKeys.DESERT}};
        uncommonBiomes = new RegistryKey[][]{{BiomeKeys.SNOWY_TAIGA, null, BiomeKeys.SNOWY_TAIGA, null, null}, {null, null, null, null, BiomeKeys.TAIGA}, {BiomeKeys.PLAINS, null, null, BiomeKeys.BIRCH_FOREST, null}, {null, null, BiomeKeys.PLAINS, BiomeKeys.JUNGLE, BiomeKeys.JUNGLE}, {null, null, null, null, null}};
        nearMountainBiomes = new RegistryKey[][]{{BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.SNOWY_TAIGA}, {BiomeKeys.MEADOW, BiomeKeys.MEADOW, BiomeKeys.FOREST, BiomeKeys.TAIGA, BiomeKeys.TAIGA}, {BiomeKeys.MEADOW, BiomeKeys.MEADOW, BiomeKeys.MEADOW, BiomeKeys.MEADOW, BiomeKeys.FOREST}, {BiomeKeys.SAVANNA, BiomeKeys.SAVANNA, BiomeKeys.FOREST, BiomeKeys.FOREST, BiomeKeys.JUNGLE}, {BiomeKeys.BADLANDS, BiomeKeys.BADLANDS, BiomeKeys.BADLANDS, BiomeKeys.WOODED_BADLANDS, BiomeKeys.WOODED_BADLANDS}};
        specialNearMountainBiomes = new RegistryKey[][]{{BiomeKeys.SNOWY_TAIGA, null, null, null, null}, {null, null, BiomeKeys.MEADOW, BiomeKeys.MEADOW, BiomeKeys.TAIGA}, {BiomeKeys.PLAINS, BiomeKeys.FOREST, BiomeKeys.FOREST, BiomeKeys.BIRCH_FOREST, null}, {null, null, null, null, null}, {BiomeKeys.ERODED_BADLANDS, BiomeKeys.ERODED_BADLANDS, null, null, null}};
        windsweptBiomes = new RegistryKey[][]{{BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_FOREST}, {BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_FOREST}, {BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_FOREST}, {null, null, null, null, null}, {null, null, null, null, null}};
    }

    @Redirect(method = "writeLowBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 2))
    private void gbw$removeMangroveSwampLow(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[3], temperatureParameters[4]), defaultParameter, MultiNoiseUtil.ParameterRange.combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0f, BiomeKeys.SWAMP);
    }

    @Redirect(method = "writeMidBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 2))
    private void gbw$removeMangroveSwampMid(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[3], temperatureParameters[4]), defaultParameter, MultiNoiseUtil.ParameterRange.combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0f, BiomeKeys.SWAMP);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 9))
    private void gbw$removeMangroveSwampValley(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[3], temperatureParameters[4]), defaultParameter, MultiNoiseUtil.ParameterRange.combine(nearInlandContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0f, BiomeKeys.SWAMP);
    }

    @Inject(method = "getBiomeOrWindsweptSavanna", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$removeWindsweptSavanna(int temperature, int humidity, MultiNoiseUtil.ParameterRange weirdness, RegistryKey<Biome> biomeKey, CallbackInfoReturnable<RegistryKey<Biome>> cir) {
        cir.setReturnValue(BiomeKeys.SAVANNA);
    }
}

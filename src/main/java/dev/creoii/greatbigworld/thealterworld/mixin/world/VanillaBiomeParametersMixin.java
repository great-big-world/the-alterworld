package dev.creoii.greatbigworld.thealterworld.mixin.world;

import com.mojang.datafixers.util.Pair;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;

@Mixin(OverworldBiomeBuilder.class)
public abstract class VanillaBiomeParametersMixin {
    @Mutable @Shadow @Final private ResourceKey<Biome>[][] MIDDLE_BIOMES;
    @Mutable @Shadow @Final private ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT;
    @Mutable @Shadow @Final private ResourceKey<Biome>[][] PLATEAU_BIOMES;
    @Mutable @Shadow @Final private ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT;
    @Mutable @Shadow @Final private ResourceKey<Biome>[][] SHATTERED_BIOMES;
    @Shadow @Final private Climate.Parameter[] temperatures;
    @Shadow @Final private Climate.Parameter FULL_RANGE;
    @Shadow @Final private Climate.Parameter nearInlandContinentalness;
    @Shadow @Final private Climate.Parameter farInlandContinentalness;
    @Shadow @Final private Climate.Parameter[] erosions;
    @Shadow protected abstract void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome);

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$replaceOldGrowthBirchForest(CallbackInfo ci) {
        //MIDDLE_BIOMES = new ResourceKey[][]{{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA}, {Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.TAIGA}, {Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST}, {Biomes.DESERT, Biomes.PLAINS, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE}, {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}};
        //MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{{Biomes.SNOWY_TAIGA, null, Biomes.SNOWY_TAIGA, null, null}, {null, null, null, null, Biomes.TAIGA}, {Biomes.PLAINS, null, null, Biomes.BIRCH_FOREST, null}, {null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.JUNGLE}, {null, null, null, null, null}};
        //PLATEAU_BIOMES = new ResourceKey[][]{{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA}, {Biomes.MEADOW, Biomes.MEADOW, Biomes.BIRCH_FOREST, Biomes.TAIGA, Biomes.TAIGA}, {Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST}, {Biomes.DESERT, Biomes.PLAINS, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE}, {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}};
        //PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{{Biomes.SNOWY_TAIGA, null, null, null, null}, {null, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.TAIGA}, {Biomes.PLAINS, Biomes.FOREST, Biomes.FOREST, Biomes.BIRCH_FOREST, null}, {null, null, null, null, null}, {Biomes.DESERT, Biomes.DESERT, null, null, null}};
        //SHATTERED_BIOMES = new ResourceKey[][]{{Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA}, {Biomes.MEADOW, Biomes.MEADOW, Biomes.BIRCH_FOREST, Biomes.TAIGA, Biomes.TAIGA}, {Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.BIRCH_FOREST}, {null, null, null, null, null}, {null, null, null, null, null}};
    }

    /*@Redirect(method = "addLowSlice", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 2))
    private void gbw$removeMangroveSwampLow(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0f, Biomes.SWAMP);
    }

    @Redirect(method = "addMidSlice", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 2))
    private void gbw$removeMangroveSwampMid(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0f, Biomes.SWAMP);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 9))
    private void gbw$removeMangroveSwampValley(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[3], temperatures[4]), FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0f, Biomes.SWAMP);
    }

    @Inject(method = "maybePickWindsweptSavannaBiome", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$removeWindsweptSavanna(int temperature, int humidity, Climate.Parameter weirdness, ResourceKey<Biome> biomeKey, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        cir.setReturnValue(Biomes.FOREST);
    }

    @Redirect(method = "addUndergroundBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addUndergroundBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 1))
    private void gbw$removeLushCaves(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
    }

    @Inject(method = "pickBadlandsBiome", at = @At("HEAD"), cancellable = true)
    private void gbw$removeBadlands(int humidity, Climate.Parameter weirdness, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        cir.setReturnValue(Biomes.DESERT);
    }*/
}

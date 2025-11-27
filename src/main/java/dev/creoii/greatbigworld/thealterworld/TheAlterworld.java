package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.data.Mappings;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.registry.*;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import java.util.Optional;
import java.util.function.Predicate;

public class TheAlterworld implements ModInitializer {
    @Override
    public void onInitialize() {
        TheAlterworldBlocks.register();
        TheAlterworldDataComponentTypes.register();
        TheAlterworldItems.register();
        TheAlterworldBlockEntityTypes.register();
        TheAlterworldStatusEffects.register();
        TheAlterworldSoundEvents.register();
        TheAlterworldStructureTriggerDataTypes.register();
        TheAlterworldStructureTriggers.register();

        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            Optional<Registry<Mappings>> optionalRegistry = minecraftServer.getRegistryManager().getOptional(GBWRegistries.MAPPINGS_KEY);
            Optional<Registry<ChunkGeneratorSettings>> optionalRegistry2 = minecraftServer.getRegistryManager().getOptional(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
            if (optionalRegistry.isPresent() && optionalRegistry2.isPresent()) {
                Mappings mappings = optionalRegistry.get().get(Identifier.of(GreatBigWorld.NAMESPACE, "world_lava_heights"));
                if (mappings == null)
                    return;

                optionalRegistry2.get().getEntrySet().forEach(entry -> {
                    Mappings.Value value = mappings.getValue(minecraftServer.getRegistryManager(), entry.getKey().getValue());
                    if (value.type() == Mappings.Value.PrimitiveType.STRING)
                        return;
                    ((ExtendedChunkGeneratorSettings) (Object) entry.getValue()).gbw$setLavaHeight(value.getAsNumber().intValue());
                });
            }
        });
    }

    public static Predicate<BiomeSelectionContext> foundInAlterworld() {
        return context -> context.canGenerateIn(GreatBigWorld.ALTERWORLD_OPTIONS);
    }

    public static Predicate<BiomeSelectionContext> foundInOverworldLike() {
        return context -> context.canGenerateIn(GreatBigWorld.ALTERWORLD_OPTIONS) || context.canGenerateIn(DimensionOptions.OVERWORLD);
    }
}

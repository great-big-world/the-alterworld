package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.data.Mappings;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.registry.*;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
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
        TheAlterworldGameEvents.register();
        TheAlterworldSoundEvents.register();
        TheAlterworldStructureTriggerDataTypes.register();
        TheAlterworldStructureTriggers.register();
        TheAlterworldParticleTypes.register();

        PayloadTypeRegistry.playS2C().register(ReinforcedDeepslateBlock.FractureS2C.PACKET_ID, ReinforcedDeepslateBlock.FractureS2C.PACKET_CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            Optional<Registry<Mappings>> optionalRegistry = minecraftServer.registryAccess().lookup(GBWRegistries.MAPPINGS_KEY);
            Optional<Registry<NoiseGeneratorSettings>> optionalRegistry2 = minecraftServer.registryAccess().lookup(Registries.NOISE_SETTINGS);
            if (optionalRegistry.isPresent() && optionalRegistry2.isPresent()) {
                Mappings mappings = optionalRegistry.get().getValue(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "world_lava_heights"));
                if (mappings == null)
                    return;

                optionalRegistry2.get().entrySet().forEach(entry -> {
                    Mappings.Value value = mappings.getValue(minecraftServer.registryAccess(), entry.getKey().identifier());
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
        return context -> context.canGenerateIn(GreatBigWorld.ALTERWORLD_OPTIONS) || context.canGenerateIn(LevelStem.OVERWORLD);
    }

    public static boolean disableVariantsInOverworld(EntitySpawnReason reason) {
        return reason == EntitySpawnReason.NATURAL || reason == EntitySpawnReason.BREEDING || reason == EntitySpawnReason.TRIAL_SPAWNER || reason == EntitySpawnReason.SPAWNER || reason == EntitySpawnReason.SPAWN_ITEM_USE || reason == EntitySpawnReason.CONVERSION || reason == EntitySpawnReason.BUCKET || reason == EntitySpawnReason.DISPENSER || reason == EntitySpawnReason.REINFORCEMENT || reason == EntitySpawnReason.JOCKEY || reason == EntitySpawnReason.EVENT || reason == EntitySpawnReason.CHUNK_GENERATION || reason == EntitySpawnReason.STRUCTURE;
    }
}

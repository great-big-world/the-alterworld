package dev.creoii.greatbigworld.thealterworld;

import com.google.common.collect.Maps;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.data.Mappings;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.registry.*;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TheAlterworld implements ModInitializer {
    private static final Map<RelicStructureType, List<Item>> RELICS = Maps.newEnumMap(RelicStructureType.class);

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

        final List<RegistryKey<LootTable>> ARCHAEOLOGY_COMMON_LOOT_TABLES = List.of(LootTables.DESERT_WELL_ARCHAEOLOGY, LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY);
        final List<RegistryKey<LootTable>> ARCHAEOLOGY_RARE_LOOT_TABLES = List.of(LootTables.DESERT_PYRAMID_ARCHAEOLOGY, LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY);
        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
            if (lootTableSource.isBuiltin()) {
                if (ARCHAEOLOGY_COMMON_LOOT_TABLES.contains(registryKey)) {
                    builder.modifyPools(builder1 -> {
                        builder1.with(ItemEntry.builder(TheAlterworldItems.ANCIENT_BRICKS));
                    });
                } else if (ARCHAEOLOGY_RARE_LOOT_TABLES.contains(registryKey)) {
                    builder.modifyPools(builder1 -> {
                        builder1.with(ItemEntry.builder(TheAlterworldItems.ANCIENT_BRICKS));
                    });
                }
            }
        });

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

    public static void registerRelic(RelicStructureType type, Item relicItem) {
        if (RELICS.containsKey(type)) {
            RELICS.get(type).add(relicItem);
        } else {
            List<Item> relics = new ArrayList<>();
            relics.add(relicItem);
            RELICS.put(type, relics);
        }
    }

    @Nullable
    public static Item getRandomRelic(RelicStructureType type, Random random) {
        List<Item> relics = RELICS.get(type);
        if (relics.isEmpty())
            return null;
        return relics.get(random.nextInt(relics.size()));
    }

    public enum RelicStructureType {
        JUNGLE_TEMPLE,
        DESERT_TEMPLE,
        SWAMP_TEMPLE,
        ICE_TEMPLE
    }
}

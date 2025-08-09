package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldItems;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStructureTriggers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public class TheAlterworld implements ModInitializer {
    @Override
    public void onInitialize() {
        TheAlterworldBlocks.register();
        TheAlterworldItems.register();
        TheAlterworldStatusEffects.register();
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
    }
}

package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldItems;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

/**
 * Todo:
 * - Add Fractured Portal to temple structures
 * - Dont clear Fractured Portal on Milk drink
 */
public class TheAlterworld implements ModInitializer {
    public static final RegistryKey<World> ALTERWORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(GreatBigWorld.NAMESPACE, "the_alterworld"));;

    @Override
    public void onInitialize() {
        TheAlterworldBlocks.register();
        TheAlterworldItems.register();
        TheAlterworldStatusEffects.register();

        List<RegistryKey<LootTable>> ARCHAEOLOGY_COMMON_LOOT_TABLES = List.of(LootTables.DESERT_WELL_ARCHAEOLOGY, LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY);
        List<RegistryKey<LootTable>> ARCHAEOLOGY_RARE_LOOT_TABLES = List.of(LootTables.DESERT_PYRAMID_ARCHAEOLOGY, LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY);
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

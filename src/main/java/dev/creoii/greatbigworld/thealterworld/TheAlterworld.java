package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.registry.GBWBlockEntityTypes;
import dev.creoii.greatbigworld.thealterworld.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.dimension.LevelStem;

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
        TheAlterworldNetworking.register();
        TheAlterworldEvents.register();

        if (GBWBlockEntityTypes.KNOWLEDGE_BLOCK != null)
            ((BlockEntityTypeAccessor) GBWBlockEntityTypes.KNOWLEDGE_BLOCK).getBlocks().add(TheAlterworldBlocks.REINFORCED_DEEPSLATE);
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

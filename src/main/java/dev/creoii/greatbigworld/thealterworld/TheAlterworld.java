package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldItems;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestType;

public class TheAlterworld implements ModInitializer {
    public static final RegistryKey<World> ALTERWORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(GreatBigWorld.NAMESPACE, "the_alterworld"));;
    public static RegistryKey<PointOfInterestType> ALTERWORLD_PORTAL_POI_KEY = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "alterworld_portal"));

    @Override
    public void onInitialize() {
        TheAlterworldBlocks.register();
        TheAlterworldItems.register();
        TheAlterworldStatusEffects.register();
        PointOfInterestHelper.register(ALTERWORLD_KEY.getValue(), 0, 1, TheAlterworldBlocks.ALTERWORLD_PORTAL);
    }
}

package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class TheAlterworldItems {
    public static Item ANCIENT_PEDESTAL;
    public static Item REINFORCED_DEEPSLATE;

    public static void register() {
        ANCIENT_PEDESTAL = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ancient_pedestal"), TheAlterworldBlocks.ANCIENT_PEDESTAL);
        REINFORCED_DEEPSLATE = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "reinforced_deepslate"), TheAlterworldBlocks.REINFORCED_DEEPSLATE);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.ENDER_EYE, ANCIENT_PEDESTAL);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.REINFORCED_DEEPSLATE, REINFORCED_DEEPSLATE);
        });
    }
}

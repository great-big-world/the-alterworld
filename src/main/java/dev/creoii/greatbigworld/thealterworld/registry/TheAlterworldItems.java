package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.architectsassembly.item.SlabItem;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class TheAlterworldItems {
    public static Item ANCIENT_MOSAIC;
    public static Item FRACTURED_ANCIENT_MOSAIC;
    public static Item ANCIENT_BRICKS;
    public static Item ANCIENT_BRICK_STAIRS;
    public static Item ANCIENT_BRICK_SLAB;
    public static Item ANCIENT_BRICK_WALL;
    public static Item ANCIENT_PEDESTAL;

    public static void register() {
        ANCIENT_MOSAIC = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_mosaic"), TheAlterworldBlocks.ANCIENT_MOSAIC);
        FRACTURED_ANCIENT_MOSAIC = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "fractured_ancient_mosaic"), TheAlterworldBlocks.FRACTURED_ANCIENT_MOSAIC);
        ANCIENT_BRICKS = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_bricks"), TheAlterworldBlocks.ANCIENT_BRICKS);
        ANCIENT_BRICK_STAIRS = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_brick_stairs"), TheAlterworldBlocks.ANCIENT_BRICK_STAIRS);
        ANCIENT_BRICK_SLAB = RegistryHelper.registerItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_brick_slab"), settings -> new SlabItem(TheAlterworldBlocks.ANCIENT_BRICK_SLAB, TheAlterworldBlocks.VERTICAL_ANCIENT_BRICK_SLAB, settings.useBlockPrefixedTranslationKey()));
        ANCIENT_BRICK_WALL = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_brick_wall"), TheAlterworldBlocks.ANCIENT_BRICK_WALL);
        ANCIENT_PEDESTAL = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_pedestal"), TheAlterworldBlocks.ANCIENT_PEDESTAL);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.REINFORCED_DEEPSLATE, ANCIENT_MOSAIC, FRACTURED_ANCIENT_MOSAIC, ANCIENT_BRICKS, ANCIENT_BRICK_STAIRS, ANCIENT_BRICK_SLAB, ANCIENT_BRICK_WALL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ANCIENT_PEDESTAL);
        });
    }
}

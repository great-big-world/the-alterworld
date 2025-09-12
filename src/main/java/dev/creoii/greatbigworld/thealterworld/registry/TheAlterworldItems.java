package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public final class TheAlterworldItems {
    public static Item ANCIENT_PEDESTAL;
    public static Item REINFORCED_DEEPSLATE;

    public static void register() {
        ANCIENT_PEDESTAL = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_pedestal"), TheAlterworldBlocks.ANCIENT_PEDESTAL);
        REINFORCED_DEEPSLATE = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "reinforced_deepslate"), TheAlterworldBlocks.REINFORCED_DEEPSLATE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Items.ENDER_EYE, ANCIENT_PEDESTAL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.REINFORCED_DEEPSLATE, REINFORCED_DEEPSLATE);
            entries.getDisplayStacks().replaceAll(stack -> {
                if (stack.isOf(Items.REINFORCED_DEEPSLATE))
                    return REINFORCED_DEEPSLATE.getDefaultStack();
                return stack;
            });
            entries.getSearchTabStacks().replaceAll(stack -> {
                if (stack.isOf(Items.REINFORCED_DEEPSLATE))
                    return REINFORCED_DEEPSLATE.getDefaultStack();
                return stack;
            });
        });
    }
}

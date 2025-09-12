package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;

public final class TheAlterworldBlocks {
    public static Block ALTERWORLD_PORTAL;
    public static Block ANCIENT_PEDESTAL;
    public static Block REINFORCED_DEEPSLATE;

    public static void register() {
        ALTERWORLD_PORTAL = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "alterworld_portal"), AlterworldPortalBlock::new, AbstractBlock.Settings.copy(Blocks.NETHER_PORTAL).luminance(state -> state.get(AlterworldPortalBlock.FRACTURED) ? 5 : 3));
        ANCIENT_PEDESTAL = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_pedestal"), AncientPedestalBlock::new, AbstractBlock.Settings.copy(Blocks.REINFORCED_DEEPSLATE).strength(30f, 30f).luminance(state -> state.get(AncientPedestalBlock.LIT) ? 11 : 0));
        REINFORCED_DEEPSLATE = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "reinforced_deepslate"), ReinforcedDeepslateBlock::new, AbstractBlock.Settings.copy(Blocks.REINFORCED_DEEPSLATE));
    }
}

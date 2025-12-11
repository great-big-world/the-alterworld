package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class TheAlterworldBlocks {
    public static Block ALTERWORLD_PORTAL;
    public static Block ANCIENT_PEDESTAL;
    public static Block REINFORCED_DEEPSLATE;

    public static void register() {
        ALTERWORLD_PORTAL = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "alterworld_portal"), AlterworldPortalBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL).lightLevel(state -> state.getValue(AlterworldPortalBlock.FRACTURED) ? 5 : 3));
        ANCIENT_PEDESTAL = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ancient_pedestal"), AncientPedestalBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.REINFORCED_DEEPSLATE).strength(30f, 30f).lightLevel(state -> state.getValue(AncientPedestalBlock.LIT) ? 11 : 0));
        REINFORCED_DEEPSLATE = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "reinforced_deepslate"), ReinforcedDeepslateBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.REINFORCED_DEEPSLATE));
    }
}

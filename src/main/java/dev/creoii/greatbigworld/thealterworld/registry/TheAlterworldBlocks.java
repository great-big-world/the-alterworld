package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.architectsassembly.block.VerticalSlabBlock;
import dev.creoii.greatbigworld.block.FacingBlock;
import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.block.AncientMosaicBlock;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;

public class TheAlterworldBlocks {
    public static Block ALTERWORLD_PORTAL;
    public static Block ANCIENT_MOSAIC;
    public static Block FRACTURED_ANCIENT_MOSAIC;
    public static Block ANCIENT_BRICKS;
    public static Block ANCIENT_BRICK_SLAB;
    public static Block VERTICAL_ANCIENT_BRICK_SLAB;
    public static Block ANCIENT_BRICK_STAIRS;
    public static Block ANCIENT_BRICK_WALL;
    public static Block ANCIENT_PEDESTAL;

    public static void register() {
        ALTERWORLD_PORTAL = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "alterworld_portal"), AlterworldPortalBlock::new, AbstractBlock.Settings.copy(Blocks.NETHER_PORTAL).luminance(state -> state.get(AlterworldPortalBlock.FRACTURED) ? 5 : 1));
        ANCIENT_MOSAIC = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_mosaic"), settings -> new AncientMosaicBlock(settings, false), AbstractBlock.Settings.copy(Blocks.END_STONE).strength(16f).mapColor(MapColor.TERRACOTTA_GREEN).luminance(state -> 5));
        FRACTURED_ANCIENT_MOSAIC = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "fractured_ancient_mosaic"), settings -> new AncientMosaicBlock(settings, true), AbstractBlock.Settings.copy(ANCIENT_MOSAIC).luminance(state -> 0));
        ANCIENT_BRICKS = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_bricks"), FacingBlock::new, AbstractBlock.Settings.copy(Blocks.END_STONE).mapColor(MapColor.TERRACOTTA_GREEN));
        ANCIENT_BRICK_STAIRS = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_brick_stairs"), settings -> new StairsBlock(ANCIENT_BRICKS.getDefaultState(), settings), AbstractBlock.Settings.copy(ANCIENT_BRICKS));
        ANCIENT_BRICK_SLAB = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_brick_slab"), SlabBlock::new, AbstractBlock.Settings.copy(ANCIENT_BRICKS));
        VERTICAL_ANCIENT_BRICK_SLAB = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "vertical_ancient_brick_slab"), VerticalSlabBlock::new, AbstractBlock.Settings.copy(ANCIENT_BRICKS));
        ANCIENT_BRICK_WALL = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_brick_wall"), WallBlock::new, AbstractBlock.Settings.copy(ANCIENT_BRICKS));
        ANCIENT_PEDESTAL = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_pedestal"), AncientPedestalBlock::new, AbstractBlock.Settings.copy(ANCIENT_BRICKS).strength(30f, 30f));
    }
}

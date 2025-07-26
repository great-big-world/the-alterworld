package dev.creoii.greatbigworld.thealterworld.block;

import net.minecraft.block.Block;

public class AncientMosaicBlock extends Block {
    private final boolean fractured;

    public AncientMosaicBlock(Settings settings, boolean fractured) {
        super(settings);
        this.fractured = fractured;
    }

    public boolean isFractured() {
        return fractured;
    }
}

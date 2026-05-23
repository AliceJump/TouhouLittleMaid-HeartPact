package net.minecraftforge.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

// NeoForge compatibility shim for Forge-compiled dependencies.
public class ItemStackHandler extends net.neoforged.neoforge.items.ItemStackHandler {
    public ItemStackHandler() {
        super();
    }

    public ItemStackHandler(int size) {
        super(size);
    }

    public ItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }
}

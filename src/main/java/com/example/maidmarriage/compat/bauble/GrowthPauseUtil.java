package com.example.maidmarriage.compat.bauble;

import com.example.maidmarriage.init.ModItems;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;

public final class GrowthPauseUtil {
    private GrowthPauseUtil() {
    }

    public static boolean hasSunflowerHairpin(EntityMaid maid) {
        var bauble = maid.getMaidBauble();

        for (int slot = 0; slot < 20; slot++) {
            ItemStack stack = bauble.getBaubleInSlot(slot);

            if (!stack.isEmpty()
                    && stack.getItem() == ModItems.SUNFLOWER_HAIRPIN.get()) {
                return true;
            }
        }

        return false;
    }
}

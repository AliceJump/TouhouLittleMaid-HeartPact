package com.example.maidmarriage.compat.bauble;

import com.example.maidmarriage.init.ModItems;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public final class GrowthPauseUtil {
    private GrowthPauseUtil() {
    }

    public static boolean hasSunflowerHairpin(EntityMaid maid) {
        return maid.getMaidBauble().containsItem(ModItems.SUNFLOWER_HAIRPIN.get());
    }
}

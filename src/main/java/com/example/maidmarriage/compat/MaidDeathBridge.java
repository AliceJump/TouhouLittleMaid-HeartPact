package com.example.maidmarriage.compat;

import com.example.maidmarriage.MaidMarriageMod;
import com.example.maidmarriage.entity.MaidChildEntity;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTombstoneEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MaidMarriageMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class MaidDeathBridge {
    private MaidDeathBridge() {
    }

    @SubscribeEvent
    public static void onMaidTombstone(MaidTombstoneEvent event) {
        if (MaidChildEntity.shouldStayChild(event.getMaid())) {
            event.setCanceled(true);
        }
    }
}


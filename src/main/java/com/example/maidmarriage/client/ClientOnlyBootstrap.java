package com.example.maidmarriage.client;

import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.fml.ModContainer;

public final class ClientOnlyBootstrap {

    private ClientOnlyBootstrap() {
    }

    public static void init(ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> new MaidMarriageConfigScreen(parent)
        );
    }
}
package com.example.maidmarriage.advancement;

import com.example.maidmarriage.MaidMarriageMod;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class ModAdvancements {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation ROOT = id("root");
    public static final ResourceLocation STAGE_WARM = id("stage_warm");
    public static final ResourceLocation STAGE_CLOSE = id("stage_close");
    public static final ResourceLocation HEART_PACT = id("heart_pact");
    public static final ResourceLocation MARRIAGE = id("marriage");
    public static final ResourceLocation FIRST_ROMANCE = id("first_romance");
    public static final ResourceLocation CHILDBIRTH = id("childbirth");
    public static final ResourceLocation ROMANCE_TEN = id("romance_ten");

    private ModAdvancements() {
    }

    private static void grant(ServerPlayer player, ResourceLocation id) {
        MinecraftServer server = player.server;

        if (server == null) {
            return;
        }

        AdvancementHolder advancement = server.getAdvancements().get(id);

        if (advancement == null) {
            LOGGER.warn("Cannot find advancement {} while granting to {}",
                    id,
                    player.getGameProfile().getName());
            return;
        }

        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementProgress progress =
                advancements.getOrStartProgress(advancement);

        if (progress.isDone()) {
            return;
        }

        for (String criterion : advancement.value().criteria().keySet()) {
            advancements.award(advancement, criterion);
        }
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                MaidMarriageMod.MOD_ID,
                path
        );
    }
}
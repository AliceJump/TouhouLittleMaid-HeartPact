package com.example.maidmarriage.client;

import com.example.maidmarriage.MaidMarriageMod;
import com.example.maidmarriage.config.ModConfigs;
import com.example.maidmarriage.network.ModNetworking;
import com.example.maidmarriage.network.payload.SubmitRomanceRhythmPayload;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = MaidMarriageMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class RomanceRhythmHud {
    private static final int FAIL_STREAK_LIMIT = 8;
    private static final int PEAK_LIMIT = 3;
    private static final float NOTE_SPEED = 228f;
    private static final long STEP_MS = 580L;
    private static final int MAX_SPAWN_BATCH_PER_TICK = 2;
    private static final float PERFECT_EARLY_WINDOW = 48f;
    private static final float PERFECT_LATE_WINDOW = 32f;
    private static final float GOOD_EARLY_WINDOW = 92f;
    private static final float GOOD_LATE_WINDOW = 58f;
    private static final float TYPEWRITER_CPS = 20f;
    private static final float TRACK_WIDTH = 360f;
    private static final float JUDGE_OFFSET = 92f;
    private static final float TRACK_START_X = -24f;
    private static final float NOTE_RENDER_SIZE = 12f;
    private static final float MISS_BUFFER = 12f;
    private static final float EIGHTH_SPACING_FACTOR = 0.82f;
    private static final float SKIP_SCORE = 0.18f;
    private static final float FAIL_SCORE = 0.0f;

    private static final int[] PATTERN_STEPS = {
            1, 0, 1, 0, 2, 0, 1, 0,
            1, 0, 2, 0, 1, 1, 0, 0,
            2, 0, 1, 0, 2, 0, 1, 0,
            1, 1, 0, 1, 2, 0, 0, 0
    };

    private static final String[] POOL_START = {
            "dialogue.maidmarriage.rhythm.start.1",
            "dialogue.maidmarriage.rhythm.start.2",
            "dialogue.maidmarriage.rhythm.start.3"
    };

    private static final String[] POOL_PERFECT = {
            "dialogue.maidmarriage.rhythm.perfect.1",
            "dialogue.maidmarriage.rhythm.perfect.2",
            "dialogue.maidmarriage.rhythm.perfect.3"
    };

    private static final String[] POOL_GOOD = {
            "dialogue.maidmarriage.rhythm.good.1",
            "dialogue.maidmarriage.rhythm.good.2",
            "dialogue.maidmarriage.rhythm.good.3"
    };

    private static final String[] POOL_MISS = {
            "dialogue.maidmarriage.rhythm.miss.1",
            "dialogue.maidmarriage.rhythm.miss.2",
            "dialogue.maidmarriage.rhythm.miss.3"
    };

    private static final String[] POOL_FAIL = {
            "dialogue.maidmarriage.rhythm.fail.1",
            "dialogue.maidmarriage.rhythm.fail.2"
    };

    private static final String[] POOL_PLAYER_PEAK = {
            "dialogue.maidmarriage.rhythm.player_peak.1",
            "dialogue.maidmarriage.rhythm.player_peak.2"
    };

    private static final String[] POOL_MAID_PEAK = {
            "dialogue.maidmarriage.rhythm.maid_peak.1",
            "dialogue.maidmarriage.rhythm.maid_peak.2"
    };

    private static final ResourceLocation PORTRAIT_SOFT_SMILE =
            ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "textures/gui/emotion/soft_smile.png");

    private static boolean active = false;
    private static UUID maidId = null;
    private static boolean sent = false;
    private static boolean lastMouseDown = false;

    private static final List<Note> notes = new ArrayList<>();

    private static int missStreak = 0;
    private static int combo = 0;

    private static String judge = "";
    private static String fullLine = I18n.get("dialogue.maidmarriage.longing_wait");
    private static String shownLine = "";

    private static float typeProgress = 0f;

    private static String cachedMaidName = "";
    private static String cachedMasterName = "";

    private static ResourceLocation currentPortrait = PORTRAIT_SOFT_SMILE;

    private static float player = 10f;
    private static float maid = 15f;

    private static int playerPeak = 0;
    private static int maidPeak = 0;

    private static int perfectHits = 0;
    private static int goodHits = 0;
    private static int misses = 0;
    private static int maxCombo = 0;

    private static int patternIndex = 0;

    private static long lastMs = 0L;
    private static long stepTimer = 0L;

    private RomanceRhythmHud() {
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        if (!active) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {
            return;
        }

        if (mc.screen != null) {
            return;
        }

        long now = Util.getMillis();
        long rawDt = Math.max(0L, now - lastMs);

        lastMs = now;

        long dt = Math.min(rawDt, 50L);

        updateTypewriter(dt);

        if (ModConfigs.rhythmAlwaysSkip()) {
            finishAndSend(SKIP_SCORE);
            return;
        }

        updateNotes(dt);

        if (!active) {
            return;
        }

        if (consumeHitInput(mc)) {
            hit();

            if (!active) {
                return;
            }
        }

        if (consumeSkipClick(mc)) {
            finishAndSend(SKIP_SCORE);
            return;
        }

        player = clamp(player - 0.9f * (dt / 1000f), 0f, 100f);
        maid = clamp(maid - 0.6f * (dt / 1000f), 0f, 100f);

        checkPeakOrEnd();
    }

    @SubscribeEvent
    public static void render(RenderGuiLayerEvent.Post event) {
        if (!active) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.screen != null) {
            return;
        }

        GuiGraphics g = event.getGuiGraphics();

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int panelW = 480;
        int panelH = 270;

        int x = w / 2 - panelW / 2;
        int y = Math.max(8, h / 2 - panelH / 2);

        g.fill(x, y, x + panelW, y + panelH, 0xEE12101A);
    }

    @SubscribeEvent
    public static void hideVanillaHudWhenActive(RenderGuiLayerEvent.Pre event) {
        if (!active) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.screen != null) {
            return;
        }

        var layer = event.getName();

        if (layer.equals(VanillaGuiLayers.CHAT)
                || layer.equals(VanillaGuiLayers.HOTBAR)) {
            event.setCanceled(true);
        }
    }

    private static boolean consumeHitInput(Minecraft mc) {
        return RhythmKeyMappings.RHYTHM_HIT.consumeClick();
    }

    private static void updateNotes(long dt) {
    }

    private static void updateTypewriter(long dt) {
    }

    private static void hit() {
    }

    private static void checkPeakOrEnd() {
    }

    private static void finishAndSend(float score) {
    }

    private static boolean consumeSkipClick(Minecraft mc) {
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int panelW = 480;
        int panelH = 270;

        int x = w / 2 - panelW / 2;
        int y = Math.max(8, h / 2 - panelH / 2);

        int skipW = 52;
        int skipH = 14;

        int skipX = x + panelW - skipW - 8;
        int skipY = y + 4;

        double mouseGuiX = mc.mouseHandler.xpos() * w / (double) mc.getWindow().getScreenWidth();
        double mouseGuiY = mc.mouseHandler.ypos() * h / (double) mc.getWindow().getScreenHeight();

        boolean inBox = mouseGuiX >= skipX && mouseGuiX <= skipX + skipW
                && mouseGuiY >= skipY && mouseGuiY <= skipY + skipH;

        boolean down = GLFW.glfwGetMouseButton(
                mc.getWindow().getWindow(),
                GLFW.GLFW_MOUSE_BUTTON_LEFT
        ) == GLFW.GLFW_PRESS;

        boolean clicked = inBox && down && !lastMouseDown;

        lastMouseDown = down;

        return clicked;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static final class Note {
        private float x;
        private final boolean eighth;

        private Note(float x, boolean eighth) {
            this.x = x;
            this.eighth = eighth;
        }
    }
}
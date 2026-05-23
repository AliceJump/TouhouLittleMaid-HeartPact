package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

/**
 * 关键剧情推进动作上报包。
 *
 * <p>用于把 Gal 剧情里的“表白接受”“打开女仆主面板”“提交婚礼誓约”这些关键节点
 * 显式上报给服务端处理，避免把关系状态只留在客户端演出层。
 */
public class StoryProgressActionPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "story_progress_action");
    public static final CustomPacketPayload.Type<StoryProgressActionPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, StoryProgressActionPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> StoryProgressActionPayload.encode(msg, buf),
            StoryProgressActionPayload::decode);

    @Nullable
    private final UUID maidUuid;
    private final String actionId;

    public StoryProgressActionPayload(@Nullable UUID maidUuid, String actionId) {
        this.maidUuid = maidUuid;
        this.actionId = actionId == null ? "" : actionId;
    }

    @Nullable
    public UUID maidUuid() {
        return maidUuid;
    }

    public String actionId() {
        return actionId;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(StoryProgressActionPayload msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.maidUuid != null);
        if (msg.maidUuid != null) {
            buf.writeUUID(msg.maidUuid);
        }
        buf.writeUtf(msg.actionId, 128);
    }

    public static StoryProgressActionPayload decode(FriendlyByteBuf buf) {
        UUID maidUuid = buf.readBoolean() ? buf.readUUID() : null;
        String actionId = buf.readUtf(128);
        return new StoryProgressActionPayload(maidUuid, actionId);
    }
}
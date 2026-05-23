package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

/**
 * 送礼结算后的客户端剧情反馈。
 *
 * <p>客户端送出礼物后会先回到互动 UI，服务端再发这个包让互动 UI 播放结果剧情。
 * 成功收礼会播放感谢台词；每日上限等需要女仆明确拒绝的情况，也会通过专用分类播放拒收台词。
 */
public class GiftResultPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "gift_result");
    public static final CustomPacketPayload.Type<GiftResultPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, GiftResultPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> GiftResultPayload.encode(msg, buf),
            GiftResultPayload::decode);

    private final UUID maidUuid;
    private final String category;
    private final String reaction;

    public GiftResultPayload(UUID maidUuid, String category, String reaction) {
        this.maidUuid = maidUuid;
        this.category = category == null ? "" : category;
        this.reaction = reaction == null ? "" : reaction;
    }

    public UUID maidUuid() {
        return maidUuid;
    }

    public String category() {
        return category;
    }

    public String reaction() {
        return reaction;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(GiftResultPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.maidUuid);
        buf.writeUtf(msg.category);
        buf.writeUtf(msg.reaction);
    }

    public static GiftResultPayload decode(FriendlyByteBuf buf) {
        return new GiftResultPayload(buf.readUUID(), buf.readUtf(), buf.readUtf());
    }
}
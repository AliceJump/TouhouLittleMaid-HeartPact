package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

/**
 * 送礼提交包。
 *
 * <p>客户端只提交“目标女仆 + 背包槽位”，
 * 服务端再按当前真实库存和礼物表完成最终结算。
 */
public class GiftSubmitPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "gift_submit");
    public static final CustomPacketPayload.Type<GiftSubmitPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, GiftSubmitPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> GiftSubmitPayload.encode(msg, buf),
            GiftSubmitPayload::decode);

    private final UUID maidUuid;
    private final int slotIndex;

    public GiftSubmitPayload(UUID maidUuid, int slotIndex) {
        this.maidUuid = maidUuid;
        this.slotIndex = slotIndex;
    }

    public UUID maidUuid() {
        return maidUuid;
    }

    public int slotIndex() {
        return slotIndex;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(GiftSubmitPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.maidUuid);
        buf.writeVarInt(msg.slotIndex);
    }

    public static GiftSubmitPayload decode(FriendlyByteBuf buf) {
        return new GiftSubmitPayload(buf.readUUID(), buf.readVarInt());
    }
}
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
 * 亲吻请求数据包（客户端 -> 服务端）。
 * 客户端只负责提交当前想交互的女仆 UUID，
 * 服务端会再次核对所有权与拥抱状态，防止状态不同步。
 */
public class KissMaidPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "kiss_maid");
    public static final CustomPacketPayload.Type<KissMaidPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, KissMaidPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> KissMaidPayload.encode(msg, buf),
            KissMaidPayload::decode);

    @Nullable
    private final UUID maidUuid;

    public KissMaidPayload(@Nullable UUID maidUuid) {
        this.maidUuid = maidUuid;
    }

    @Nullable
    public UUID maidUuid() {
        return maidUuid;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(KissMaidPayload msg, FriendlyByteBuf buf) {
        boolean hasMaid = msg.maidUuid != null;
        buf.writeBoolean(hasMaid);
        if (hasMaid) {
            buf.writeUUID(msg.maidUuid);
        }
    }

    public static KissMaidPayload decode(FriendlyByteBuf buf) {
        boolean hasMaid = buf.readBoolean();
        return new KissMaidPayload(hasMaid ? buf.readUUID() : null);
    }
}
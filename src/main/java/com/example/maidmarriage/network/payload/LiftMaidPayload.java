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
 * 举高高请求包（客户端 -> 服务端）。
 * <p>
 * 按键触发时发送，允许携带一个可选女仆 UUID（准星命中时）。
 */
public class LiftMaidPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "lift_maid");
    public static final CustomPacketPayload.Type<LiftMaidPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, LiftMaidPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> LiftMaidPayload.encode(msg, buf),
            LiftMaidPayload::decode);

    @Nullable
    private final UUID maidUuid;

    public LiftMaidPayload(@Nullable UUID maidUuid) {
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

    public static void encode(LiftMaidPayload msg, FriendlyByteBuf buf) {
        boolean has = msg.maidUuid != null;
        buf.writeBoolean(has);
        if (has) {
            buf.writeUUID(msg.maidUuid);
        }
    }

    public static LiftMaidPayload decode(FriendlyByteBuf buf) {
        boolean has = buf.readBoolean();
        return new LiftMaidPayload(has ? buf.readUUID() : null);
    }
}
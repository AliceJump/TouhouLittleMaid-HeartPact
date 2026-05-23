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
 * 小女仆互动会话切换包。
 *
 * <p>语义与成年女仆的互动入口一致：
 * - 当前没有小女仆互动会话时：尝试进入站立锁定；
 * - 当前已经有会话时：结束这份会话。
 */
public class ChildInteractionPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "child_interaction");
    public static final CustomPacketPayload.Type<ChildInteractionPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChildInteractionPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> ChildInteractionPayload.encode(msg, buf),
            ChildInteractionPayload::decode);

    @Nullable
    private final UUID maidUuid;

    public ChildInteractionPayload(@Nullable UUID maidUuid) {
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

    public static void encode(ChildInteractionPayload msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.maidUuid != null);
        if (msg.maidUuid != null) {
            buf.writeUUID(msg.maidUuid);
        }
    }

    public static ChildInteractionPayload decode(FriendlyByteBuf buf) {
        boolean has = buf.readBoolean();
        return new ChildInteractionPayload(has ? buf.readUUID() : null);
    }
}
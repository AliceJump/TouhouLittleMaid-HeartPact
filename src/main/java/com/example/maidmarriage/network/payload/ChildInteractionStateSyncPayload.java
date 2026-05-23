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
 * 小女仆互动会话的客户端同步包。
 *
 * <p>这里只同步“是否正在和某只小女仆保持站立锁定”。
 * 这层不带拥抱标记，因为小女仆互动页没有“hugging / not hugging”二级状态。
 */
public class ChildInteractionStateSyncPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "child_interaction_state_sync");
    public static final CustomPacketPayload.Type<ChildInteractionStateSyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChildInteractionStateSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> ChildInteractionStateSyncPayload.encode(msg, buf),
            ChildInteractionStateSyncPayload::decode);

    private final UUID playerUuid;
    @Nullable
    private final UUID maidUuid;

    public ChildInteractionStateSyncPayload(UUID playerUuid, @Nullable UUID maidUuid) {
        this.playerUuid = playerUuid;
        this.maidUuid = maidUuid;
    }

    public UUID playerUuid() {
        return playerUuid;
    }

    @Nullable
    public UUID maidUuid() {
        return maidUuid;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(ChildInteractionStateSyncPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUuid);
        buf.writeBoolean(msg.maidUuid != null);
        if (msg.maidUuid != null) {
            buf.writeUUID(msg.maidUuid);
        }
    }

    public static ChildInteractionStateSyncPayload decode(FriendlyByteBuf buf) {
        UUID playerUuid = buf.readUUID();
        boolean hasMaid = buf.readBoolean();
        UUID maidUuid = hasMaid ? buf.readUUID() : null;
        return new ChildInteractionStateSyncPayload(playerUuid, maidUuid);
    }
}
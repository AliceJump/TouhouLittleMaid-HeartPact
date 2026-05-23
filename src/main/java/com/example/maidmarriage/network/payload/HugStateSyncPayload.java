package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public class HugStateSyncPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "hug_state_sync");
    public static final CustomPacketPayload.Type<HugStateSyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, HugStateSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> HugStateSyncPayload.encode(msg, buf),
            HugStateSyncPayload::decode);

    private final UUID playerUuid;
    @Nullable
    private final UUID maidUuid;
    private final boolean hugging;
    private final boolean childNameRequired;

    public HugStateSyncPayload(UUID playerUuid, @Nullable UUID maidUuid, boolean hugging) {
        this(playerUuid, maidUuid, hugging, false);
    }

    public HugStateSyncPayload(UUID playerUuid, @Nullable UUID maidUuid, boolean hugging, boolean childNameRequired) {
        this.playerUuid = playerUuid;
        this.maidUuid = maidUuid;
        this.hugging = maidUuid != null && hugging;
        this.childNameRequired = maidUuid != null && childNameRequired;
    }

    public UUID playerUuid() {
        return playerUuid;
    }

    @Nullable
    public UUID maidUuid() {
        return maidUuid;
    }

    public boolean hugging() {
        return hugging;
    }

    public boolean childNameRequired() {
        return childNameRequired;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(HugStateSyncPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUuid);
        buf.writeBoolean(msg.maidUuid != null);
        if (msg.maidUuid != null) {
            buf.writeUUID(msg.maidUuid);
        }
        buf.writeBoolean(msg.hugging);
        buf.writeBoolean(msg.childNameRequired);
    }

    public static HugStateSyncPayload decode(FriendlyByteBuf buf) {
        UUID playerUuid = buf.readUUID();
        boolean hasMaid = buf.readBoolean();
        UUID maidUuid = hasMaid ? buf.readUUID() : null;
        boolean hugging = buf.readBoolean();
        boolean childNameRequired = buf.readBoolean();
        return new HugStateSyncPayload(playerUuid, maidUuid, hugging, childNameRequired);
    }
}
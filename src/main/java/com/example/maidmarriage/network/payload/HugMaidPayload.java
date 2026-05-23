package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public class HugMaidPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "hug_maid");
    public static final CustomPacketPayload.Type<HugMaidPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, HugMaidPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> HugMaidPayload.encode(msg, buf),
            HugMaidPayload::decode);

    @Nullable
    private final UUID maidUuid;

    public HugMaidPayload(@Nullable UUID maidUuid) {
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

    public static void encode(HugMaidPayload msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.maidUuid != null);
        if (msg.maidUuid != null) {
            buf.writeUUID(msg.maidUuid);
        }
    }

    public static HugMaidPayload decode(FriendlyByteBuf buf) {
        boolean has = buf.readBoolean();
        return new HugMaidPayload(has ? buf.readUUID() : null);
    }
}
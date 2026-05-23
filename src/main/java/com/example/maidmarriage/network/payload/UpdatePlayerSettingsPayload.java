package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import net.minecraft.network.FriendlyByteBuf;

public class UpdatePlayerSettingsPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "update_player_settings");
    public static final CustomPacketPayload.Type<UpdatePlayerSettingsPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdatePlayerSettingsPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> UpdatePlayerSettingsPayload.encode(msg, buf),
            UpdatePlayerSettingsPayload::decode);

    private final double liftHeight;
    private final double hugDistance;
    private final boolean haremMode;

    public UpdatePlayerSettingsPayload(double liftHeight, double hugDistance, boolean haremMode) {
        this.liftHeight = liftHeight;
        this.hugDistance = hugDistance;
        this.haremMode = haremMode;
    }

    public double liftHeight() {
        return liftHeight;
    }

    public double hugDistance() {
        return hugDistance;
    }

    public boolean haremMode() {
        return haremMode;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(UpdatePlayerSettingsPayload msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.liftHeight);
        buf.writeDouble(msg.hugDistance);
        buf.writeBoolean(msg.haremMode);
    }

    public static UpdatePlayerSettingsPayload decode(FriendlyByteBuf buf) {
        return new UpdatePlayerSettingsPayload(
                buf.readDouble(),
                buf.readDouble(),
                buf.readBoolean());
    }
}
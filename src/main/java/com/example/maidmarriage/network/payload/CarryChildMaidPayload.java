package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public class CarryChildMaidPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "carry_child_maid");
    public static final CustomPacketPayload.Type<CarryChildMaidPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, CarryChildMaidPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> CarryChildMaidPayload.encode(msg, buf),
            CarryChildMaidPayload::decode);

    @Nullable
    private final UUID childUuid;

    public CarryChildMaidPayload(@Nullable UUID childUuid) {
        this.childUuid = childUuid;
    }

    @Nullable
    public UUID childUuid() {
        return childUuid;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(CarryChildMaidPayload msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.childUuid != null);
        if (msg.childUuid != null) {
            buf.writeUUID(msg.childUuid);
        }
    }

    public static CarryChildMaidPayload decode(FriendlyByteBuf buf) {
        return new CarryChildMaidPayload(buf.readBoolean() ? buf.readUUID() : null);
    }
}
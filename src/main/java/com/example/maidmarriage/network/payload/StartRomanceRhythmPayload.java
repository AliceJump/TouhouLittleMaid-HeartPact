package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class StartRomanceRhythmPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "start_romance_rhythm");
    public static final CustomPacketPayload.Type<StartRomanceRhythmPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, StartRomanceRhythmPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> StartRomanceRhythmPayload.encode(msg, buf),
            StartRomanceRhythmPayload::decode);

    private final UUID maidUuid;

    public StartRomanceRhythmPayload(UUID maidUuid) {
        this.maidUuid = maidUuid;
    }

    public UUID maidUuid() {
        return maidUuid;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(StartRomanceRhythmPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.maidUuid);
    }

    public static StartRomanceRhythmPayload decode(FriendlyByteBuf buf) {
        return new StartRomanceRhythmPayload(buf.readUUID());
    }
}

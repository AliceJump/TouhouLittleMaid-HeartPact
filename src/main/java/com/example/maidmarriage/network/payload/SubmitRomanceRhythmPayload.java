package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class SubmitRomanceRhythmPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "submit_romance_rhythm");
    public static final CustomPacketPayload.Type<SubmitRomanceRhythmPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SubmitRomanceRhythmPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> SubmitRomanceRhythmPayload.encode(msg, buf),
            SubmitRomanceRhythmPayload::decode);

    private final UUID maidUuid;
    private final float rhythmScore;

    public SubmitRomanceRhythmPayload(UUID maidUuid, float rhythmScore) {
        this.maidUuid = maidUuid;
        this.rhythmScore = rhythmScore;
    }

    public UUID maidUuid() {
        return maidUuid;
    }

    public float rhythmScore() {
        return rhythmScore;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(SubmitRomanceRhythmPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.maidUuid);
        buf.writeFloat(msg.rhythmScore);
    }

    public static SubmitRomanceRhythmPayload decode(FriendlyByteBuf buf) {
        return new SubmitRomanceRhythmPayload(buf.readUUID(), buf.readFloat());
    }
}
package com.example.maidmarriage.network.payload;
import com.example.maidmarriage.MaidMarriageMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

/**
 * 女仆调试面板的数据修改包。
 *
 * <p>这个包只用于测试面板：客户端提交目标女仆 UUID、好感度和心情值，
 * 服务端再校验权限并写入真实数据。
 */
public class MaidDebugDataPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "maid_debug_data");
    public static final CustomPacketPayload.Type<MaidDebugDataPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MaidDebugDataPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> MaidDebugDataPayload.encode(msg, buf),
            MaidDebugDataPayload::decode);

    private final UUID maidUuid;
    private final int favorability;
    private final int mood;

    public MaidDebugDataPayload(UUID maidUuid, int favorability, int mood) {
        this.maidUuid = maidUuid;
        this.favorability = favorability;
        this.mood = mood;
    }

    public UUID maidUuid() {
        return maidUuid;
    }

    public int favorability() {
        return favorability;
    }

    public int mood() {
        return mood;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(MaidDebugDataPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.maidUuid);
        buf.writeVarInt(msg.favorability);
        buf.writeVarInt(msg.mood);
    }

    public static MaidDebugDataPayload decode(FriendlyByteBuf buf) {
        UUID maidUuid = buf.readUUID();
        int favorability = buf.readVarInt();
        int mood = buf.readVarInt();
        return new MaidDebugDataPayload(maidUuid, favorability, mood);
    }
}
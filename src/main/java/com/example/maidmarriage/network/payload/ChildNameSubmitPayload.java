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
 * 小女仆命名提交包。
 *
 * <p>客户端只提交“正在互动的妈妈 + 玩家输入的名字”，具体能不能命名、
 * 应该命名哪个孩子，都由服务端按当前实体状态重新判定。
 */
public class ChildNameSubmitPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MaidMarriageMod.MOD_ID, "child_name_submit");
    public static final CustomPacketPayload.Type<ChildNameSubmitPayload> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChildNameSubmitPayload> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> ChildNameSubmitPayload.encode(msg, buf),
            ChildNameSubmitPayload::decode);

    @Nullable
    private final UUID motherUuid;
    private final String name;

    public ChildNameSubmitPayload(@Nullable UUID motherUuid, String name) {
        this.motherUuid = motherUuid;
        this.name = name == null ? "" : name;
    }

    @Nullable
    public UUID motherUuid() {
        return motherUuid;
    }

    public String name() {
        return name;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(ChildNameSubmitPayload msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.motherUuid != null);
        if (msg.motherUuid != null) {
            buf.writeUUID(msg.motherUuid);
        }
        buf.writeUtf(msg.name, 64);
    }

    public static ChildNameSubmitPayload decode(FriendlyByteBuf buf) {
        UUID motherUuid = buf.readBoolean() ? buf.readUUID() : null;
        return new ChildNameSubmitPayload(motherUuid, buf.readUtf(64));
    }
}
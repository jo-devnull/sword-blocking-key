package github.jodevnull.swordblockingkey.network;

import github.jodevnull.swordblockingkey.SwordBlockingKey;
import github.jodevnull.swordblockingkey.SwordBlockingKeyClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Set;
import java.util.UUID;

public record SBKSyncPacket(Set<UUID> uuids) implements CustomPacketPayload
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SwordBlockingKey.MODID, "sync_packet");
    public static final CustomPacketPayload.Type<SBKSyncPacket> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<ByteBuf, SBKSyncPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(UUIDUtil.CODEC_SET),
        SBKSyncPacket::uuids,
        SBKSyncPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void clientHandle(final SBKSyncPacket paylod, final IPayloadContext context) {
        SwordBlockingKeyClient.sync(paylod.uuids());
    }

    public static void serverHandle(final SBKSyncPacket paylod, final IPayloadContext context) {}
}
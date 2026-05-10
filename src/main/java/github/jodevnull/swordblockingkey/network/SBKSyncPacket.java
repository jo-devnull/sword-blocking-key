package github.jodevnull.swordblockingkey.network;

import github.jodevnull.swordblockingkey.SwordBlockingKeyClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.UUID;
import java.util.function.Supplier;

public record SBKSyncPacket(HashSet<UUID> uuids)
{
    public static void encode(SBKSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.uuids().size());

        for (var uuid : packet.uuids()) {
            buf.writeUUID(uuid);
        }
    }

    public static SBKSyncPacket decode(FriendlyByteBuf buf) {
        final var count = buf.readInt();
        final var uuids = new HashSet<UUID>();

        for (int i = 0; i < count; i++) {
            uuids.add(buf.readUUID());
        }

        return new SBKSyncPacket(uuids);
    }

    public static void handler(SBKSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SwordBlockingKeyClient.sync(packet.uuids));
        });

        context.get().setPacketHandled(true);
    }
}

package github.jodevnull.swordblockingkey.network;

import github.jodevnull.swordblockingkey.SwordBlockingKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record SBKeybindPacket(KeyEventType type, UUID playerId)
{
    public static SBKeybindPacket press(Player player) {
        return new SBKeybindPacket(KeyEventType.PRESS, player.getUUID());
    }

    public static SBKeybindPacket release(Player player) {
        return new SBKeybindPacket(KeyEventType.RELEASE, player.getUUID());
    }

    public static void encode(SBKeybindPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.type().ordinal());
        buf.writeUUID(packet.playerId());
    }

    public static SBKeybindPacket decode(FriendlyByteBuf buf) {
        final var type = KeyEventType.from(buf.readInt());
        return new SBKeybindPacket(type, buf.readUUID());
    }

    public static void clientHandle(SBKeybindPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            final var player = ctx.get().getSender();

            if (player == null)
                return;

            if (packet.type() == KeyEventType.PRESS)
                SwordBlockingKey.onKeyPress(player);
            else if (packet.type() == KeyEventType.RELEASE)
                SwordBlockingKey.onKeyRelease(player);
        });

        ctx.get().setPacketHandled(true);
    }
}

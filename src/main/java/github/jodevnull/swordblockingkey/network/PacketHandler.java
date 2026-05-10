package github.jodevnull.swordblockingkey.network;

import github.jodevnull.swordblockingkey.SwordBlockingKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler
{
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        ResourceLocation.fromNamespaceAndPath(SwordBlockingKey.MODID, "main"), () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;

        INSTANCE.registerMessage(id++,
            SBKeybindPacket.class,
            SBKeybindPacket::encode,
            SBKeybindPacket::decode,
            SBKeybindPacket::clientHandle
        );

        INSTANCE.registerMessage(id++,
            SBKSyncPacket.class,
            SBKSyncPacket::encode,
            SBKSyncPacket::decode,
            SBKSyncPacket::handler
        );
    }
}

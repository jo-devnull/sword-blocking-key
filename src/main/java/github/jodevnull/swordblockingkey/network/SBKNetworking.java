package github.jodevnull.swordblockingkey.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SBKNetworking
{
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(SBKeybindPacket.TYPE,
            SBKeybindPacket.STREAM_CODEC,
            new DirectionalPayloadHandler<>(
                SBKeybindPacket::clientHandle,
                SBKeybindPacket::serverHandle
            )
        );

        registrar.playToClient(SBKSyncPacket.TYPE,
            SBKSyncPacket.STREAM_CODEC,
            new DirectionalPayloadHandler<>(
                SBKSyncPacket::clientHandle,
                SBKSyncPacket::serverHandle
            )
        );
    }
}

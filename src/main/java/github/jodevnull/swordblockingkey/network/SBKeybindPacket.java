package github.jodevnull.swordblockingkey.network;

import github.jodevnull.swordblockingkey.SwordBlockingKey;
import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@MethodsReturnNonnullByDefault
public record SBKeybindPacket(KeyEventType eventType) implements CustomPacketPayload
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SwordBlockingKey.MODID, "keybind_event");
    public static final CustomPacketPayload.Type<SBKeybindPacket> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<ByteBuf, SBKeybindPacket> STREAM_CODEC = StreamCodec.composite(
        KeyEventType.CODEC,
        SBKeybindPacket::eventType,
        SBKeybindPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void clientHandle(final SBKeybindPacket paylod, final IPayloadContext context) {}

    public static void serverHandle(final SBKeybindPacket paylod, final IPayloadContext context) {
        context.enqueueWork(() -> {
            final var player = context.player();

            if (paylod.eventType() == KeyEventType.PRESS)
                SwordBlockingKey.onKeyPress((ServerPlayer) player);
            else if (paylod.eventType() == KeyEventType.RELEASE)
                SwordBlockingKey.onKeyRelease((ServerPlayer) player);
        })
        .exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
    }
}

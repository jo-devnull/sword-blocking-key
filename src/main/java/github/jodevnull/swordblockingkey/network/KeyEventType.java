package github.jodevnull.swordblockingkey.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public enum KeyEventType
{
    PRESS,
    RELEASE;

    public static KeyEventType from(int value) {
        if (value == 0) {
            return PRESS;
        } else if (value == 1) {
            return RELEASE;
        }

        throw new IllegalStateException("%d is not a valid KeyEventType".formatted(value));
    }

    public static final StreamCodec<ByteBuf, KeyEventType> CODEC = new StreamCodec<>()
    {
        @Override
        public @NotNull KeyEventType decode(ByteBuf byteBuf) {
            return KeyEventType.from(byteBuf.readInt());
        }

        @Override
        public void encode(ByteBuf o, KeyEventType type) {
            o.writeInt(type.ordinal());
        }
    };
}

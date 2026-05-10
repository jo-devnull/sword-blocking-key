package github.jodevnull.swordblockingkey.network;

public enum KeyEventType
{
    PRESS,
    RELEASE;

    public static KeyEventType from(int value) {
        for (var type : KeyEventType.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }

        throw new IllegalStateException("%d is not a valid KeyEventType".formatted(value));
    }
}

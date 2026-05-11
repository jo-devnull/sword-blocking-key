package github.jodevnull.swordblockingkey;

import com.mojang.blaze3d.platform.InputConstants;
import github.jodevnull.swordblockingkey.network.KeyEventType;
import github.jodevnull.swordblockingkey.network.SBKeybindPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = SwordBlockingKey.MODID, value = Dist.CLIENT)
public class SwordBlockingKeyClient
{
    private static final HashSet<UUID> IS_PRESSING_THE_BLOCK_KEY = new HashSet<>();

    private static final KeyMapping BLOCK_KEY = new KeyMapping(
        "key.sword_blocking_key.sword_block",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_LEFT_ALT,
        "key.categories.gameplay"
    );

    public static void sync(Set<UUID> uuids) {
        IS_PRESSING_THE_BLOCK_KEY.addAll(uuids);
    }

    public static boolean isPressingTheBlockKey(Player player) {
        return IS_PRESSING_THE_BLOCK_KEY.contains(player.getUUID());
    }

    public static void onRegisterKeymappings(RegisterKeyMappingsEvent event) {
        event.register(BLOCK_KEY);
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        final var minecraft = Minecraft.getInstance();

        if (minecraft.player == null)
            return;

        if (event.getAction() == GLFW.GLFW_PRESS && BLOCK_KEY.isDown()) {
            PacketDistributor.sendToServer(new SBKeybindPacket(KeyEventType.PRESS));
            IS_PRESSING_THE_BLOCK_KEY.add(minecraft.player.getUUID());
        }

        else if (event.getAction() == GLFW.GLFW_RELEASE && BLOCK_KEY.matches(event.getKey(), event.getScanCode())) {
            PacketDistributor.sendToServer(new SBKeybindPacket(KeyEventType.RELEASE));
            IS_PRESSING_THE_BLOCK_KEY.remove(minecraft.player.getUUID());
        }
    }
}
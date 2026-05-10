package github.jodevnull.swordblockingkey;

import com.mojang.blaze3d.platform.InputConstants;
import github.jodevnull.swordblockingkey.network.PacketHandler;
import github.jodevnull.swordblockingkey.network.SBKeybindPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = SwordBlockingKey.MODID, value = Dist.CLIENT)
public class SwordBlockingKeyClient
{
    private static final HashSet<UUID> IS_PRESSING_THE_BLOCK_KEY = new HashSet<>();

    private static final KeyMapping BLOCK_KEY = new KeyMapping(
        "key.sword_blocking_key.sword_block",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_LEFT_ALT,
        "key.categories.gameplay"
    );

    public static void sync(HashSet<UUID> uuids) {
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
            PacketHandler.INSTANCE.sendToServer(SBKeybindPacket.press(minecraft.player));
            IS_PRESSING_THE_BLOCK_KEY.add(minecraft.player.getUUID());
        }

        else if (event.getAction() == GLFW.GLFW_RELEASE && BLOCK_KEY.matches(event.getKey(), event.getScanCode())) {
            PacketHandler.INSTANCE.sendToServer(SBKeybindPacket.release(minecraft.player));
            IS_PRESSING_THE_BLOCK_KEY.remove(minecraft.player.getUUID());
        }
    }
}
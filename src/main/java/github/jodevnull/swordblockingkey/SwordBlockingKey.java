package github.jodevnull.swordblockingkey;

import com.mojang.logging.LogUtils;
import github.jodevnull.swordblockingkey.network.SBKNetworking;
import github.jodevnull.swordblockingkey.network.SBKSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.UUID;

@Mod(SwordBlockingKey.MODID)
public class SwordBlockingKey
{
    public static final String MODID = "sword_blocking_key";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final HashSet<UUID> IS_PRESSING_THE_BLOCK_KEY = new HashSet<>();

    public SwordBlockingKey(IEventBus modEventBus, ModContainer modContainer) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            modEventBus.addListener(SwordBlockingKeyClient::onRegisterKeymappings);
        }

        modEventBus.addListener(SBKNetworking::register);
    }

    public static boolean isPressingTheBlockKey(Player player) {
        if (player.level().isClientSide())
            return SwordBlockingKeyClient.isPressingTheBlockKey(player);

        return IS_PRESSING_THE_BLOCK_KEY.contains(player.getUUID());
    }

    public static void onKeyPress(ServerPlayer player) {
        IS_PRESSING_THE_BLOCK_KEY.add(player.getUUID());
        PacketDistributor.sendToAllPlayers(new SBKSyncPacket(IS_PRESSING_THE_BLOCK_KEY));
    }

    public static void onKeyRelease(ServerPlayer player) {
        IS_PRESSING_THE_BLOCK_KEY.remove(player.getUUID());
        PacketDistributor.sendToAllPlayers(new SBKSyncPacket(IS_PRESSING_THE_BLOCK_KEY));
    }
}

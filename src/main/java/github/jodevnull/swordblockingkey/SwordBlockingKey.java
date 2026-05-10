package github.jodevnull.swordblockingkey;

import com.mojang.logging.LogUtils;
import github.jodevnull.swordblockingkey.network.PacketHandler;
import github.jodevnull.swordblockingkey.network.SBKSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.UUID;

@Mod(SwordBlockingKey.MODID)
public class SwordBlockingKey
{
    public static final String MODID = "sword_blocking_key";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final HashSet<UUID> IS_PRESSING_THE_BLOCK_KEY = new HashSet<>();

    public SwordBlockingKey(FMLJavaModLoadingContext context) {
        final var modEventBus = context.getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(SwordBlockingKeyClient::onRegisterKeymappings);
        });

        PacketHandler.register();
    }

    public static boolean isPressingTheBlockKey(Player player) {
        if (player.level().isClientSide())
            return SwordBlockingKeyClient.isPressingTheBlockKey(player);

        return IS_PRESSING_THE_BLOCK_KEY.contains(player.getUUID());
    }

    public static void onKeyPress(ServerPlayer player) {
        IS_PRESSING_THE_BLOCK_KEY.add(player.getUUID());
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SBKSyncPacket(IS_PRESSING_THE_BLOCK_KEY));
    }

    public static void onKeyRelease(ServerPlayer player) {
        IS_PRESSING_THE_BLOCK_KEY.remove(player.getUUID());
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SBKSyncPacket(IS_PRESSING_THE_BLOCK_KEY));
    }
}

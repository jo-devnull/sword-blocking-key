package github.jodevnull.swordblockingkey.mixin;

import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.swordblockingmechanics.handler.SwordBlockingHandler;
import github.jodevnull.swordblockingkey.SwordBlockingKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SwordBlockingHandler.class, remap = false)
public class MixinSwordBlockHandler
{
    @Inject(method = "isActiveItemStackBlocking", at=@At("HEAD"), cancellable = true)
    private static void insomnia$isActiveItemStackBlocking(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!SwordBlockingKey.isPressingTheBlockKey(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onUseItem", at = @At("HEAD"), cancellable = true)
    private static void insomnia$onUseItem(Player player, Level level, InteractionHand hand, CallbackInfoReturnable<EventResultHolder<InteractionResultHolder<ItemStack>>> cir) {
        if (!SwordBlockingKey.isPressingTheBlockKey(player)) {
            cir.setReturnValue(EventResultHolder.pass());
        }
    }
}

package com.example.maidmarriage.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 禁止小女仆被传送门传送到其他维度。
 *
 * <p>原版 {@link Entity#canChangeDimensions()} 默认返回 {@code true}，
 * 导致小女仆会被下界门/末地传送门带走。此 Mixin 注入到 {@link Entity} 中，
 * 当实体是 {@link EntityMaid} 时强制返回 {@code false}，使女仆永远留在当前维度。
 */
@Mixin(Entity.class)
public abstract class EntityMaidPortalMixin {
    @Inject(method = "canChangeDimensions", at = @At("HEAD"), cancellable = true)
    private void maidmarriage$blockMaidPortalTeleport(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof EntityMaid) {
            cir.setReturnValue(false);
        }
    }
}

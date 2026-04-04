package dev.vanta.mixin.client;

import dev.vanta.module.render.CombatVisualThrottle;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public abstract class SoundManagerMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void vanta$dedupeCombatSound(SoundInstance sound, CallbackInfo ci) {
        if (sound == null) {
            return;
        }

        Identifier id = sound.getId();
        if (CombatVisualThrottle.shouldDedupeSound(id)) {
            ci.cancel();
        }
    }
}

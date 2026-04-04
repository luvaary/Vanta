package dev.vanta.mixin.client;

import dev.vanta.module.render.CombatVisualThrottle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void vanta$throttleParticleAdd(Particle particle, CallbackInfo ci) {
        if (CombatVisualThrottle.shouldDropParticle()) {
            ci.cancel();
        }
    }
}

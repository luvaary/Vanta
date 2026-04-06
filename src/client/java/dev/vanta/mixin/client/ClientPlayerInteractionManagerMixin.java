package dev.vanta.mixin.client;

import dev.vanta.module.render.CrystalAttackPrediction;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void vanta$recordCrystalAttackPrediction(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (target instanceof EndCrystalEntity crystal) {
            CrystalAttackPrediction.recordAttack(crystal);
        }
    }
}

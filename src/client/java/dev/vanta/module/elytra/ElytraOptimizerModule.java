package dev.vanta.module.elytra;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class ElytraOptimizerModule implements VantaModule {
    private VantaContext context;

    public String id() {
        return "elytra_optimizer";
    }

    public int priority() {
        return 400;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        VantaConfig.ElytraSettings settings = this.context.config().elytra;
        CombatSnapshot snapshot = this.context.snapshot();

        if (!settings.enabled || client.player == null) {
            snapshot.setFallFlying(false);
            snapshot.setElytraSpeed(0.0);
            snapshot.setElytraHorizontalSpeed(0.0);
            snapshot.setElytraPitch(0.0F);
            snapshot.setElytraDurabilityRemaining(0);
            snapshot.setElytraDurabilityMax(0);
            return;
        }

        ClientPlayerEntity player = client.player;
        boolean fallFlying = player.isFallFlying();
        double speed = player.getVelocity().length() * 20.0;
        double horizontalSpeed = Math.sqrt(player.getVelocity().x * player.getVelocity().x + player.getVelocity().z * player.getVelocity().z) * 20.0;
        float pitch = player.getPitch();

        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        int max = 0;
        int remaining = 0;
        if (chest.isOf(Items.ELYTRA)) {
            max = chest.getMaxDamage();
            remaining = Math.max(0, max - chest.getDamage());
        }

        snapshot.setFallFlying(fallFlying);
        snapshot.setElytraSpeed(speed);
        snapshot.setElytraHorizontalSpeed(horizontalSpeed);
        snapshot.setElytraPitch(pitch);
        snapshot.setElytraDurabilityMax(max);
        snapshot.setElytraDurabilityRemaining(remaining);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}

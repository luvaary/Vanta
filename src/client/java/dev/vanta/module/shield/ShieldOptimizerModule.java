package dev.vanta.module.shield;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class ShieldOptimizerModule implements VantaModule {
    private VantaContext context;
    private long shieldRaisedAtMs;

    public String id() {
        return "shield_optimizer";
    }

    public int priority() {
        return 300;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        VantaConfig.ShieldSettings settings = this.context.config().shield;
        CombatSnapshot snapshot = this.context.snapshot();

        if (!settings.enabled || client.player == null) {
            snapshot.setHasShieldOffhand(false);
            snapshot.setShieldBlocking(false);
            snapshot.setShieldCooldownPercent(0);
            snapshot.setShieldBlockingDurationMs(0L);
            return;
        }

        ClientPlayerEntity player = client.player;
        ItemStack offhand = player.getOffHandStack();
        boolean hasShieldOffhand = settings.trackOffhand && offhand.isOf(Items.SHIELD);
        boolean isBlockingShield = hasShieldOffhand && player.isUsingItem() && player.getActiveItem().isOf(Items.SHIELD);
        int cooldownPercent = hasShieldOffhand
            ? Math.round(player.getItemCooldownManager().getCooldownProgress(offhand, 0.0F) * 100.0F)
            : 0;

        long blockingDurationMs = 0L;
        if (isBlockingShield) {
            if (this.shieldRaisedAtMs == 0L) {
                this.shieldRaisedAtMs = System.currentTimeMillis();
            }
            blockingDurationMs = Math.max(0L, System.currentTimeMillis() - this.shieldRaisedAtMs);
        } else {
            this.shieldRaisedAtMs = 0L;
        }

        snapshot.setHasShieldOffhand(hasShieldOffhand);
        snapshot.setShieldBlocking(isBlockingShield);
        snapshot.setShieldCooldownPercent(cooldownPercent);
        snapshot.setShieldBlockingDurationMs(blockingDurationMs);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}

package dev.vanta.module.ping;

import dev.vanta.config.VantaConfig;
import dev.vanta.core.CombatSnapshot;
import dev.vanta.core.VantaContext;
import dev.vanta.core.VantaModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;

public final class PingOptimizerModule implements VantaModule {
    private VantaContext context;
    private int lastPingMs = -1;
    private double jitterEma;

    public String id() {
        return "ping_optimizer";
    }

    public int priority() {
        return 120;
    }

    public void onInitialize(VantaContext context) {
        this.context = context;
    }

    public void onClientTick(MinecraftClient client) {
        if (!this.context.config().enabled) {
            return;
        }

        CombatSnapshot snapshot = this.context.snapshot();
        VantaConfig.PingSettings settings = this.context.config().ping;

        if (!settings.enabled || client.player == null || client.getNetworkHandler() == null) {
            this.lastPingMs = -1;
            this.jitterEma = 0.0;
            snapshot.setPingMs(0);
            snapshot.setPingJitterMs(0);
            snapshot.setPingUnstable(false);
            return;
        }

        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        int pingMs = entry == null ? 0 : Math.max(0, entry.getLatency());

        if (this.lastPingMs >= 0) {
            int delta = Math.abs(pingMs - this.lastPingMs);
            this.jitterEma = this.jitterEma * (1.0 - settings.jitterEmaAlpha) + delta * settings.jitterEmaAlpha;
        } else {
            this.jitterEma = 0.0;
        }

        this.lastPingMs = pingMs;

        int jitterMs = (int) Math.round(this.jitterEma);
        boolean unstable = pingMs >= settings.highPingThresholdMs || jitterMs >= settings.highJitterThresholdMs;

        snapshot.setPingMs(pingMs);
        snapshot.setPingJitterMs(jitterMs);
        snapshot.setPingUnstable(unstable);
    }

    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    }
}
